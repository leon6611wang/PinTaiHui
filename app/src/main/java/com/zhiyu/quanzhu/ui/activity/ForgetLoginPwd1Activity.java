package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.zhiyu.quanzhu.utils.PhoneNumberUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

public class ForgetLoginPwd1Activity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView, getVertifyCodeTextView, goToNextTextView;
    private EditText phoneNumberEdit, vertifyCodeEdit;
    private ImageView clearPhoneNumberImageView;
    private Timer timer;
    private TimerTask task;
    private final int COUNT=60;
    private int timeCount=COUNT;
    private MyHandler myHandler=new MyHandler(this);
    private static class MyHandler extends Handler{
        WeakReference<ForgetLoginPwd1Activity> bindPhoneNumberActivityWeakReference;
        public MyHandler(ForgetLoginPwd1Activity activity){
            bindPhoneNumberActivityWeakReference=new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ForgetLoginPwd1Activity activity=bindPhoneNumberActivityWeakReference.get();
            switch (msg.what){
                case 0:
                    if(activity.timeCount>0){
                        activity.getVertifyCodeTextView.setClickable(false);
                        activity.getVertifyCodeTextView.setText("获取验证码("+activity.timeCount+")");
                        activity.getVertifyCodeTextView.setTextColor(activity.getResources().getColor(R.color.text_color_gray));
                    }else{
                        activity.getVertifyCodeTextView.setClickable(true);
                        activity.getVertifyCodeTextView.setText("获取验证码");
                        activity.getVertifyCodeTextView.setTextColor(activity.getResources().getColor(R.color.text_color_yellow));
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_login_pwd_1);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
        initTimerTask();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("忘记密码");
        getVertifyCodeTextView=findViewById(R.id.getVertifyCodeTextView);
        getVertifyCodeTextView.setOnClickListener(this);
        goToNextTextView=findViewById(R.id.goToNextTextView);
        goToNextTextView.setOnClickListener(this);
        phoneNumberEdit=findViewById(R.id.phoneNumberEdit);
        vertifyCodeEdit=findViewById(R.id.vertifyCodeEdit);
        clearPhoneNumberImageView=findViewById(R.id.clearPhoneNumberImageView);
        clearPhoneNumberImageView.setOnClickListener(this);

        phoneNumberEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phoneNumberStr=phoneNumberEdit.getText().toString().trim();
                if(!TextUtils.isEmpty(phoneNumberStr)){
                    clearPhoneNumberImageView.setVisibility(View.VISIBLE);
                }else{
                    clearPhoneNumberImageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.getVertifyCodeTextView:
                String phoneNumber=phoneNumberEdit.getText().toString();
                if(PhoneNumberUtils.getInstance().isMobileNO(phoneNumber)){
                    timeCount=COUNT;
                    Field field;
                    try {
                        field = TimerTask.class.getDeclaredField("state");
                        field.setAccessible(true);
                        field.set(task, 0);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    timer.schedule(task,0,1000);
                }else{
                    Toast.makeText(this,"请输入正确手机号",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.goToNextTextView:
                if(!TextUtils.isEmpty(vertifyCodeEdit.getText().toString().trim())){
                    Intent intent=new Intent(ForgetLoginPwd1Activity.this,SetLoginPwdActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(this,"请输入验证码",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.clearPhoneNumberImageView:
                phoneNumberEdit.setText(null);
                break;
        }
    }

    private void initTimerTask(){
        if(null==timer){
            timer=new Timer();
        }
        if(null==task){
            task=new TimerTask() {
                @Override
                public void run() {
                    timeCount--;
                    if(timeCount>=0){
                        Message message=myHandler.obtainMessage(0);
                        message.sendToTarget();
                    }
                }
            };
        }
    }
}
