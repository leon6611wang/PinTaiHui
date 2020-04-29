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

public class CheckInSuccessDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private RecyclerView mRecyclerView;
    private QianDaoRiQiRecyclerAdapter adapter;

    public void setDays(int days) {
        adapter.setDays(days);
    }

    public CheckInSuccessDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public CheckInSuccessDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_check_in_success);
        initViews();
        float ratio = (float) 272 / (float) 375;
        float heightRatio = (float) 276 / (float) 272;
        int width = Math.round(ScreentUtils.getInstance().getScreenWidth(mContext) * ratio);
        int height = Math.round(width * heightRatio);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = width;
        params.height = height;
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
