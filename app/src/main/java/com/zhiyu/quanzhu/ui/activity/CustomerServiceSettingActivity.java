package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.result.ServiceShopInfoResult;
import com.zhiyu.quanzhu.model.result.ShopProfileResult;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.SwitchButton;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

public class CustomerServiceSettingActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout, shopLayout, enterShopLayout, historyLayout, clearLayout, complaintLayout;
    private TextView titleTextView, shopNameTextView, appraiseTextView;
    private CircleImageView shopIconImageView;
    private SwitchButton silenceSwitchButton, blackSwitchButton;
    private YNDialog ynDialog;
    private int shop_id;
    private boolean isSilenceFirst = true, isBlackFirst = true;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CustomerServiceSettingActivity> activityWeakReference;

        public MyHandler(CustomerServiceSettingActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CustomerServiceSettingActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    Glide.with(activity).load(activity.serviceShopInfoResult.getData().getData().getShop_logo()).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                            .fallback(R.drawable.image_error).into(activity.shopIconImageView);
                    activity.shopNameTextView.setText(activity.serviceShopInfoResult.getData().getData().getShop_name());
                    activity.appraiseTextView.setText("好评率：" + activity.serviceShopInfoResult.getData().getData().getMark() + "%");
                    if (activity.serviceShopInfoResult.getData().getData().getIs_disturb() == 1) {
                        activity.silenceSwitchButton.open();
                    } else {
                        activity.silenceSwitchButton.close();
                    }
                    if (activity.serviceShopInfoResult.getData().getData().getIs_black() == 1) {
                        activity.blackSwitchButton.open();
                    } else {
                        activity.blackSwitchButton.close();
                    }
                    activity.isBlackFirst = false;
                    activity.isSilenceFirst = false;
                    break;
                case 2:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service_setting);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        shop_id = getIntent().getIntExtra("shop_id", 0);
        initDialogs();
        initViews();
        serviceShopInfo();
    }

    private void initDialogs() {
        ynDialog = new YNDialog(this, R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                clear();
            }
        });
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("通知设置");
        shopLayout = findViewById(R.id.shopLayout);
        shopLayout.setOnClickListener(this);
        enterShopLayout = findViewById(R.id.enterShopLayout);
        enterShopLayout.setOnClickListener(this);
        historyLayout = findViewById(R.id.historyLayout);
        historyLayout.setOnClickListener(this);
        clearLayout = findViewById(R.id.clearLayout);
        clearLayout.setOnClickListener(this);
        complaintLayout = findViewById(R.id.complaintLayout);
        complaintLayout.setOnClickListener(this);
        shopNameTextView = findViewById(R.id.shopNameTextView);
        appraiseTextView = findViewById(R.id.appraiseTextView);
        shopIconImageView = findViewById(R.id.shopIconImageView);
        silenceSwitchButton = findViewById(R.id.silenceSwitchButton);
        silenceSwitchButton.setOnSwitchButtonListener(new SwitchButton.OnSwitchButtonListener() {
            @Override
            public void onOpen(boolean isOpen) {
                if (!isSilenceFirst)
                    silence(isOpen ? 1 : 0);
            }
        });
        blackSwitchButton = findViewById(R.id.blackSwitchButton);
        blackSwitchButton.setOnSwitchButtonListener(new SwitchButton.OnSwitchButtonListener() {
            @Override
            public void onOpen(boolean isOpen) {
                if (!isBlackFirst)
                    black(isOpen ? 1 : 0);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.shopLayout:
                Intent shopProfileIntent = new Intent(this, ShopProfileActivity.class);
                shopProfileIntent.putExtra("shop_id", shop_id);
                startActivity(shopProfileIntent);
                break;
            case R.id.enterShopLayout:
                Intent shopInfoIntent = new Intent(this, ShopInformationActivity.class);
                shopInfoIntent.putExtra("shop_id", shop_id);
                startActivity(shopInfoIntent);
                break;
            case R.id.historyLayout:
                Intent historyIntent=new Intent(this,SearchServiceChatHistoryActivity.class);
                historyIntent.putExtra("shop_id", shop_id);
                startActivity(historyIntent);
                break;
            case R.id.clearLayout:
                ynDialog.show();
                ynDialog.setTitle("确定清除聊天记录？");
                break;
            case R.id.complaintLayout:
                Intent complaintIntent = new Intent(this, ComplaintActivity.class);
                complaintIntent.putExtra("report_id", shop_id);
                complaintIntent.putExtra("module_type", "store");
                startActivity(complaintIntent);
                break;
        }
    }


    private ServiceShopInfoResult serviceShopInfoResult;

    private void serviceShopInfo() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL_2 + ConstantsUtils.SERVICE_SHOP_INFO);
        params.addBodyParameter("shop_id", String.valueOf(shop_id));
        params.setAsJsonContent(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("serviceShopInfo: " + result);
                serviceShopInfoResult = GsonUtils.GsonToBean(result, ServiceShopInfoResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("serviceShopInfo: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private BaseResult baseResult;

    private void silence(int state) {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL_2 + ConstantsUtils.SERVICE_SILENCE);
        params.addBodyParameter("shop_id", String.valueOf(shop_id));
        params.addBodyParameter("state", String.valueOf(state));
        params.setAsJsonContent(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("silence: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("silence: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private void black(int state) {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL_2 + ConstantsUtils.SERVICE_BLACK);
        params.addBodyParameter("shop_id", String.valueOf(shop_id));
        params.addBodyParameter("state", String.valueOf(state));
        params.setAsJsonContent(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("black: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("black: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private void clear() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL_2 + ConstantsUtils.SERVICE_CLEAR_MESSAGE);
        params.addBodyParameter("shop_id", String.valueOf(shop_id));
        params.setAsJsonContent(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("clear: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("clear: " + ex.toString());
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
