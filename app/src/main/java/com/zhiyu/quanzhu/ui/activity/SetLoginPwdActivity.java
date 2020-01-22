package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;

public class SetLoginPwdActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private EditText pwdEdit;
    private ImageView clearPwdImageView;
    private TextView goToHomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_login_pwd);
        initViews();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("设置密码");
        backLayout.setOnClickListener(this);
        pwdEdit = findViewById(R.id.pwdEdit);
        clearPwdImageView = findViewById(R.id.clearPwdImageView);
        clearPwdImageView.setOnClickListener(this);
        pwdEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pwdStr = pwdEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(pwdStr)) {
                    clearPwdImageView.setVisibility(View.VISIBLE);
                } else {
                    clearPwdImageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        goToHomeTextView=findViewById(R.id.goToHomeTextView);
        goToHomeTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.clearPwdImageView:
                pwdEdit.setText(null);
                break;
            case R.id.goToHomeTextView:
                if(pwdEdit.getText().toString().trim().length()<6){
                    Toast.makeText(this,"登录密码不小于6位",Toast.LENGTH_SHORT).show();
                }else if(pwdEdit.getText().toString().trim().length()>14){
                    Toast.makeText(this,"登录密码不大于14位",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"登录密码设置成功",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(SetLoginPwdActivity.this,HomeActivity2.class);
                    startActivity(intent);
                    finish();
                }

                break;
        }
    }
}
