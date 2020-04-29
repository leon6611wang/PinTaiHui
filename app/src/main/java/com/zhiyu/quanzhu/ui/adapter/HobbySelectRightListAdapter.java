package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.Hobby;
import com.zhiyu.quanzhu.ui.dialog.InputDialog;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;

import java.util.List;

public class HobbySelectRightListAdapter extends BaseExpandableListAdapter {
    private List<Hobby> list;
    private InputDialog inputDialog;
    private YNDialog ynDialog;

    public HobbySelectRightListAdapter(Context context) {
        inputDialog = new InputDialog(context, R.style.dialog, new InputDialog.OnInputCallback() {
            @Override
            public void onInput(String content) {
                Hobby hobby = new Hobby();
                hobby.setName(content);
                if (null != onAddHobbyListener) {
                    onAddHobbyListener.onAddHobby(hobby);
                }
            }
        });
        ynDialog = new YNDialog(context, R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                if (null != onDeleteHobbyListener) {
                    onDeleteHobbyListener.onDeleteHobby(delete_hobby_id, delete_parent_position, delete_child_position);
                }
            }
        });
    }

    private boolean isXingQu = false;

    public void setList(List<Hobby> hobbyList, boolean isxq) {
        this.list = hobbyList;
        this.isXingQu = isxq;
        notifyDataSetChanged();
    }

    private boolean currentSelected = false;//当前是否选中

    public void childSelect(int groupPosition, int childPosition) {
        currentSelected = list.get(groupPosition).getChild().get(childPosition).isIs_choose();
//        for (int i = 0; i < list.get(groupPosition).getChild().size(); i++) {
//            list.get(groupPosition).getChild().get(i).setIs_choose(false);
//        }
        list.get(groupPosition).getChild().get(childPosition).setIs_choose(!currentSelected);

        boolean groupHasChoose = false;
        for (Hobby hobby : list.get(groupPosition).getChild()) {
            if (hobby.isIs_choose()) {
                groupHasChoose = true;
            }
        }

        if (groupHasChoose) {
            list.get(groupPosition).setIs_choose(true);
        } else {
            list.get(groupPosition).setIs_choose(false);
        }
        notifyDataSetChanged();
    }

    public List<Hobby> getHobbyList() {
        return list;
    }


    @Override
    public int getGroupCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return null == list.get(groupPosition).getChild() ? 0 : list.get(groupPosition).getChild().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getChild().get(childPosition);
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
        return false;
    }

    private int parentCurrentPosition = -1, childCurrentPosition = -1, pp;

    public void clearPosition() {
        this.parentCurrentPosition = -1;
        childCurrentPosition = -1;
        pp = -1;
    }

    public void setParentCurrentPosition(int position) {
        this.parentCurrentPosition = position;
        if (pp != parentCurrentPosition) {
            childCurrentPosition = -1;
        }
        pp = parentCurrentPosition;
        notifyDataSetChanged();
    }

//    public void setChildCurrentPosition(int position) {
//        this.childCurrentPosition = position;
//        notifyDataSetChanged();
//    }

    private class ParentViewHolder {
        TextView contentTextView, subConTextTextView, dingYiTextView;
        ImageView selectedImageView;
    }

    private class ChildViewHolder {
        TextView contentTextView;
        ImageView selectedImageView, deleteImageView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentViewHolder holder = null;
        if (null == convertView) {
            holder = new ParentViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hobby_select_parent, null);
            holder.contentTextView = convertView.findViewById(R.id.contentTextView);
            holder.subConTextTextView = convertView.findViewById(R.id.subConTextTextView);
            holder.dingYiTextView = convertView.findViewById(R.id.dingYiTextView);
            holder.selectedImageView = convertView.findViewById(R.id.selectedImageView);
            convertView.setTag(holder);
        } else {
            holder = (ParentViewHolder) convertView.getTag();
        }

//        if (parentCurrentPosition == groupPosition) {
//            holder.selectedImageView.setVisibility(View.VISIBLE);
//        } else {
//            holder.selectedImageView.setVisibility(View.INVISIBLE);
//        }
        if (list.get(groupPosition).isIs_choose()) {
            holder.selectedImageView.setVisibility(View.VISIBLE);
        } else {
            holder.selectedImageView.setVisibility(View.INVISIBLE);
        }

        if (isXingQu) {
            if ((list.size() - 1) == groupPosition) {
                holder.dingYiTextView.setVisibility(View.VISIBLE);
            } else {
                holder.dingYiTextView.setVisibility(View.GONE);
            }
        } else {
            holder.dingYiTextView.setVisibility(View.GONE);
        }

        holder.contentTextView.setText(list.get(groupPosition).getName());
        holder.subConTextTextView.setText(list.get(groupPosition).getSub_name());
        holder.dingYiTextView.setOnClickListener(new OnDingYiClick());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (null == convertView) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hobby_select_child, null);
            holder.contentTextView = convertView.findViewById(R.id.contentTextView);
            holder.selectedImageView = convertView.findViewById(R.id.selectedImageView);
            holder.deleteImageView = convertView.findViewById(R.id.deleteImageView);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }

        if (isXingQu) {
            if (groupPosition == (list.size() - 1)) {
                holder.deleteImageView.setVisibility(View.VISIBLE);
                holder.selectedImageView.setVisibility(View.VISIBLE);
            } else {
                holder.deleteImageView.setVisibility(View.GONE);
                holder.selectedImageView.setVisibility(View.GONE);
            }
            holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete_hobby_id = list.get(groupPosition).getChild().get(childPosition).getId();
                    delete_parent_position = groupPosition;
                    delete_child_position = childPosition;
                    ynDialog.show();
                    ynDialog.setTitle("确定删除这条兴趣？");
                }
            });
        } else {
            holder.deleteImageView.setVisibility(View.GONE);
        }
        if (list.get(groupPosition).getChild().get(childPosition).isIs_choose()) {
            holder.selectedImageView.setVisibility(View.VISIBLE);
        } else {
            holder.selectedImageView.setVisibility(View.INVISIBLE);
        }
        holder.contentTextView.setText(list.get(groupPosition).getChild().get(childPosition).getName());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class OnDingYiClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            inputDialog.show();
            inputDialog.setTitleAndHint("兴趣自定义", "请输入您的自定义兴趣...");
        }
    }


    private OnAddHobbyListener onAddHobbyListener;

    public void setOnAddHobbyListener(OnAddHobbyListener listener) {
        this.onAddHobbyListener = listener;
    }

    public interface OnAddHobbyListener {
        void onAddHobby(Hobby hobby);
    }


    private int delete_hobby_id = 0, delete_parent_position, delete_child_position;
    private OnDeleteHobbyListener onDeleteHobbyListener;

    public void setOnDeleteHobbyListener(OnDeleteHobbyListener listener) {
        this.onDeleteHobbyListener = listener;
    }

    public interface OnDeleteHobbyListener {
        void onDeleteHobby(int hobby_id, int delete_parent_position, int delete_child_position);
    }
}
