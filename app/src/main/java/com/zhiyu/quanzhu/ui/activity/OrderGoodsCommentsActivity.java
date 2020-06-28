package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcw.library.imagepicker.ImagePicker;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.CommentOrderParam;
import com.zhiyu.quanzhu.model.bean.OrderGoods;
import com.zhiyu.quanzhu.model.bean.OrderShop;
import com.zhiyu.quanzhu.model.bean.UploadImage;
import com.zhiyu.quanzhu.ui.adapter.OrderGoodsCommentsAdapter;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GlideLoader;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.UploadImageUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class OrderGoodsCommentsActivity extends BaseActivity implements View.OnClickListener, OrderGoodsCommentsAdapter.OnAddImageListener {
    private LinearLayout backLayout;
    private TextView titleTextView, confirmCommentTextView;
    private RecyclerView mRecyclerView;
    private OrderGoodsCommentsAdapter adapter;
    private OrderShop orderShop;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<OrderGoodsCommentsActivity> activityWeakReference;

        public MyHandler(OrderGoodsCommentsActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            OrderGoodsCommentsActivity activity = activityWeakReference.get();
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
        setContentView(R.layout.activity_order_goods_comments);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        String orderShopJson = getIntent().getStringExtra("orderShop");
        orderShop = GsonUtils.GsonToBean(orderShopJson, OrderShop.class);
        initViews();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("商品评价");
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new OrderGoodsCommentsAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter.setList(orderShop.getGoods());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapter.setOnAddImageListener(this);

        confirmCommentTextView = findViewById(R.id.confirmCommentTextView);
        confirmCommentTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.confirmCommentTextView:
                List<OrderGoods> list = adapter.getList();
                if (null != list && list.size() > 0) {
                    for (OrderGoods goods : list) {
                        if (goods.getScore() == 0) {
                            MessageToast.getInstance(OrderGoodsCommentsActivity.this).show("请对所有商品做出评价~");
                            return;
                        }
                    }
                }
                commentOrder();
                break;
        }
    }

    private int adapterPosition;

    @Override
    public void onAddImage(int position, ArrayList<String> list) {
        this.adapterPosition = position;
        selectImages(list);
    }


    private final int REQUEST_SELECT_IMAGES_CODE = 1001;

    private void selectImages(ArrayList<String> list) {
        this.mImageList = list;
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
                .start(OrderGoodsCommentsActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            mImageList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            mImageList.add("add");
            orderShop.getGoods().get(adapterPosition).setCommentImageList(mImageList);
            adapter.setList(orderShop.getGoods());
            uploadImages();
        }
    }

    private ArrayList<String> mImageList = new ArrayList<>();
    private LinkedHashMap<String, String> map = new LinkedHashMap<>();

    private void uploadImages() {
        map.clear();
        for (final String path : mImageList) {
            if (!map.containsKey(path)) {
                UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLEFEES, path, new UploadImageUtils.OnUploadCallback() {
                    @Override
                    public void onUploadSuccess(String name) {
                        map.put(path, name);
                        orderShop.getGoods().get(adapterPosition).getUrlList().add(name);
                    }
                });
            }
        }

    }

    private BaseResult baseResult;

    private void commentOrder() {
        List<OrderGoods> list = adapter.getList();
        List<CommentOrderParam> paramList = new ArrayList<>();
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                CommentOrderParam param = new CommentOrderParam();
                param.setContent(list.get(i).getCommentContent());
                param.setGoods_id(list.get(i).getGoods_id());
                param.setGoods_item_id(list.get(i).getId());
                param.setGoods_name(list.get(i).getGoods_name());
                param.setGoods_price(list.get(i).getGoods_price());
                param.setNorms_name(list.get(i).getGoods_normas_name());
                param.setSorce(list.get(i).getScore());
                param.setShop_id(orderShop.getShop_id());
                param.setImg(orderShop.getGoods().get(i).getUrlList());
                paramList.add(param);
            }
        }
        String json = GsonUtils.GsonString(paramList);
        System.out.println("json: " + json);
//        System.out.println("oid: " + orderShop.getId());
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ORDER_COMMENT);
        params.addBodyParameter("oid", String.valueOf(orderShop.getId()));
        params.addBodyParameter("arr", json);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("商品评价: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
                System.out.println("comment order: " + ex.toString());
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
