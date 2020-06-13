package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.CustomerServiceMessage;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.ui.widget.emoji.widget.EmojiTextview;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.PriceParseUtils;
import com.zhiyu.quanzhu.utils.SpaceItemDecoration;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

public class CustomerServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CustomerServiceMessage> list;
    private Context context;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CustomerServiceAdapter> adapterWeakReference;

        public MyHandler(CustomerServiceAdapter adapter) {
            adapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            CustomerServiceAdapter adapter = adapterWeakReference.get();
            switch (msg.what) {
                case 1:
                    MessageToast.getInstance(adapter.context).show(adapter.baseResult.getMsg());
                    if (200 == adapter.baseResult.getCode()) {
                        int position = (Integer) msg.obj;
                        adapter.list.get(position).getMessage().getOrder().setIs_confirm(true);
                        adapter.notifyDataSetChanged();
                    }

                    break;
            }
        }
    }

    public CustomerServiceAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<CustomerServiceMessage> messageList) {
        this.list = messageList;
        notifyDataSetChanged();
    }

    class LeftViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView, nameTextView;
        EmojiTextview mTextView;
        CircleImageView avatarImageView;
        FrameLayout contentLayout;
        RelativeLayout imageLayout;
        ImageView mWrapImageView;
        LinearLayout orderLayout, goodsLayout;
        ImageView goodsImageImageView;
        TextView goodsTitleTextView, goodsZhengShuTextView, goodsXiaoShuTextView, goodsSaleNumTextView;
        CardView goodsCardView;
        RecyclerView orderRecyclerView;
        OrderMessageGoodsAdapter adapter = new OrderMessageGoodsAdapter(context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        TextView orderGoodsCountTextView, orderPriceTextView, orderAddressNameTextView, orderAddressPhoneTextView, orderAddressAddressTextView, orderButtonTextView;

        public LeftViewHolder(View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            contentLayout = itemView.findViewById(R.id.contentLayout);
            mTextView = itemView.findViewById(R.id.mTextView);
            imageLayout = itemView.findViewById(R.id.imageLayout);
            mWrapImageView = itemView.findViewById(R.id.mWrapImageView);
            orderLayout = itemView.findViewById(R.id.orderLayout);
            goodsImageImageView = itemView.findViewById(R.id.goodsImageImageView);
            goodsTitleTextView = itemView.findViewById(R.id.goodsTitleTextView);
            goodsZhengShuTextView = itemView.findViewById(R.id.goodsZhengShuTextView);
            goodsXiaoShuTextView = itemView.findViewById(R.id.goodsXiaoShuTextView);
            goodsSaleNumTextView = itemView.findViewById(R.id.goodsSaleNumTextView);
            goodsLayout = itemView.findViewById(R.id.goodsLayout);
            goodsImageImageView = itemView.findViewById(R.id.goodsImageImageView);
            goodsCardView = itemView.findViewById(R.id.goodsCardView);
            orderRecyclerView = itemView.findViewById(R.id.orderRecyclerView);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            orderRecyclerView.setLayoutManager(layoutManager);
            orderRecyclerView.setAdapter(adapter);
            orderGoodsCountTextView = itemView.findViewById(R.id.orderGoodsCountTextView);
            orderPriceTextView = itemView.findViewById(R.id.orderPriceTextView);
            orderAddressNameTextView = itemView.findViewById(R.id.orderAddressNameTextView);
            orderAddressPhoneTextView = itemView.findViewById(R.id.orderAddressPhoneTextView);
            orderAddressAddressTextView = itemView.findViewById(R.id.orderAddressAddressTextView);
            orderButtonTextView = itemView.findViewById(R.id.orderButtonTextView);

        }
    }

    class RightViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView, nameTextView;
        EmojiTextview mTextView;
        CircleImageView avatarImageView;
        FrameLayout contentLayout;
        RelativeLayout imageLayout;
        ImageView mWrapImageView;
        LinearLayout orderLayout, goodsLayout;
        ImageView goodsImageImageView;
        TextView goodsTitleTextView, goodsZhengShuTextView, goodsXiaoShuTextView, goodsSaleNumTextView;
        CardView goodsCardView;
        RecyclerView orderRecyclerView;
        OrderMessageGoodsAdapter adapter = new OrderMessageGoodsAdapter(context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        TextView orderGoodsCountTextView, orderPriceTextView, orderAddressNameTextView, orderAddressPhoneTextView, orderAddressAddressTextView, orderButtonTextView;

        public RightViewHolder(View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            contentLayout = itemView.findViewById(R.id.contentLayout);
            mTextView = itemView.findViewById(R.id.mTextView);
            imageLayout = itemView.findViewById(R.id.imageLayout);
            mWrapImageView = itemView.findViewById(R.id.mWrapImageView);
            orderLayout = itemView.findViewById(R.id.orderLayout);
            goodsImageImageView = itemView.findViewById(R.id.goodsImageImageView);
            goodsTitleTextView = itemView.findViewById(R.id.goodsTitleTextView);
            goodsZhengShuTextView = itemView.findViewById(R.id.goodsZhengShuTextView);
            goodsXiaoShuTextView = itemView.findViewById(R.id.goodsXiaoShuTextView);
            goodsSaleNumTextView = itemView.findViewById(R.id.goodsSaleNumTextView);
            goodsLayout = itemView.findViewById(R.id.goodsLayout);
            goodsImageImageView = itemView.findViewById(R.id.goodsImageImageView);
            goodsImageImageView = itemView.findViewById(R.id.goodsImageImageView);
            goodsCardView = itemView.findViewById(R.id.goodsCardView);
            orderRecyclerView = itemView.findViewById(R.id.orderRecyclerView);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            orderRecyclerView.setLayoutManager(layoutManager);
            orderRecyclerView.setAdapter(adapter);
            orderGoodsCountTextView = itemView.findViewById(R.id.orderGoodsCountTextView);
            orderPriceTextView = itemView.findViewById(R.id.orderPriceTextView);
            orderAddressNameTextView = itemView.findViewById(R.id.orderAddressNameTextView);
            orderAddressPhoneTextView = itemView.findViewById(R.id.orderAddressPhoneTextView);
            orderAddressAddressTextView = itemView.findViewById(R.id.orderAddressAddressTextView);
            orderButtonTextView = itemView.findViewById(R.id.orderButtonTextView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CustomerServiceMessage.LEFT) {
            return new LeftViewHolder(LayoutInflater.from(context).inflate(R.layout.item_customer_service_left, parent, false));
        } else if (viewType == CustomerServiceMessage.RIGHT) {
            return new RightViewHolder(LayoutInflater.from(context).inflate(R.layout.item_customer_service_right, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof LeftViewHolder) {
            LeftViewHolder holder = (LeftViewHolder) viewHolder;
            holder.timeTextView.setText(list.get(position).getCreate_time());
            Glide.with(context).load(list.get(position).getUser_avatar()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                    .fallback(R.drawable.image_error).into(holder.avatarImageView);
            holder.nameTextView.setText(list.get(position).getUser_name());
            switch (list.get(position).getMsg_type()) {
                case CustomerServiceMessage.TYPE_TEXT:
                    holder.mTextView.setVisibility(View.VISIBLE);
                    holder.imageLayout.setVisibility(View.GONE);
                    holder.orderLayout.setVisibility(View.GONE);
                    holder.goodsLayout.setVisibility(View.GONE);
                    holder.mTextView.setText(list.get(position).getMessage().getTxt().getContent());
                    break;
                case CustomerServiceMessage.TYPE_IMAGE:
                    holder.mTextView.setVisibility(View.GONE);
                    holder.imageLayout.setVisibility(View.VISIBLE);
                    holder.orderLayout.setVisibility(View.GONE);
                    holder.goodsLayout.setVisibility(View.GONE);
                    Glide.with(context).load(list.get(position).getMessage().getImage().getUrl()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                            .fallback(R.drawable.image_error).into(holder.mWrapImageView);
                    break;
                case CustomerServiceMessage.TYPE_ORDER:
                    holder.mTextView.setVisibility(View.GONE);
                    holder.imageLayout.setVisibility(View.GONE);
                    holder.orderLayout.setVisibility(View.VISIBLE);
                    holder.goodsLayout.setVisibility(View.GONE);
                    holder.adapter.setList(list.get(position).getMessage().getOrder().getOrder_goods());
                    holder.orderGoodsCountTextView.setText(String.valueOf(list.get(position).getMessage().getOrder().getNum()));
                    holder.orderPriceTextView.setText(PriceParseUtils.getInstance().parsePrice(list.get(position).getMessage().getOrder().getPrice()));
                    holder.orderAddressNameTextView.setText("收件人：" + list.get(position).getMessage().getOrder().getName());
                    holder.orderAddressPhoneTextView.setText("电话：" + list.get(position).getMessage().getOrder().getMobile());
                    holder.orderAddressAddressTextView.setText("地址：" + list.get(position).getMessage().getOrder().getAddress());
                    if (list.get(position).getMessage().getOrder().isIs_confirm()) {
                        holder.orderButtonTextView.setBackground(context.getResources().getDrawable(R.mipmap.button_bg_grey));
                        holder.orderButtonTextView.setText("已确认");
                        holder.orderButtonTextView.setTextColor(context.getResources().getColor(R.color.text_color_grey));
                    } else {
                        holder.orderButtonTextView.setBackground(context.getResources().getDrawable(R.mipmap.bond_button_bg));
                        holder.orderButtonTextView.setText("确认");
                        holder.orderButtonTextView.setTextColor(context.getResources().getColor(R.color.white));
                        holder.orderButtonTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                orderConfirm(position);
                            }
                        });
                    }
                    break;
                case CustomerServiceMessage.TYPE_GOODS:
                    holder.mTextView.setVisibility(View.GONE);
                    holder.imageLayout.setVisibility(View.GONE);
                    holder.orderLayout.setVisibility(View.GONE);
                    holder.goodsLayout.setVisibility(View.VISIBLE);
                    holder.goodsCardView.setLayoutParams(list.get(position).getMessage().getGoods().getLayoutParams(context));
                    Glide.with(context).load(list.get(position).getMessage().getGoods().getThumb().getUrl()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                            .fallback(R.drawable.image_error).into(holder.goodsImageImageView);
                    holder.goodsTitleTextView.setText(list.get(position).getMessage().getGoods().getGoods_name());
                    holder.goodsZhengShuTextView.setText(PriceParseUtils.getInstance().getZhengShu(list.get(position).getMessage().getGoods().getGoods_price()));
                    holder.goodsXiaoShuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(list.get(position).getMessage().getGoods().getGoods_price()));
                    holder.goodsSaleNumTextView.setText(String.valueOf(list.get(position).getMessage().getGoods().getSales()));
                    break;
            }
        } else if (viewHolder instanceof RightViewHolder) {
            RightViewHolder holder = (RightViewHolder) viewHolder;
            holder.timeTextView.setText(list.get(position).getCreate_time());
            Glide.with(context).load(list.get(position).getUser_avatar()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                    .fallback(R.drawable.image_error).into(holder.avatarImageView);
            holder.nameTextView.setText(list.get(position).getUser_name());

            switch (list.get(position).getMsg_type()) {
                case CustomerServiceMessage.TYPE_TEXT:
                    holder.mTextView.setVisibility(View.VISIBLE);
                    holder.imageLayout.setVisibility(View.GONE);
                    holder.orderLayout.setVisibility(View.GONE);
                    holder.goodsLayout.setVisibility(View.GONE);
                    holder.mTextView.setText(list.get(position).getMessage().getTxt().getContent());
                    break;
                case CustomerServiceMessage.TYPE_IMAGE:
                    holder.mTextView.setVisibility(View.GONE);
                    holder.imageLayout.setVisibility(View.VISIBLE);
                    holder.orderLayout.setVisibility(View.GONE);
                    holder.goodsLayout.setVisibility(View.GONE);
                    if (null != list.get(position).getMessage().getImage().getImageParams(context))
                        holder.mWrapImageView.setLayoutParams(list.get(position).getMessage().getImage().getImageParams(context));
                    Glide.with(context).load(list.get(position).getMessage().getImage().getUrl()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                            .fallback(R.drawable.image_error).into(holder.mWrapImageView);
                    break;
                case CustomerServiceMessage.TYPE_ORDER:
                    holder.mTextView.setVisibility(View.GONE);
                    holder.imageLayout.setVisibility(View.GONE);
                    holder.orderLayout.setVisibility(View.VISIBLE);
                    holder.goodsLayout.setVisibility(View.GONE);
                    holder.adapter.setList(list.get(position).getMessage().getOrder().getOrder_goods());
                    holder.orderGoodsCountTextView.setText(String.valueOf(list.get(position).getMessage().getOrder().getNum()));
                    holder.orderPriceTextView.setText(PriceParseUtils.getInstance().parsePrice(list.get(position).getMessage().getOrder().getPrice()));
                    holder.orderAddressNameTextView.setText("收件人：" + list.get(position).getMessage().getOrder().getName());
                    holder.orderAddressPhoneTextView.setText("电话：" + list.get(position).getMessage().getOrder().getMobile());
                    holder.orderAddressAddressTextView.setText("地址：" + list.get(position).getMessage().getOrder().getAddress());
                    if (list.get(position).getMessage().getOrder().isIs_confirm()) {
                        holder.orderButtonTextView.setBackground(context.getResources().getDrawable(R.mipmap.button_bg_grey));
                        holder.orderButtonTextView.setText("已确认");
                        holder.orderButtonTextView.setTextColor(context.getResources().getColor(R.color.text_color_grey));
                    } else {
                        holder.orderButtonTextView.setBackground(context.getResources().getDrawable(R.mipmap.bond_button_bg));
                        holder.orderButtonTextView.setText("确认");
                        holder.orderButtonTextView.setTextColor(context.getResources().getColor(R.color.white));
                        holder.orderButtonTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                orderConfirm(position);
                            }
                        });
                    }
                    break;
                case CustomerServiceMessage.TYPE_GOODS:
                    holder.mTextView.setVisibility(View.GONE);
                    holder.imageLayout.setVisibility(View.GONE);
                    holder.orderLayout.setVisibility(View.GONE);
                    holder.goodsLayout.setVisibility(View.VISIBLE);
                    holder.goodsCardView.setLayoutParams(list.get(position).getMessage().getGoods().getLayoutParams(context));
                    Glide.with(context).load(list.get(position).getMessage().getGoods().getThumb().getUrl()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                            .fallback(R.drawable.image_error).into(holder.goodsImageImageView);
                    holder.goodsTitleTextView.setText(list.get(position).getMessage().getGoods().getGoods_name());
                    holder.goodsZhengShuTextView.setText(PriceParseUtils.getInstance().getZhengShu(list.get(position).getMessage().getGoods().getGoods_price()));
                    holder.goodsXiaoShuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(list.get(position).getMessage().getGoods().getGoods_price()));
                    holder.goodsSaleNumTextView.setText(String.valueOf(list.get(position).getMessage().getGoods().getSales()));
                    break;
            }
        }


    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }


    @Override
    public int getItemViewType(int position) {
        return list.get(position).getOwner();
    }

    private BaseResult baseResult;

    private void orderConfirm(final int position) {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ORDER_CONFIRM);
        params.addBodyParameter("oid", String.valueOf(list.get(position).getMessage().getOrder().getOid()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("订单确认:" + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.obj = position;
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
}
