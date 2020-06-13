package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;

/**
 * 名片详情-菜单
 */
public class CardInfoMenuDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private int menuImageY;
    private LinearLayout sharelayout, editlayout, collectlayout, tousulayout, followlayout;
    private ImageView collectImageView, followImageView;
    private TextView collectTextView, followTextView;
    private View topMarginView;
    private boolean isMyCard, isCollect, isFollow;

    public CardInfoMenuDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public CardInfoMenuDialog(@NonNull Context context, int themeResId, OnMyMingPianMenuChooseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.onMyMingPianMenuChooseListener = listener;
    }

    public void setInitData(boolean myCard, boolean collect, boolean follow) {
        this.isMyCard = myCard;
        this.isCollect = collect;
        this.isFollow = follow;
        editlayout.setVisibility(isMyCard ? View.VISIBLE : View.GONE);
        collectlayout.setVisibility(isMyCard ? View.GONE : View.VISIBLE);
        tousulayout.setVisibility(isMyCard ? View.GONE : View.VISIBLE);
        collectImageView.setImageDrawable(collect ? getContext().getResources().getDrawable(R.mipmap.mymingpian_menu_collect_yellow) :
                getContext().getResources().getDrawable(R.mipmap.mymingpian_menu_collect_gra));
        collectTextView.setText(collect ? "取消收藏" : "收藏名片");
        followlayout.setVisibility(isMyCard ? View.GONE : View.VISIBLE);
        followImageView.setImageDrawable(follow ? getContext().getResources().getDrawable(R.mipmap.mymingpian_menu_follow_yellow) :
                getContext().getResources().getDrawable(R.mipmap.mymingpian_menu_follow_white));
        followTextView.setText(follow ? "取消关注" : "关注用户");

    }

    public void setMenuImageY(int y) {
        this.menuImageY = y;
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, menuImageY);
        topMarginView.setLayoutParams(ll);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_card_info_menu);
        initViews();
        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.width = ScreentUtils.getInstance().getScreenWidth(mContext);
//        params.height = ScreentUtils.getInstance().getScreenHeight(mContext);
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.TOP | Gravity.RIGHT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }


    private void initViews() {
        sharelayout = findViewById(R.id.sharelayout);
        sharelayout.setOnClickListener(this);
        editlayout = findViewById(R.id.editlayout);
        editlayout.setOnClickListener(this);
        topMarginView = findViewById(R.id.topMarginView);
        collectlayout = findViewById(R.id.collectlayout);
        collectlayout.setOnClickListener(this);
        collectImageView = findViewById(R.id.collectImageView);
        collectTextView = findViewById(R.id.collectTextView);
        tousulayout = findViewById(R.id.tousulayout);
        tousulayout.setOnClickListener(this);
        followlayout = findViewById(R.id.followlayout);
        followlayout.setOnClickListener(this);
        followImageView = findViewById(R.id.followImageView);
        followTextView = findViewById(R.id.followTextView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sharelayout:
                if (null != onMyMingPianMenuChooseListener) {
                    onMyMingPianMenuChooseListener.onMyMingPianMenuChoose(1, "分享");
                    dismiss();
                }
                break;
            case R.id.editlayout:
                if (null != onMyMingPianMenuChooseListener) {
                    onMyMingPianMenuChooseListener.onMyMingPianMenuChoose(2, "编辑");
                    dismiss();
                }
                break;
            case R.id.collectlayout:
                if (null != onMyMingPianMenuChooseListener) {
                    onMyMingPianMenuChooseListener.onMyMingPianMenuChoose(3, "收藏");
                    dismiss();
                }
                break;
            case R.id.tousulayout:
                if (null != onMyMingPianMenuChooseListener) {
                    onMyMingPianMenuChooseListener.onMyMingPianMenuChoose(4, "投诉");
                    dismiss();
                }
                break;
            case R.id.followlayout:
                if (null != onMyMingPianMenuChooseListener) {
                    onMyMingPianMenuChooseListener.onMyMingPianMenuChoose(5, "关注");
                    dismiss();
                }
                break;
        }
    }

    private OnMyMingPianMenuChooseListener onMyMingPianMenuChooseListener;

    public interface OnMyMingPianMenuChooseListener {
        void onMyMingPianMenuChoose(int position, String desc);
    }

}
