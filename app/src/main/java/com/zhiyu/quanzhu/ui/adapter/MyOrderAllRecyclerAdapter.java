package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.OrderShop;
import com.zhiyu.quanzhu.ui.activity.OrderGoodsCommentsActivity;
import com.zhiyu.quanzhu.ui.activity.OrderInformationActivity;
import com.zhiyu.quanzhu.ui.activity.ShopInformationActivity;
import com.zhiyu.quanzhu.ui.dialog.DeliveryInfoDialog;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.ui.widget.TimeDownTextView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.OrderStatusUtils;
import com.zhiyu.quanzhu.utils.PriceParseUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

public class MyOrderAllRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<OrderShop> list;
    private YNDialog ynDialog;
    private DeliveryInfoDialog deliveryInfoDialog;
    private int ynType;//1:取消订单；2：删除订单,3:确认收货
    private int mPosition;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<MyOrderAllRecyclerAdapter> adapterWeakReference;

        public MyHandler(MyOrderAllRecyclerAdapter adapter) {
            adapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            MyOrderAllRecyclerAdapter adapter = adapterWeakReference.get();
            switch (msg.what) {
                case 1://取消订单
                    MessageToast.getInstance(adapter.context).show(adapter.baseResult.getMsg());
                    if (200 == adapter.baseResult.getCode()) {
                        if (null != adapter.onRefreshDataListener) {
                            adapter.onRefreshDataListener.onRefreshData();
                        }
                    }
                    break;
                case 2://删除订单
                    MessageToast.getInstance(adapter.context).show(adapter.baseResult.getMsg());
                    if (200 == adapter.baseResult.getCode()) {
                        if (null != adapter.onRefreshDataListener) {
                            adapter.onRefreshDataListener.onRefreshData();
                        }
                    }
                    break;
                case 3://提醒发货
                    MessageToast.getInstance(adapter.context).show(adapter.baseResult.getMsg());
                    if (null != adapter.onRefreshDataListener) {
                        adapter.onRefreshDataListener.onRefreshData();
                    }
                    break;
                case 4://确认收货
                    MessageToast.getInstance(adapter.context).show(adapter.baseResult.getMsg());
                    if (200 == adapter.baseResult.getCode()) {
                        if (null != adapter.onRefreshDataListener) {
                            adapter.onRefreshDataListener.onRefreshData();
                        }
                    }
                    break;
                case 5://倒计时结束
                    int position = (Integer) msg.obj;
                    adapter.list.get(position).setStatus(1);
                    adapter.list.get(position).setTimeCountComplete(true);
                    adapter.notifyItemChanged(position);
                    break;
            }
        }
    }

    public void setList(List<OrderShop> orderShopList) {
        this.list = orderShopList;
        notifyDataSetChanged();
    }

    private Context context;

    public MyOrderAllRecyclerAdapter(Context context) {
        this.context = context;
        initDialogs();
    }

    private void initDialogs() {
        ynDialog = new YNDialog(context, R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                switch (ynType) {
                    case 1:
                        cancelOrder();
                        break;
                    case 2:
                        deleteOrder();
                        break;
                    case 3:
                        confirmShouHuo();
                        break;
                }
            }
        });
        deliveryInfoDialog = new DeliveryInfoDialog(context, R.style.dialog);
    }

    class ViewHolderDaiFuKuan extends RecyclerView.ViewHolder {
        TextView shopNameTextView, goodsCountTextView, zhengshuTextView, xiaoshuTextView, payWayTextView;
        TextView editAddressTextView, cancelOrderTextView, payOrderTextView;
        TimeDownTextView timeDownTextView;
        MyRecyclerView shangpinRecyclerView;
        MyOrderShangPinRecyclerAdapter adapter;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        public ViewHolderDaiFuKuan(View itemView) {
            super(itemView);
            shangpinRecyclerView = itemView.findViewById(R.id.shangpinRecyclerView);
            adapter = new MyOrderShangPinRecyclerAdapter(context);
            shangpinRecyclerView.setLayoutManager(linearLayoutManager);
            shangpinRecyclerView.setAdapter(adapter);
            timeDownTextView = itemView.findViewById(R.id.timeDownTextView);
            shopNameTextView = itemView.findViewById(R.id.shopNameTextView);
            goodsCountTextView = itemView.findViewById(R.id.goodsCountTextView);
            zhengshuTextView = itemView.findViewById(R.id.zhengshuTextView);
            xiaoshuTextView = itemView.findViewById(R.id.xiaoshuTextView);
            editAddressTextView = itemView.findViewById(R.id.editAddressTextView);
            cancelOrderTextView = itemView.findViewById(R.id.cancelOrderTextView);
            payOrderTextView = itemView.findViewById(R.id.payOrderTextView);
            payWayTextView = itemView.findViewById(R.id.payWayTextView);
        }
    }

    class ViewHolderDaiFaHuo extends RecyclerView.ViewHolder {
        TextView shopNameTextView, goodsCountTextView, zhengshuTextView, xiaoshuTextView, payWayTextView;
        TextView editAddressTextView, cancelOrderTextView, notificatOrderTextView;
        ImageView payWayImageView;
        MyRecyclerView shangpinRecyclerView;
        MyOrderShangPinRecyclerAdapter adapter;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        public ViewHolderDaiFaHuo(View itemView) {
            super(itemView);
            shangpinRecyclerView = itemView.findViewById(R.id.shangpinRecyclerView);
            adapter = new MyOrderShangPinRecyclerAdapter(context);
            shangpinRecyclerView.setLayoutManager(linearLayoutManager);
            shangpinRecyclerView.setAdapter(adapter);
            shopNameTextView = itemView.findViewById(R.id.shopNameTextView);
            goodsCountTextView = itemView.findViewById(R.id.goodsCountTextView);
            zhengshuTextView = itemView.findViewById(R.id.zhengshuTextView);
            xiaoshuTextView = itemView.findViewById(R.id.xiaoshuTextView);
            payWayImageView = itemView.findViewById(R.id.payWayImageView);
            editAddressTextView = itemView.findViewById(R.id.editAddressTextView);
            cancelOrderTextView = itemView.findViewById(R.id.cancelOrderTextView);
            payWayTextView = itemView.findViewById(R.id.payWayTextView);
            notificatOrderTextView = itemView.findViewById(R.id.notificatOrderTextView);
        }
    }

    class ViewHolderDaiShouHuo extends RecyclerView.ViewHolder {
        TextView shopNameTextView, goodsCountTextView, zhengshuTextView, xiaoshuTextView, payWayTextView;
        TextView reviewDeliveryTextView, confirmShouHuoTextView;
        ImageView payWayImageView;
        MyRecyclerView shangpinRecyclerView;
        MyOrderShangPinRecyclerAdapter adapter;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        public ViewHolderDaiShouHuo(View itemView) {
            super(itemView);
            shangpinRecyclerView = itemView.findViewById(R.id.shangpinRecyclerView);
            adapter = new MyOrderShangPinRecyclerAdapter(context);
            shangpinRecyclerView.setLayoutManager(linearLayoutManager);
            shangpinRecyclerView.setAdapter(adapter);
            shopNameTextView = itemView.findViewById(R.id.shopNameTextView);
            goodsCountTextView = itemView.findViewById(R.id.goodsCountTextView);
            zhengshuTextView = itemView.findViewById(R.id.zhengshuTextView);
            xiaoshuTextView = itemView.findViewById(R.id.xiaoshuTextView);
            payWayImageView = itemView.findViewById(R.id.payWayImageView);
            reviewDeliveryTextView = itemView.findViewById(R.id.reviewDeliveryTextView);
            confirmShouHuoTextView = itemView.findViewById(R.id.confirmShouHuoTextView);
            payWayTextView = itemView.findViewById(R.id.payWayTextView);
        }
    }

    class ViewHolderDaiPingJia extends RecyclerView.ViewHolder {
        TextView shopNameTextView, goodsCountTextView, zhengshuTextView, xiaoshuTextView, payWayTextView;
        TextView reviewDeliveryTextView, commentTextView;
        ImageView payWayImageView;
        MyRecyclerView shangpinRecyclerView;
        MyOrderShangPinRecyclerAdapter adapter;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        public ViewHolderDaiPingJia(View itemView) {
            super(itemView);
            shangpinRecyclerView = itemView.findViewById(R.id.shangpinRecyclerView);
            adapter = new MyOrderShangPinRecyclerAdapter(context);
            shangpinRecyclerView.setLayoutManager(linearLayoutManager);
            shangpinRecyclerView.setAdapter(adapter);
            shopNameTextView = itemView.findViewById(R.id.shopNameTextView);
            goodsCountTextView = itemView.findViewById(R.id.goodsCountTextView);
            zhengshuTextView = itemView.findViewById(R.id.zhengshuTextView);
            xiaoshuTextView = itemView.findViewById(R.id.xiaoshuTextView);
            payWayImageView = itemView.findViewById(R.id.payWayImageView);
            reviewDeliveryTextView = itemView.findViewById(R.id.reviewDeliveryTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            payWayTextView = itemView.findViewById(R.id.payWayTextView);
        }
    }

    class ViewHolderYiQuXiao extends RecyclerView.ViewHolder {
        TextView shopNameTextView, goodsCountTextView, zhengshuTextView, xiaoshuTextView, payWayTextView;
        TextView deleteOrderTextView, tuikuanSuccessTextView, statusDescTextView;
        ImageView payWayImageView;
        MyRecyclerView shangpinRecyclerView;
        MyOrderShangPinRecyclerAdapter adapter;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        public ViewHolderYiQuXiao(View itemView) {
            super(itemView);
            tuikuanSuccessTextView = itemView.findViewById(R.id.tuikuanSuccessTextView);
            shangpinRecyclerView = itemView.findViewById(R.id.shangpinRecyclerView);
            adapter = new MyOrderShangPinRecyclerAdapter(context);
            shangpinRecyclerView.setLayoutManager(linearLayoutManager);
            shangpinRecyclerView.setAdapter(adapter);
            shopNameTextView = itemView.findViewById(R.id.shopNameTextView);
            goodsCountTextView = itemView.findViewById(R.id.goodsCountTextView);
            zhengshuTextView = itemView.findViewById(R.id.zhengshuTextView);
            xiaoshuTextView = itemView.findViewById(R.id.xiaoshuTextView);
            payWayImageView = itemView.findViewById(R.id.payWayImageView);
            deleteOrderTextView = itemView.findViewById(R.id.deleteOrderTextView);
            payWayTextView = itemView.findViewById(R.id.payWayTextView);
            statusDescTextView = itemView.findViewById(R.id.statusDescTextView);
        }
    }

    class ViewHolderYiWanCheng extends RecyclerView.ViewHolder {
        TextView shopNameTextView, goodsCountTextView, zhengshuTextView, xiaoshuTextView, payWayTextView;
        TextView reviewDeliveryTextView, serviceTextView;
        ImageView payWayImageView;
        MyRecyclerView shangpinRecyclerView;
        MyOrderShangPinRecyclerAdapter adapter;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        public ViewHolderYiWanCheng(View itemView) {
            super(itemView);
            shangpinRecyclerView = itemView.findViewById(R.id.shangpinRecyclerView);
            adapter = new MyOrderShangPinRecyclerAdapter(context);
            shangpinRecyclerView.setLayoutManager(linearLayoutManager);
            shangpinRecyclerView.setAdapter(adapter);
            shopNameTextView = itemView.findViewById(R.id.shopNameTextView);
            goodsCountTextView = itemView.findViewById(R.id.goodsCountTextView);
            zhengshuTextView = itemView.findViewById(R.id.zhengshuTextView);
            xiaoshuTextView = itemView.findViewById(R.id.xiaoshuTextView);
            payWayImageView = itemView.findViewById(R.id.payWayImageView);
            reviewDeliveryTextView = itemView.findViewById(R.id.reviewDeliveryTextView);
            serviceTextView = itemView.findViewById(R.id.serviceTextView);
            payWayTextView = itemView.findViewById(R.id.payWayTextView);
        }
    }

    class ViewHolderShouHouZhong extends RecyclerView.ViewHolder {
        TextView shopNameTextView, goodsCountTextView, zhengshuTextView, xiaoshuTextView, payWayTextView;
        TextView reviewDeliveryTextView, reviewProgressTextView;
        ImageView payWayImageView;
        MyRecyclerView shangpinRecyclerView;
        MyOrderShangPinRecyclerAdapter adapter;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        public ViewHolderShouHouZhong(View itemView) {
            super(itemView);
            shangpinRecyclerView = itemView.findViewById(R.id.shangpinRecyclerView);
            adapter = new MyOrderShangPinRecyclerAdapter(context);
            shangpinRecyclerView.setLayoutManager(linearLayoutManager);
            shangpinRecyclerView.setAdapter(adapter);
            shopNameTextView = itemView.findViewById(R.id.shopNameTextView);
            goodsCountTextView = itemView.findViewById(R.id.goodsCountTextView);
            zhengshuTextView = itemView.findViewById(R.id.zhengshuTextView);
            xiaoshuTextView = itemView.findViewById(R.id.xiaoshuTextView);
            payWayImageView = itemView.findViewById(R.id.payWayImageView);
            reviewDeliveryTextView = itemView.findViewById(R.id.reviewDeliveryTextView);
            reviewProgressTextView = itemView.findViewById(R.id.reviewProgressTextView);
            payWayTextView = itemView.findViewById(R.id.payWayTextView);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case OrderStatusUtils.DAIFUKUAN://待付款
                return new ViewHolderDaiFuKuan(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_order_daifukuan, parent, false));
            case OrderStatusUtils.DAIFAHUO://待发货
                return new ViewHolderDaiFaHuo(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_order_daifahuo, parent, false));
            case OrderStatusUtils.DAISHOUHUO://待收货
                return new ViewHolderDaiShouHuo(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_order_daishouhuo, parent, false));
            case OrderStatusUtils.DAIPINGJIA://待评价
                return new ViewHolderDaiPingJia(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_order_daipingjia, parent, false));
            case OrderStatusUtils.YIQUXIAO://已取消
                return new ViewHolderYiQuXiao(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_order_yiquxiao, parent, false));
            case OrderStatusUtils.YIWANCHENG://已完成
                return new ViewHolderYiWanCheng(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_order_yiwancheng, parent, false));
            case OrderStatusUtils.SHOUHOUZHONG://售后中
                return new ViewHolderShouHouZhong(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_order_shouhouzhong, parent, false));
            case OrderStatusUtils.GUANBI://交易关闭
                return new ViewHolderYiQuXiao(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_order_yiquxiao, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof ViewHolderDaiFuKuan) {
            final ViewHolderDaiFuKuan holder = (ViewHolderDaiFuKuan) viewHolder;
            holder.shopNameTextView.setText(list.get(position).getShop_name());
            holder.shopNameTextView.setOnClickListener(new OnShopInfoClick(position));
            holder.timeDownTextView.setOverTime(list.get(position).getOver_time(), new TimeDownTextView.OnTimeDownListener() {
                @Override
                public void onTImeDownFinish() {
                    System.out.println("倒计时完成: " + position);
                    Message message = myHandler.obtainMessage(5);
                    message.obj = position;
                    message.sendToTarget();
                }
            });
            holder.goodsCountTextView.setText(String.valueOf(list.get(position).getNum()));
            holder.zhengshuTextView.setText(PriceParseUtils.getInstance().getZhengShu(list.get(position).getPrice()));
            holder.xiaoshuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(list.get(position).getPrice()));
            holder.adapter.setList(list.get(position).getGoods());
            holder.shangpinRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return holder.itemView.onTouchEvent(event);
                }
            });
            holder.itemView.setOnClickListener(new OnOrderInformationClick(position));
            holder.cancelOrderTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPosition = position;
                    ynType = 1;
                    ynDialog.show();
                    ynDialog.setTitle("确定取消订单？");
                }
            });
        } else if (viewHolder instanceof ViewHolderDaiFaHuo) {
            final ViewHolderDaiFaHuo holder = (ViewHolderDaiFaHuo) viewHolder;
            holder.shopNameTextView.setText(list.get(position).getShop_name());
            holder.shopNameTextView.setOnClickListener(new OnShopInfoClick(position));
            switch (list.get(position).getPay_type()) {
                case 1:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_alipay));
                    holder.payWayTextView.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_wechatpay));
                    holder.payWayTextView.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_balancepay));
                    holder.payWayTextView.setVisibility(View.VISIBLE);
                    holder.payWayTextView.setText("余额（支付宝）");
                    break;
                case 4:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_balancepay));
                    holder.payWayTextView.setVisibility(View.VISIBLE);
                    holder.payWayTextView.setText("余额（微信）");
                    break;
            }
            holder.goodsCountTextView.setText(String.valueOf(list.get(position).getNum()));
            holder.zhengshuTextView.setText(PriceParseUtils.getInstance().getZhengShu(list.get(position).getPrice()));
            holder.xiaoshuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(list.get(position).getPrice()));
            holder.adapter.setList(list.get(position).getGoods());
            holder.shangpinRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return holder.itemView.onTouchEvent(event);
                }
            });
            holder.itemView.setOnClickListener(new OnOrderInformationClick(position));
            holder.cancelOrderTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ynType = 1;
                    mPosition = position;
                    ynDialog.show();
                    ynDialog.setTitle("确定取消订单？");
                }
            });
            holder.notificatOrderTextView.setOnClickListener(new OnNoticeOrderClick(position));
        } else if (viewHolder instanceof ViewHolderDaiShouHuo) {
            final ViewHolderDaiShouHuo holder = (ViewHolderDaiShouHuo) viewHolder;
            holder.shopNameTextView.setText(list.get(position).getShop_name());
            holder.shopNameTextView.setOnClickListener(new OnShopInfoClick(position));
            switch (list.get(position).getPay_type()) {
                case 1:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_alipay));
                    holder.payWayTextView.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_wechatpay));
                    holder.payWayTextView.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_balancepay));
                    holder.payWayTextView.setVisibility(View.VISIBLE);
                    holder.payWayTextView.setText("余额（支付宝）");
                    break;
                case 4:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_balancepay));
                    holder.payWayTextView.setVisibility(View.VISIBLE);
                    holder.payWayTextView.setText("余额（微信）");
                    break;
            }
            holder.goodsCountTextView.setText(String.valueOf(list.get(position).getNum()));
            holder.zhengshuTextView.setText(PriceParseUtils.getInstance().getZhengShu(list.get(position).getPrice()));
            holder.xiaoshuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(list.get(position).getPrice()));
            holder.adapter.setList(list.get(position).getGoods());
            holder.shangpinRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return holder.itemView.onTouchEvent(event);
                }
            });
            holder.itemView.setOnClickListener(new OnOrderInformationClick(position));
            holder.confirmShouHuoTextView.setOnClickListener(new OnConfirmShouHuoClick(position));
            holder.reviewDeliveryTextView.setOnClickListener(new OnReviewDeliveryClick(position));
        } else if (viewHolder instanceof ViewHolderDaiPingJia) {
            final ViewHolderDaiPingJia holder = (ViewHolderDaiPingJia) viewHolder;
            holder.shopNameTextView.setText(list.get(position).getShop_name());
            holder.shopNameTextView.setOnClickListener(new OnShopInfoClick(position));
            switch (list.get(position).getPay_type()) {
                case 1:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_alipay));
                    holder.payWayTextView.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_wechatpay));
                    holder.payWayTextView.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_balancepay));
                    holder.payWayTextView.setVisibility(View.VISIBLE);
                    holder.payWayTextView.setText("余额（支付宝）");
                    break;
                case 4:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_balancepay));
                    holder.payWayTextView.setVisibility(View.VISIBLE);
                    holder.payWayTextView.setText("余额（微信）");
                    break;
            }
            holder.goodsCountTextView.setText(String.valueOf(list.get(position).getNum()));
            holder.zhengshuTextView.setText(PriceParseUtils.getInstance().getZhengShu(list.get(position).getPrice()));
            holder.xiaoshuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(list.get(position).getPrice()));
            holder.adapter.setList(list.get(position).getGoods());
            holder.shangpinRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return holder.itemView.onTouchEvent(event);
                }
            });
            holder.itemView.setOnClickListener(new OnOrderInformationClick(position));
            holder.reviewDeliveryTextView.setOnClickListener(new OnReviewDeliveryClick(position));
            holder.commentTextView.setOnClickListener(new OnCommentGoodsClick(position));
        } else if (viewHolder instanceof ViewHolderYiQuXiao) {
            final ViewHolderYiQuXiao holder = (ViewHolderYiQuXiao) viewHolder;
            holder.shopNameTextView.setText(list.get(position).getShop_name());
            String status_desc = "";
            switch (list.get(position).getStatus()) {
                case OrderStatusUtils.YIQUXIAO:
                    status_desc = "交易取消";
                    break;
                case OrderStatusUtils.GUANBI:
                    status_desc = "交易关闭";
                    break;
            }
            holder.statusDescTextView.setText(status_desc);
            holder.shopNameTextView.setOnClickListener(new OnShopInfoClick(position));
            if (list.get(position).getIs_pay() == 1) {
                holder.payWayImageView.setVisibility(View.VISIBLE);
                holder.payWayTextView.setVisibility(View.VISIBLE);
            } else {
                holder.payWayImageView.setVisibility(View.INVISIBLE);
                holder.payWayTextView.setVisibility(View.INVISIBLE);
            }
            holder.tuikuanSuccessTextView.setVisibility(View.INVISIBLE);
//            switch (list.get(position).getCancel_type()) {
//                case 1:
//                    holder.tuikuanSuccessTextView.setVisibility(View.INVISIBLE);
//                    break;
//                case 2:
//                    holder.tuikuanSuccessTextView.setVisibility(View.VISIBLE);
//                    break;
//                default:
//                    holder.tuikuanSuccessTextView.setVisibility(View.INVISIBLE);
//                    break;
//            }
            switch (list.get(position).getPay_type()) {
                case 1:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_alipay));
                    holder.payWayTextView.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_wechatpay));
                    holder.payWayTextView.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_balancepay));
                    holder.payWayTextView.setVisibility(View.VISIBLE);
                    holder.payWayTextView.setText("余额（支付宝）");
                    break;
                case 4:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_balancepay));
                    holder.payWayTextView.setVisibility(View.VISIBLE);
                    holder.payWayTextView.setText("余额（微信）");
                    break;
            }
            holder.goodsCountTextView.setText(String.valueOf(list.get(position).getNum()));
            holder.zhengshuTextView.setText(PriceParseUtils.getInstance().getZhengShu(list.get(position).getPrice()));
            holder.xiaoshuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(list.get(position).getPrice()));
            holder.adapter.setList(list.get(position).getGoods());
            holder.shangpinRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return holder.itemView.onTouchEvent(event);
                }
            });
            holder.itemView.setOnClickListener(new OnOrderInformationClick(position));
            holder.deleteOrderTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ynType = 2;
                    mPosition = position;
                    ynDialog.show();
                    ynDialog.setTitle("确定删除订单？");
                }
            });
        } else if (viewHolder instanceof ViewHolderYiWanCheng) {
            final ViewHolderYiWanCheng holder = (ViewHolderYiWanCheng) viewHolder;
            holder.shopNameTextView.setText(list.get(position).getShop_name());
            holder.shopNameTextView.setOnClickListener(new OnShopInfoClick(position));
            switch (list.get(position).getPay_type()) {
                case 1:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_alipay));
                    holder.payWayTextView.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_wechatpay));
                    holder.payWayTextView.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_balancepay));
                    holder.payWayTextView.setVisibility(View.VISIBLE);
                    holder.payWayTextView.setText("余额（支付宝）");
                    break;
                case 4:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_balancepay));
                    holder.payWayTextView.setVisibility(View.VISIBLE);
                    holder.payWayTextView.setText("余额（微信）");
                    break;
            }
            holder.goodsCountTextView.setText(String.valueOf(list.get(position).getNum()));
            holder.zhengshuTextView.setText(PriceParseUtils.getInstance().getZhengShu(list.get(position).getPrice()));
            holder.xiaoshuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(list.get(position).getPrice()));
            holder.adapter.setList(list.get(position).getGoods());
            holder.shangpinRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return holder.itemView.onTouchEvent(event);
                }
            });
            holder.itemView.setOnClickListener(new OnOrderInformationClick(position));
            holder.reviewDeliveryTextView.setOnClickListener(new OnReviewDeliveryClick(position));

        } else if (viewHolder instanceof ViewHolderShouHouZhong) {
            final ViewHolderShouHouZhong holder = (ViewHolderShouHouZhong) viewHolder;
            holder.shopNameTextView.setText(list.get(position).getShop_name());
            holder.shopNameTextView.setOnClickListener(new OnShopInfoClick(position));
            switch (list.get(position).getPay_type()) {
                case 1:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_alipay));
                    holder.payWayTextView.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_wechatpay));
                    holder.payWayTextView.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_balancepay));
                    holder.payWayTextView.setVisibility(View.VISIBLE);
                    holder.payWayTextView.setText("余额（支付宝）");
                    break;
                case 4:
                    holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_balancepay));
                    holder.payWayTextView.setVisibility(View.VISIBLE);
                    holder.payWayTextView.setText("余额（微信）");
                    break;
            }
            holder.goodsCountTextView.setText(String.valueOf(list.get(position).getNum()));
            holder.zhengshuTextView.setText(PriceParseUtils.getInstance().getZhengShu(list.get(position).getPrice()));
            holder.xiaoshuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(list.get(position).getPrice()));
            holder.adapter.setList(list.get(position).getGoods());
            holder.shangpinRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return holder.itemView.onTouchEvent(event);
                }
            });
            holder.itemView.setOnClickListener(new OnOrderInformationClick(position));
        }

    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }


    @Override
    public int getItemViewType(int position) {
        return list.get(position).getStatus();
    }


    private class OnOrderInformationClick implements View.OnClickListener {
        private int position;

        public OnOrderInformationClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, OrderInformationActivity.class);
            intent.putExtra("order_id", list.get(position).getId());
            intent.putExtra("position", position);
            intent.putExtra("order_status", list.get(position).getStatus());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }


    private BaseResult baseResult;

    private void cancelOrder() {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CANCEL_ORDER);
        params.addBodyParameter("oid", String.valueOf(list.get(mPosition).getId()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
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

    private void deleteOrder() {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.DELETE_ORDER);
        params.addBodyParameter("oid", String.valueOf(list.get(mPosition).getId()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
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

    private class OnNoticeOrderClick implements View.OnClickListener {
        private int position;

        public OnNoticeOrderClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            noticeOrder(position);
        }
    }

    private void noticeOrder(int position) {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.NOTICE_ORDER);
        params.addBodyParameter("oid", String.valueOf(list.get(position).getId()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(3);
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

    private class OnReviewDeliveryClick implements View.OnClickListener {
        private int position;

        public OnReviewDeliveryClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            deliveryInfoDialog.show();
            deliveryInfoDialog.setOrderId(list.get(position).getId());
        }
    }

    private class OnConfirmShouHuoClick implements View.OnClickListener {
        private int position;

        public OnConfirmShouHuoClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            ynType = 3;
            mPosition = position;
            ynDialog.show();
            ynDialog.setTitle("确定已收货？");
        }
    }

    private class OnCommentGoodsClick implements View.OnClickListener {
        private int position;

        public OnCommentGoodsClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, OrderGoodsCommentsActivity.class);
            String json = GsonUtils.GsonString(list.get(position));
            intent.putExtra("orderShop", json);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }


    private void confirmShouHuo() {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CONFIRM_SHOU_HUO);
        params.addBodyParameter("oid", String.valueOf(list.get(mPosition).getId()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(4);
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


    private class OnShopInfoClick implements View.OnClickListener {
        private int position;

        public OnShopInfoClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent shopIntent = new Intent(context, ShopInformationActivity.class);
            shopIntent.putExtra("shop_id", String.valueOf(list.get(position).getShop_id()));
            shopIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(shopIntent);
        }
    }

    private OnRefreshDataListener onRefreshDataListener;

    public void setOnRefreshDataListener(OnRefreshDataListener listener) {
        this.onRefreshDataListener = listener;
    }

    public interface OnRefreshDataListener {
        void onRefreshData();
    }
}
