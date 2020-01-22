package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;

public class ForgetLoginPwd2Activity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private EditText pwdEdit;
    private ImageView pwdShowImageView;
    private TextView goToHomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_login_pwd_2);
        initViews();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("更改密码");
        backLayout.setOnClickListener(this);
        pwdEdit = findViewById(R.id.pwdEdit);
        pwdShowImageView = findViewById(R.id.clearPwdImageView);
        pwdShowImageView.setOnClickListener(this);
        goToHomeTextView=findViewById(R.id.goToHomeTextView);
        goToHomeTextView.setOnClickListener(this);
    }

    private boolean pwdIsShow=false;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.pwdShowImageView:
                if(pwdIsShow){
                    pwdIsShow=false;
                    pwdEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT );//设置密码不可见
                }else{
                    pwdIsShow=true;
                    pwdEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);//设置密码可见
                }
                break;
            case R.id.goToHomeTextView:
                if(pwdEdit.getText().toString().trim().length()<6){
                    Toast.makeText(this,"登录密码不小于6位",Toast.LENGTH_SHORT).show();
                }else if(pwdEdit.getText().toString().trim().length()>14){
                    Toast.makeText(this,"登录密码不大于14位",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"登录密码设置成功",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ForgetLoginPwd2Activity.this,HomeActivity2.class);
                    startActivity(intent);
                    finish();
                }

                break;
        }
    }
}
