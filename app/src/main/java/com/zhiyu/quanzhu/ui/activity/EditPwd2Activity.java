package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;

/**
 * 我的-设置-账号与安全-修改密码2
 */
public class EditPwd2Activity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private EditText pwdEditText;
    private ImageView showPwdImageView;
    private TextView wanchengButtonTextView;
    private boolean isShowPwd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pwd2);
        initViews();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("修改密码");
        pwdEditText = findViewById(R.id.pwdEditText);
        showPwdImageView = findViewById(R.id.showPwdImageView);
        showPwdImageView.setOnClickListener(this);
        wanchengButtonTextView = findViewById(R.id.wanchengButtonTextView);
        wanchengButtonTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.showPwdImageView:
                if (isShowPwd) {
                    isShowPwd = false;
                    pwdEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    Glide.with(this).load(R.mipmap.eye_close).into(showPwdImageView);
                } else {
                    isShowPwd = true;
                    pwdEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    Glide.with(this).load(R.mipmap.eye_open).into(showPwdImageView);
                }
                break;
            case R.id.wanchengButtonTextView:

                break;
        }
    }
}
