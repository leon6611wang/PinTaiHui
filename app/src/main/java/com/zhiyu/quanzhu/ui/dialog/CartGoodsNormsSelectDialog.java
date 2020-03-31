package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.CartGoods;
import com.zhiyu.quanzhu.model.bean.Goods;
import com.zhiyu.quanzhu.model.bean.GoodsNorm;
import com.zhiyu.quanzhu.model.bean.GoodsNormGroup;
import com.zhiyu.quanzhu.model.bean.GoodsStock;
import com.zhiyu.quanzhu.model.dao.GoodsNormStockDao;
import com.zhiyu.quanzhu.model.result.GoodsNormResult;
import com.zhiyu.quanzhu.model.result.GoodsStockResult;
import com.zhiyu.quanzhu.ui.activity.OrderConfirmActivity;
import com.zhiyu.quanzhu.ui.adapter.CartGoodsNormsSelectRecyclerAdapter;
import com.zhiyu.quanzhu.ui.adapter.GoodsNormsRecyclerAdapter;
import com.zhiyu.quanzhu.ui.listener.OnShangPinInformationGuiGePiPeiListener;
import com.zhiyu.quanzhu.ui.toast.FailureToast;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.toast.SuccessToast;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.PriceParseUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车-商品规格选择
 */
public class CartGoodsNormsSelectDialog extends Dialog implements View.OnClickListener, CartGoodsNormsSelectRecyclerAdapter.OnGuiGeSelectedListener {
    private Context mContext;
    private float heightRatio = 0.739f;
    private int dialogHeight, screenHeight;
    private RecyclerView mRecyclerView;
    private CartGoodsNormsSelectRecyclerAdapter adapter;
    private TextView confirmTextView;
    private int currentNumber = 1;
    private LinearLayout closelayout;
    private List<GoodsNormGroup> list = null;
    private boolean isFirst = true;
    private RoundImageView mImageView;
    private TextView priceTextView, titleTextView, stockTextView, selectTextView;
    private GoodsStock selectedGoodsStock;
    private LoadingDialog loadingDialog;
    private int goods_id;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CartGoodsNormsSelectDialog> dialogWeakReference;

