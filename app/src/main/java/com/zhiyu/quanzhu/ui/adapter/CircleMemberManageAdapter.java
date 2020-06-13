package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.MyCardFriend;
import com.zhiyu.quanzhu.ui.listener.ShareInnerSelectListener;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.hoverExpandableListView.IDockingController;

import java.util.ArrayList;
import java.util.List;

public class CircleMemberManageAdapter extends BaseExpandableListAdapter implements IDockingController {

    private Context mContext;
    private ExpandableListView mListView;
    private List<List<MyCardFriend>> list;

    public void setList(List<List<MyCardFriend>> frendList) {
        this.list = frendList;
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedIdList() {
        List<Integer> idsList = new ArrayList<>();
        for (List<MyCardFriend> childs : list) {
            for (MyCardFriend friend : childs) {
                if (friend.isSelected()) {
                    idsList.add(friend.getUid());
                }
            }
        }
        return idsList;
    }

    public CircleMemberManageAdapter(Context context, ExpandableListView listView) {
        mContext = context;
        mListView = listView;
    }

    @Override
    public int getGroupCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null == list.get(groupPosition) ? 0 : list.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle_member_manage_add_frend_parent, parent, false);
        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        titleTextView.setText(list.get(groupPosition).get(0).getLetter());
        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle_member_manage_add_frend_child, null);
        LinearLayout itemRootLayout = convertView.findViewById(R.id.itemRootLayout);
        ImageView selectedImageView = convertView.findViewById(R.id.selectedImageView);
        CircleImageView avatarImageView = convertView.findViewById(R.id.avatarImageView);
        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        Glide.with(parent.getContext()).load(list.get(groupPosition).get(childPosition).getCard_thumb()).error(R.mipmap.no_avatar).into(avatarImageView);
        String name = StringUtils.isNullOrEmpty(list.get(groupPosition).get(childPosition).getNotename()) ? list.get(groupPosition).get(childPosition).getCard_name() : list.get(groupPosition).get(childPosition).getNotename();
        nameTextView.setText(name);
        itemRootLayout.setOnClickListener(new OnSelectClick(groupPosition, childPosition, selectedImageView));
        if (list.get(groupPosition).get(childPosition).isSelected()) {
            selectedImageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.gouwuche_selected));
        } else {
            selectedImageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.gouwuche_unselect));
        }
        return convertView;
    }

    private class OnSelectClick implements View.OnClickListener {
        private int groupPosition, childPosition;
        private ImageView selectedImageView;

        public OnSelectClick(int groupPosition, int childPosition, ImageView selectedImageView) {
            this.groupPosition = groupPosition;
            this.childPosition = childPosition;
            this.selectedImageView = selectedImageView;
        }

        @Override
        public void onClick(View v) {
            list.get(groupPosition).get(childPosition).setSelected(!list.get(groupPosition).get(childPosition).isSelected());
            if (list.get(groupPosition).get(childPosition).isSelected()) {
                selectedImageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.gouwuche_selected));
                selectedList.add(list.get(groupPosition).get(childPosition));
            } else {
                selectedImageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.gouwuche_unselect));
                selectedList.remove(list.get(groupPosition).get(childPosition));
            }
            if (null != onSelectedCardFrendListener) {
                onSelectedCardFrendListener.OnSelectedCardFrend(selectedList);
            }
        }
    }

    private List<MyCardFriend> selectedList = new ArrayList<>();
    private OnSelectedCardFrendListener onSelectedCardFrendListener;

    public void setOnSelectedCardFrendListener(OnSelectedCardFrendListener listener) {
        this.onSelectedCardFrendListener = listener;
    }

    public interface OnSelectedCardFrendListener {
        void OnSelectedCardFrend(List<MyCardFriend> list);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public int getDockingState(int firstVisibleGroup, int firstVisibleChild) {
        // No need to draw header view if this group does not contain any child & also not expanded.
        if (firstVisibleChild == -1 && !mListView.isGroupExpanded(firstVisibleGroup)) {
            return DOCKING_HEADER_HIDDEN;
        }

        // Reaching current group's last child, preparing for docking next group header.
        if (firstVisibleChild == getChildrenCount(firstVisibleGroup) - 1) {
            return IDockingController.DOCKING_HEADER_DOCKING;
        }

        // Scrolling inside current group, header view is docked.
        return IDockingController.DOCKING_HEADER_DOCKED;
    }

}
