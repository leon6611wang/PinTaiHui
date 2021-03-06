package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.CartGoods;
import com.zhiyu.quanzhu.model.bean.GoodsNorm;
import com.zhiyu.quanzhu.ui.dialog.CartGoodsNormsDialog;
import com.zhiyu.quanzhu.ui.dialog.CartGoodsNormsSelectDialog;
import com.zhiyu.quanzhu.ui.dialog.GoodsNormsDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.PriceParseUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

public class CartAvailableGoodsRecyclerAdapter extends RecyclerView.Adapter<CartAvailableGoodsRecyclerAdapter.ViewHolder> {
    private List<CartGoods> list;
    private Context context;
    private int itemIndex;
    private boolean manage;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CartAvailableGoodsRecyclerAdapter> weakReference;

        public MyHandler(CartAvailableGoodsRecyclerAdapter adapter) {
            weakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            CartAvailableGoodsRecyclerAdapter adapter = weakReference.get();
            switch (msg.what) {
                case 1:
                    if (200 == adapter.baseResult.getCode()) {
                        if (null != adapter.onChangeNormsListener) {
                            adapter.onChangeNormsListener.onChangeNorms();
                        }
                    } else {
                        MessageToast.getInstance(adapter.context).show(adapter.baseResult.getMsg());
                    }
                    break;
                case 99:
                    MessageToast.getInstance(adapter.context).show("服务器内部错误，请稍后再试.");
                    break;
            }
        }
    }

    public void setManage(boolean isManage) {
        this.manage = isManage;
        notifyDataSetChanged();
    }

    private CartGoodsNormsDialog cartGoodsNormsSelectDialog;

    public CartAvailableGoodsRecyclerAdapter(Context context) {
        this.context = context;
        cartGoodsNormsSelectDialog = new CartGoodsNormsDialog(context, R.style.dialog, new CartGoodsNormsDialog.OnGoodsNormsSelectedListener() {
            @Override
            public void onGoodsNormsSelected(String norms_name, String norms_id, int position, int goodsCount) {
                list.get(position).setNorms_id(norms_id);
                list.get(position).setNorms_name(norms_name);
                //只要选择了规格，商品数量重置到1
                list.get(position).setCurrentNum(1);
                list.get(position).setNum(1);
                notifyDataSetChanged();
                editCartGoods(position);

            }
        });
    }

    private OnChangeNormsListener onChangeNormsListener;

    public void setOnChangeNormsListener(OnChangeNormsListener listener) {
        this.onChangeNormsListener = listener;
    }

    public interface OnChangeNormsListener {
        void onChangeNorms();
    }

    public List<CartGoods> getList() {
        return list;
    }

    public void setData(List<CartGoods> gouWuCheItemItemList, int index) {
        this.list = gouWuCheItemItemList;
        this.itemIndex = index;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemItemSelectedImageView;
        LinearLayout itemItemSelectedLayout, noStockLayout;
        TextView jianTextView, numberTextView, jiaTextView;
        RoundImageView goodsImgImageView;
        TextView goodsNameTextView, goodsNormsTextView, zhengshuPriceTextView, xiaoshuPriceTextView, noStockTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemItemSelectedLayout = itemView.findViewById(R.id.itemItemSelectedLayout);
            itemItemSelectedImageView = itemView.findViewById(R.id.itemItemSelectedImageView);
            jianTextView = itemView.findViewById(R.id.jianTextView);
            numberTextView = itemView.findViewById(R.id.numberTextView);
            jiaTextView = itemView.findViewById(R.id.jiaTextView);
            goodsImgImageView = itemView.findViewById(R.id.goodsImgImageView);
            goodsNameTextView = itemView.findViewById(R.id.goodsNameTextView);
            goodsNormsTextView = itemView.findViewById(R.id.goodsNormsTextView);
            zhengshuPriceTextView = itemView.findViewById(R.id.zhengshuPriceTextView);
            xiaoshuPriceTextView = itemView.findViewById(R.id.xiaoshuPriceTextView);
            noStockLayout = itemView.findViewById(R.id.noStockLayout);
            noStockTextView = itemView.findViewById(R.id.noStockTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_available_goods, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (list.get(position).isSelected()) {
            holder.itemItemSelectedImageView.setImageDrawable(context.getDrawable(R.mipmap.gouwuche_selected));
        } else {
            holder.itemItemSelectedImageView.setImageDrawable(context.getDrawable(R.mipmap.gouwuche_unselect));
        }
        holder.itemItemSelectedLayout.setOnClickListener(new OnItemItemSelectedListener(position, holder.numberTextView));
        holder.jianTextView.setOnClickListener(new OnJianClickListener(position, holder.numberTextView, holder.jianTextView, holder.jiaTextView));
        holder.jiaTextView.setOnClickListener(new OnJiaClickListener(position, holder.numberTextView, holder.jianTextView, holder.jiaTextView));
        Glide.with(context).load(list.get(position).getImg())
                .error(R.drawable.image_error)
                .into(holder.goodsImgImageView);
        if (!StringUtils.isNullOrEmpty(list.get(position).getGoods_name()))
            holder.goodsNameTextView.setText(list.get(position).getGoods_name());
        if (!StringUtils.isNullOrEmpty(list.get(position).getNorms_name())) {
            holder.goodsNormsTextView.setText(list.get(position).getNorms_name());
            holder.goodsNormsTextView.setVisibility(View.VISIBLE);
        } else {
            holder.goodsNormsTextView.setVisibility(View.INVISIBLE);
        }

        holder.goodsNormsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartGoodsNormsSelectDialog.show();
                cartGoodsNormsSelectDialog.setGoods(list.get(position), position);
            }
        });
        holder.zhengshuPriceTextView.setText(PriceParseUtils.getInstance().getZhengShu(list.get(position).getPrice()));
        holder.xiaoshuPriceTextView.setText(PriceParseUtils.getInstance().getXiaoShu(list.get(position).getPrice()));
        holder.numberTextView.setText(String.valueOf(list.get(position).getNum()));
        list.get(position).setCurrentNum(list.get(position).getNum());

        if (list.get(position).getNum() > 1) {
            holder.jianTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_usable));
        } else if (list.get(position).getNum() == 1) {
            holder.jianTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_unusable));
        }
        if (list.get(position).getStock() == 0 || list.get(position).getStock() < list.get(position).getNum()) {
            holder.noStockLayout.setVisibility(View.VISIBLE);
            holder.jiaTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_unusable));
        } else {
            holder.noStockLayout.setVisibility(View.GONE);
            holder.jiaTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_usable));
        }
    }


    class OnItemItemSelectedListener implements View.OnClickListener {
        private int position;
        private TextView numberTextView;
        private int currentNumber;

        public OnItemItemSelectedListener(int position, TextView numberTextView) {
            this.position = position;
            this.numberTextView = numberTextView;
        }

        @Override
        public void onClick(View v) {
            String numberstr = numberTextView.getText().toString().trim();
            if (!TextUtils.isEmpty(numberstr))
                currentNumber = Integer.parseInt(numberstr);
            if (manage) {
                boolean selected = list.get(position).isSelected();
//                System.out.println("selected: " + selected);
                list.get(position).setSelected(!selected, true);
//                System.out.println("管理模式，可选: itemIndex: " + itemIndex + " , position: " + position + " , selected: " + list.get(position).isSelected());
                notifyDataSetChanged();
//                notifyItemChanged(position);
                if (null != onItemItemSelected) {
                    onItemItemSelected.onItemItemSelected(itemIndex, position, list.get(position).isSelected(), true);
                }
            } else {
//                System.out.println("结算模式");
                if (list.get(position).getStock() == 0 || list.get(position).getStock() < currentNumber) {
                    MessageToast.getInstance(context).show("库存不足，无法选定");
                } else {
                    boolean selected = list.get(position).isSelected();
                    list.get(position).setSelected(!selected, false);
                    notifyDataSetChanged();
//                    notifyItemChanged(position);
                    if (null != onItemItemSelected) {
                        onItemItemSelected.onItemItemSelected(itemIndex, position, list.get(position).isSelected(), false);
                    }
                }
            }
        }
    }

    class OnJianClickListener implements View.OnClickListener {
        private int position;
        private TextView numberTextView, jianTextView, jiaTextView;
        private int currentNumber;

        public OnJianClickListener(int position, TextView numberTextView, TextView jianTextView, TextView jiaTextView) {
            this.position = position;
            this.numberTextView = numberTextView;
            this.jianTextView = jianTextView;
            this.jiaTextView = jiaTextView;
        }

        @Override
        public void onClick(View v) {
            String numberstr = numberTextView.getText().toString().trim();
            if (!TextUtils.isEmpty(numberstr))
                currentNumber = Integer.parseInt(numberstr);
            if (currentNumber > 1) {
                currentNumber--;
                numberTextView.setText(String.valueOf(currentNumber));
                list.get(position).setCurrentNum(currentNumber);
                editCartGoods(position);
            }
            if (currentNumber > 1) {
                jianTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_usable));
            } else {
                jianTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_unusable));
            }

            if (currentNumber < list.get(position).getStock() && currentNumber >= 1) {
                jiaTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_usable));
            } else {
                jiaTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_unusable));
            }
        }
    }

    class OnJiaClickListener implements View.OnClickListener {
        private int position;
        private TextView numberTextView, jianTextView, jiaTextView;
        private int currentNumber;

        public OnJiaClickListener(int position, TextView numberTextView, TextView jianTextView, TextView jiaTextView) {
            this.position = position;
            this.numberTextView = numberTextView;
            this.jianTextView = jianTextView;
            this.jiaTextView = jiaTextView;
        }

        @Override
        public void onClick(View v) {
            String numberstr2 = numberTextView.getText().toString().trim();
            if (!TextUtils.isEmpty(numberstr2))
                currentNumber = Integer.parseInt(numberstr2);
            if (currentNumber < list.get(position).getStock()) {
                currentNumber++;
                numberTextView.setText(String.valueOf(currentNumber));
                list.get(position).setCurrentNum(currentNumber);
                editCartGoods(position);
                if (currentNumber > 1) {
                    jianTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_usable));
                } else {
                    jianTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_unusable));
                }

                if (currentNumber < list.get(position).getStock()) {
                    jiaTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_usable));
                } else {
                    jiaTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_unusable));
                }
            } else {
                MessageToast.getInstance(context).show("库存不足");
            }
        }
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private OnItemItemSelected onItemItemSelected;

    public void setOnItemItemSelected(OnItemItemSelected selected) {
        this.onItemItemSelected = selected;
    }

    public interface OnItemItemSelected {
        void onItemItemSelected(int parentPosition, int childPosition, boolean selected, boolean isManage);
    }

    private OnGoodsNumChangeListener onGoodsNumChangeListener;

    public void setOnGoodsNumChangeListener(OnGoodsNumChangeListener listener) {
        this.onGoodsNumChangeListener = listener;
    }

    public interface OnGoodsNumChangeListener {
        void onGoodsNumChange();
    }

    private BaseResult baseResult;

    //更改购物车商品
    private void editCartGoods(int position) {
        if (null != onGoodsNumChangeListener) {
            onGoodsNumChangeListener.onGoodsNumChange();
        }
//        System.out.println("id: " + list.get(position).getId() + " , norms_id: " + list.get(position).getNorms_id() + " , num: " + list.get(position).getCurrentNum());
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CART_GOODS_EDIT);
        params.addBodyParameter("id", String.valueOf(list.get(position).getId()));
        params.addBodyParameter("norms_id", list.get(position).getNorms_id());
        params.addBodyParameter("num", String.valueOf(list.get(position).getCurrentNum()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
//                System.out.println("edit cart goods: " + ex.toString());
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
