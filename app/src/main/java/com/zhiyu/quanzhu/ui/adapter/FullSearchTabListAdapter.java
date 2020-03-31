package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.FullSearchTab;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.List;

public class FullSearchTabListAdapter extends BaseAdapter {
    private Context context;
    private List<FullSearchTab> list;
    private LinearLayout.LayoutParams params;

    public FullSearchTabListAdapter(Context context) {
        this.context = context;
        int screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        int dp_35 = (int) context.getResources().getDimension(R.dimen.dp_35);
        params = new LinearLayout.LayoutParams(Math.round(screenWidth / 6), dp_35);
        params.gravity = Gravity.CENTER;
    }

    public void setList(List<FullSearchTab> tabList) {
        this.list = tabList;
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
        TextView tabTextView;
        View lineView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_full_search_tabtitle, null);
            holder.tabTextView = convertView.findViewById(R.id.tabTextView);
            holder.tabTextView.setLayoutParams(params);
            holder.lineView = convertView.findViewById(R.id.lineView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list.get(position).isSelected()) {
            holder.lineView.setVisibility(View.VISIBLE);
            holder.tabTextView.setTextSize(17);
            holder.tabTextView.setTextColor(context.getResources().getColor(R.color.text_color_yellow));
            holder.tabTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            holder.lineView.setVisibility(View.INVISIBLE);
            holder.tabTextView.setTextSize(14);
            holder.tabTextView.setTextColor(context.getResources().getColor(R.color.text_color_gray));
            holder.tabTextView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        holder.tabTextView.setText(list.get(position).getTab());
        holder.tabTextView.setOnClickListener(new OnTabClickListener(position));
        return convertView;
    }

    private class OnTabClickListener implements View.OnClickListener {
        private int position;

        public OnTabClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            for (FullSearchTab tab : list) {
                tab.setSelected(false);
            }
            list.get(position).setSelected(true);
            notifyDataSetChanged();
            if (null != onSelectTagListener) {
                onSelectTagListener.onSelectTag(position);
            }

        }
    }

    private OnSelectTagListener onSelectTagListener;

    public void setOnSelectTagListener(OnSelectTagListener listener) {
        this.onSelectTagListener = listener;
    }

    public interface OnSelectTagListener {
        void onSelectTag(int position);
    }
}
