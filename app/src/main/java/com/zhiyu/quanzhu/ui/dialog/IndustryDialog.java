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
import com.zhiyu.quanzhu.model.bean.IndustryChild;
import com.zhiyu.quanzhu.model.bean.IndustryParent;
import com.zhiyu.quanzhu.model.dao.IndustryDao;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 行业
 */
public class IndustryDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private LoopView parentView, childView;
    private List<String> parentList = new ArrayList<>();
    private List<String> childList = new ArrayList<>();
    private String parent, child;
    private TextView cancelTextView, confirmTextView;
    private List<IndustryParent> industryParentList;
    private List<IndustryChild> industryChildList;
    private IndustryParent industryParent;
    private IndustryChild industryChild;

    public IndustryDialog(@NonNull Context context, int themeResId, OnHangYeChooseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.onHangYeChooseListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_industry);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext);
        params.height = ScreentUtils.getInstance().getScreenHeight(mContext) / 5 * 2;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initData();
        initViews();
    }

    private void initData() {
        industryParentList = IndustryDao.getInstance().industryParentList();
        if (null != industryParentList && industryParentList.size() > 0) {
            industryParent=industryParentList.get(0);
            for (IndustryParent parent : industryParentList) {
                parentList.add(parent.getName());
                System.out.println(parent.toString());
            }
        }

        industryChildList = IndustryDao.getInstance().industryChildList(industryParentList.get(0).getId());
        if (null != industryChildList && industryChildList.size() > 0) {
            industryChild=industryChildList.get(0);
            for (IndustryChild child : industryChildList) {
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
                    industryParent = IndustryDao.getInstance().industryParent(parent);


                    if (null != industryChildList) {
                        industryChildList.clear();
                    }
                    if (null != childList) {
                        childList.clear();
                    }
                    industryChildList = IndustryDao.getInstance().industryChildList(industryParentList.get(index).getId());
                    if (null != industryChildList && industryChildList.size() > 0) {
                        for (IndustryChild child : industryChildList) {
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
                    industryChild = IndustryDao.getInstance().industryChild(child);
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
                if (null != onHangYeChooseListener) {
                    onHangYeChooseListener.onHangYeChoose(industryParent, industryChild);
                    dismiss();
                }
                break;
        }
    }

    private OnHangYeChooseListener onHangYeChooseListener;

    public interface OnHangYeChooseListener {
        void onHangYeChoose(IndustryParent parent, IndustryChild child);
    }
}
