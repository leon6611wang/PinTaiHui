package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.leon.chic.dao.CardDao;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.CardFrend;
import com.zhiyu.quanzhu.model.bean.MyCardFriend;
import com.zhiyu.quanzhu.model.bean.MyCardFriendBean;
import com.zhiyu.quanzhu.model.dao.CardFrendDao;
import com.zhiyu.quanzhu.ui.adapter.CircleMemberManageAdapter;
import com.zhiyu.quanzhu.ui.adapter.CircleMemberManageAddFrendListAdapter;
import com.zhiyu.quanzhu.ui.adapter.LetterListAdapter;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.hoverExpandableListView.HoverExpandableListView;
import com.zhiyu.quanzhu.ui.widget.hoverExpandableListView.IDockingHeaderUpdateListener;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SoftKeyboardUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 圈子-成员管理-添加好友
 */
public class CircleMemberManageAddFrendActivity extends BaseActivity implements View.OnClickListener, CircleMemberManageAdapter.OnSelectedCardFrendListener {
    private LinearLayout backLayout, rightLayout;
    private TextView titleTextView;
    private EditText searchEditText;
    private HoverExpandableListView mExpandableListView;
    private CircleMemberManageAdapter adapter;
    private ListView letterListView;
    private LetterListAdapter letterAdapter;
    private List<String> letterList = new ArrayList<>();
    private List<List<MyCardFriend>> list;
    private long circle_id;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CircleMemberManageAddFrendActivity> weakReference;

        public MyHandler(CircleMemberManageAddFrendActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CircleMemberManageAddFrendActivity activity = weakReference.get();
            switch (msg.what) {
                case 1:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        if (null != activity.list && activity.list.size() > 0) {
                            for (int i = 0; i < activity.list.size(); i++) {
                                for (int j = 0; j < activity.list.get(i).size(); j++) {
                                    activity.list.get(i).get(j).setSelected(false);
                                }
                            }
                        }
                        activity.titleTextView.setText("发送给(0)");
                        activity.adapter.setList(activity.list);
                    }
                    break;
                case 99:
                    MessageToast.getInstance(activity).show("服务器内部错误，请稍后再试.");
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_member_manage_add_frend);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        circle_id = getIntent().getLongExtra("circle_id", 0l);
        initViews();
        initLetterData();
        initCardData();
    }

    private void initCardData() {
        String s = CardDao.getInstance().selectCardListByLetter(BaseApplication.getInstance());
        letterList = CardDao.getInstance().getLetterList();
        String json = "{\"list\":" + s + "}";
        MyCardFriendBean bean = GsonUtils.GsonToBean(json, MyCardFriendBean.class);
        list = bean.getList();
        setData();
        System.out.println("list : " + bean.getList().size());

    }

    private void initLetterData() {
        letterList.add("a");
        letterList.add("b");
        letterList.add("c");
        letterList.add("d");
        letterList.add("e");
        letterList.add("f");
        letterList.add("g");
        letterList.add("h");
        letterList.add("i");
        letterList.add("j");
        letterList.add("k");
        letterList.add("l");
        letterList.add("m");
        letterList.add("n");
        letterList.add("o");
        letterList.add("p");
        letterList.add("q");
        letterList.add("r");
        letterList.add("s");
        letterList.add("t");
        letterList.add("u");
        letterList.add("v");
        letterList.add("w");
        letterList.add("x");
        letterList.add("y");
        letterList.add("z");

    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String search = searchEditText.getText().toString().trim();
                    SoftKeyboardUtil.hideSoftKeyboard(CircleMemberManageAddFrendActivity.this);
                    if (!StringUtils.isNullOrEmpty(search)) {
                        String s = CardDao.getInstance().searchCardListByName(BaseApplication.getInstance(), search);
                        letterList = CardDao.getInstance().getLetterList();
                        String json = "{\"list\":" + s + "}";
                        MyCardFriendBean bean = GsonUtils.GsonToBean(json, MyCardFriendBean.class);
                        list = bean.getList();
                        setData();
                    } else {
                        initCardData();
                    }

                    return true;
                }
                return false;
            }
        });
        mExpandableListView = findViewById(R.id.mExpandableListView);
        mExpandableListView.setGroupIndicator(null);
        mExpandableListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter = new CircleMemberManageAdapter(this, mExpandableListView);
        adapter.setOnSelectedCardFrendListener(this);
        mExpandableListView.setAdapter(adapter);
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        letterListView = findViewById(R.id.letterListView);
        letterAdapter = new LetterListAdapter();
        letterListView.setAdapter(letterAdapter);
        letterAdapter.setLetterList(letterList);
        letterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int groupPosition = 0;
                if (null != groupCharList && groupCharList.size() > 0) {
                    for (int i = 0; i < groupCharList.size(); i++) {
                        if (groupCharList.get(i).equals(letterList.get(position).toUpperCase())) {
                            groupPosition = i;
                        }
                    }
                }
                mExpandableListView.setSelectedGroup(groupPosition);
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
                List<Integer> selectList = adapter.getSelectedIdList();
                if (null != selectList && selectList.size() > 0) {
                    inviteJoinCircle(selectList);
                } else {
                    MessageToast.getInstance(this).show("请选择名片圈友");
                }
                break;
        }
    }


    @Override
    public void OnSelectedCardFrend(List<MyCardFriend> list) {
        titleTextView.setText("发送给(" + (null == list ? 0 : list.size()) + ")");
    }

    private List<String> groupCharList = new ArrayList<>();


    private void setData() {
        adapter.setList(list);
        letterAdapter.setLetterList(letterList);
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            mExpandableListView.expandGroup(i);
        }
        View headerView = getLayoutInflater().inflate(R.layout.item_circle_member_manage_add_frend_parent, mExpandableListView, false);
        mExpandableListView.setDockingHeader(headerView, new IDockingHeaderUpdateListener() {
            @Override
            public void onUpdate(View headerView, int groupPosition, boolean expanded) {
                String groupTitle = list.get(groupPosition).get(0).getLetter();
                TextView titleView = headerView.findViewById(R.id.titleTextView);
                titleView.setText(groupTitle);
            }
        });
        if (null != list && list.size() > 0) {
            groupCharList.clear();
            for (List<MyCardFriend> frendList : list) {
                groupCharList.add(frendList.get(0).getLetter());
            }
        }
    }

    private BaseResult baseResult;

    private void inviteJoinCircle(List<Integer> list) {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.INVITE_JOIN_CIRCLE);
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        params.addBodyParameter("uids", GsonUtils.GsonString(list));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
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
}
