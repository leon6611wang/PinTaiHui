package com.leon.shehuibang.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.leon.shehuibang.R;

public class CommentsRecyclerViewItemMenu extends LinearLayout implements View.OnClickListener {
    private View view;
    private Context context;
    private int menu_height;
    private ValueAnimator show, hide;
    private RelativeLayout itemMenuLayout0, itemMenuLayout1, itemMenuLayout2, itemMenuLayout3, itemMenuLayout4;

    public CommentsRecyclerViewItemMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        menu_height = (int) context.getResources().getDimension(R.dimen.comments_item_menu_height);
        view = inflate(context, R.layout.widget_comments_recyclerview_item_menu, this);
        initViews();
        this.setVisibility(GONE);
    }

    private void initViews() {
        itemMenuLayout0 = view.findViewById(R.id.itemMenuLayout0);
        itemMenuLayout0.setOnClickListener(this);
        itemMenuLayout1 = view.findViewById(R.id.itemMenuLayout1);
        itemMenuLayout1.setOnClickListener(this);
        itemMenuLayout2 = view.findViewById(R.id.itemMenuLayout2);
        itemMenuLayout2.setOnClickListener(this);
        itemMenuLayout3 = view.findViewById(R.id.itemMenuLayout3);
        itemMenuLayout3.setOnClickListener(this);
        itemMenuLayout4 = view.findViewById(R.id.itemMenuLayout4);
        itemMenuLayout4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.itemMenuLayout0:
                BottomToast.getInstance(context).show("转发");
                break;
            case R.id.itemMenuLayout1:
                BottomToast.getInstance(context).show("收藏");
                break;
            case R.id.itemMenuLayout2:
                BottomToast.getInstance(context).show("分享");
                break;
            case R.id.itemMenuLayout3:
                BottomToast.getInstance(context).show("删除");
                break;
            case R.id.itemMenuLayout4:
                BottomToast.getInstance(context).show("举报");
                break;
        }
    }

    private boolean isShow = false;

    public void operateMenu() {
        if (this.getVisibility() == GONE) {
            this.setVisibility(VISIBLE);
        }
        showMenu();
    }

    private ValueAnimator animator;

    private void showMenu() {
        if (isShow) {
            animator = ValueAnimator.ofInt(menu_height, 0);
        } else {
            animator = ValueAnimator.ofInt(0, menu_height);
        }
        animator.setDuration(500);
        animator.setStartDelay(0);
        animator.setRepeatCount(0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = currentValue;
                view.setLayoutParams(layoutParams);
                view.requestLayout();
            }
        });
        animator.start();
        isShow = !isShow;
    }


}
