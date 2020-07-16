package com.zhiyu.quanzhu.ui.widget.rongshare;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
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
import com.leon.chic.utils.LogUtils;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.Message;
import com.zhiyu.quanzhu.model.bean.ShareMessageContent;
import com.zhiyu.quanzhu.ui.activity.ArticleInformationActivity;
import com.zhiyu.quanzhu.ui.activity.CardInformationActivity;
import com.zhiyu.quanzhu.ui.activity.CircleInfoActivity;
import com.zhiyu.quanzhu.ui.activity.FeedInformationActivity;
import com.zhiyu.quanzhu.ui.activity.GoodsInformationActivity;
import com.zhiyu.quanzhu.ui.activity.ShopInformationActivity;
import com.zhiyu.quanzhu.ui.activity.VideoInformationActivity;
import com.zhiyu.quanzhu.ui.adapter.OrderMessageGoodsRecyclerAdapter;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.ShareUtils;

import java.util.List;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;

@ProviderTag(messageContent = ShareMessage.class, showReadState = true, showPortrait = true, centerInHorizontal = false)
public class ShareMessageItemProvider extends IContainerItemProvider.MessageProvider<ShareMessage> {
    private Context context;

    public ShareMessageItemProvider(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void bindView(View view, int i, ShareMessage shareMessage, UIMessage uiMessage) {
        final ViewHolder holder = (ViewHolder) view.getTag();
        LogUtils.getInstance().show("ShareMessage", "content: " + shareMessage.getContent());
        ShareMessageContent content = null;
        if (null != shareMessage&&null!=shareMessage.getContent() && shareMessage.getContent().startsWith("{") && shareMessage.getContent().endsWith("}")) {
            content = GsonUtils.GsonToBean2(shareMessage.getContent(), ShareMessageContent.class);
        }
        if (null != content) {
            if (!StringUtils.isNullOrEmpty(content.getShareTitle()))
                holder.titleTextView.setText(content.getShareTitle());
            if (!StringUtils.isNullOrEmpty(content.getShareContent()))
                holder.descriptionTextView.setText(content.getShareContent());
            Glide.with(context).load(content.getShareImageUrl()).error(R.drawable.image_error).into(holder.mImageView);
        }

//        holder.descriptionTextView.setText(shareMessage.getContent());
    }

    @Override
    public Spannable getContentSummary(ShareMessage shareMessage) {
//        return new SpannableString(shareMessage.getShareTitle());
        ShareMessageContent content = null;
        if (null != shareMessage && shareMessage.getContent().startsWith("{") && shareMessage.getContent().endsWith("}")) {
            content = GsonUtils.GsonToBean2(shareMessage.getContent(), ShareMessageContent.class);
        }
        return new SpannableString(content.getShareTitle());
    }

    @Override
    public void onItemClick(View view, int i, ShareMessage shareMessage, UIMessage uiMessage) {
        ShareMessageContent content = null;
        if (null != shareMessage && shareMessage.getContent().startsWith("{") && shareMessage.getContent().endsWith("}")) {
            content = GsonUtils.GsonToBean2(shareMessage.getContent(), ShareMessageContent.class);
        }
        if (null != content && !StringUtils.isNullOrEmpty(content.getShareTypeId())) {
            switch (content.getShareType()) {
                case ShareUtils.SHARE_TYPE_FEED:
                    Intent feedIntent = new Intent(context, FeedInformationActivity.class);
                    feedIntent.putExtra("feed_id", Integer.parseInt(content.getShareTypeId()));
                    feedIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(feedIntent);
                    break;
                case ShareUtils.SHARE_TYPE_ARTICLE:
                    Intent articleIntent = new Intent(context, ArticleInformationActivity.class);
                    articleIntent.putExtra("article_id", Integer.parseInt(content.getShareTypeId()));
                    articleIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(articleIntent);
                    break;
                case ShareUtils.SHARE_TYPE_VIDEO:
                    Intent videoIntent = new Intent(context, VideoInformationActivity.class);
                    videoIntent.putExtra("feeds_id", Integer.parseInt(content.getShareTypeId()));
                    videoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(videoIntent);
                    break;
                case ShareUtils.SHARE_TYPE_CARD:
                    Intent cardIntent = new Intent(context, CardInformationActivity.class);
                    cardIntent.putExtra("card_id", Long.parseLong(content.getShareTypeId()));
                    cardIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(cardIntent);
                    break;
                case ShareUtils.SHARE_TYPE_CIRCLE:
                    Intent circleIntent = new Intent(context, CircleInfoActivity.class);
                    circleIntent.putExtra("circle_id", Long.parseLong(content.getShareTypeId()));
                    circleIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(circleIntent);
                    break;
                case ShareUtils.SHARE_TYPE_GOODS:
                    Intent goodsIntent = new Intent(context, GoodsInformationActivity.class);
                    goodsIntent.putExtra("goods_id", Long.parseLong(content.getShareTypeId()));
                    goodsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(goodsIntent);
                    break;
                case ShareUtils.SHARE_TYPE_SHOP:
                    Intent shopIntent = new Intent(context, ShopInformationActivity.class);
                    shopIntent.putExtra("shop_id", content.getShareTypeId());
                    shopIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(shopIntent);
                    break;
            }
        }
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.rong_share_message, null);
        ViewHolder holder = new ViewHolder();
        holder.titleTextView = view.findViewById(R.id.titleTextView);
        holder.descriptionTextView = view.findViewById(R.id.descriptionTextView);
        holder.mImageView = view.findViewById(R.id.mImageView);
        holder.mCardView = view.findViewById(R.id.mCardView);
//        holder.mCardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("分享cardview点击");
//            }
//        });
        view.setTag(holder);
        return view;
    }

    private static class ViewHolder {
        TextView titleTextView, descriptionTextView;
        NiceImageView mImageView;
        CardView mCardView;
    }
}
