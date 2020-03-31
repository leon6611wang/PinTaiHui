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
import com.zhiyu.quanzhu.model.result.AddressDetailResult;
import com.zhiyu.quanzhu.ui.dialog.LoadingDialog;
import com.zhiyu.quanzhu.ui.dialog.ProvinceCityDialog;
import com.zhiyu.quanzhu.ui.toast.FailureToast;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.toast.SuccessToast;
import com.zhiyu.quanzhu.ui.widget.SwitchButton;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.PhoneNumberUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

/**
 * 新增/编辑收货地址
 */
public class AddEditAddressActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView, citySelectTextView, saveButtonTextView;
    private EditText nameEditText, phonenumEditText, addressEditText;
    private SwitchButton defaultAddressSwitchButton;
    private int is_def;
    private ProvinceCityDialog provinceCityDialog;
    private AreaProvince areaProvince;
    private AreaCity areaCity;
    private LoadingDialog loadingDialog;
    private String address_id;
    private boolean isEdit;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<AddEditAddressActivity> activityWeakReference;

        public MyHandler(AddEditAddressActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AddEditAddressActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.loadingDialog.dismiss();
                    if (activity.baseResult.getCode() == 200) {
                        SuccessToast.getInstance(activity).show();
                        activity.finish();
                    } else {
                        FailureToast.getInstance(activity).show();
                    }
                    break;
                case 2:
                    FailureToast.getInstance(activity).show();
                    break;
                case 3:
                    if (activity.detailResult.getCode() == 200) {
                        activity.nameEditText.setText(activity.detailResult.getData().getAddress().getName());
                        activity.phonenumEditText.setText(activity.detailResult.getData().getAddress().getPhone());
                        activity.addressEditText.setText(activity.detailResult.getData().getAddress().getAddress());
                        activity.citySelectTextView.setText(activity.detailResult.getData().getAddress().getProvince_name() + " " +
                                activity.detailResult.getData().getAddress().getCity_name());
                        activity.areaProvince = new AreaProvince();
                        activity.areaProvince.setCode(activity.detailResult.getData().getAddress().getProvince_id());
                        activity.areaProvince.setName(activity.detailResult.getData().getAddress().getProvince_name());
                        activity.areaCity = new AreaCity();
                        activity.areaCity.setCode(activity.detailResult.getData().getAddress().getCity_id());
                        activity.areaCity.setName(activity.detailResult.getData().getAddress().getCity_name());
                        activity.is_def = activity.detailResult.getData().getAddress().getIs_def();
                        if (activity.detailResult.getData().getAddress().getIs_def() == 1) {
                            activity.defaultAddressSwitchButton.open();
                        } else {
                            activity.defaultAddressSwitchButton.close();
                        }
                    } else {
                        MessageToast.getInstance(activity).show(activity.detailResult.getMsg());
                    }
                    break;
                case 4:
                    if (activity.baseResult.getCode() == 200) {
                        SuccessToast.getInstance(activity).show();
                        activity.finish();
                    } else {
                        FailureToast.getInstance(activity).show();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedit_address);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        address_id = getIntent().getStringExtra("address_id");
        System.out.println("address_id 2: " + address_id);
        isEdit = !StringUtils.isNullOrEmpty(address_id);
        initDialogs();
        initViews();
        if (isEdit) {
            addressDetail();
        }
    }

    private void initDialogs() {
        provinceCityDialog = new ProvinceCityDialog(this, R.style.dialog, new ProvinceCityDialog.OnCityChooseListener() {
            @Override
            public void onCityChoose(AreaProvince province, AreaCity city) {
                citySelectTextView.setText(province.getName() + " " + city.getName());
                areaProvince = province;
                areaCity = city;

            }
        });
        loadingDialog = new LoadingDialog(this, R.style.dialog);
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(isEdit ? "编辑地址" : "新增地址");
        citySelectTextView = findViewById(R.id.citySelectTextView);
        citySelectTextView.setOnClickListener(this);
        saveButtonTextView = findViewById(R.id.saveButtonTextView);
        saveButtonTextView.setOnClickListener(this);
        nameEditText = findViewById(R.id.nameEditText);
        phonenumEditText = findViewById(R.id.phonenumEditText);
        addressEditText = findViewById(R.id.addressEditText);
        defaultAddressSwitchButton = findViewById(R.id.defaultAddressSwitchButton);
        defaultAddressSwitchButton.setOnSwitchButtonListener(new SwitchButton.OnSwitchButtonListener() {
            @Override
            public void onOpen(boolean isOpen) {
                is_def = isOpen ? 1 : 0;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.citySelectTextView:
                provinceCityDialog.show();
                break;
            case R.id.saveButtonTextView:
                if (StringUtils.isNullOrEmpty(nameEditText.getText().toString().trim())) {
                    MessageToast.getInstance(this).show("请输入收件人姓名");
                    break;
                }
                if (StringUtils.isNullOrEmpty(phonenumEditText.getText().toString().trim()) || !PhoneNumberUtils.getInstance().isMobileNO(phonenumEditText.getText().toString().trim())) {
                    MessageToast.getInstance(this).show("请输入有效号码");
                    break;
                }
                if (null == areaProvince || null == areaCity) {
                    MessageToast.getInstance(this).show("请选择收货地址");
                    break;
                }
                if (StringUtils.isNullOrEmpty(addressEditText.getText().toString().trim())) {
                    MessageToast.getInstance(this).show("请输入详细地址");
                    break;
                }
                if (isEdit) {
                    editAddress();
                } else {
                    addAddress();
                }

                break;

        }
    }

    private BaseResult baseResult;

    private void addAddress() {
        loadingDialog.show();
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ADDRESS_ADD);
        params.addBodyParameter("province_id", String.valueOf(areaProvince.getCode()));
        params.addBodyParameter("province_name", areaProvince.getName());
        params.addBodyParameter("city_id", String.valueOf(areaCity.getCode()));
        params.addBodyParameter("city_name", areaCity.getName());
        params.addBodyParameter("name", nameEditText.getText().toString().trim());
        params.addBodyParameter("phone", phonenumEditText.getText().toString().trim());
        params.addBodyParameter("address", addressEditText.getText().toString().trim());
        params.addBodyParameter("is_def", String.valueOf(is_def));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("add address: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(2);
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

    private AddressDetailResult detailResult;

    private void addressDetail() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ADDRESS_DETAIL);
        params.addBodyParameter("id", address_id);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                detailResult = GsonUtils.GsonToBean(result, AddressDetailResult.class);
                Message message = myHandler.obtainMessage(3);
                message.sendToTarget();
                System.out.println("addressDetail: " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(2);
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

    private void editAddress() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ADDRESS_UPDATE);
        params.addBodyParameter("id", detailResult.getData().getAddress().get_id());
        params.addBodyParameter("province_id", String.valueOf(areaProvince.getCode()));
        params.addBodyParameter("province_name", areaProvince.getName());
        params.addBodyParameter("city_id", String.valueOf(areaCity.getCode()));
        params.addBodyParameter("city_name", areaCity.getName());
        params.addBodyParameter("name", nameEditText.getText().toString().trim());
        params.addBodyParameter("phone", phonenumEditText.getText().toString().trim());
        params.addBodyParameter("address", addressEditText.getText().toString().trim());
        params.addBodyParameter("is_def", String.valueOf(is_def));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(4);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(2);
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
