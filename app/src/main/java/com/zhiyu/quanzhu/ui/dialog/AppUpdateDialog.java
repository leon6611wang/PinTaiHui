package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;

public class AppUpdateDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView versionTextView, cancelTextView, updateTextView, contentTextView;

    public AppUpdateDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public void setData(String content, String version) {
        content = "1.新增视频下载功能 \n2.优化页面卡顿问题\n3.新增趣味模块\n4.修复已知BUG\n2.优化页面卡顿问题\n3.新增趣味模块\n4.修复已知BUG\n2.优化页面卡顿问题\n3.新增趣味模块\n4.修复已知BUG\n2.优化页面卡顿问题\n3.新增趣味模块\n4.修复已知BUG";
        contentTextView.setText(content);
        versionTextView.setText("V" + version);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_app_update);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(context) / 5 * 4;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        initViews();
    }

    private void initViews() {
        contentTextView = findViewById(R.id.contentTextView);
        versionTextView = findViewById(R.id.versionTextView);
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
        updateTextView = findViewById(R.id.updateTextView);
        updateTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelTextView:
                SharedPreferencesUtils.getInstance(context).saveAppNeglect(true);
                dismiss();
                break;
            case R.id.updateTextView:

                break;
        }
    }
}
