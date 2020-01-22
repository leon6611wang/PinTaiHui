package com.zhiyu.quanzhu.ui.widget.rongshare;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.adapter.OrderMessageGoodsRecyclerAdapter;
import com.zhiyu.quanzhu.utils.GsonUtils;

import java.util.List;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;

@ProviderTag(messageContent = ShareMessage.class, showReadState = true)
public class ShareMessageItemProvider extends IContainerItemProvider.MessageProvider<ShareMessage> {
    private Context context;

    public ShareMessageItemProvider(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void bindView(View view, int i, ShareMessage shareMessage, UIMessage uiMessage) {
        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.titleTextView.setText("" + shareMessage.getTitle());
        holder.descriptionTextView.setText(shareMessage.getDescription());
        Glide.with(context).load(shareMessage.getImage_url()).into(holder.mImageView);
    }

    @Override
    public Spannable getContentSummary(ShareMessage shareMessage) {
        return new SpannableString("内部分享");
    }

    @Override
    public void onItemClick(View view, int i, ShareMessage shareMessage, UIMessage uiMessage) {

    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.rong_share_message, null);
        ViewHolder holder = new ViewHolder();
        holder.titleTextView = view.findViewById(R.id.titleTextView);
        holder.descriptionTextView = view.findViewById(R.id.descriptionTextView);
        holder.mImageView = view.findViewById(R.id.mImageView);
        view.setTag(holder);
        return view;
    }

    private static class ViewHolder {
       TextView titleTextView,descriptionTextView;
       ImageView mImageView;
    }
}
