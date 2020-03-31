package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.CardFrend;
import com.zhiyu.quanzhu.model.bean.FullSearchHistory;
import com.zhiyu.quanzhu.model.dao.CardFrendDao;
import com.zhiyu.quanzhu.model.dao.FullSearchHistoryDao;
import com.zhiyu.quanzhu.ui.adapter.CircleMemberManageAddFrendListAdapter;
import com.zhiyu.quanzhu.ui.adapter.LetterListAdapter;
import com.zhiyu.quanzhu.ui.widget.hoverExpandableListView.HoverExpandableListView;
import com.zhiyu.quanzhu.ui.widget.hoverExpandableListView.IDockingHeaderUpdateListener;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SoftKeyboardUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 圈子-成员管理-添加好友
 */
public class CircleMemberManageAddFrendActivity extends BaseActivity implements View.OnClickListener, CircleMemberManageAddFrendListAdapter.OnSelectedCardFrendListener {
    private LinearLayout backLayout, rightLayout;
    private TextView titleTextView;
    private EditText searchEditText;
    private HoverExpandableListView mExpandableListView;
    private CircleMemberManageAddFrendListAdapter adapter;
    private ListView letterListView;
    private LetterListAdapter letterAdapter;
    private List<String> letterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_member_manage_add_frend);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initData();
        initViews();
        cardFrendList();
    }

    private void initData() {
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
                        List<List<CardFrend>> list = CardFrendDao.getDao().searchFrend(search);
                        setData(list);
                    }

                    return true;
                }
                return false;
            }
        });
        mExpandableListView = findViewById(R.id.mExpandableListView);
        mExpandableListView.setGroupIndicator(null);
        mExpandableListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter = new CircleMemberManageAddFrendListAdapter(this, mExpandableListView);
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

                break;
        }
    }

    @Override
    public void OnSelectedCardFrend(List<CardFrend> list) {
        titleTextView.setText("发送给(" + (null == list ? 0 : list.size()) + ")");
    }

    private List<String> groupCharList = new ArrayList<>();

    private void cardFrendList() {
        List<List<CardFrend>> list = CardFrendDao.getDao().cardFrendList();
        setData(list);
    }

    private void setData(final List<List<CardFrend>> list) {
        adapter.setList(list);
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
            for (List<CardFrend> frendList : list) {
                groupCharList.add(frendList.get(0).getLetter());
            }
        }
    }

}
