package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
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
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 修改密码第一步
 */
public class EditPwdFirstActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private int payOrLoginPwd = 0;//0:登录密码；1:支付密码
    private TextView contentTextView, timeCountTextView, nextTextView;
    private EditText vertifyCodeEditText;
    private String content;
    private String phoneNumber = "18757591055";
    private Timer timer;
    private TimerTask task;
    private final int COUNT = 60;
    private int timeCount = COUNT;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<EditPwdFirstActivity> activityWeakReference;

        public MyHandler(EditPwdFirstActivity activity) {
            activityWeakReference = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            EditPwdFirstActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 0:
                    if (activity.timeCount > 0) {
                        activity.timeCountTextView.setClickable(false);
                        activity.timeCountTextView.setText(activity.timeCount + " s");
                        activity.timeCountTextView.setTextColor(activity.getResources().getColor(R.color.text_color_gray));
                    } else {
                        activity.timeCountTextView.setClickable(true);
                        activity.timeCountTextView.setText("获取验证码");
                        activity.timeCountTextView.setTextColor(activity.getResources().getColor(R.color.text_color_yellow));
                    }

                    break;
                case 1:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pwd_first);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        payOrLoginPwd = getIntent().getIntExtra("payOrLoginPwd", 0);
//         phoneNumber = SharedPreferencesUtils.getInstance(this).getUserPhoneNumber();
        String subPhoneNumber = phoneNumber.substring(phoneNumber.length() - 4, phoneNumber.length());
        content = "我们已经发送了一条包含验证码数字的短信到您尾号 <font color='#FE8627'>" + subPhoneNumber + "</font> 的手机上，请在下方输入您收到的验证码信息";
        initViews();
        initTimerTask();
        timeCountGetVertifyCode();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("更改" + (payOrLoginPwd == 0 ? "登录" : "支付") + "密码");
        contentTextView = findViewById(R.id.contentTextView);
        contentTextView.setText(Html.fromHtml(content));
        timeCountTextView = findViewById(R.id.timeCountTextView);
        timeCountTextView.setOnClickListener(this);
        timeCountTextView.setClickable(false);
        nextTextView = findViewById(R.id.nextTextView);
        nextTextView.setOnClickListener(this);
        vertifyCodeEditText = findViewById(R.id.vertifyCodeEditText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.timeCountTextView:
                timeCountGetVertifyCode();
                break;
            case R.id.nextTextView:
                editPwdNext();
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


    private void editPwdNext() {
        String code = vertifyCodeEditText.getText().toString().trim();
        if (!StringUtils.isNullOrEmpty(code)) {
            Intent intent = new Intent(EditPwdFirstActivity.this, EditPwdSecondActivity.class);
            intent.putExtra("code", code);
            intent.putExtra("mobile", phoneNumber);
            intent.putExtra("payOrLoginPwd", payOrLoginPwd);
            startActivityForResult(intent, 996);
        } else {
            MessageToast.getInstance(this).show("请输入短信验证码");
        }
    }

    private BaseResult baseResult;

    private void getVertifiyCode() {
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GET_VERTIFY_CODE);
        params.addBodyParameter("mobile", phoneNumber);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 996) {
            Intent intent=new Intent();
            intent.putExtra("isPwd",true);
            setResult(997,intent);
            boolean isfinish = data.getBooleanExtra("isfinish", false);
            if (isfinish)
                finish();
        }
    }
}
