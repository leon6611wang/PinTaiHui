package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.AreaCity;
import com.zhiyu.quanzhu.model.bean.AreaProvince;
import com.zhiyu.quanzhu.model.bean.HobbyDaoChild;
import com.zhiyu.quanzhu.model.bean.HobbyDaoParent;
import com.zhiyu.quanzhu.model.bean.IndustryChild;
import com.zhiyu.quanzhu.model.bean.IndustryHobby;
import com.zhiyu.quanzhu.model.bean.IndustryParent;
import com.zhiyu.quanzhu.model.result.CircleInfoResult;
import com.zhiyu.quanzhu.ui.dialog.CircleTypeDialog;
import com.zhiyu.quanzhu.ui.dialog.HobbyDialog;
import com.zhiyu.quanzhu.ui.dialog.IndustryDialog;
import com.zhiyu.quanzhu.ui.dialog.IndustryHobbyDialog;
import com.zhiyu.quanzhu.ui.dialog.ProvinceCityDialog;
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
 * 圈子设置
 * 圈主
 */
public class CircleSettingActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout, xingzhiLayout, chengshiLayout, hangyeLayout;
    private TextView titleTextView, xingzhiTextView, chengshiTextView, industryTextView, hangyeTextView, confirmTextView;
    private EditText mingchengEditText;
    private CircleTypeDialog circleTypeDialog;
    private ProvinceCityDialog cityDialog;
    private IndustryHobbyDialog industryDialog;
    private IndustryHobbyDialog hobbyDialog;
    private int circleType = 1;
    private long circle_id;
    private String circle_name, province_name, city_name, descirption, logo, thumb, two_industry, three_industry, video;
    private int province, city, is_verify, is_price, price, status;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CircleSettingActivity> weakReference;

        public MyHandler(CircleSettingActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CircleSettingActivity activity = weakReference.get();
            switch (msg.what) {
                case 1:
                    if (200 == activity.circleInfoResult.getCode()) {
                        if (!StringUtils.isNullOrEmpty(activity.circleInfoResult.getData().getName())) {
                            activity.mingchengEditText.setText(activity.circleInfoResult.getData().getName());
                        }
                        activity.province = activity.circleInfoResult.getData().getProvince();
                        activity.province_name = activity.circleInfoResult.getData().getProvince_name();
                        activity.city = activity.circleInfoResult.getData().getCity();
                        activity.city_name = activity.circleInfoResult.getData().getCity_name();
                        activity.two_industry = activity.circleInfoResult.getData().getTwo_industry();
                        activity.three_industry = activity.circleInfoResult.getData().getThree_industry();
                        activity.circle_name = activity.circleInfoResult.getData().getName();
                        activity.circleType = activity.circleInfoResult.getData().getType();
                        activity.xingzhiTextView.setText(activity.circleInfoResult.getData().getType() == 1 ? "行业" : "兴趣");
                        activity.chengshiTextView.setText(activity.circleInfoResult.getData().getProvince_name() + " " + activity.circleInfoResult.getData().getCity_name());
                        activity.hangyeTextView.setText(activity.circleInfoResult.getData().getThree_industry());
                    }
                    break;
                case 2:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.finish();
                    }
                    break;
                case 99:
                    MessageToast.getInstance(activity).show("服务器内部错误，请稍后再试");
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_setting);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        circle_id = getIntent().getLongExtra("circle_id", 0l);
        initViews();
        initDialogs();
        circleBase();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("更改圈子信息");
        mingchengEditText = findViewById(R.id.mingchengEditText);
        xingzhiLayout = findViewById(R.id.xingzhiLayout);
        xingzhiLayout.setOnClickListener(this);
        xingzhiTextView = findViewById(R.id.xingzhiTextView);
        chengshiLayout = findViewById(R.id.chengshiLayout);
        chengshiLayout.setOnClickListener(this);
        chengshiTextView = findViewById(R.id.chengshiTextView);
        hangyeLayout = findViewById(R.id.hangyeLayout);
        hangyeLayout.setOnClickListener(this);
        industryTextView = findViewById(R.id.industryTextView);
        hangyeTextView = findViewById(R.id.hangyeTextView);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmTextView:
                circle_name = mingchengEditText.getText().toString().trim();
                updateCircleProfile();
                break;
            case R.id.backLayout:
                finish();
                break;
            case R.id.xingzhiLayout:
                circleTypeDialog.show();
                break;
            case R.id.chengshiLayout:
                cityDialog.show();
                break;
            case R.id.hangyeLayout:
                switch (circleType) {
                    case 1:
                        industryDialog.show();
                        break;
                    case 2:
                        hobbyDialog.show();
                        break;
                }
                break;

        }
    }

    private void initDialogs() {
        circleTypeDialog = new CircleTypeDialog(this, R.style.dialog, new CircleTypeDialog.OnCircleTypeListener() {
            @Override
            public void onCircleType(int t, String desc) {
                circleType = t;
                xingzhiTextView.setText(desc);
                industryTextView.setText(desc);
            }
        });
        cityDialog = new ProvinceCityDialog(this, R.style.dialog, new ProvinceCityDialog.OnCityChooseListener() {
            @Override
            public void onCityChoose(AreaProvince p, AreaCity c) {
                province = p.getCode();
                province_name = p.getName();
                city = (int) c.getCode();
                city_name = c.getName();
                chengshiTextView.setText(p.getName() + " " + c.getName());
            }
        });
        industryDialog = new IndustryHobbyDialog(this, R.style.dialog,true, new IndustryHobbyDialog.OnIndustryHobbySelectedListener() {
            @Override
            public void onIndustryHobbySelected(IndustryHobby parent, IndustryHobby child) {
                two_industry = parent.getName();
                three_industry = child.getName();
                hangyeTextView.setText(parent.getName() + "/" + child.getName());
            }
        });
        hobbyDialog = new IndustryHobbyDialog(this, R.style.dialog,false, new IndustryHobbyDialog.OnIndustryHobbySelectedListener() {
            @Override
            public void onIndustryHobbySelected(IndustryHobby parent, IndustryHobby child) {
                two_industry = parent.getName();
                three_industry = child.getName();
                hangyeTextView.setText(parent.getName() + "/" + child.getName());
            }
        });
    }

    private CircleInfoResult circleInfoResult;

    /**
     * 圈子详情-基础信息
     */
    private void circleBase() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CIRCLE_BASE);
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("circle info : " + result);
                circleInfoResult = GsonUtils.GsonToBean(result, CircleInfoResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("circle info error: " + ex.toString());
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

    private void updateCircleProfile() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.EDIT_CIRCLE_PROFILE);
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        params.addBodyParameter("type", String.valueOf(circleType));
        params.addBodyParameter("province_name", province_name);
        params.addBodyParameter("city_name", city_name);
        params.addBodyParameter("province", String.valueOf(province));
        params.addBodyParameter("city", String.valueOf(city));
        params.addBodyParameter("two_industry", two_industry);
        params.addBodyParameter("three_industry", three_industry);
        params.addBodyParameter("circle_name", circle_name);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
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

}
