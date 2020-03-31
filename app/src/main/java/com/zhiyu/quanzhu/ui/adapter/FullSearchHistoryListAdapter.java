package com.zhiyu.quanzhu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.FullSearchHistory;
import com.zhiyu.quanzhu.model.dao.FullSearchHistoryDao;

import java.util.List;

public class FullSearchHistoryListAdapter extends BaseAdapter {
    private List<FullSearchHistory> list;
    public void setList(List<FullSearchHistory> historyList){
        this.list=historyList;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return null==list?0:list.size();
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
        TextView historyTextView;
        RelativeLayout closeLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(null==convertView){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_full_search_history,null);
            holder.historyTextView=convertView.findViewById(R.id.historyTextView);
            holder.closeLayout=convertView.findViewById(R.id.closeLayout);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.historyTextView.setText(list.get(position).getHistory());
        holder.closeLayout.setOnClickListener(new OnDeleteFullSearchHistoryClick(position));
        return convertView;
    }

    private class OnDeleteFullSearchHistoryClick implements View.OnClickListener{
        private int position;
        public OnDeleteFullSearchHistoryClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            FullSearchHistoryDao.getDao().deleteFullSearchHistory(list.get(position));
            if(null!=onDeleteFullSearchHistory){
                onDeleteFullSearchHistory.onRefreshFullSearchHistory();
            }
        }
    }
    private OnDeleteFullSearchHistory onDeleteFullSearchHistory;
    public void setOnDeleteFullSearchHistory(OnDeleteFullSearchHistory listener){
        onDeleteFullSearchHistory=listener;
    }
    public interface OnDeleteFullSearchHistory{
        void onRefreshFullSearchHistory();
    }
}
