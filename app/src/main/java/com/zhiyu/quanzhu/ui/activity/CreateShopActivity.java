package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.AreaCity;
import com.zhiyu.quanzhu.model.bean.AreaProvince;
import com.zhiyu.quanzhu.model.bean.ShopType;
import com.zhiyu.quanzhu.model.result.ShopSearchResult;
import com.zhiyu.quanzhu.ui.adapter.CreateShopListAdapter;
import com.zhiyu.quanzhu.ui.dialog.LoadingDialog;
import com.zhiyu.quanzhu.ui.dialog.ProvinceCityDialog;
import com.zhiyu.quanzhu.ui.dialog.ShopTypeDialog;
import com.zhiyu.quanzhu.ui.toast.FailureToast;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SoftKeyboardUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

/**
 * 开店申请
 */
public class CreateShopActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView, nextTextView;
    private EditText yingyezhizhaoEditText, dianhuaEditText, mingchengEditText;
    private TextView xinyongdaimaTextView, farenTextView, dizhiTextView, guishudiTextView, leixingTextView;
    private LinearLayout guishudiLayout, leixingLayout, contentLayout;
    private RelativeLayout closeLayout;
    private ListView nameListView;
    private CreateShopListAdapter adapter;
    private ProvinceCityDialog provinceCityDialog;
    private ShopTypeDialog shopTypeDialog;
    private LoadingDialog loadingDialog;
    private ShopType shopType;
    private AreaProvince areaProvince;
    private AreaCity areaCity;
    private int circle_id;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CreateShopActivity> activityWeakReference;

        public MyHandler(CreateShopActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CreateShopActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 99:
                    activity.loadingDialog.dismiss();
                    FailureToast.getInstance(activity).show();
                    activity.isSearching = false;
                    break;
                case 100:
                    activity.loadingDialog.dismiss();
                    MessageToast.getInstance(activity).show(activity.shopSearchResult.getMsg());
                    activity.isSearching = false;
                    break;
                case 1:
                    activity.isSearching = false;
                    if (200 == activity.shopSearchResult.getCode()) {
                        activity.adapter.setList(activity.shopSearchResult.getData().getList());
                        activity.nameListView.setVisibility(View.VISIBLE);
                        activity.contentLayout.setVisibility(View.GONE);
                    } else {
                        activity.nameListView.setVisibility(View.GONE);
                        activity.contentLayout.setVisibility(View.VISIBLE);
                    }
                    break;
                case 2:
                    activity.loadingDialog.dismiss();
                    activity.yingyezhizhaoEditText.setText(activity.shopSearchResult.getData().getList().get(0).getName());
                    activity.xinyongdaimaTextView.setText(activity.shopSearchResult.getData().getList().get(0).getCreditNo());
                    activity.farenTextView.setText(activity.shopSearchResult.getData().getList().get(0).getPartnerName());
                    activity.dizhiTextView.setText(activity.shopSearchResult.getData().getList().get(0).getAddress());
                    activity.isSearching = false;
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        circle_id = getIntent().getIntExtra("circle_id", 0);
        initViews();
        initDialogs();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("开店申请");
        closeLayout = findViewById(R.id.closeLayout);
        closeLayout.setOnClickListener(this);
        yingyezhizhaoEditText = findViewById(R.id.yingyezhizhaoEditText);
        yingyezhizhaoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = yingyezhizhaoEditText.getText().toString().trim();
                if (!StringUtils.isNullOrEmpty(str)) {
                    closeLayout.setVisibility(View.VISIBLE);
                    if (!isSearching) {
                        shopSearch(str, 1);
                    }
                } else {
                    closeLayout.setVisibility(View.INVISIBLE);
                    nameListView.setVisibility(View.GONE);
                    contentLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        xinyongdaimaTextView = findViewById(R.id.xinyongdaimaTextView);
        farenTextView = findViewById(R.id.farenTextView);
        dizhiTextView = findViewById(R.id.dizhiTextView);
        dianhuaEditText = findViewById(R.id.dianhuaEditText);
        mingchengEditText = findViewById(R.id.mingchengEditText);
        guishudiLayout = findViewById(R.id.guishudiLayout);
        guishudiLayout.setOnClickListener(this);
        guishudiTextView = findViewById(R.id.guishudiTextView);
        leixingLayout = findViewById(R.id.leixingLayout);
        leixingLayout.setOnClickListener(this);
        leixingTextView = findViewById(R.id.leixingTextView);
        nextTextView = findViewById(R.id.nextTextView);
        nextTextView.setOnClickListener(this);
        contentLayout = findViewById(R.id.contentLayout);
        nameListView = findViewById(R.id.nameListView);
        adapter = new CreateShopListAdapter();
        nameListView.setAdapter(adapter);
        nameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isSearching) {
                    shopSearch(shopSearchResult.getData().getList().get(position).getName(), 2);
                    SoftKeyboardUtil.hideSoftKeyboard(CreateShopActivity.this);
                    nameListView.setVisibility(View.GONE);
                    contentLayout.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private boolean isSearching = false;

    private void initDialogs() {
        provinceCityDialog = new ProvinceCityDialog(this, R.style.dialog, new ProvinceCityDialog.OnCityChooseListener() {
            @Override
            public void onCityChoose(AreaProvince province, AreaCity city) {
                areaProvince = province;
                areaCity = city;
                System.out.println("province_code: " + areaProvince.getCode() + " , city_code: " + areaCity.getCode());
                guishudiTextView.setText(province.getName() + " " + city.getName());
            }
        });
        shopTypeDialog = new ShopTypeDialog(this, R.style.dialog, new ShopTypeDialog.OnShopTypeSelectListener() {
            @Override
            public void onShopTypeSelect(ShopType type) {
                shopType = type;
                leixingTextView.setText(shopType.getName());

            }
        });

        loadingDialog = new LoadingDialog(this, R.style.dialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.guishudiLayout:
                provinceCityDialog.show();
                break;
            case R.id.leixingLayout:
                shopTypeDialog.show();
                break;
            case R.id.nextTextView:
                String company_name = yingyezhizhaoEditText.getText().toString().trim();
                String company_licenseno = xinyongdaimaTextView.getText().toString().trim();
                String company_legal = farenTextView.getText().toString().trim();
                String company_address = dizhiTextView.getText().toString().trim();
                if (StringUtils.isNullOrEmpty(company_licenseno)) {
                    MessageToast.getInstance(this).show("请输入正确的营业执照名称进行查询");
                    break;
                }
                String shop_name = mingchengEditText.getText().toString().trim();
                if (StringUtils.isNullOrEmpty(shop_name)) {
                    MessageToast.getInstance(this).show("请输入店铺名称");
                    break;
                }
                String shop_mobile = dianhuaEditText.getText().toString().trim();
                if (StringUtils.isNullOrEmpty(shop_mobile)) {
                    MessageToast.getInstance(this).show("请输入店铺联系人电话");
                    break;
                }
                if (null == areaProvince || null == areaCity) {
                    MessageToast.getInstance(this).show("请选择店铺归属地");
                    break;
                }
                if (null == shopType) {
                    MessageToast.getInstance(this).show("请选择店铺类型");
                    break;
                }

                Intent shopVertifyIntent = new Intent(this, ShopVertifyActivity.class);
                shopVertifyIntent.putExtra("circle_id", circle_id);
                shopVertifyIntent.putExtra("company_name", company_name);
                shopVertifyIntent.putExtra("company_licenseno", company_licenseno);
                shopVertifyIntent.putExtra("company_legal", company_legal);
                shopVertifyIntent.putExtra("company_address", company_address);
                shopVertifyIntent.putExtra("shop_name", shop_name);
                shopVertifyIntent.putExtra("shop_mobile", shop_mobile);
                shopVertifyIntent.putExtra("province", areaProvince.getCode());
                shopVertifyIntent.putExtra("province_name", areaProvince.getName());
                shopVertifyIntent.putExtra("city", (int) areaCity.getCode());
                shopVertifyIntent.putExtra("city_name", areaCity.getName());
                shopVertifyIntent.putExtra("shop_type", shopType.getId());
                shopVertifyIntent.putExtra("shop_type_name", shopType.getName());
                startActivityForResult(shopVertifyIntent, 10231);
                break;
            case R.id.closeLayout:
                yingyezhizhaoEditText.setText(null);
                break;
        }
    }

    /**
     * 企查查
     * shopType:1模糊查询，2精准查询
     *
     * @param keywords
     */
    private ShopSearchResult shopSearchResult;

    private void shopSearch(String keywords, final int type) {
        if (type == 2) {
            loadingDialog.show();
        }

        isSearching = true;
        System.out.println("keywords: " + keywords + " , type: " + type);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.QI_CHA_CHA);
        params.addBodyParameter("keywords", keywords);
        params.addBodyParameter("type", String.valueOf(type));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                shopSearchResult = GsonUtils.GsonToBean(result, ShopSearchResult.class);
                if (shopSearchResult.getCode() == 200 && null != shopSearchResult.getData() && null != shopSearchResult.getData().getList() && shopSearchResult.getData().getList().size() > 0) {
                    Message message = myHandler.obtainMessage(type);
                    message.sendToTarget();
                } else if (shopSearchResult.getCode() != 200) {
                    Message message = myHandler.obtainMessage(100);
                    message.sendToTarget();
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10231) {
            int close = data.getIntExtra("close", 0);
            if (close == 1) {
                finish();
            }
        }
    }
}
