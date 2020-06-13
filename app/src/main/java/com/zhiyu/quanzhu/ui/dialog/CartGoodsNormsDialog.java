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
import com.zhiyu.quanzhu.ui.activity.LargeImageActivity;
import com.zhiyu.quanzhu.ui.activity.OrderConfirmActivity;
import com.zhiyu.quanzhu.ui.adapter.CartGoodsNormsRecyclerAdapter;
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
 * 商品详情-规格
 */
public class CartGoodsNormsDialog extends Dialog implements View.OnClickListener, CartGoodsNormsRecyclerAdapter.OnGuiGeSelectedListener {
    private Context mContext;
    private float heightRatio = 0.739f;
    private int dialogHeight, screenHeight;
    private RecyclerView mRecyclerView;
    private CartGoodsNormsRecyclerAdapter adapter;
    private TextView jianTextView, jiaTextView, numberTextView;
    private int currentNumber = 1;
    private LinearLayout closelayout, confirmLayout;
    private List<GoodsNormGroup> list = null;
    private RoundImageView mImageView;
    private TextView priceTextView, titleTextView, stockTextView, selectTextView;
    private View nullView;
    private CartGoods goods;
    private GoodsStock selectedGoodsStock;
    private boolean hasNorm = true;
    private int stock;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CartGoodsNormsDialog> dialogWeakReference;

        public MyHandler(CartGoodsNormsDialog dialog) {
            dialogWeakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            CartGoodsNormsDialog dialog = dialogWeakReference.get();
            switch (msg.what) {
                case 0:
                    if(dialog.stock>0){
                        Glide.with(dialog.getContext()).load(dialog.selectedGoodsStock.getImg()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                                .fallback(R.drawable.image_error).into(dialog.mImageView);
                        dialog.stockTextView.setText(String.valueOf(dialog.selectedGoodsStock.getStock()));
                        dialog.priceTextView.setText(PriceParseUtils.getInstance().parsePrice(dialog.selectedGoodsStock.getPrice()));
                        dialog.selectTextView.setText("选择：" + msg.obj);
                    }
                    dialog.changeBottomButton(dialog.selectedGoodsStock.getStock() > 0);
                    break;
                case 1:
                    if (null != dialog.goodsNormResult && dialog.goodsNormResult.getCode() == 200 &&
                            null != dialog.goodsNormResult.getData() && null != dialog.goodsNormResult.getData().getList() &&
                            dialog.goodsNormResult.getData().getList().size() > 0) {
                        dialog.setGuiGeList(dialog.goodsNormResult.getData().getList());
                    }
                    break;
            }
        }
    }

    public CartGoodsNormsDialog(@NonNull Context context, int themeResId, OnGoodsNormsSelectedListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.onGoodsNormsSelectedListener = listener;
        screenHeight = ScreentUtils.getInstance().getScreenHeight(mContext);
        dialogHeight = (int) (heightRatio * screenHeight);
    }

    private void changeBottomButton(boolean isCanBuy) {
        if (isCanBuy) {
            confirmLayout.setClickable(true);
            confirmLayout.setBackground(getContext().getResources().getDrawable(R.drawable.shape_oval_solid_bg_yellow));
        } else {
            confirmLayout.setClickable(false);
            confirmLayout.setBackground(getContext().getResources().getDrawable(R.drawable.shape_oval_solid_bg_gray));
        }
    }

    private int position;

