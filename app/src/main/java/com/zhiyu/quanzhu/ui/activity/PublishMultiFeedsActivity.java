package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.widget.MoveImageView;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class PublishMultiFeedsActivity extends BaseActivity {
    private MoveImageView moveImageView,moveImageView2,moveImageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_multi_feeds);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
    }

    private void initViews() {
        moveImageView = findViewById(R.id.moveImageView);
        moveImageView2 = findViewById(R.id.moveImageView2);
        moveImageView3 = findViewById(R.id.moveImageView3);
//        int[] location = new int[2];
//        moveImageView.getLocationOnScreen(location);
//        moveImageView.setLocation(location[0], location[1]);
        Glide.with(this).load("https://c-ssl.duitang.com/uploads/item/201711/20/20171120152533_iJ3Fz.thumb.1000_0.jpeg").into(moveImageView);
        Glide.with(this).load("https://c-ssl.duitang.com/uploads/item/202003/19/20200319104657_rpqsc.thumb.1000_0.jpg").into(moveImageView2);
        Glide.with(this).load("https://c-ssl.duitang.com/uploads/item/202001/22/20200122123449_VEjGt.thumb.1000_0.jpeg").into(moveImageView3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        moveImageView.autoMouse(event);
        return false;
    }
}
