package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.PhoneNumberUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

public class UpdatePhoneNumberSecondActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout, closeLayout;
    private TextView titleTextView, timeCountTextView, cofirmTextView;
    private EditText phoneNumberEditText, vertifyCodeEditText;
    private Timer timer;
    private TimerTask task;
    private final int COUNT = 60;
    private int timeCount = COUNT;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<UpdatePhoneNumberSecondActivity> activityWeakReference;

        public MyHandler(UpdatePhoneNumberSecondActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            UpdatePhoneNumberSecondActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (activity.timeCount > 0) {
                        activity.timeCountTextView.setClickable(false);
                        activity.timeCountTextView.setTextColor(activity.getResources().getColor(R.color.text_color_gray));
                        activity.timeCountTextView.setText(activity.timeCount + " s");
                    } else {
                        activity.timeCountTextView.setClickable(true);
                        activity.timeCountTextView.setTextColor(activity.getResources().getColor(R.color.text_color_yellow));
                        activity.timeCountTextView.setText("获取验证码");
                    }
                    break;
                case 2:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if(200==activity.baseResult.getCode()){
                        Intent intent=new Intent();
                        activity.setResult(956,intent);
                        activity.finish();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phonenumber_second);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
        initTimerTask();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("更改手机号");
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!StringUtils.isNullOrEmpty(phoneNumberEditText.getText().toString().trim())){
                    closeLayout.setVisibility(View.VISIBLE);
                }else{
                    closeLayout.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        closeLayout = findViewById(R.id.closeLayout);
        closeLayout.setOnClickListener(this);
        vertifyCodeEditText = findViewById(R.id.vertifyCodeEditText);
        timeCountTextView = findViewById(R.id.timeCountTextView);
        timeCountTextView.setOnClickListener(this);
        cofirmTextView = findViewById(R.id.cofirmTextView);
        cofirmTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.closeLayout:
                phoneNumberEditText.setText("");
                break;
            case R.id.timeCountTextView:
                if(PhoneNumberUtils.getInstance().isMobileNO(phoneNumberEditText.getText().toString().trim())){
                    timeCountGetVertifyCode();
                }else{
                    MessageToast.getInstance(this).show("请输入正确的手机号");
                }
                break;
            case R.id.cofirmTextView:
                if(PhoneNumberUtils.getInstance().isMobileNO(phoneNumberEditText.getText().toString().trim())){
                    updatePhoneNumber();
                }else{
                    MessageToast.getInstance(this).show("请输入正确的手机号");
                }
                break;
        }
    }

    private void timeCountGetVertifyCode() {
        getVertifiyCode();
        timeCount = COUNT;
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
        timer.schedule(task, 0, 1000);
    }

    private void initTimerTask() {
        if (null == timer) {
            timer = new Timer();
        }
        if (null == task) {
            task = new TimerTask() {
                @Override
                public void run() {
                    timeCount--;
                    if (timeCount >= 0) {
                        Message message = myHandler.obtainMessage(0);
                        message.sendToTarget();
                    }
                }
            };
        }
    }

    private BaseResult baseResult;

    private void getVertifiyCode() {
        RequestParams params =MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GET_VERTIFY_CODE);
        params.addBodyParameter("mobile", phoneNumberEditText.getText().toString().trim());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void updatePhoneNumber(){
        RequestParams params= MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL+ConstantsUtils.BIND_PHONE_NUMBER);
        params.addBodyParameter("code",vertifyCodeEditText.getText().toString().trim());
        params.addBodyParameter("mobile",phoneNumberEditText.getText().toString().trim());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult=GsonUtils.GsonToBean(result,BaseResult.class);
                Message message=myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
