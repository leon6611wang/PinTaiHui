package com.zhiyu.quanzhu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;

import java.util.ArrayList;
import java.util.List;

public class LetterListAdapter extends BaseAdapter {
    private List<String> letterList = new ArrayList<>();

   public void setLetterList(List<String> list){
       this.letterList=list;
       notifyDataSetChanged();
   }



    @Override
    public int getCount() {
        return null==letterList?0:letterList.size();
    }

    @Override
    public Object getItem(int position) {
        return letterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_letter_listview, null);
        TextView letterTextView = convertView.findViewById(R.id.letterTextView);
        letterTextView.setText(letterList.get(position));
        return convertView;
    }
}
