package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.leon.chic.utils.SPUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.Hobby;
import com.zhiyu.quanzhu.model.bean.HobbySelect;
import com.zhiyu.quanzhu.model.result.AddHobbyResult;
import com.zhiyu.quanzhu.model.result.HobbyResult;
import com.zhiyu.quanzhu.ui.adapter.HobbySelectLeftListAdapter;
import com.zhiyu.quanzhu.ui.adapter.HobbySelectRightListAdapter;
import com.zhiyu.quanzhu.ui.dialog.LoadingDialog;
import com.zhiyu.quanzhu.ui.toast.FailureToast;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 偏好选择
 */
public class HobbySelectActivity extends BaseActivity implements View.OnClickListener, HobbySelectRightListAdapter.OnAddHobbyListener, HobbySelectRightListAdapter.OnDeleteHobbyListener {
    private LinearLayout backLayout, rightLayout;
    private TextView rightTextView, titleTextView;
    private ListView leftList;
    private ExpandableListView rightList;
    private HobbySelectLeftListAdapter leftListAdapter;
    private HobbySelectRightListAdapter rightListAdapter;
    private int leftIndex;
    private Hobby zidingyiHobby;//自定义的Hobby，目前后端未做处理，本地处理放在列表最后面
    private MyHandler myHandler = new MyHandler(this);
    private LoadingDialog loadingDialog;

    private int loadType = 0;//0:注册登录进入(默认)，1：修改个人资料进入

    private static class MyHandler extends Handler {
        WeakReference<HobbySelectActivity> activityWeakReference;

