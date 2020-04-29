package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.result.PointGoodsInformationResult;
import com.zhiyu.quanzhu.ui.adapter.PointGoodsImageListAdapter;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.GoodsInfoBanner;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

/**
 * 积分商品详情
 */
public class PointGoodsInformationActivity extends BaseActivity implements View.OnClickListener {
    private int goods_id;
    private GoodsInfoBanner goodsInfoBanner;
    private ListView goodsInfoListView;
    private PointGoodsImageListAdapter adapter;
    private View listViewHeader;
    private TextView pointTextView, nameTextView, descTextView;
    private LinearLayout backLayout, headerLayout, titleLayout;
    private TextView titleTextView, exchangeButtonTextView;
    private ImageView shareImageView, backImageView;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<PointGoodsInformationActivity> activityWeakReference;

        public MyHandler(PointGoodsInformationActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PointGoodsInformationActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    if (activity.informationResult.getCode() == 200) {
                        activity.bondData();
                    } else {
                        MessageToast.getInstance(activity).show(activity.informationResult.getMsg());
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_goods_information);
        goods_id = getIntent().getIntExtra("goods_id", 0);
        initViews();
        goodsInformation();
    }

    private void initViews() {
        goodsInfoListView = findViewById(R.id.goodsInfoListView);
        listViewHeader = LayoutInflater.from(this).inflate(R.layout.header_point_goods_information_list, null);
        adapter = new PointGoodsImageListAdapter(this);
        goodsInfoListView.addHeaderView(listViewHeader);
        goodsInfoListView.setAdapter(adapter);
        pointTextView = listViewHeader.findViewById(R.id.pointTextView);
        nameTextView = listViewHeader.findViewById(R.id.nameTextView);
        descTextView = listViewHeader.findViewById(R.id.descTextView);
        goodsInfoBanner = listViewHeader.findViewById(R.id.goodsInfoBanner);
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        shareImageView = findViewById(R.id.shareImageView);
        backImageView = findViewById(R.id.backImageView);
        headerLayout = findViewById(R.id.headerLayout);
        titleLayout = findViewById(R.id.titleLayout);
        goodsInfoListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            // 创建一个稀疏数组，用于存储Item的高度和mTop
            private SparseArray recordSp = new SparseArray(0);
            private int mCurrentFirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                mCurrentFirstVisibleItem = firstVisibleItem;
                // 这里获取的Item是ListView中第一个可见的Item
                View firstView = view.getChildAt(0);
                if (null != firstView) {
                    ItemRecord itemRecord =
                            (ItemRecord) recordSp.get(firstVisibleItem);
                    if (null == itemRecord) {
                        itemRecord = new ItemRecord();
                    }
                    itemRecord.height = firstView.getHeight();
                    // top值总是小于或等于0的
                    itemRecord.top = firstView.getTop();
                    /**
                     * 将当前第一个可见Item的高度和top存入SparseArray中，
                     * SparseArray的key是Item的position
                     */
                    recordSp.append(firstVisibleItem, itemRecord);
                    totalDy = getScrollY();
                    headerChange();
                }

            }

            private int getScrollY() {
                int height = 0;
                for (int i = 0; i < mCurrentFirstVisibleItem; i++) {
                    /**
                     * 取出所有已滑过的Item的高度，相加。
                     * 说明下这里为什么不直接用一个Item高度乘以所有已滑过的Item的数量，
                     * 主要是考虑到可能为ListView添加Header这种情况，如果Header的高度
                     * 与Item的高度是相同的可以这样做，如果Header的高度不等于Item的高度，
                     * 这时将Header的高度直接按照Item的高度来计算就不准确了。
                     */
                    ItemRecord itemRecord = (ItemRecord) recordSp.get(i);
                    height += itemRecord.height;
                }
                //取出当前第一个可见Item的ItemRecord对象
                ItemRecord itemRecord =
                        (ItemRecord) recordSp.get(mCurrentFirstVisibleItem);
                if (null == itemRecord) {
                    itemRecord = new ItemRecord();
                }
                //由于存入的top值是小于或等于0的，这里是减去top值而不是加
                return height - itemRecord.top;
            }

            class ItemRecord {
                int height = 0;
                int top = 0;
            }
        });
        exchangeButtonTextView = findViewById(R.id.exchangeButtonTextView);
        exchangeButtonTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.exchangeButtonTextView:
                if(null!=informationResult&&informationResult.getData().getDetail().getGoods_stock()>0){
                    Intent orderConfirmIntent = new Intent(this, PointGoodsOrderConfirmActivity.class);
                    orderConfirmIntent.putExtra("goods_id", informationResult.getData().getDetail().getId());
                    startActivityForResult(orderConfirmIntent, 1003);
                }else{
                    MessageToast.getInstance(this).show("当前商品库存不足，无法兑换");
                }

                break;
        }
    }

    private void bondData() {
        goodsInfoBanner.setList(informationResult.getData().getDetail().getPics());
        pointTextView.setText(String.valueOf(informationResult.getData().getDetail().getCredits()));
        nameTextView.setText(informationResult.getData().getDetail().getGoods_name());
        descTextView.setText(informationResult.getData().getDetail().getDesc());
        adapter.setList(informationResult.getData().getDetail().getPics());
        titleTextView.setText(informationResult.getData().getDetail().getGoods_name());
    }

    private boolean statusColorWhite = true;
    private int totalDy;

    private void headerChange() {
        if (Math.abs(totalDy) > 30) {
            headerLayout.setBackgroundColor(getResources().getColor(R.color.white));
            backImageView.setImageDrawable(getResources().getDrawable(R.mipmap.goods_info_back_black));
            shareImageView.setImageDrawable(getResources().getDrawable(R.mipmap.fenxiang_gray));
            titleLayout.setVisibility(View.VISIBLE);
            float alpha = (float) Math.abs(totalDy) / (float) 300;
            headerLayout.setAlpha(alpha);
            if (statusColorWhite) {
                ScreentUtils.getInstance().setStatusBarLightMode(this, true);
                statusColorWhite = false;
            }
        } else {
            if (!statusColorWhite) {
                ScreentUtils.getInstance().setStatusBarLightMode(this, false);
                statusColorWhite = true;
            }
            backImageView.setImageDrawable(getResources().getDrawable(R.mipmap.goods_info_back));
            shareImageView.setImageDrawable(getResources().getDrawable(R.mipmap.point_goods_info_share));
            titleLayout.setVisibility(View.INVISIBLE);
            headerLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
            headerLayout.setAlpha(1);
        }
    }

    private PointGoodsInformationResult informationResult;

    private void goodsInformation() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.POINT_GOODS_INFORMATION);
        params.addBodyParameter("goods_id", String.valueOf(goods_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("积分兑换商品详情: " + result);
                informationResult = GsonUtils.GsonToBean(result, PointGoodsInformationResult.class);
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
        if (requestCode == 1003&&null!=data) {
            boolean exchange_success = data.getBooleanExtra("exchange_success", false);
            if (exchange_success) {
                finish();
            }
        }
    }
}
