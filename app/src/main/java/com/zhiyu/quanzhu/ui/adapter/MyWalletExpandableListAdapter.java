package com.zhiyu.quanzhu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.PurseRecordParent;
import com.zhiyu.quanzhu.utils.PriceParseUtils;

import java.util.List;

public class MyWalletExpandableListAdapter extends BaseExpandableListAdapter {
    private List<PurseRecordParent> list;

    public void setList(List<PurseRecordParent> parentList) {
        this.list = parentList;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return null == list.get(groupPosition).getRecord_list() ?
                0 : list.get(groupPosition).getRecord_list().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getRecord_list().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return list.get(groupPosition).getRecord_list().get(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    class ViewHolderParent {
        TextView titleTextView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderParent holder = null;
        if (null == convertView) {
            holder = new ViewHolderParent();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_wallet_list_parent, null);
            holder.titleTextView = convertView.findViewById(R.id.titleTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderParent) convertView.getTag();
        }
        holder.titleTextView.setText(list.get(groupPosition).getTitle());
        return convertView;
    }

    class ViewHolderChild {
        TextView remarkTextView, payTypeTextView, moneyTextView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderChild holder = null;
        if (null == convertView) {
            holder = new ViewHolderChild();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_wallet_list_child, parent, false);
            holder.remarkTextView = convertView.findViewById(R.id.remarkTextView);
            holder.payTypeTextView = convertView.findViewById(R.id.payTypeTextView);
            holder.moneyTextView = convertView.findViewById(R.id.moneyTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderChild) convertView.getTag();
        }
        holder.remarkTextView.setText(list.get(groupPosition).getRecord_list().get(childPosition).getRemark());
        holder.payTypeTextView.setText(list.get(groupPosition).getRecord_list().get(childPosition).getPay_type());
        holder.moneyTextView.setText((list.get(groupPosition).getRecord_list().get(childPosition).getType() == 1 ? "+" : "-") +
                PriceParseUtils.getInstance().parsePrice(list.get(groupPosition).getRecord_list().get(childPosition).getMoney()));
        holder.moneyTextView.setTextColor(list.get(groupPosition).getRecord_list().get(childPosition).getType() == 1 ?
                parent.getResources().getColor(R.color.text_color_purse_in) : parent.getResources().getColor(R.color.text_color_purse_out));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
