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
import com.zhiyu.quanzhu.ui.activity.OrderInformationActivity;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
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

public class MyOrderDaiFaHuoRecyclerAdapter extends RecyclerView.Adapter<MyOrderDaiFaHuoRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<OrderShop> list;
    private YNDialog ynDialog;
    private int mPosition;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<MyOrderDaiFaHuoRecyclerAdapter> adapterWeakReference;

        public MyHandler(MyOrderDaiFaHuoRecyclerAdapter adapter) {
            adapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            MyOrderDaiFaHuoRecyclerAdapter adapter = adapterWeakReference.get();
            switch (msg.what) {
                case 1://取消订单
                    MessageToast.getInstance(adapter.context).show(adapter.baseResult.getMsg());
                    if (200 == adapter.baseResult.getCode()) {
                        adapter.list.remove(adapter.mPosition);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 2://提醒发货
                    MessageToast.getInstance(adapter.context).show(adapter.baseResult.getMsg());
                    break;
            }
        }
    }

    public void setList(List<OrderShop> orderShopList) {
        this.list = orderShopList;
        notifyDataSetChanged();
    }

    public MyOrderDaiFaHuoRecyclerAdapter(Context context) {
        this.context = context;
        initDialogs();
    }

    private void initDialogs() {
        ynDialog = new YNDialog(context, R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                cancelOrder();
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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

        public ViewHolder(View itemView) {
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_order_daifahuo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final  ViewHolder holder, final int position) {
        holder.shopNameTextView.setText(list.get(position).getShop_name());
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
        holder.cancelOrderTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = position;
                ynDialog.show();
                ynDialog.setTitle("确定取消订单？");
            }
        });
        holder.notificatOrderTextView.setOnClickListener(new OnNoticeOrderClick(position));
        holder.shangpinRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return holder.itemView.onTouchEvent(event);
            }
        });
        holder.itemView.setOnClickListener(new OnOrderInformationClick(position));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
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

    private class OnOrderInformationClick implements View.OnClickListener {
        private int position;

        public OnOrderInformationClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, OrderInformationActivity.class);
            intent.putExtra("order_id", list.get(position).getId());
            intent.putExtra("order_status",list.get(position).getStatus());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }



}
