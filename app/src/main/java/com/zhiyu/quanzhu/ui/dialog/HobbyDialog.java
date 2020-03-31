package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.HobbyDaoChild;
import com.zhiyu.quanzhu.model.bean.HobbyDaoParent;
import com.zhiyu.quanzhu.model.dao.HobbyDao;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 兴趣
 */
public class HobbyDialog extends Dialog implements View.OnClickListener{
    private List<String> parentList = new ArrayList<>();
    private List<String> childList = new ArrayList<>();
    private LoopView parentView, childView;
    private String parent, child;
    private TextView cancelTextView, confirmTextView;
    private List<HobbyDaoParent> hobbyParentList;
    private List<HobbyDaoChild> hobbyChildList;
    private HobbyDaoParent hobbyDaoParent;
    private HobbyDaoChild hobbyDaoChild;
    public HobbyDialog(@NonNull Context context, int themeResId,OnChooseHobbyListener listener) {
        super(context, themeResId);
        this.onChooseHobbyListener=listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_hobby);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
        params.height = ScreentUtils.getInstance().getScreenHeight(getContext()) / 5 * 2;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initData();
        initViews();
    }

    private void initData() {
        hobbyParentList = HobbyDao.getInstance().hobbyParentList();
        if (null != hobbyParentList && hobbyParentList.size() > 0) {
            hobbyDaoParent=hobbyParentList.get(0);
            for (HobbyDaoParent parent : hobbyParentList) {
                parentList.add(parent.getName());
                System.out.println(parent.toString());
            }
        }

        hobbyChildList =HobbyDao.getInstance().hobbyChildList(hobbyParentList.get(0).getId());
        if (null != hobbyChildList && hobbyChildList.size() > 0) {
            hobbyDaoChild=hobbyChildList.get(0);
            for (HobbyDaoChild child : hobbyChildList) {
                childList.add(child.getName());
                System.out.println(child.toString());
            }
        }
    }

    private void initViews() {
        parentView = findViewById(R.id.parentView);
        parentView.setNotLoop();
        childView = findViewById(R.id.childView);
        childView.setNotLoop();
        parentView.setItems(parentList);
        parentView.setInitPosition(0);
        parentView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (!TextUtils.isEmpty(parentList.get(index))) {
                    parent = parentList.get(index);
                    hobbyDaoParent =HobbyDao.getInstance().getHobbyParent(parent);
                    if (null != hobbyChildList) {
                        hobbyChildList.clear();
                    }
                    if (null != childList) {
                        childList.clear();
                    }
                    hobbyChildList = HobbyDao.getInstance().hobbyChildList(hobbyParentList.get(index).getId());
                    if (null != hobbyChildList && hobbyChildList.size() > 0) {
                        for (HobbyDaoChild child : hobbyChildList) {
                            childList.add(child.getName());
                        }
                    }
                    childView.setItems(childList);
                }
            }
        });
        parent = parentList.get(parentView.getSelectedItem());
        childView.setItems(childList);
        childView.setInitPosition(0);
        childView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (!TextUtils.isEmpty(childList.get(index))) {
                    child = childList.get(index);
                    hobbyDaoChild = HobbyDao.getInstance().getHobbyChild(child);
                }
            }
        });
        child = childList.get(childView.getSelectedItem());
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelTextView:
                dismiss();
                break;
            case R.id.confirmTextView:
                if (null != onChooseHobbyListener) {
                    onChooseHobbyListener.onChooseHobby(hobbyDaoParent,hobbyDaoChild);
                    dismiss();
                }
                break;
        }
    }

    private OnChooseHobbyListener onChooseHobbyListener;
    public interface OnChooseHobbyListener{
        void onChooseHobby(HobbyDaoParent parent,HobbyDaoChild child);
    }
}
