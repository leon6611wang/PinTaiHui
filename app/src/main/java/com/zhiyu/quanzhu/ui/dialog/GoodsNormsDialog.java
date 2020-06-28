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
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.Goods;
import com.zhiyu.quanzhu.model.bean.GoodsNorm;
import com.zhiyu.quanzhu.model.bean.GoodsNormGroup;
import com.zhiyu.quanzhu.model.bean.GoodsStock;
import com.zhiyu.quanzhu.model.dao.GoodsNormStockDao;
import com.zhiyu.quanzhu.ui.activity.LargeImageActivity;
import com.zhiyu.quanzhu.ui.activity.OrderConfirmActivity;
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
 * 商品详情-规格
 */
public class GoodsNormsDialog extends Dialog implements View.OnClickListener, GoodsNormsRecyclerAdapter.OnGuiGeSelectedListener {
    private Context mContext;
    private float heightRatio = 0.739f;
    private int dialogHeight, screenHeight;
    private RecyclerView mRecyclerView;
    private GoodsNormsRecyclerAdapter adapter;
    private TextView jianTextView, jiaTextView, numberTextView;
    private int currentNumber = 1;
    private TextView gouwucheTextView, goumaiTextView;
    private LinearLayout closelayout;
    private List<GoodsNormGroup> list = null;
    private boolean isFirst = true;
    private RoundImageView mImageView;
    private TextView priceTextView, titleTextView, stockTextView, selectTextView;
    private View nullView;
    private Goods goods;
    private GoodsStock selectedGoodsStock;
    private boolean hasNorm = true;
    private int type = 0;//0:直接点开规格进入,1:加入购物车,2:立即购买
    private int stock;
    private LoadingDialog loadingDialog;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<GoodsNormsDialog> dialogWeakReference;

        public MyHandler(GoodsNormsDialog dialog) {
            dialogWeakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            GoodsNormsDialog dialog = dialogWeakReference.get();
            switch (msg.what) {
                case 1:
                    Glide.with(dialog.getContext()).load(dialog.selectedGoodsStock.getImg()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                            .fallback(R.drawable.image_error).into(dialog.mImageView);
                    dialog.stockTextView.setText(String.valueOf(dialog.selectedGoodsStock.getStock()));
                    dialog.priceTextView.setText(PriceParseUtils.getInstance().parsePrice(dialog.selectedGoodsStock.getPrice()));
                    dialog.selectTextView.setText("选择：" + msg.obj);
                    dialog.changeBottomButton(dialog.selectedGoodsStock.getStock() > 0);
                    break;
                case 2:
                    SuccessToast.getInstance(dialog.getContext()).show("加入成功");
                    dialog.dismiss();
                    break;
                case 3:
                    FailureToast.getInstance(dialog.getContext()).show();
                    break;
                case 4:
                    dialog.loadingDialog.dismiss();
                    dialog.goToGoodsConfirm();
                    break;
            }
        }
    }

