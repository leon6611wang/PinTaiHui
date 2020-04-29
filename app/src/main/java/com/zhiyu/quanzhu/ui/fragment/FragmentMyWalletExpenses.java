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

import com.zhiyu.quanzhu.R;

import java.lang.ref.WeakReference;

/**
 * 钱包-支出
 */
public class FragmentMyWalletExpenses extends Fragment {
    private View view;
    private MyHandler myHandler=new MyHandler(this);
    private static class MyHandler extends Handler{
        WeakReference<FragmentMyWalletExpenses> fragmentWeakReference;
        public MyHandler(FragmentMyWalletExpenses fragment){
            fragmentWeakReference=new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentMyWalletExpenses fragment=fragmentWeakReference.get();
            switch (msg.what){
                case 1:

                    break;
            }
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_my_wallet_expenses,null);
        return view;
    }
}
