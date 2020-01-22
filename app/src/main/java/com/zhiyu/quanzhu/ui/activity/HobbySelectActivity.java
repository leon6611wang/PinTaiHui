package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.Hobby;
import com.zhiyu.quanzhu.model.result.HobbyResult;
import com.zhiyu.quanzhu.ui.adapter.HobbySelectLeftListAdapter;
import com.zhiyu.quanzhu.ui.adapter.HobbySelectRightListAdapter;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;


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
    private MyHandler myHandler = new MyHandler(this);

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
                    activity.leftListAdapter.setList(activity.hobbyResult.getData().getList());
                    activity.rightListAdapter.setList(activity.hobbyResult.getData().getList().get(0).getChild(), false);
                    break;
                case 2:
                    Toast.makeText(activity, activity.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    activity.addLocalHobby();
                    break;
                case 3:
                    Toast.makeText(activity, activity.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    activity.deleteLocalHobby();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobby_select);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        requestHobbyList();
        initViews();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        rightTextView = findViewById(R.id.rightTextView);
        rightTextView.setText("下一步");
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("偏好选择");
        leftList = findViewById(R.id.leftList);
        leftListAdapter = new HobbySelectLeftListAdapter();
        leftList.setAdapter(leftListAdapter);
        leftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                leftListAdapter.setCurrentItem(position);
                leftListAdapter.notifyDataSetChanged();
                rightListAdapter.setList(hobbyResult.getData().getList().get(position).getChild(), (position == 1));
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
                System.out.println("groupPosition: " + groupPosition + " , childPosition: " + childPosition);
                rightListAdapter.setChildCurrentPosition(childPosition);
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
                Intent interestQuanZiIntent = new Intent(this, InterestQuanZiSelectActivity.class);
                startActivity(interestQuanZiIntent);
                break;
        }
    }

    private HobbyResult hobbyResult;

    private void requestHobbyList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.HOBBY_LIST);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("hobby list: " + result);
                hobbyResult = GsonUtils.GsonToBean(result, HobbyResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("hobbylist:" + ex.toString());
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

    private void addHobby(String name) {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ADD_HOBBY);
        params.addBodyParameter("name", name);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
                System.out.println("add hobby: " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("add hobby: " + ex.toString());
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
        this.hobby = hobby;
        addHobby(hobby.getName());

    }

    private void addLocalHobby() {
        int index = hobbyResult.getData().getList().get(0).getChild().size() - 1;
        hobbyResult.getData().getList().get(0).getChild().get(index).getChild().add(0, hobby);
        rightListAdapter.setParentCurrentPosition(index);
        if (!rightList.isGroupExpanded(index)) {
            rightList.expandGroup(index);
        }
        rightListAdapter.setChildCurrentPosition(0);
    }


    int delete_parent_position, delete_child_position;

    @Override
    public void onDeleteHobby(int hobby_id, int delete_parent_position, int delete_child_position) {
        this.delete_parent_position = delete_parent_position;
        this.delete_child_position = delete_child_position;
        deleteHobby(hobbyResult.getData().getList().get(1).getChild().get(delete_parent_position).getChild().get(delete_child_position).getId());
//        System.out.println("2: "+hobbyResult.getData().getList().get(1).getChild().get(delete_parent_position).getChild().get(delete_child_position).getName());
    }

    private void deleteLocalHobby() {
        hobbyResult.getData().getList().get(1).getChild().get(delete_parent_position).getChild().remove(delete_child_position);
        rightListAdapter.setList(hobbyResult.getData().getList().get(1).getChild(), true);
        rightListAdapter.clearPosition();
        if (!rightList.isGroupExpanded(delete_parent_position)) {
            rightList.expandGroup(delete_parent_position);
        }
    }


}
