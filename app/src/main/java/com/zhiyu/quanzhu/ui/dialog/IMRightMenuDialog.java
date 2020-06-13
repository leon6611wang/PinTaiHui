package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.activity.ConversationActivity;
import com.zhiyu.quanzhu.ui.adapter.ConversationGroupGoodsRecyclerAdapter;
import com.zhiyu.quanzhu.ui.adapter.ViewPagerAdapter;
import com.zhiyu.quanzhu.ui.fragment.ConversationGroupGoodsFragment;
import com.zhiyu.quanzhu.utils.GridSpacingItemDecoration;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 圈聊-右侧商品菜单
 */
public class IMRightMenuDialog extends DialogFragment implements View.OnClickListener {
    private int dialogHeight, screenHeight, screenWidth, dp_10, contentLayoutWidth, dp_5;
    private LinearLayout contentLayout;
    private WindowManager.LayoutParams params;
    private boolean isGetHeight = false;
    private LinearLayout closeLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList = new ArrayList<>();

    //    public IMRightMenuDialog(Context context, int themeResId) {
//        super(context, themeResId);
//        this.context = context;
//        screenHeight = ScreentUtils.getInstance().getScreenHeight(context);
//        dp_10 = (int) context.getResources().getDimension(R.dimen.dp_10);
//        dp_5 = (int) context.getResources().getDimension(R.dimen.dp_5);
//    }


    public void setDialogHeight(int sw, int sh, int height) {
        this.screenWidth = sw;
        this.screenHeight = sh;
        this.dialogHeight = height;
//        params = getDialog().getWindow().getAttributes();
//        params.height = screenHeight - dialogHeight - dp_10;
//        params.width = screenWidth;
//        getDialog().getWindow().setAttributes(params);
//        getDialog().getWindow().setGravity(Gravity.RIGHT | Gravity.BOTTOM);
//        getDialog().getWindow().setWindowAnimations(R.style.dialogRightShow);
//        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_im_right_menu, container);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.dialogRightShow);
    }

    @Override
    public void onStart() {
        super.onStart();

        Window win = getDialog().getWindow();
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = dialogHeight;
        win.setAttributes(params);


    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dialog_im_right_menu);
//
//        params.width = ScreentUtils.getInstance().getScreenWidth(context);
//        getWindow().setAttributes(params);
//        getWindow().setGravity(Gravity.RIGHT | Gravity.BOTTOM);
//        getWindow().setWindowAnimations(R.style.dialogRightShow);
//        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        initViews();
//    }

    private void initViews() {
        contentLayout = view.findViewById(R.id.contentLayout);
        contentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!isGetHeight) {
                    isGetHeight = true;
                    contentLayoutWidth = contentLayout.getHeight();
//                    adapter.setWidth(contentLayoutWidth);
                }
            }
        });
        mViewPager = view.findViewById(R.id.mViewPager);
        fragmentList.add(new ConversationGroupGoodsFragment());
        fragmentList.add(new ConversationGroupGoodsFragment());
        fragmentList.add(new ConversationGroupGoodsFragment());
        adapter = new ViewPagerAdapter(getChildFragmentManager(), fragmentList);
        mViewPager.setAdapter(adapter);

        closeLayout = view.findViewById(R.id.closeLayout);
        closeLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeLayout:
                dismiss();
                break;
        }
    }
}
