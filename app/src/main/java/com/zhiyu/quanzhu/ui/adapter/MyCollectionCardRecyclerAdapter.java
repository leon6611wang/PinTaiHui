package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.Card;
import com.zhiyu.quanzhu.model.bean.Feed;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MyCollectionCardRecyclerAdapter extends RecyclerView.Adapter<MyCollectionCardRecyclerAdapter.ViewHolder> {
    private List<Card> list;
    private Context context;
    private MyHandler myHandler = new MyHandler(this);
    private boolean isSelectModel;

    public void setAllSelect(boolean isAllSelected){
        for(Card feed:list){
            feed.setSelected(isAllSelected);
        }
        notifyDataSetChanged();
    }

    public String getCancelCollectIds(){
        String ids="";
        for(Card feed:list){
            if(feed.isSelected()){
                ids+=feed.getId()+",";
            }
        }
        return ids;
    }
    public void cancelCollectSuccess() {
        List<Integer> positionList = new ArrayList<>();
        for (int i = list.size()-1; i >0; i--) {
            if (list.get(i).isSelected()) {
                positionList.add(i);
            }
        }
        for (Integer position : positionList) {
            list.remove((int)position);
        }
        notifyDataSetChanged();
    }
    public void setSelectModel(boolean isSelected) {
        this.isSelectModel = isSelected;
        notifyDataSetChanged();
    }
    private static class MyHandler extends Handler {
        WeakReference<MyCollectionCardRecyclerAdapter> adapterWeakReference;

        public MyHandler(MyCollectionCardRecyclerAdapter adapter) {
            adapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            MyCollectionCardRecyclerAdapter adapter = adapterWeakReference.get();
            switch (msg.what) {
                case 1:
                    MessageToast.getInstance(adapter.context).show(adapter.baseResult.getMsg());
                    break;
            }
        }
    }

    public MyCollectionCardRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<Card> cardList) {
        this.list = cardList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        NiceImageView avatarImageView;
        TextView nameTextView, occupionTextView, companyTextView, cityTextView, industryTextView;
        ImageView cardRightImageView,selectImageView;
        RelativeLayout selectLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            occupionTextView = itemView.findViewById(R.id.occupionTextView);
            companyTextView = itemView.findViewById(R.id.companyTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            industryTextView = itemView.findViewById(R.id.industryTextView);
            cardRightImageView = itemView.findViewById(R.id.cardRightImageView);
            selectImageView=itemView.findViewById(R.id.selectImageView);
            selectLayout=itemView.findViewById(R.id.selectLayout);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_collection_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getCard_thumb()).error(R.drawable.image_error).into(holder.avatarImageView);
        holder.nameTextView.setText(list.get(position).getCard_name());
        holder.occupionTextView.setText(list.get(position).getOccupation());
        holder.companyTextView.setText(list.get(position).getCompany());
        if (!StringUtils.isNullOrEmpty(list.get(position).getCity_name())) {
            holder.cityTextView.setText(list.get(position).getCity_name());
            holder.cityTextView.setVisibility(View.VISIBLE);
        } else {
            holder.cityTextView.setVisibility(View.GONE);
        }
        if (!StringUtils.isNullOrEmpty(list.get(position).getIndustry())) {
            holder.industryTextView.setVisibility(View.VISIBLE);
            holder.industryTextView.setText(list.get(position).getIndustry());
        } else {
            holder.industryTextView.setVisibility(View.GONE);
        }
        if (list.get(position).isIs_follow()) {
            holder.cardRightImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.card_wechat));
        } else {
            holder.cardRightImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.card_add));
        }
        if (isSelectModel) {
            holder.selectLayout.setVisibility(View.VISIBLE);
            if (list.get(position).isSelected()) {
                holder.selectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.gouwuche_selected));
            } else {
                holder.selectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.gouwuche_unselect));
            }
        } else {
            holder.selectLayout.setVisibility(View.GONE);
        }
        holder.selectLayout.setOnClickListener(new OnSelectClick(position));
        holder.cardRightImageView.setOnClickListener(new OnCardRightImageClick(position));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }


    private class OnCardRightImageClick implements View.OnClickListener {
        private int position;

        public OnCardRightImageClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (list.get(position).isIs_follow()) {//聊天

            } else {
                addCard(position);
            }
        }
    }

    private BaseResult baseResult;
    private void addCard(final int position) {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CARD_EXCHANGE);
        params.addBodyParameter("tuid", String.valueOf(list.get(position).getId()));
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

    private class OnSelectClick implements View.OnClickListener{
        private int position;

        public OnSelectClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            list.get(position).setSelected(!list.get(position).isSelected());
            notifyDataSetChanged();
        }
    }
}
