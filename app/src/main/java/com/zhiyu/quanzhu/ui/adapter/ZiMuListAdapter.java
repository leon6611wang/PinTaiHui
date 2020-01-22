package com.zhiyu.quanzhu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zhiyu.quanzhu.R;
import java.util.List;

public class ZiMuListAdapter extends BaseAdapter {
    private List<String> ziMuList;

    public void setData(List<String> list){
        this.ziMuList=list;
        notifyDataSetInvalidated();
    }
    @Override
    public int getCount() {
        return null==ziMuList?0:ziMuList.size();
    }

    @Override
    public Object getItem(int position) {
        return ziMuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

     class ViewHolder {
        TextView ziMuTextView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(null==convertView){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zimu_list,null);
            holder.ziMuTextView=convertView.findViewById(R.id.ziMuTextView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.ziMuTextView.setText(ziMuList.get(position));
        return convertView;
    }
}
