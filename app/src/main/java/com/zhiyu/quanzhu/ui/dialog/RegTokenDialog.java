package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.CopyUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * yes or no dialog
 */
public class RegTokenDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView regTokenTextView, sharetxtTextView, copyTextView, shareTextView;
    private String regToken, shareUrl;

    public RegTokenDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public void setRegTokenData(String regToken, String shareTxt, String shareUrl) {
        this.regToken = regToken;
        this.shareUrl = shareUrl;
        regTokenTextView.setText(regToken);
        sharetxtTextView.setText(shareTxt);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_regtoken);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(context) / 5 * 4;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(R.drawable.shape_wait_dialog_bg);

        initViews();
    }

    private void initViews() {
        regTokenTextView = findViewById(R.id.regTokenTextView);
        sharetxtTextView = findViewById(R.id.sharetxtTextView);
        copyTextView = findViewById(R.id.copyTextView);
        copyTextView.setOnClickListener(this);
        shareTextView = findViewById(R.id.shareTextView);
        shareTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.copyTextView:
                CopyUtils.getInstance().copy(getContext(), regToken);
                MessageToast.getInstance(getContext()).show("邀请码已复制到剪贴板，发送给朋友吧~");
                dismiss();
                break;
            case R.id.shareTextView:
                MessageToast.getInstance(getContext()).show("分享邀请链接");
                dismiss();
                break;
        }
    }


}
