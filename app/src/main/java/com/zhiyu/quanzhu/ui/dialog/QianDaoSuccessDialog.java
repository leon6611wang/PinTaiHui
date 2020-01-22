package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.adapter.QianDaoRiQiRecyclerAdapter;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class QianDaoSuccessDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private RecyclerView mRecyclerView;
    private QianDaoRiQiRecyclerAdapter adapter;

    public void setDays(int days){
        adapter.setDays(days);
    }

    public QianDaoSuccessDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public QianDaoSuccessDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_qiandao_success);
        initViews();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext) / 5 * 4;
        params.height = ScreentUtils.getInstance().getScreenHeight(mContext) / 5 * 3;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }


    private void initViews() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new QianDaoRiQiRecyclerAdapter(mContext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeImageView:
                dismiss();
                break;
        }
    }

}
