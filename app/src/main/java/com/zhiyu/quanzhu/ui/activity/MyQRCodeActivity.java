package com.zhiyu.quanzhu.ui.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.ThreadPoolUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import io.vov.vitamio.utils.Log;

/**
 * 我的二维码
 */
public class MyQRCodeActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout, bottomLayout;
    private TextView titleTextView, nameTextView, positionTextView, companyTextView, closeTextView, saveTextView;
    private CircleImageView headerpicImageView;
    private ImageView qrImageView;
    private CardView qrCardView;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<MyQRCodeActivity> activityWeakReference;

        public MyHandler(MyQRCodeActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MyQRCodeActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    Toast.makeText(activity, "保存成功!", Toast.LENGTH_SHORT).show();
                    activity.bottomLayout.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qrcode);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
    }

    private void initViews() {
        qrCardView = findViewById(R.id.qrCardView);
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("我的二维码");
        closeTextView = findViewById(R.id.closeTextView);
        closeTextView.setOnClickListener(this);
        saveTextView = findViewById(R.id.saveTextView);
        saveTextView.setOnClickListener(this);
        nameTextView = findViewById(R.id.nameTextView);
        positionTextView = findViewById(R.id.positionTextView);
        companyTextView = findViewById(R.id.companyTextView);
        headerpicImageView = findViewById(R.id.headerpicImageView);
        qrImageView = findViewById(R.id.qrImageView);
        bottomLayout = findViewById(R.id.bottomLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.closeTextView:
                finish();
                break;
            case R.id.saveTextView:
                bottomLayout.setVisibility(View.GONE);
                qrCardView.invalidate();
                qrCardView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (bottomLayout.getVisibility() == View.GONE) {
                            qrCardView.buildDrawingCache();
                            final Bitmap bmp = qrCardView.getDrawingCache(); // 获取图片
                            ThreadPoolUtils.getInstance().init().execute(new Runnable() {
                                @Override
                                public void run() {
                                    savePicture(bmp, "我的名片二维码.jpg");// 保存图片
                                }
                            });
                        }
                    }
                });

                break;
        }
    }

    public void savePicture(Bitmap bm, String fileName) {
        if (null == bm) {
            return;
        }
        File foder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/圈助");
        if (!foder.exists()) {
            foder.mkdirs();
        }
        File myCaptureFile = new File(foder, fileName);
        try {
            if (!myCaptureFile.exists()) {
                myCaptureFile.createNewFile();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            //压缩保存到本地
            bm.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message message = myHandler.obtainMessage(1);
        message.sendToTarget();
    }
}