        public MyHandler(HobbySelectActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            HobbySelectActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.loadingDialog.dismiss();
                    if (activity.hobbyResult.getCode() == 200) {
                        activity.leftListAdapter.setList(activity.hobbyResult.getData().getList());
                        activity.rightListAdapter.setList(activity.hobbyResult.getData().getList().get(activity.leftIndex).getChild(), (activity.hobbyResult.getData().getList().get(activity.leftIndex).getType() == 2));
                    } else {
                        MessageToast.getInstance(activity).show(activity.hobbyResult.getMsg());
                    }
                    break;
                case 2:
                    MessageToast.getInstance(activity).show(activity.addHobbyResult.getMsg());
                    if (activity.addHobbyResult.getCode() == 200) {
                        activity.hobby = activity.addHobbyResult.getData();
                        activity.addLocalHobby();
                        activity.leftListAdapter.addHobbyCount();
                    }
                    break;
                case 3:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (activity.baseResult.getCode() == 200)
                        activity.deleteLocalHobby();
                    break;
                case 4:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        if (activity.loadType == 1) {
                            activity.finish();
                        } else {
                            Intent interestQuanZiIntent = new Intent(activity, InterestQuanZiSelectActivity.class);
                            activity.startActivity(interestQuanZiIntent);
                            activity.finish();
                        }
                    }
                    break;
                case 5:
                    activity.loadingDialog.dismiss();
                    FailureToast.getInstance(activity).show();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobby_select);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        loadType = getIntent().getIntExtra("loadType", 0);
        initViews();
        initDialogs();
        hobbyList();
    }

    private void initDialogs() {
        loadingDialog = new LoadingDialog(this, R.style.dialog);
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        rightTextView = findViewById(R.id.rightTextView);
        rightTextView.setText(loadType == 0 ? "下一步" : "确定");
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("偏好选择");
        leftList = findViewById(R.id.leftList);
        leftListAdapter = new HobbySelectLeftListAdapter();
        leftList.setAdapter(leftListAdapter);
        leftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                leftIndex = position;
                leftListAdapter.setCurrentItem(position);
                leftListAdapter.notifyDataSetChanged();
                rightListAdapter.setList(hobbyResult.getData().getList().get(position).getChild(), (hobbyResult.getData().getList().get(position).getType() == 2));
                int count = rightList.getExpandableListAdapter().getGroupCount();
                for (int i = 0; i < count; i++) {
                    rightList.collapseGroup(i);
                }
                rightListAdapter.clearPosition();
            }
        });
        rightList = findViewById(R.id.rightList);
        rightListAdapter = new HobbySelectRightListAdapter(this);
        rightListAdapter.setOnAddHobbyListener(this);
        rightListAdapter.setOnDeleteHobbyListener(this);
        rightList.setAdapter(rightListAdapter);
        rightList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                rightListAdapter.setParentCurrentPosition(groupPosition);
                int count = rightList.getExpandableListAdapter().getGroupCount();
                for (int i = 0; i < count; i++) {
                    if (groupPosition != i) {
                        rightList.collapseGroup(i);
                    }
                }
            }
        });
        rightList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                int selectCount = 0;
                for (Hobby hobby : rightListAdapter.getHobbyList()) {
                    for (Hobby hobby1 : hobby.getChild()) {
                        if (hobby1.isIs_choose()) {
                            selectCount++;
                        }
                    }
                }
                if (selectCount < 10 || rightListAdapter.getHobbyList().get(groupPosition).getChild().get(childPosition).isIs_choose()) {
                    rightListAdapter.childSelect(groupPosition, childPosition);
                    int leftSelectCount = 0;
                    for (Hobby hobby : rightListAdapter.getHobbyList()) {
                        for (Hobby hobby1 : hobby.getChild()) {
                            if (hobby1.isIs_choose()) {
                                leftSelectCount++;
                            }
                        }
                    }
                    map.put(leftIndex, rightListAdapter.getHobbyList());
                    leftListAdapter.setSelectedCount(leftIndex, leftSelectCount);
                } else {
                    MessageToast.getInstance(HobbySelectActivity.this).show("每组最多选择十个");
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.rightLayout:
                userHobby();
                break;
        }
    }

    private HobbyResult hobbyResult;

    private void hobbyList() {
        loadingDialog.show();
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.HOBBY_LIST);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("hobby list: " + result);
                hobbyResult = GsonUtils.GsonToBean(result, HobbyResult.class);
                if (null != hobbyResult && null != hobbyResult.getData() && null != hobbyResult.getData().getList() && hobbyResult.getData().getList().size() > 0) {
                    for (int i = 0; i < hobbyResult.getData().getList().size(); i++) {
                        for (int ii = 0; ii < hobbyResult.getData().getList().get(i).getChild().size(); ii++) {
                            if (hobbyResult.getData().getList().get(i).getChild().get(ii).getId() == 3) {
                                zidingyiHobby = hobbyResult.getData().getList().get(i).getChild().get(ii);
                                hobbyResult.getData().getList().get(i).getChild().remove(ii);
                            }

                            boolean groupHasChoose = false;
                            for (Hobby hobby : hobbyResult.getData().getList().get(i).getChild().get(ii).getChild()) {
                                if (hobby.isIs_choose()) {
                                    groupHasChoose = true;
                                }
                            }

                            if (groupHasChoose) {
                                hobbyResult.getData().getList().get(i).getChild().get(ii).setIs_choose(true);
                                map.put(i, hobbyResult.getData().getList().get(i).getChild());
                            } else {
                                hobbyResult.getData().getList().get(i).getChild().get(ii).setIs_choose(false);
                            }
                        }
                        if (hobbyResult.getData().getList().get(i).getType() == 2)
                            hobbyResult.getData().getList().get(i).getChild().add(zidingyiHobby);
                    }
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                System.out.println("hobbylist:" + ex.toString());
                Message message = myHandler.obtainMessage(5);
                message.sendToTarget();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                System.out.println("hobbylist finish");
            }
        });
    }

    private BaseResult baseResult;
    private AddHobbyResult addHobbyResult;

    private void addHobby(String name) {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ADD_HOBBY);
        params.addBodyParameter("name", name);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("add hobby: " + result);
                addHobbyResult = GsonUtils.GsonToBean(result, AddHobbyResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
                System.out.println("add hobby: " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                System.out.println("add hobby: " + ex.toString());
                Message message = myHandler.obtainMessage(5);
                message.sendToTarget();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void deleteHobby(int id) {
        System.out.println("hobby_id: " + id);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.DELETE_HOBBY);
        params.addBodyParameter("id", String.valueOf(id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(3);
                message.sendToTarget();
                System.out.println("deleteHobby: " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("deleteHobby: " + ex.toString());
                Message message = myHandler.obtainMessage(5);
                message.sendToTarget();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private Hobby hobby;

    @Override
    public void onAddHobby(Hobby hobby) {
        addHobby(hobby.getName());
    }

    private void addLocalHobby() {
        int index = hobbyResult.getData().getList().get(leftIndex).getChild().size() - 1;
        hobbyResult.getData().getList().get(leftIndex).getChild().get(index).getChild().add(0, hobby);
        rightListAdapter.setParentCurrentPosition(index);
        if (!rightList.isGroupExpanded(index)) {
            rightList.expandGroup(index);
        }
        map.put(leftIndex, rightListAdapter.getHobbyList());
//        rightListAdapter.setChildCurrentPosition(0);
    }


    int delete_parent_position, delete_child_position;

    @Override
    public void onDeleteHobby(int hobby_id, int delete_parent_position, int delete_child_position) {
        this.delete_parent_position = delete_parent_position;
        this.delete_child_position = delete_child_position;
        deleteHobby(hobbyResult.getData().getList().get(leftIndex).getChild().get(delete_parent_position).getChild().get(delete_child_position).getId());
    }

    private void deleteLocalHobby() {
        hobbyResult.getData().getList().get(leftIndex).getChild().get(delete_parent_position).getChild().remove(delete_child_position);
        rightListAdapter.setList(hobbyResult.getData().getList().get(leftIndex).getChild(), true);
        rightListAdapter.clearPosition();
        if (!rightList.isGroupExpanded(delete_parent_position)) {
            rightList.expandGroup(delete_parent_position);
        }
    }

    private Map<Integer, List<Hobby>> map = new ArrayMap<>();
    private List<HobbySelect> hobbySelectList = new ArrayList<>();

    private void userHobby() {
        hobbySelectList.clear();
        for (List<Hobby> list : map.values()) {
            for (Hobby hobby : list) {
                int ppid = 0;
                int pptype = 0;
                String ppname = null;
                for (Hobby pp : hobbyResult.getData().getList()) {
                    for (Hobby p : pp.getChild()) {
                        if (hobby.getId() == p.getId()) {
                            ppid = pp.getId();
                            ppname = pp.getName();
                            pptype = pp.getType();
                        }
                    }
                }
                for (Hobby hobby1 : hobby.getChild()) {
                    if (hobby1.isIs_choose()) {
                        hobbySelectList.add(new HobbySelect(hobby1.getId(), hobby1.getName(), hobby.getId(), hobby.getName(), ppid, ppname, pptype));
                    }
                }
            }
        }
        hobbySelect(GsonUtils.GsonString(hobbySelectList));
    }


    /**
     * 用户偏好选择
     *
     * @param industry
     */
    private void hobbySelect(String industry) {
        System.out.println(industry);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.USER_HOBBY);
        params.addBodyParameter("industry", industry);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                if (200 == baseResult.getCode()) {
                    SPUtils.getInstance().userChooseInterest(BaseApplication.applicationContext);
                }
                Message message = myHandler.obtainMessage(4);
                message.sendToTarget();
                System.out.println("用户偏好选择: " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(5);
                message.sendToTarget();
                System.out.println("用户偏好选择: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

}
