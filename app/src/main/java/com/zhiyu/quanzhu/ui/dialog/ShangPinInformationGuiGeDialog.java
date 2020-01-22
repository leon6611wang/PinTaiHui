package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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
import com.zhiyu.quanzhu.model.bean.Goods;
import com.zhiyu.quanzhu.model.bean.GoodsNorm;
import com.zhiyu.quanzhu.model.bean.GoodsNormGroup;
import com.zhiyu.quanzhu.model.dao.GoodsNormStockDao;
import com.zhiyu.quanzhu.ui.adapter.ShangPinInformationGuiGeRecyclerAdapter;
import com.zhiyu.quanzhu.ui.listener.OnShangPinInformationGuiGePiPeiListener;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品详情-规格
 */
public class ShangPinInformationGuiGeDialog extends Dialog implements View.OnClickListener, ShangPinInformationGuiGeRecyclerAdapter.OnGuiGeSelectedListener {
    private Context mContext;
    private float heightRatio = 0.739f;
    private int dialogHeight, screenHeight;
    private RecyclerView mRecyclerView;
    private ShangPinInformationGuiGeRecyclerAdapter adapter;
    private TextView jianTextView, jiaTextView, numberTextView;
    private int currentNumber = 1;
    private TextView gouwucheTextView, goumaiTextView;
    private LinearLayout closelayout;
    private List<GoodsNormGroup> list = null;
    private boolean isFirst = true;
    private RoundImageView mImageView;
    private TextView priceTextView, titleTextView, stockTextView, selectTextView;
    private Goods goods;

    public ShangPinInformationGuiGeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        screenHeight = ScreentUtils.getInstance().getScreenHeight(mContext);
        dialogHeight = (int) (heightRatio * screenHeight);
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
        Glide.with(getContext()).load(goods.getImg_list().get(0)).into(mImageView);
        priceTextView.setText(String.valueOf(goods.getGoods_price()));
        titleTextView.setText(goods.getGoods_name());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shangpin_information_guige);

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
        this.list = guiGeList;
        list = GoodsNormStockDao.getInstance().initGoodsNormsStock(list);
    }

    private void initViews() {
        mImageView = findViewById(R.id.mImageView);
        priceTextView = findViewById(R.id.priceTextView);
        titleTextView = findViewById(R.id.titleTextView);
        stockTextView = findViewById(R.id.stockTextView);
        selectTextView = findViewById(R.id.selectTextView);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new ShangPinInformationGuiGeRecyclerAdapter(mContext, this);
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
                break;
            case R.id.jiaTextView:
                String numberstr2 = numberTextView.getText().toString().trim();
                if (!TextUtils.isEmpty(numberstr2))
                    currentNumber = Integer.parseInt(numberstr2);
                currentNumber++;
                numberTextView.setText(String.valueOf(currentNumber));
                if (currentNumber > 1) {
                    jianTextView.setBackground(mContext.getResources().getDrawable(R.drawable.shape_buy_count_usable));
                } else {
                    jianTextView.setBackground(mContext.getResources().getDrawable(R.drawable.shape_buy_count_unusable));
                }
                break;
            case R.id.gouwucheTextView:
                System.out.println("加入购物车");
                break;
            case R.id.goumaiTextView:
                System.out.println("立即购买");
                break;
            case R.id.closelayout:
                dismiss();
                break;
        }
    }

    private Map<Integer, GoodsNorm> map = new HashMap<>();
    private List<Integer> pipeiList = null;

    @Override
    public void onGuiGeSelected(int parentPosition, int childPosition) {
//        System.out.println("parentPosition: " + parentPosition + " , childPosition: " + childPosition);
        list = GoodsNormStockDao.getInstance().calculateGoodsNormsStock(list, parentPosition, childPosition);
//        list.get(parentPosition).getList().get(childPosition).setSelectable(true);
//        if (list.get(parentPosition).getList().get(childPosition).isSelected()) {
//            list.get(parentPosition).getList().get(childPosition).setSelected(false);
//        } else {
//            list.get(parentPosition).getList().get(childPosition).setSelected(true);
//        }
//        GoodsNorm guiGeChild = list.get(parentPosition).getList().get(childPosition);
//        if (map.containsKey(parentPosition)) {
//            if (map.get(parentPosition).getNorms_id() == guiGeChild.getNorms_id()) {
//                map.remove(parentPosition);
//            } else {
//                map.put(parentPosition, guiGeChild);
//            }
//        } else {
//            map.put(parentPosition, guiGeChild);
//        }
//        Collection<GoodsNorm> collection = map.values();
//        List<GoodsNorm> child_list = new ArrayList<>(collection);
////        System.out.println("child_list size: "+(null==child_list?0:child_list.size()));
//        pipeiList = GoodsNormStockDao.getInstance().selectStocks(child_list);
//        if (child_list.size() > 0) {
//            for (int i = 0; i < list.size(); i++) {
//                for (int j = 0; j < list.get(i).getList().size(); j++) {
//                    if (i == parentPosition && j != childPosition) {
//                        list.get(i).getList().get(j).setSelected(false);
//                    }
//
//                    if (null != pipeiList && pipeiList.size() > 0) {
//                        if (i > 0) {
////                            list.get(i).getList().get(j).setSelectable(false);
//                        }
//                        list.get(i).getList().get(j).setSelectable(false);
//                        for (Integer integer : pipeiList) {
//                            if (list.get(i).getList().get(j).getNorms_id() == integer) {
//                                System.out.println("有库存的id：" + integer);
//                                list.get(i).getList().get(j).setSelectable(true);
//                            }
//                        }
//                    } else {
//                        List<Integer> idlist = GoodsNormStockDao.getInstance().getInitStock();
//                        if (null != list && list.size() > 0 && null != idlist && idlist.size() > 0) {
//                            for (int ii = 0; ii < list.size(); ii++) {
//                                for (int jj = 0; jj < list.get(ii).getList().size(); jj++) {
//                                    list.get(ii).getList().get(jj).setSelected(false);
//                                    for (int kk = 0; kk < idlist.size(); kk++) {
//                                        if (list.get(ii).getList().get(jj).getNorms_id() == idlist.get(kk)) {
//                                            list.get(ii).getList().get(jj).setSelectable(true);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                    }
//                }
//            }
//        } else {
//            List<Integer> idlist = GoodsNormStockDao.getInstance().getInitStock();
//            if (null != list && list.size() > 0 && null != idlist && idlist.size() > 0) {
//                for (int i = 0; i < list.size(); i++) {
//                    for (int j = 0; j < list.get(i).getList().size(); j++) {
//                        for (int k = 0; k < idlist.size(); k++) {
//                            if (list.get(i).getList().get(j).getNorms_id() == idlist.get(k)) {
//                                list.get(i).getList().get(j).setSelectable(true);
//                            }
//                        }
//                    }
//                }
//            }
//        }
        adapter.setData(list);


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
        }
    }


}
