package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.WhoCanSee;
import com.zhiyu.quanzhu.ui.adapter.WhoCanSeeRecyclerAdapter;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 谁可以看
 */
public class WhoCanSeeDialog extends Dialog implements View.OnClickListener {
    private List<WhoCanSee> list;
    private TextView cancelTextView, confirmTextView;
    private RecyclerView mRecyclerView;
    private WhoCanSeeRecyclerAdapter adapter;

    public WhoCanSeeDialog(@NonNull Context context, int themeResId, OnWhoCanSeeListener listener) {
        super(context, themeResId);
        this.onWhoCanSeeListener = listener;
    }

    public void setSelectCircle(boolean isSelect) {
        if (isSelect) {
            if (list.size() == 3) {
                list.add(new WhoCanSee(4, "仅@圈子可见", "仅此圈子圈友可见", false));
            }
        } else {
            if (list.size() == 4) {
                list.remove(3);
            }
        }
        adapter.setList(list);
    }


    public void setWhoCanSee(int index) {
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setSelected(false);
            }
            list.get(index - 1).setSelected(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_who_can_see);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
        params.height = ScreentUtils.getInstance().getScreenHeight(getContext()) / 5 * 3;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initDatas();
        initViews();
    }

    private void initDatas() {
        list = new ArrayList<>();
        list.add(new WhoCanSee(1, "公开", "全平台可见", true));
        list.add(new WhoCanSee(2, "名片圈友可见", "通讯录圈友可见", false));
        list.add(new WhoCanSee(3, "私密", "仅自己可见", false));

    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new WhoCanSeeRecyclerAdapter(getContext());
        adapter.setList(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);
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
                if (null != onWhoCanSeeListener) {
                    onWhoCanSeeListener.onWhoCanSee(adapter.getWhoCanSee());
                }
                dismiss();
                break;
        }
    }

    private OnWhoCanSeeListener onWhoCanSeeListener;

    public interface OnWhoCanSeeListener {
        void onWhoCanSee(WhoCanSee whoCanSee);
    }
}
