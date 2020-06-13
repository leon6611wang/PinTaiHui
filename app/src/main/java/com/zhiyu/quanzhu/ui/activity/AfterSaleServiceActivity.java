package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcw.library.imagepicker.ImagePicker;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.OrderInformationGoods;
import com.zhiyu.quanzhu.model.bean.UploadImage;
import com.zhiyu.quanzhu.ui.adapter.PublishFeedImagesGridAdapter;
import com.zhiyu.quanzhu.ui.dialog.DeleteImageDialog;
import com.zhiyu.quanzhu.ui.dialog.GoodsStatusDialog;
import com.zhiyu.quanzhu.ui.dialog.RefundReasonDialog;
import com.zhiyu.quanzhu.ui.dialog.ServiceTypeDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GlideLoader;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.ImageUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.PriceParseUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.UploadImageUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class AfterSaleServiceActivity extends BaseActivity implements View.OnClickListener, PublishFeedImagesGridAdapter.OnAddImagesListener, PublishFeedImagesGridAdapter.OnDeleteImageListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private int order_id;
    private OrderInformationGoods goods;
    private MyRecyclerView mRecyclerView;
    private PublishFeedImagesGridAdapter imageGridAdapter;
    private NiceImageView goodsImageImageView;
    private TextView goodsNameTextView, goodsNormsTextView, goodsCountTextView;
    private LinearLayout serviceTypeLayout, goodsStatusLayout, refundReasonLayout;
    private TextView serviceTypeTextView, goodsStatusTextView, reasonTextView,
            refundPriceTextView, confirmTextView;
    private EditText refundMarkEditText;
    private ServiceTypeDialog serviceTypeDialog;
    private GoodsStatusDialog goodsStatusDialog;
    private RefundReasonDialog refundReasonDialog;
    private DeleteImageDialog deleteImageDialog;
    private int serviceTypeIndex = -1, goodsStatusIndex = -1;
    private String refundReason;
    private int isUpdate;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<AfterSaleServiceActivity> activityWeakReference;

        public MyHandler(AfterSaleServiceActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AfterSaleServiceActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 99:
                    MessageToast.getInstance(activity).show("服务器内部错误，请稍后再试.");
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
        setContentView(R.layout.activity_after_sale_service);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        order_id = getIntent().getIntExtra("order_id", 0);
        isUpdate = getIntent().getIntExtra("isUpdate", 0);
        String goodsJson = getIntent().getStringExtra("goodsJson");
        goods = GsonUtils.GsonToBean(goodsJson, OrderInformationGoods.class);
        initDialogs();
        initViews();
    }

    private void initDialogs() {
        serviceTypeDialog = new ServiceTypeDialog(this, R.style.dialog, new ServiceTypeDialog.OnServiceTypeListener() {
            @Override
            public void onServiceType(int typeIndex, String servieType) {
                serviceTypeIndex = typeIndex;
                System.out.println("serviceTypeIndex: " + serviceTypeIndex);
                serviceTypeTextView.setText(servieType);
            }
        });

        goodsStatusDialog = new GoodsStatusDialog(this, R.style.dialog, new GoodsStatusDialog.OnGoodsStatusListener() {
            @Override
            public void onGoodsSatus(int statusIndex, String status) {
                goodsStatusIndex = statusIndex;
                System.out.println("goodsStatusIndex: " + goodsStatusIndex);
                goodsStatusTextView.setText(status);
            }
        });
        refundReasonDialog = new RefundReasonDialog(this, R.style.dialog, new RefundReasonDialog.OnRefundReasonListener() {
            @Override
            public void onRefundReason(String reason) {
                refundReason = reason;
                System.out.println("refundReason: " + refundReason);
                reasonTextView.setText(reason);
            }
        });
        deleteImageDialog = new DeleteImageDialog(this, R.style.dialog, new DeleteImageDialog.OnDeleteImageClickListener() {
            @Override
            public void onDeleteImage() {
                mImageList.remove(deletePosition);
                imagesUploadList.remove(deletePosition);
                imageGridAdapter.setData(mImageList);
            }
        });
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("售后服务");
        goodsImageImageView = findViewById(R.id.goodsImageImageView);
        Glide.with(this).load(goods.getGoods_img()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(goodsImageImageView);
        goodsNameTextView = findViewById(R.id.goodsNameTextView);
        goodsNameTextView.setText(goods.getGoods_name());
        goodsNormsTextView = findViewById(R.id.goodsNormsTextView);
        goodsNormsTextView.setText(goods.getNorms_name());
        goodsCountTextView = findViewById(R.id.goodsCountTextView);
        goodsCountTextView.setText("X" + goods.getGoods_num());
        serviceTypeLayout = findViewById(R.id.serviceTypeLayout);
        serviceTypeLayout.setOnClickListener(this);
        serviceTypeTextView = findViewById(R.id.serviceTypeTextView);
        goodsStatusLayout = findViewById(R.id.goodsStatusLayout);
        goodsStatusLayout.setOnClickListener(this);
        goodsStatusTextView = findViewById(R.id.goodsStatusTextView);
        refundReasonLayout = findViewById(R.id.refundReasonLayout);
        refundReasonLayout.setOnClickListener(this);
        reasonTextView = findViewById(R.id.reasonTextView);
        refundPriceTextView = findViewById(R.id.refundPriceTextView);
        refundPriceTextView.setText("￥" + PriceParseUtils.getInstance().parsePrice(goods.getRefund_price()));
        refundMarkEditText = findViewById(R.id.refundMarkEditText);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);


        mImageList.add("add");
        mRecyclerView = findViewById(R.id.mRecyclerView);
        imageGridAdapter = new PublishFeedImagesGridAdapter(this);
        imageGridAdapter.setOnAddImagesListener(this);
        imageGridAdapter.setOnDeleteImageListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        imageGridAdapter.setData(mImageList);
        mRecyclerView.setAdapter(imageGridAdapter);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.serviceTypeLayout:
                serviceTypeDialog.show();
                break;
            case R.id.goodsStatusLayout:
                goodsStatusDialog.show();
                break;
            case R.id.refundReasonLayout:
                refundReasonDialog.show();
                break;
            case R.id.confirmTextView:
                if (serviceTypeIndex == -1) {
                    MessageToast.getInstance(this).show("请选择服务类型");
                    break;
                }
                if (goodsStatusIndex == -1) {
                    MessageToast.getInstance(this).show("请选择货物状态");
                    break;
                }
                if (StringUtils.isNullOrEmpty(refundReason)) {
                    MessageToast.getInstance(this).show("请选择退款原因");
                    break;
                }
                switch (isUpdate) {
                    case 0:
                        refund();
                        break;
                    case 1:

                        break;
                }

                break;
        }
    }

    private ArrayList<String> mImageList = new ArrayList<>();
    private LinkedHashMap<String, String> map = new LinkedHashMap<>();
    private final int REQUEST_SELECT_IMAGES_CODE = 1001;

    private void selectImages() {
        mImageList.remove("add");
        ImagePicker.getInstance()
                .setTitle("图片选择")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(false)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(9)//设置最大选择图片数目(默认为1，单选)
                .setImagePaths(mImageList)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(AfterSaleServiceActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    private List<UploadImage> uploadImageList = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            mImageList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            uploadImages();
            mImageList.add("add");
            imageGridAdapter.setData(mImageList);
        }
    }

    private List<String> imagesUploadList = new ArrayList<>();

    private void uploadImages() {
        for (final String path : mImageList) {
            if (!map.containsKey(path)) {
                UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLEFEES, path, new UploadImageUtils.OnUploadCallback() {
                    @Override
                    public void onUploadSuccess(String name) {
                        map.put(path, name);
                        imagesUploadList.add(name);
                    }
                });
            }
        }
    }

    @Override
    public void onAddImages() {
        selectImages();
    }

    private int deletePosition;

    @Override
    public void onDeleteImage(int position) {
        this.deletePosition = position;
        deleteImageDialog.show();
    }

    private BaseResult baseResult;

    private void refund() {
        if (null != map && map.size() > 0) {
            List<String> list = imageGridAdapter.getList();
            uploadImageList.clear();
            for (String key : list) {
                if (!key.equals("add"))
                    uploadImageList.add(ImageUtils.getInstance().getUploadImage(key, map.get(key)));
            }
        }
        if (null == uploadImageList || uploadImageList.size() == 0) {
            MessageToast.getInstance(this).show("请上传图片凭证.");
            return;
        }
        System.out.println(GsonUtils.GsonString(uploadImageList));
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ORDER_REFUND);
        params.addBodyParameter("oid", String.valueOf(order_id));
        params.addBodyParameter("goods_item_ids", String.valueOf(goods.getId()));
        params.addBodyParameter("type", String.valueOf(serviceTypeIndex));
        params.addBodyParameter("wuliu_status", String.valueOf(goodsStatusIndex));
        params.addBodyParameter("reason", refundReason);
        params.addBodyParameter("remark", refundMarkEditText.getText().toString().trim());
        params.addBodyParameter("imgs", GsonUtils.GsonString(uploadImageList));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("refund: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
                System.out.println("refund: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }


    private void updateRefund() {
        if (null != map && map.size() > 0) {
            List<String> list = imageGridAdapter.getList();
            uploadImageList.clear();
            for (String imgStr : list) {
                if (imgStr.startsWith("http://") || imgStr.startsWith("https://")) {

                } else {

                }
            }
            for (String key : list) {
                if (!key.equals("add"))
                    uploadImageList.add(ImageUtils.getInstance().getUploadImage(key, map.get(key)));
            }
        }
        System.out.println(GsonUtils.GsonString(uploadImageList));
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ORDER_UPDATE_REFUND);
        params.addBodyParameter("oid", String.valueOf(order_id));
        params.addBodyParameter("goods_item_ids", String.valueOf(goods.getId()));
        params.addBodyParameter("type", String.valueOf(serviceTypeIndex));
        params.addBodyParameter("wuliu_status", String.valueOf(goodsStatusIndex));
        params.addBodyParameter("reason", refundReason);
        params.addBodyParameter("remark", refundMarkEditText.getText().toString().trim());
        params.addBodyParameter("imgs", GsonUtils.GsonString(uploadImageList));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("refund: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("refund: " + ex.toString());
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