        public MyHandler(CartGoodsNormsSelectDialog dialog) {
            dialogWeakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            CartGoodsNormsSelectDialog dialog = dialogWeakReference.get();
            switch (msg.what) {
                case 1:
                    dialog.loadingDialog.dismiss();
                    if (dialog.goodsNormResult.getCode() == 200) {
                        dialog.setGuiGeList(dialog.goodsNormResult.getData().getList());
                        if (null != dialog.list && dialog.list.size() > 0 && null != dialog.initSelectedList && dialog.initSelectedList.size() > 0) {
                            for (GoodsNormGroup group : dialog.list) {
                                for (GoodsNorm norm : group.getList()) {
                                    for (Integer id : dialog.initSelectedList) {
                                        if (norm.getNorms_id() == id) {
                                            norm.setSelected(true);
                                        }
                                    }
                                }
                            }
                        }
                        dialog.adapter.setData(dialog.list);
                    } else {
                        MessageToast.getInstance(dialog.getContext()).show(dialog.goodsNormResult.getMsg());
                    }

                    break;
                case 2:
                    SuccessToast.getInstance(dialog.getContext()).show("加入成功");
                    dialog.dismiss();
                    break;
                case 3:
                    FailureToast.getInstance(dialog.getContext()).show();
                    break;
            }
        }
    }

    public CartGoodsNormsSelectDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        screenHeight = ScreentUtils.getInstance().getScreenHeight(mContext);
        dialogHeight = (int) (heightRatio * screenHeight);
        loadingDialog = new LoadingDialog(getContext(), R.style.dialog);
    }


    private List<Integer> initSelectedList = new ArrayList<>();

    public void setGoods(CartGoods goods) {
        initSelectedList.clear();
        this.goods_id = (int) goods.getGoods_id();
        Glide.with(getContext()).load(goods.getImg()).error(R.drawable.image_error).into(mImageView);
        priceTextView.setText(PriceParseUtils.getInstance().parsePrice(goods.getPrice()));
        titleTextView.setText(goods.getGoods_name());
        stockTextView.setText(String.valueOf(goods.getStock()));
        selectTextView.setText("选择：" + goods.getNorms_name());
        if (!StringUtils.isNullOrEmpty(goods.getNorms_id())) {
            String[] ids = goods.getNorms_id().split("-");
            if (null != ids && ids.length > 0) {
                for (String id : ids) {
                    initSelectedList.add(Integer.parseInt(id));
                }
            }
        }
        goodsStock();
    }

    private void setGuiGeList(List<GoodsNormGroup> guiGeList) {
        list = GoodsNormStockDao.getInstance().initGoodsNormsStock(guiGeList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_cart_goods_norms_select);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext);
        params.height = dialogHeight;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        initViews();
    }


    private void initViews() {
        mImageView = findViewById(R.id.mImageView);
        priceTextView = findViewById(R.id.priceTextView);
        titleTextView = findViewById(R.id.titleTextView);
        stockTextView = findViewById(R.id.stockTextView);
        selectTextView = findViewById(R.id.selectTextView);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new CartGoodsNormsSelectRecyclerAdapter(mContext, this);
        adapter.setOnGuiGeSelectedListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);

        closelayout = findViewById(R.id.closelayout);
        closelayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmTextView:
                break;
        }
    }

    //首次点击选择，把初始化选中的去掉
    private boolean isFirstSelect = true;

    @Override
    public void onGuiGeSelected(int parentPosition, int childPosition) {
        initSelectedList.clear();
        if (isFirstSelect) {
            for (GoodsNormGroup group : list) {
                for (GoodsNorm norm : group.getList()) {
                    norm.setSelected(false);
                }
            }
            isFirstSelect = false;
        }
        list = GoodsNormStockDao.getInstance().calculateGoodsNormsStock(list, parentPosition, childPosition);
//        for (GoodsNormGroup group : list) {
//            for (GoodsNorm norm : group.getList()) {
//                if (norm.isSelected()) {
//                    System.out.println(norm.getNorms_id() + " , " + norm.getNorms_name());
//                }
//            }
//        }
        adapter.setGroupList(list,parentPosition);
        List<GoodsNorm> selectedGoodsNormList = new ArrayList<>();
        String selectedNorm = "";
        for (GoodsNormGroup group : list) {
            for (GoodsNorm goodsNorm : group.getList()) {
                if (goodsNorm.isSelected()) {
                    selectedNorm += goodsNorm.getNorms_name() + " ";
                    selectedGoodsNormList.add(goodsNorm);
                }
            }
        }
        if (selectedGoodsNormList.size() == list.size()) {
            selectedGoodsStock = GoodsNormStockDao.getInstance().getSelectedStock(selectedGoodsNormList);
            Message message = myHandler.obtainMessage(1);
            message.obj = selectedNorm;
            message.sendToTarget();
        }

    }

    private OnShangPinInformationGuiGePiPeiListener onPiPeiListener;

    public void setOnShangPinInformationGuiGePiPeiListener(OnShangPinInformationGuiGePiPeiListener listener) {
        onPiPeiListener = listener;
    }


    private GoodsNormResult goodsNormResult;

    /**
     * 商品规格
     */
    private void goodsNorms() {
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GOODS_INFO_GOODS_NORMS);
        params.addBodyParameter("goods_id", String.valueOf(goods_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("商品规格:" + result);
                goodsNormResult = GsonUtils.GsonToBean(result, GoodsNormResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("norms: " + ex.toString());
                Message message = myHandler.obtainMessage(1);
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


    private GoodsStockResult goodsStockResult;

    /**
     * 商品规格对应库存
     */
    private void goodsStock() {
        loadingDialog.show();
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GOODS_INFO_GOODS_STOCK);
        params.addBodyParameter("goods_id", String.valueOf(goods_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("规格库存: " + result);
                goodsStockResult = GsonUtils.GsonToBean(result, GoodsStockResult.class);
                GoodsNormStockDao.getInstance().saveStockList(goodsStockResult.getData().getList());
                goodsNorms();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("商品规格对应库存:" + ex.toString());
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
