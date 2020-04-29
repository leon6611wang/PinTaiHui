package com.zhiyu.quanzhu.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.adapter.MyWalletExpandableListAdapter;
import com.zhiyu.quanzhu.ui.widget.ExpandableListViewForScrollView;
import com.zhiyu.quanzhu.ui.widget.NestedExpandableListView;

import java.lang.ref.WeakReference;

/**
 * 钱包-全部
 */
public class FragmentMyWalletAll extends Fragment {
    private View view;
    private ExpandableListViewForScrollView mExpandableListView;
    private MyWalletExpandableListAdapter adapter;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FragmentMyWalletAll> fragmentWeakReference;

        public MyHandler(FragmentMyWalletAll fragment) {
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentMyWalletAll fragment = fragmentWeakReference.get();
            switch (msg.what) {
                case 1:

                    break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_wallet_all, null);
        initViews();
        return view;
    }

    private void initViews() {
        mExpandableListView = view.findViewById(R.id.mExpandableListView);
        adapter = new MyWalletExpandableListAdapter();
        mExpandableListView.setAdapter(adapter);
        int groupCount = mExpandableListView.getCount();
        for (int i = 0; i < groupCount; i++) {
            mExpandableListView.expandGroup(i);
        }
    }


}
