package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.result.CommonQuestionResult;
import com.zhiyu.quanzhu.ui.adapter.ContactCustomerServiceAdapter;
import com.zhiyu.quanzhu.ui.dialog.ContactCustomerServiceDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

/**
 * 联系客服
 */
public class ContactCustomerServiceActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout, rightLayout;
    private TextView titleTextView, callServiceTextView;
    private ListView mListView;
    private TextView tijiaoButton;
    private ContactCustomerServiceAdapter adapter;
    private int menuImageY;
    private ImageView menuImage;
    private EditText questionEditText, phoneEditText;
    private final String phoneNumber = "400-8818-176";
    private ContactCustomerServiceDialog contactCustomerServiceDialog;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<ContactCustomerServiceActivity> weakReference;

        public MyHandler(ContactCustomerServiceActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ContactCustomerServiceActivity activity = weakReference.get();
            switch (msg.what) {
                case 99:
                    MessageToast.getInstance(activity).show("服务器内部错误，请稍后再试");
                    break;
                case 0:
                    activity.adapter.stList(activity.commonQuestionResult.getData().getList());
                    break;
                case 1:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.finish();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_customer_service);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
        initDialogs();
        commonQuestion();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("联系客服");
        mListView = findViewById(R.id.mListView);
        adapter = new ContactCustomerServiceAdapter();
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ContactCustomerServiceActivity.this, H5PageActivity.class);
                intent.putExtra("url", commonQuestionResult.getData().getList().get(position).getUrl());
                startActivity(intent);
            }
        });
        tijiaoButton = findViewById(R.id.tijiaoButton);
        tijiaoButton.setOnClickListener(this);
        phoneEditText = findViewById(R.id.phoneEditText);
        questionEditText = findViewById(R.id.questionEditText);
        callServiceTextView = findViewById(R.id.callServiceTextView);
        callServiceTextView.setOnClickListener(this);
        menuImage = findViewById(R.id.menuImage);
        menuImage.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        menuImage.getViewTreeObserver().removeOnPreDrawListener(this);
                        int[] location = new int[2];
                        menuImage.getLocationOnScreen(location);
                        menuImageY = location[1];
                        int menuviewheight = menuImage.getHeight(); // 获取高度
                        menuImageY += menuviewheight;
                        return true;
                    }
                });
    }

    private void initDialogs() {
        contactCustomerServiceDialog = new ContactCustomerServiceDialog(this, R.style.dialog, new ContactCustomerServiceDialog.OnCustomerServiceMenuChooseListener() {
            @Override
            public void onCustomerService() {
                Intent intent = new Intent(ContactCustomerServiceActivity.this, CustomerServiceActivity.class);
                intent.putExtra("shop_id", 1);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.callServiceTextView:
                callService2();
                break;
            case R.id.rightLayout:
                contactCustomerServiceDialog.show();
                contactCustomerServiceDialog.setMenuImageY(ScreentUtils.getInstance().px2dip(ContactCustomerServiceActivity.this, menuImageY) + 30);
                break;
            case R.id.tijiaoButton:
                if (StringUtils.isNullOrEmpty(questionEditText.getText().toString().trim())) {
                    MessageToast.getInstance(this).show("请描述您的问题");
                    return;
                }
                if (StringUtils.isNullOrEmpty(phoneEditText.getText().toString().trim())) {
                    MessageToast.getInstance(this).show("请留下您的联系方式");
                    return;
                }
                postQuestion();
                break;
        }
    }

    /**
     * 立即拨打
     */
    private void callService1() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNumber);
        intent.setData(data);
        startActivity(intent);
    }

    /**
     * 进入拨号界面
     */
    private void callService2() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNumber);
        intent.setData(data);
        startActivity(intent);
    }

    private BaseResult baseResult;

    private void postQuestion() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.COMMON_CUSTOMER);
        params.addBodyParameter("content", questionEditText.getText().toString().trim());
        params.addBodyParameter("contact", phoneEditText.getText().toString().trim());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private CommonQuestionResult commonQuestionResult;

    private void commonQuestion() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.COMMON_QUESION);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("commone quesion: " + result);
                commonQuestionResult = GsonUtils.GsonToBean(result, CommonQuestionResult.class);
                Message message = myHandler.obtainMessage(0);
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