    public GoodsNormsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        screenHeight = ScreentUtils.getInstance().getScreenHeight(mContext);
        dialogHeight = (int) (heightRatio * screenHeight);
        loadingDialog = new LoadingDialog(getContext(), R.style.dialog);
    }

    public void setType(int t) {
        this.type = t;
    }

    private void changeBottomButton(boolean isCanBuy) {
        if (isCanBuy) {
            gouwucheTextView.setClickable(true);
            goumaiTextView.setClickable(true);
            gouwucheTextView.setBackground(getContext().getResources().getDrawable(R.drawable.shape_oval_bg_yellow));
            gouwucheTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_yellow));
            goumaiTextView.setBackground(getContext().getResources().getDrawable(R.mipmap.mingpian_fenxiang_button_bg));
            goumaiTextView.setTextColor(getContext().getResources().getColor(R.color.white));
        } else {
            gouwucheTextView.setClickable(false);
            goumaiTextView.setClickable(false);
            gouwucheTextView.setBackground(getContext().getResources().getDrawable(R.drawable.shape_oval_bg_gray));
            gouwucheTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_gray));
            goumaiTextView.setBackground(getContext().getResources().getDrawable(R.drawable.shape_oval_solid_bg_gray));
            goumaiTextView.setTextColor(getContext().getResources().getColor(R.color.white));
        }
    }

    public void setGoods(Goods goods, boolean hasNorms) {
        this.goods = goods;
        this.hasNorm = hasNorms;
        this.stock = goods.getGoods_stock();
        this.imageUrl = goods.getImg_list().get(0).getUrl();
        Glide.with(getContext()).load(goods.getImg_list().get(0).getUrl()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(mImageView);
        String price;
        if (goods.getMin_price() > 0 || goods.getMax_price() > 0) {
            if (goods.getMin_price() == goods.getMax_price()) {
                price = PriceParseUtils.getInstance().parsePrice(goods.getGoods_price());
            } else {
                price = PriceParseUtils.getInstance().parsePrice(goods.getMin_price()) + "-" + PriceParseUtils.getInstance().parsePrice(goods.getMax_price());
            }
        } else {
            price = PriceParseUtils.getInstance().parsePrice(goods.getGoods_price());
        }
        priceTextView.setText(price);
        titleTextView.setText(goods.getGoods_name());
        stockTextView.setText(String.valueOf(goods.getGoods_stock()));
        selectTextView.setText("选择：默认");
        if (hasNorm) {
            nullView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            nullView.setVisibility(View.VISIBLE);
        }
//        changeBottomButton(!hasNorms && goods.getGoods_stock() > 0);
        if (!hasNorms && goods.getGoods_stock() > 0) {
            changeBottomButton(true);
        } else if (hasNorms) {
//            GoodsNormStockDao.getInstance().hasGoodsStockByNorms()
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_goods_norms);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext);
        params.height = dialogHeight;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        initViews();
    }

    public void setGuiGeList(List<GoodsNormGroup> guiGeList) {
        list = GoodsNormStockDao.getInstance().initGoodsNormsStock(guiGeList);
//        System.out.println("************************************商品详情-规格选择-guiGeList: " + (null == guiGeList ? 0 : guiGeList.size()) + " , list: " + (null == list ? 0 : list.size()));
//        if (null != list && list.size() > 0) {
//            for (GoodsNormGroup group : list) {
//                if (null != group && null != group.getList() && group.getList().size() > 0) {
//                    for (GoodsNorm norm : group.getList()) {
//                        if (norm.isSelectable()) {
//                            System.out.println("有库存可选的: " + norm.getNorms_name());
//                        }
//                    }
//                }
//            }
//        }

    }

    private void initViews() {
        mImageView = findViewById(R.id.mImageView);
        mImageView.setOnClickListener(this);
        priceTextView = findViewById(R.id.priceTextView);
        titleTextView = findViewById(R.id.titleTextView);
        stockTextView = findViewById(R.id.stockTextView);
        selectTextView = findViewById(R.id.selectTextView);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new GoodsNormsRecyclerAdapter(mContext, this);
        adapter.setData(list);
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
        gouwucheTextView = findViewById(R.id.gouwucheTextView);
        gouwucheTextView.setOnClickListener(this);
        goumaiTextView = findViewById(R.id.goumaiTextView);
        goumaiTextView.setOnClickListener(this);
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
            case R.id.gouwucheTextView:
                if (hasNorm) {
                    if (null != selectedGoodsStock) {
                        addCart();
                    } else {
                        MessageToast.getInstance(getContext()).show("请选择规格");
                    }
                } else {
                    addCart();
                }

                break;
            case R.id.goumaiTextView:
                if (hasNorm) {
                    if (null != selectedGoodsStock) {
                        lijigoumai();
                    } else {
                        MessageToast.getInstance(getContext()).show("请选择规格");
                    }
                } else {
                    lijigoumai();
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

    private void goToGoodsConfirm() {
        Intent orderConfirmIntent = new Intent(getContext(), OrderConfirmActivity.class);
        orderConfirmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(orderConfirmIntent);
    }

    public void clearSelectedNorms() {
        if (null != list && list.size() > 0) {
            for (GoodsNormGroup group : list) {
                if (null != group && null != group.getList() && group.getList().size() > 0) {
                    for (GoodsNorm norm : group.getList()) {
                        norm.setSelected(false);
                    }
                }
            }
            if (null != adapter)
                adapter.setData(list);
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
        if (selectedGoodsNormList.size() == list.size()) {
            selectedGoodsStock = GoodsNormStockDao.getInstance().getSelectedStock(selectedGoodsNormList);
            stock = selectedGoodsStock.getStock();
            imageUrl = selectedGoodsStock.getImg();
            Message message = myHandler.obtainMessage(1);
            message.obj = selectedNorm;
            message.sendToTarget();
        }

    }

    private OnShangPinInformationGuiGePiPeiListener onPiPeiListener;

    public void setOnShangPinInformationGuiGePiPeiListener(OnShangPinInformationGuiGePiPeiListener listener) {
        onPiPeiListener = listener;
    }

    @Override
    public void show() {
        super.show();
        if (isFirst) {
            isFirst = false;
            adapter.setData(list);
            List<GoodsStock> stockList = GoodsNormStockDao.getInstance().selectStockList();
//            System.out.println("本地存储的库存: " + (null == stockList ? 0 : stockList.size()));
            long norm_id = 0;
            if (null != stockList && stockList.size() > 0) {
                for (GoodsStock stock : stockList) {
//                    System.out.println("库存: " + stock.toString());
                    if (stock.getId1() > 0) {
                        norm_id = stock.getId1();
//                        break;
                    } else if (stock.getId2() > 0) {
                        norm_id = stock.getId2();
//                        break;
                    } else if (stock.getId3() > 0) {
                        norm_id = stock.getId3();
//                        break;
                    } else if (stock.getId4() > 0) {
                        norm_id = stock.getId4();
//                        break;
                    } else if (stock.getId5() > 0) {
                        norm_id = stock.getId5();
//                        break;
                    } else if (stock.getId6() > 0) {
                        norm_id = stock.getId6();
//                        break;
                    } else if (stock.getId7() > 0) {
                        norm_id = stock.getId7();
//                        break;
                    } else if (stock.getId8() > 0) {
                        norm_id = stock.getId8();
//                        break;
                    } else if (stock.getId9() > 0) {
                        norm_id = stock.getId9();
//                        break;
                    } else if (stock.getId10() > 0) {
                        norm_id = stock.getId10();
//                        break;
                    }
//                    break;
                }
            }
//            System.out.println("norm_id: " + norm_id);
            if (null != list && list.size() > 0) {
                int parentPosition = -1, childPosition = -1;
                for (int i = 0; i < list.size(); i++) {
                    for (int j = 0; j < list.get(i).getList().size(); j++) {
                        if (list.get(i).getList().get(j).getNorms_id() == norm_id) {
                            parentPosition = i;
                            childPosition = j;
//                            System.out.println("parentPosition: " + parentPosition + " , childPosition: " + childPosition);
                            break;
                        }
                    }
                }
                if (parentPosition > -1 && childPosition > -1) {
                    onGuiGeSelected(parentPosition, childPosition);
                }
            }

        }

    }


    private BaseResult baseResult;

    //加入购物车
    private void addCart() {
        String norms_id = "";
        if (hasNorm) {
            if (null != selectedGoodsStock.getNormas_id() && selectedGoodsStock.getNormas_id().size() > 0) {
                for (int i = 0; i < selectedGoodsStock.getNormas_id().size(); i++) {
                    norms_id += selectedGoodsStock.getNormas_id().get(i);
                    if (i < selectedGoodsStock.getNormas_id().size() - 1) {
                        norms_id += "-";
                    }
                }
            }
        }

        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ADD_CART);
        params.addBodyParameter("goods_id", String.valueOf(goods.getId()));
        params.addBodyParameter("norms_id", norms_id);
        params.addBodyParameter("goods_num", numberTextView.getText().toString().trim());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                if (baseResult.getCode() == 200) {
                    Message message = myHandler.obtainMessage(2);
                    message.sendToTarget();
                } else {
                    Message message = myHandler.obtainMessage(3);
                    message.sendToTarget();
                }
                System.out.println(baseResult.getMsg());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(3);
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

    /**
     * 立即购买
     */
    private void lijigoumai() {
        String norms_id = "";
        if (hasNorm) {
            if (null != selectedGoodsStock && null != selectedGoodsStock.getNormas_id() && selectedGoodsStock.getNormas_id().size() > 0) {
                for (int i = 0; i < selectedGoodsStock.getNormas_id().size(); i++) {
                    norms_id += selectedGoodsStock.getNormas_id().get(i);
                    if (i < selectedGoodsStock.getNormas_id().size() - 1) {
                        norms_id += "-";
                    }
                }
            }
        }
        Intent intent = new Intent(getContext(), OrderConfirmActivity.class);
        intent.putExtra("goods_id", String.valueOf(goods.getId()));
        intent.putExtra("norms_id", norms_id);
        intent.putExtra("goods_num", numberTextView.getText().toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
        dismiss();
    }

    private String imageUrl;

    private void reviewLargeImage() {
        Intent intent = new Intent(getContext(), LargeImageActivity.class);
        intent.putExtra("imgUrl", imageUrl);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

}