    public void setGoods(CartGoods goods, int p) {
        this.position = p;
        this.goods = goods;
        goodsInfoGoodsStock();
        goodsInfoGoodsNorms();
        this.stock = goods.getStock();
        this.imageUrl = goods.getImg();
        Glide.with(getContext()).load(imageUrl).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(mImageView);
        priceTextView.setText(PriceParseUtils.getInstance().parsePrice(goods.getPrice()));
        titleTextView.setText(goods.getGoods_name());
        stockTextView.setText(String.valueOf(goods.getStock()));
        numberTextView.setText(String.valueOf(goods.getCurrentNum()));
        selectTextView.setText("选择：" + goods.getNorms_name());
        nullView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_cart_goods_norms);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext);
        params.height = dialogHeight;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        initViews();
    }

    private void setGuiGeList(List<GoodsNormGroup> guiGeList) {
        list = GoodsNormStockDao.getInstance().initGoodsNormsStock(guiGeList);
        adapter.setData(list);
        String norms_id = goods.getNorms_id();
        if (!StringUtils.isNullOrEmpty(norms_id)) {
            String[] norms = norms_id.split("-");
            for (String id : norms) {
                int n_id = Integer.parseInt(id);
                int parentPosition, childPosition;
                if (null != list && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (null != list.get(i).getList() && list.get(i).getList().size() > 0) {
                            for (int j = 0; j < list.get(i).getList().size(); j++) {
                                if (list.get(i).getList().get(j).getNorms_id() == n_id) {
                                    parentPosition = i;
                                    childPosition = j;
                                    onGuiGeSelected(parentPosition, childPosition);
                                }
                            }
                        }
                    }
                }
            }

        }


    }

    private void initViews() {
        mImageView = findViewById(R.id.mImageView);
        mImageView.setOnClickListener(this);
        priceTextView = findViewById(R.id.priceTextView);
        titleTextView = findViewById(R.id.titleTextView);
        stockTextView = findViewById(R.id.stockTextView);
        selectTextView = findViewById(R.id.selectTextView);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new CartGoodsNormsRecyclerAdapter(mContext, this);
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
        jianTextView = findViewById(R.id.jianTextView);
        jianTextView.setOnClickListener(this);
        jiaTextView = findViewById(R.id.jiaTextView);
        jiaTextView.setOnClickListener(this);
        numberTextView = findViewById(R.id.numberTextView);
        numberTextView.setText(String.valueOf(currentNumber));
        confirmLayout = findViewById(R.id.confirmLayout);
        confirmLayout.setOnClickListener(this);
        changeBottomButton(false);
        closelayout = findViewById(R.id.closelayout);
        closelayout.setOnClickListener(this);

        nullView = findViewById(R.id.nullView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jianTextView:
                String numberstr = numberTextView.getText().toString().trim();
                if (!TextUtils.isEmpty(numberstr))
                    currentNumber = Integer.parseInt(numberstr);
                if (currentNumber > 1) {
                    currentNumber--;
                    numberTextView.setText(String.valueOf(currentNumber));
                }
                if (currentNumber > 1) {
                    jianTextView.setBackground(mContext.getResources().getDrawable(R.drawable.shape_buy_count_usable));
                } else {
                    jianTextView.setBackground(mContext.getResources().getDrawable(R.drawable.shape_buy_count_unusable));
                }
                jiaTextView.setBackground(mContext.getResources().getDrawable(R.drawable.shape_buy_count_usable));
                break;
            case R.id.jiaTextView:
                String numberstr2 = numberTextView.getText().toString().trim();
                if (!TextUtils.isEmpty(numberstr2))
                    currentNumber = Integer.parseInt(numberstr2);
                if (currentNumber < stock) {
                    currentNumber++;
                    numberTextView.setText(String.valueOf(currentNumber));
                    jiaTextView.setBackground(mContext.getResources().getDrawable(R.drawable.shape_buy_count_usable));
                    if (currentNumber > 1) {
                        jianTextView.setBackground(mContext.getResources().getDrawable(R.drawable.shape_buy_count_usable));
                    } else {
                        jianTextView.setBackground(mContext.getResources().getDrawable(R.drawable.shape_buy_count_unusable));
                    }
                } else {
                    jiaTextView.setBackground(mContext.getResources().getDrawable(R.drawable.shape_buy_count_unusable));
                }

                break;
            case R.id.confirmLayout:
                if (hasNorm) {
                    if (null != selectedGoodsStock) {
                        confirmNorms();
                    } else {
                        MessageToast.getInstance(getContext()).show("请选择规格");
                    }
                } else {
                    confirmNorms();
                }
                break;
            case R.id.closelayout:
                dismiss();
                break;
            case R.id.mImageView:
                reviewLargeImage();
                break;

        }
    }

    @Override
    public void onGuiGeSelected(int parentPosition, int childPosition) {
        list = GoodsNormStockDao.getInstance().calculateGoodsNormsStock(list, parentPosition, childPosition);
        adapter.setData(list);
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
        if (selectedGoodsNormList.size() == list.size() ) {
            selectedGoodsStock = GoodsNormStockDao.getInstance().getSelectedStock(selectedGoodsNormList);
            stock = selectedGoodsStock.getStock();
            imageUrl = selectedGoodsStock.getImg();
            Message message = myHandler.obtainMessage(0);
            message.obj = selectedNorm;
            message.sendToTarget();
        }

    }


    private void confirmNorms() {
        String norms_id = "";
        String norms_name = "";
        if (null != selectedGoodsStock.getNormas_id() && selectedGoodsStock.getNormas_id().size() > 0) {
            for (int i = 0; i < selectedGoodsStock.getNormas_id().size(); i++) {
                norms_id += selectedGoodsStock.getNormas_id().get(i);
                norms_name += getNormsName(Integer.parseInt(selectedGoodsStock.getNormas_id().get(i)));
                if (i < selectedGoodsStock.getNormas_id().size() - 1) {
                    norms_id += "-";
                    norms_name += "-";
                }
            }
        }
        int goodsCount = Integer.parseInt(numberTextView.getText().toString().trim());
        if (null != onGoodsNormsSelectedListener) {
            onGoodsNormsSelectedListener.onGoodsNormsSelected(norms_name, norms_id, position, goodsCount);
        }
        dismiss();
    }

    private OnGoodsNormsSelectedListener onGoodsNormsSelectedListener;

    public interface OnGoodsNormsSelectedListener {
        void onGoodsNormsSelected(String norms_name, String norms_id, int position, int goodsCount);
    }

    private String getNormsName(int norms_id) {
        String normsName = "";
        if (null != list && list.size() > 0) {
            for (GoodsNormGroup group : list) {
                if (null != group && null != group.getList() && group.getList().size() > 0) {
                    for (GoodsNorm norm : group.getList()) {
                        if (norm.getNorms_id() == norms_id) {
                            normsName = norm.getNorms_name();
                        }
                    }
                }
            }
        }
        return normsName;
    }

    private String imageUrl;

    private void reviewLargeImage() {
        Intent intent = new Intent(getContext(), LargeImageActivity.class);
        intent.putExtra("imgUrl", imageUrl);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    private GoodsNormResult goodsNormResult;

    /**
     * 商品规格
     */
    private void goodsInfoGoodsNorms() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GOODS_INFO_GOODS_NORMS);
        params.addBodyParameter("goods_id", String.valueOf(goods.getGoods_id()));
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
    private void goodsInfoGoodsStock() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GOODS_INFO_GOODS_STOCK);
        params.addBodyParameter("goods_id", String.valueOf(goods.getGoods_id()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("商品规格对应库存:" + result);
                goodsStockResult = GsonUtils.GsonToBean(result, GoodsStockResult.class);
                GoodsNormStockDao.getInstance().saveStockList(goodsStockResult.getData().getList());
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
