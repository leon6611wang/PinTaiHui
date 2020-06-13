package com.zhiyu.quanzhu.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.leon.chic.dao.CardDao;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.CardFrend;
import com.zhiyu.quanzhu.model.bean.MyCardFriend;
import com.zhiyu.quanzhu.model.bean.MyCardFriendBean;
import com.zhiyu.quanzhu.model.dao.CardFrendDao;
import com.zhiyu.quanzhu.ui.activity.CircleMemberManageAddFrendActivity;
import com.zhiyu.quanzhu.ui.adapter.CircleMemberManageAddFrendListAdapter;
import com.zhiyu.quanzhu.ui.adapter.InnerShareQuanYouAdapter;
import com.zhiyu.quanzhu.ui.adapter.LetterListAdapter;
import com.zhiyu.quanzhu.ui.widget.hoverExpandableListView.HoverExpandableListView;
import com.zhiyu.quanzhu.ui.widget.hoverExpandableListView.IDockingHeaderUpdateListener;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.SoftKeyboardUtil;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class FragmentShareInnerQuanYou extends Fragment {
    private View view;
    private HoverExpandableListView mExpandableListView;
    private InnerShareQuanYouAdapter adapter;
    private ListView letterListView;
    private LetterListAdapter letterAdapter;
    private List<String> letterList = new ArrayList<>();
    private List<List<MyCardFriend>> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_share_inner_quanyou, null);
        initViews();
        initCardData();
        return view;
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

    public List<Integer> getSelectedIdList(){
        return adapter.getSelectedIdList();
    }

    private void initViews() {
        mExpandableListView = view.findViewById(R.id.mExpandableListView);
        mExpandableListView.setGroupIndicator(null);
        mExpandableListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter = new InnerShareQuanYouAdapter(getContext(), mExpandableListView);
//        adapter.setOnSelectedCardFrendListener(this);
        mExpandableListView.setAdapter(adapter);
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        letterListView = view.findViewById(R.id.letterListView);
        letterAdapter = new LetterListAdapter();
        letterListView.setAdapter(letterAdapter);
        letterAdapter.setLetterList(letterList);
        letterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int groupPosition = 0;
                if (null != groupCharList && groupCharList.size() > 0) {
                    for (int i = 0; i < groupCharList.size(); i++) {
                        if (groupCharList.get(i).equals(letterList.get(position))) {
                            groupPosition = i;
                        }
                    }
                }
                mExpandableListView.setSelectedGroup(groupPosition);
            }
        });
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
}
