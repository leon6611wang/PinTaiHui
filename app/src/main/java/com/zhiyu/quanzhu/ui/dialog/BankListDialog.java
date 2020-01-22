package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.Bank;
import com.zhiyu.quanzhu.ui.adapter.BankListDialogRecyclerAdapter;
import com.zhiyu.quanzhu.utils.CalendarUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 日历
 */
public class BankListDialog extends Dialog implements BankListDialogRecyclerAdapter.OnBankChooseListener, View.OnClickListener {
    private Context mContext;
    private RecyclerView mRecyclerView;
    private BankListDialogRecyclerAdapter adapter;
    private List<Bank> list = new ArrayList<>();
    private Bank selectedBank;
    private TextView confirmTextView;

    public BankListDialog(@NonNull Context context, int themeResId, OnChooseBankListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.onChooseBankListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_banklist);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext);
        params.height = ScreentUtils.getInstance().getScreenHeight(mContext) / 5 * 3;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initDatas();
        initViews();
    }

    private void initDatas() {
        list.add(new Bank("中国银行", true));
        list.add(new Bank("工商银行", false));
        list.add(new Bank("农业银行", false));
        list.add(new Bank("建设银行", false));
        list.add(new Bank("招商银行", false));
        list.add(new Bank("兴业银行", false));
        list.add(new Bank("浦发银行", false));
        list.add(new Bank("民生银行", false));
        list.add(new Bank("广发银行", false));
        list.add(new Bank("南京银行", false));
        list.add(new Bank("渤海银行", false));
        list.add(new Bank("徽商银行", false));
        list.add(new Bank("杭州银行", false));
        list.add(new Bank("北京银行", false));
        selectedBank = list.get(0);
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new BankListDialogRecyclerAdapter(mContext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapter.setData(list);
        adapter.setOnBankChooseListener(this);
        mRecyclerView.setAdapter(adapter);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmTextView:
                if (null != onChooseBankListener) {
                    onChooseBankListener.onChooseBank(selectedBank);
                }
                dismiss();
                break;
        }
    }

    @Override
    public void onBankChoose(Bank bank) {
        selectedBank = bank;
    }

    private OnChooseBankListener onChooseBankListener;

    public interface OnChooseBankListener {
        void onChooseBank(Bank bank);
    }
}
