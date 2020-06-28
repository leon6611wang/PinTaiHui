package com.zhiyu.quanzhu.ui.widget.rongorder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.adapter.OrderMessageGoodsRecyclerAdapter;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.SpaceItemDecoration;

import java.util.List;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

@ProviderTag(messageContent = OrderMessage.class, showReadState = true)
public class OrderMessageItemProvider extends IContainerItemProvider.MessageProvider<OrderMessage> {
    private Context context;

    public OrderMessageItemProvider(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void bindView(View view, int i, OrderMessage orderMessage, UIMessage uiMessage) {
        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.allCountTextView.setText("" + orderMessage.getAll_count());
        holder.allPriceTextView.setText("￥" + orderMessage.getAll_price());
        holder.buyerNameTextView.setText("收件人："+orderMessage.getBuyer_name());
        holder.buyerPhoneTextView.setText("电话："+orderMessage.getBuyer_phone());
        holder.buyerAddressTextView.setText("收件地址："+orderMessage.getBuyer_address());
        if (null == holder.goodsList || holder.goodsList.size() == 0) {
            holder.goodsList = GsonUtils.getObjectList(orderMessage.getOrder_goods_list(), OrderMessage.OrderGoods.class);
        }
        holder.adapter.setList(holder.goodsList);
        holder.mRecyclerView.setLayoutManager(holder.linearLayoutManager);
        holder.mRecyclerView.setAdapter(holder.adapter);
        holder.orderConfirmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("当前订单进行了确认操作");
                holder.orderConfirmTextView.setBackground(context.getResources().getDrawable(R.mipmap.order_message_confirmed_bg));
                holder.orderConfirmTextView.setTextColor(context.getResources().getColor(R.color.text_color_grey));
                holder.orderConfirmTextView.setText("已确认");
            }
        });
    }

    @Override
    public Spannable getContentSummary(OrderMessage orderMessage) {
        return new SpannableString("订单信息确认");
    }

    @Override
    public void onItemClick(View view, int i, OrderMessage orderMessage, UIMessage uiMessage) {
        System.out.println("订单 点击");
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.rong_order_message, null);
        ViewHolder holder = new ViewHolder();
        holder.mRecyclerView = view.findViewById(R.id.mRecyclerView);
        holder.allCountTextView = view.findViewById(R.id.allCountTextView);
        holder.allPriceTextView = view.findViewById(R.id.allPriceTextView);
        holder.buyerNameTextView = view.findViewById(R.id.buyerNameTextView);
        holder.buyerPhoneTextView = view.findViewById(R.id.buyerPhoneTextView);
        holder.buyerAddressTextView = view.findViewById(R.id.buyerAddressTextView);
        holder.orderConfirmTextView = view.findViewById(R.id.orderConfirmTextView);
        holder.adapter = new OrderMessageGoodsRecyclerAdapter(context);
        holder.linearLayoutManager = new LinearLayoutManager(context);
        holder.linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        view.setTag(holder);
        return view;
    }

    private static class ViewHolder {
        RecyclerView mRecyclerView;
        TextView allCountTextView, allPriceTextView, buyerNameTextView, buyerPhoneTextView, buyerAddressTextView, orderConfirmTextView;
        OrderMessageGoodsRecyclerAdapter adapter;
        LinearLayoutManager linearLayoutManager;
        List<OrderMessage.OrderGoods> goodsList;
    }
}
