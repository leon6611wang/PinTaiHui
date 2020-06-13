package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
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
import com.zhiyu.quanzhu.model.bean.Fans;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

public class FansListAdapter extends BaseAdapter {
    private List<Fans> list;
    private Context context;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FansListAdapter> adapterWeakReference;

        public MyHandler(FansListAdapter adapter) {
            adapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            FansListAdapter adapter = adapterWeakReference.get();
            switch (msg.what) {
                case 1:
                    int position = (Integer) msg.obj;
                    if (adapter.baseResult.getCode() == 200) {
                        adapter.list.get(position).setIs_follow(!adapter.list.get(position).isIs_follow());
                        adapter.notifyDataSetChanged();
                        Toast.makeText(adapter.context, adapter.list.get(position).isIs_follow() ? "关注成功" : "取消关注成功", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    public FansListAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<Fans> fansList) {
        this.list = fansList;
        notifyDataSetChanged();
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
        CircleImageView avatarImageView;
        TextView nameTextView, fansCountTextView, followTextView;
        ImageView followImageView;
        LinearLayout followLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fans, null);
            holder.avatarImageView = convertView.findViewById(R.id.avatarImageView);
            holder.nameTextView = convertView.findViewById(R.id.nameTextView);
            holder.fansCountTextView = convertView.findViewById(R.id.fansCountTextView);
            holder.followLayout = convertView.findViewById(R.id.followLayout);
            holder.followTextView = convertView.findViewById(R.id.followTextView);
            holder.followImageView = convertView.findViewById(R.id.followImageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(parent.getContext()).load(list.get(position).getAvatar()).error(R.drawable.image_error).into(holder.avatarImageView);
        if (!StringUtils.isNullOrEmpty(list.get(position).getUsername()))
            holder.nameTextView.setText(list.get(position).getUsername());
        holder.fansCountTextView.setText(String.valueOf(list.get(position).getFans()));
        if (list.get(position).isIs_follow()) {
            if (list.get(position).isIs_all()) {
                holder.followLayout.setBackground(parent.getResources().getDrawable(R.drawable.shape_oval_solid_bg_yellow));
                holder.followImageView.setVisibility(View.VISIBLE);
                holder.followImageView.setImageDrawable(parent.getContext().getResources().getDrawable(R.mipmap.hu_guan));
                holder.followTextView.setText("互关");
                holder.followTextView.setTextColor(parent.getContext().getResources().getColor(R.color.white));
            } else {
                holder.followLayout.setBackground(parent.getResources().getDrawable(R.drawable.shape_oval_solid_bg_yellow));
                holder.followImageView.setVisibility(View.GONE);
                holder.followTextView.setText("已关注");
                holder.followTextView.setTextColor(parent.getContext().getResources().getColor(R.color.white));
            }
        } else {
            holder.followLayout.setBackground(parent.getResources().getDrawable(R.drawable.shape_oval_bg_yellow));
            holder.followImageView.setVisibility(View.VISIBLE);
            holder.followImageView.setImageDrawable(parent.getContext().getResources().getDrawable(R.mipmap.guanzhu_yellow));
            holder.followTextView.setText("关注");
            holder.followTextView.setTextColor(parent.getContext().getResources().getColor(R.color.text_color_yellow));
        }
        holder.followLayout.setOnClickListener(new OnFollowClickListener(position));
        return convertView;
    }


    private class OnFollowClickListener implements View.OnClickListener {
        private int position;

        public OnFollowClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            follow(position);
        }
    }

    private BaseResult baseResult;

    private void follow(final int position) {
        System.out.println("follow position: " + position);
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FOLLOW);
        params.addBodyParameter("follow_id", String.valueOf(list.get(position).getId()));
        params.addBodyParameter("module_type", "user");
        params.addBodyParameter("type", list.get(position).isIs_follow() ? "1" : "0");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("follow: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.obj = position;
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("follow: " + ex.toString());
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
