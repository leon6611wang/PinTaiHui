package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcw.library.imagepicker.ImagePicker;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.ShopEnclosure;
import com.zhiyu.quanzhu.ui.adapter.ShopVertifyGridAdapter;
import com.zhiyu.quanzhu.ui.toast.FailureToast;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GlideLoader;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.ThreadPoolUtils;
import com.zhiyu.quanzhu.utils.UploadImageUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.utils.Log;

/**
 * 商店认证
 */
public class ShopVertifyActivity extends BaseActivity implements View.OnClickListener, ShopVertifyGridAdapter.OnAddImageListener {
    private LinearLayout backLayout;
    private TextView titleTextView, confirmTextView;
    private GridView mGridView;
    private ShopVertifyGridAdapter adapter;
    private List<ShopEnclosure> shopEnclosureList = new ArrayList<>();
    private int circle_id, province, city, shop_type;
    private String company_name, company_licenseno, company_legal, company_address, shop_name, shop_mobile, province_name, city_name, shop_address, shop_type_name;


    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<ShopVertifyActivity> activityWeakReference;

        public MyHandler(ShopVertifyActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ShopVertifyActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 99:
                    FailureToast.getInstance(activity).show();
                    break;
                case 1:
                    if (activity.position >= 4 && (activity.position == activity.shopEnclosureList.size() - 1)) {
                        activity.shopEnclosureList.add(new ShopEnclosure("其他资质或许可证"));
                    }
                    activity.adapter.setList(activity.shopEnclosureList);
                    break;
                case 2:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (activity.baseResult.getCode() == 200) {
                        Intent intent = new Intent();
                        intent.putExtra("close", 1);
                        activity.setResult(10231, intent);
                        activity.finish();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_vertify);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        getIntentData();
        initData();
        initViews();
    }

    private void getIntentData() {
        circle_id = getIntent().getIntExtra("circle_id", 0);
        province = getIntent().getIntExtra("province", 0);
        city = getIntent().getIntExtra("city", 0);
        shop_type = getIntent().getIntExtra("shop_type", 0);
        company_name = getIntent().getStringExtra("company_name");
        company_licenseno = getIntent().getStringExtra("company_licenseno");
        company_legal = getIntent().getStringExtra("company_legal");
        company_address = getIntent().getStringExtra("company_address");
        shop_name = getIntent().getStringExtra("shop_name");
        shop_mobile = getIntent().getStringExtra("shop_mobile");
        province_name = getIntent().getStringExtra("province_name");
        city_name = getIntent().getStringExtra("city_name");
        shop_type_name = getIntent().getStringExtra("shop_type_name");
    }

    private void initData() {
        shopEnclosureList.add(new ShopEnclosure("上传营业执照"));
        shopEnclosureList.add(new ShopEnclosure("法人身份证正面"));
        shopEnclosureList.add(new ShopEnclosure("法人身份证反面"));
        shopEnclosureList.add(new ShopEnclosure("法人手持身份证"));
        shopEnclosureList.add(new ShopEnclosure("其他资质或许可证"));

    }


    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("资料上传");
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
        mGridView = findViewById(R.id.mGridView);
        adapter = new ShopVertifyGridAdapter(this);
        adapter.setOnAddImageListener(this);
        mGridView.setAdapter(adapter);
        adapter.setList(shopEnclosureList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.confirmTextView:
                if (StringUtils.isNullOrEmpty(shopEnclosureList.get(0).getUrl())) {
                    MessageToast.getInstance(this).show("请上传营业执照");
                    break;
                }
                if (StringUtils.isNullOrEmpty(shopEnclosureList.get(1).getUrl())) {
                    MessageToast.getInstance(this).show("请上传法人身份证正面");
                    break;
                }

                if (StringUtils.isNullOrEmpty(shopEnclosureList.get(2).getUrl())) {
                    MessageToast.getInstance(this).show("请上传法人身份证反面");
                    break;
                }
                if (StringUtils.isNullOrEmpty(shopEnclosureList.get(3).getUrl())) {
                    MessageToast.getInstance(this).show("请上传法人手持身份证");
                    break;
                }
                shopVertify();
                break;
        }
    }


    private int position;

    @Override
    public void onAddImage(int position) {
        if (shopEnclosureList.size() < 21) {
            this.position = position;
            selectImage();
        } else {
            MessageToast.getInstance(this).show("资料最多二十张");
        }
    }

    private ArrayList<String> imageList = new ArrayList<>();
    private final int SELECT_IMG_CODE = 10022;

    private void selectImage() {
        imageList.clear();
        if (!StringUtils.isNullOrEmpty(shopEnclosureList.get(position).getPath())) {
            imageList.add(shopEnclosureList.get(position).getPath());
        }
        ImagePicker.getInstance()
                .setTitle("图片选择")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(false)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                .setImagePaths(imageList)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(ShopVertifyActivity.this, SELECT_IMG_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_IMG_CODE && resultCode == RESULT_OK) {
            imageList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            shopEnclosureList.get(position).setPath(imageList.get(0));
            uploadImage();
        }
    }


    private void uploadImage() {
        ThreadPoolUtils.getInstance().init().execute(new Runnable() {
            @Override
            public void run() {
                UploadImageUtils.getInstance().uploadFile(UploadImageUtils.SHOP, imageList.get(0), new UploadImageUtils.OnUploadCallback() {
                    @Override
                    public void onUploadSuccess(String name) {
                        shopEnclosureList.get(position).setUrl(name);
                        Message message = myHandler.obtainMessage(1);
                        message.sendToTarget();
                    }
                });
            }
        });
    }


    private BaseResult baseResult;

    /**
     * 商店认证
     */
    private void shopVertify() {
        String other = null;
        if (shopEnclosureList.size() > 4) {
            List<String> otherList = new ArrayList<>();
            for (int i = 4; i < shopEnclosureList.size(); i++) {
                otherList.add(shopEnclosureList.get(i).getUrl());
            }
            other = GsonUtils.GsonString(otherList);
        }
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SHOP_APPLY);
        params.addBodyParameter("circle_id", String.valueOf(circle_id));//类型 1个人 2店铺
        params.addBodyParameter("company_name", company_name);//企业名称
        params.addBodyParameter("company_address", company_address);//企业地址
        params.addBodyParameter("company_legal", company_legal);//企业法人
        params.addBodyParameter("company_licenseno", company_licenseno);//营业执照编号
        params.addBodyParameter("shop_name", shop_name);//店铺名称
        params.addBodyParameter("shop_mobile", shop_mobile);//店铺联系方式
        params.addBodyParameter("province", String.valueOf(province));
        params.addBodyParameter("province_name", province_name);
        params.addBodyParameter("city", String.valueOf(city));
        params.addBodyParameter("city_name", city_name);
        params.addBodyParameter("shop_type", String.valueOf(shop_type));
        params.addBodyParameter("shop_type_name", shop_type_name);
        params.addBodyParameter("company_license", shopEnclosureList.get(0).getUrl());//营业执照
        params.addBodyParameter("identity_front", shopEnclosureList.get(1).getUrl());//身份证正面
        params.addBodyParameter("identity_back", shopEnclosureList.get(2).getUrl());//身份证反面
        params.addBodyParameter("people_identity_front", shopEnclosureList.get(3).getUrl());//手持身份证
        params.addBodyParameter("other", other);//其他证明
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message=myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message=myHandler.obtainMessage(99);
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
