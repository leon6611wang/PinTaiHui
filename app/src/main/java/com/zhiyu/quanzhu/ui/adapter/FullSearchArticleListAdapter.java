package com.zhiyu.quanzhu.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.FullSearchArticle;
import com.zhiyu.quanzhu.ui.activity.ArticleInformationActivity;
import com.zhiyu.quanzhu.ui.dialog.ShareDialog;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.HorizontalListView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

public class FullSearchArticleListAdapter extends BaseAdapter {
    private List<FullSearchArticle> list;
    private Activity activity;
    private Context context;
    private ShareDialog shareDialog;
    private MyHandler myHandler = new MyHandler(this);

    public FullSearchArticleListAdapter(Activity aty, Context context) {
        this.activity = aty;
        this.context = context;
        initDialogs();
    }

    private void initDialogs() {
        shareDialog = new ShareDialog(activity, context, R.style.dialog);
    }


    public void setList(List<FullSearchArticle> articleList) {
        this.list = articleList;
        notifyDataSetChanged();
    }

    private static class MyHandler extends Handler {
        WeakReference<FullSearchArticleListAdapter> adapterWeakReference;

        public MyHandler(FullSearchArticleListAdapter adapter) {
            adapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            FullSearchArticleListAdapter adapter = adapterWeakReference.get();
            switch (msg.what) {
                case 1:
                    Toast.makeText(adapter.context, adapter.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (adapter.baseResult.getCode() == 200) {
                        int posiiton = (Integer) msg.obj;
                        adapter.list.get(posiiton).setIs_collect(!adapter.list.get(posiiton).isIs_collect());
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    Toast.makeText(adapter.context, adapter.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (adapter.baseResult.getCode() == 200) {
                        int posiiton = (Integer) msg.obj;
                        adapter.list.get(posiiton).setIs_prise(!adapter.list.get(posiiton).isIs_prise());
                        int priseCount = adapter.list.get(posiiton).getPrise_num();
                        adapter.list.get(posiiton).setPrise_num(adapter.list.get(posiiton).isIs_prise() ? priseCount + 1 : priseCount - 1);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    @Override
    public int getCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        LinearLayout rootLayout;
        CircleImageView avatarImageView;
        TextView nameTextView, titleTextView, sourceTextView, timeTextView, shareTextView, commentTextView, priseTextView;
        LinearLayout priseLayout;
        NiceImageView coverImageView;
        HorizontalListView tagListView;
        FullSearchFeedTagListAdapter adapter = new FullSearchFeedTagListAdapter();
        ImageView collectImageView, priseImageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_full_search_article, null);
            holder.rootLayout = convertView.findViewById(R.id.rootLayout);
            holder.avatarImageView = convertView.findViewById(R.id.avatarImageView);
            holder.nameTextView = convertView.findViewById(R.id.nameTextView);
            holder.titleTextView = convertView.findViewById(R.id.titleTextView);
            holder.coverImageView = convertView.findViewById(R.id.coverImageView);
            holder.sourceTextView = convertView.findViewById(R.id.sourceTextView);
            holder.timeTextView = convertView.findViewById(R.id.timeTextView);
            holder.collectImageView = convertView.findViewById(R.id.collectImageView);
            holder.shareTextView = convertView.findViewById(R.id.shareTextView);
            holder.commentTextView = convertView.findViewById(R.id.commentTextView);
            holder.priseLayout = convertView.findViewById(R.id.priseLayout);
            holder.priseImageView = convertView.findViewById(R.id.priseImageView);
            holder.priseTextView = convertView.findViewById(R.id.priseTextView);
            holder.tagListView = convertView.findViewById(R.id.tagListView);
            holder.tagListView.setAdapter(holder.adapter);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.titleTextView.setText(list.get(position).getTitle());
        if(!StringUtils.isNullOrEmpty(list.get(position).getThumb())){
            holder.coverImageView.setVisibility(View.VISIBLE);
            Glide.with(parent.getContext()).load(list.get(position).getThumb()).error(R.drawable.image_error).into(holder.coverImageView);
        }else{
            holder.coverImageView.setVisibility(View.GONE);
        }

        holder.timeTextView.setText(list.get(position).getTime());
        holder.adapter.setList(list.get(position).getFeeds_tags());
        if (list.get(position).isIs_collect()) {
            holder.collectImageView.setImageDrawable(parent.getContext().getResources().getDrawable(R.mipmap.shoucang_yellow));
        } else {
            holder.collectImageView.setImageDrawable(parent.getContext().getResources().getDrawable(R.mipmap.shoucang_gray));
        }
        holder.shareTextView.setText(String.valueOf(list.get(position).getShare_num()));
        holder.commentTextView.setText(String.valueOf(list.get(position).getComment_num()));
        holder.priseTextView.setText(String.valueOf(list.get(position).getPrise_num()));
        if (list.get(position).isIs_prise()) {
            holder.priseImageView.setImageDrawable(parent.getContext().getResources().getDrawable(R.mipmap.dianzan_yellow));
        } else {
            holder.priseImageView.setImageDrawable(parent.getContext().getResources().getDrawable(R.mipmap.dianzan_gray));
        }
        holder.collectImageView.setOnClickListener(new OnCollectClick(position));
        holder.priseLayout.setOnClickListener(new OnPriseClick(position));
        holder.shareTextView.setOnClickListener(new OnShareClick(position));
        holder.rootLayout.setOnClickListener(new OnArticleInfoClick(position));
        return convertView;
    }


    private class OnCollectClick implements View.OnClickListener {
        private int position;

        public OnCollectClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            collectArticle(position);
        }
    }

    private class OnShareClick implements View.OnClickListener {
        private int position;

        public OnShareClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            shareDialog.show();
        }
    }

    private class OnPriseClick implements View.OnClickListener {
        private int position;

        public OnPriseClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            priseArticle(position);
        }
    }

    private class OnArticleInfoClick implements View.OnClickListener {
        private int position;

        public OnArticleInfoClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent articleInfoIntent = new Intent(context, ArticleInformationActivity.class);
            articleInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            articleInfoIntent.putExtra("article_id", list.get(position).getId());
            context.startActivity(articleInfoIntent);
        }
    }

    private BaseResult baseResult;

    private void priseArticle(final int position) {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.PRISE);
        params.addBodyParameter("prise_id", String.valueOf(list.get(position).getId()));
        params.addBodyParameter("module_type", "quanzi");
        params.addBodyParameter("type", (list.get(position).isIs_prise() ? "1" : "0"));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
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

    private void collectArticle(final int position) {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.COLLECT);
        params.addBodyParameter("collect_id", String.valueOf(list.get(position).getId()));
        params.addBodyParameter("module_type", "quanzi");
        params.addBodyParameter("type", (list.get(position).isIs_collect() ? "1" : "0"));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
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
