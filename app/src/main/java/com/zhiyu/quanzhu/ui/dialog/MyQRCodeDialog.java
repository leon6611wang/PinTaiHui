package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.Card;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.utils.SaveBitmapUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.ThreadPoolUtils;

import java.lang.ref.WeakReference;

/**
 * 我的二维码dialog
 */
public class MyQRCodeDialog extends Dialog implements View.OnClickListener {
    private LinearLayout bottomLayout;
    private TextView nameTextView, positionTextView, companyTextView, closeTextView, saveTextView;
    private CircleImageView headerImageView;
    private ImageView qrImageView;
    private CardView qrCardView;
    private int dp_64;
    private LinearLayout.LayoutParams ll;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<MyQRCodeDialog> dialogWeakReference;

        public MyHandler(MyQRCodeDialog dialog) {
            dialogWeakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            MyQRCodeDialog dialog = dialogWeakReference.get();
            switch (msg.what) {
                case 1:
                    dialog.isSaving = false;
                    MessageToast.getInstance(dialog.getContext()).show("保存成功!");
                    dialog.bottomLayout.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    public MyQRCodeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_my_qrcode);
        dp_64 = (int) getContext().getResources().getDimension(R.dimen.dp_64);
        int width = ScreentUtils.getInstance().getScreenWidth(getContext()) - dp_64;
        ll = new LinearLayout.LayoutParams(width, width);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(context);
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initViews();
    }

    public void setCard(Card card) {
        Glide.with(getContext()).load(card.getCard_thumb()).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error) .placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(headerImageView);
        Glide.with(getContext()).load(card.getQrcode()).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(qrImageView);
        nameTextView.setText(card.getCard_name());
        positionTextView.setText(card.getOccupation());
        companyTextView.setText(card.getCompany());

    }

    private void initViews() {
        qrCardView = findViewById(R.id.qrCardView);
        closeTextView = findViewById(R.id.closeTextView);
        closeTextView.setOnClickListener(this);
        saveTextView = findViewById(R.id.saveTextView);
        saveTextView.setOnClickListener(this);
        nameTextView = findViewById(R.id.nameTextView);
        positionTextView = findViewById(R.id.positionTextView);
        companyTextView = findViewById(R.id.companyTextView);
        headerImageView = findViewById(R.id.headerImageView);
        qrImageView = findViewById(R.id.qrImageView);
        qrImageView.setLayoutParams(ll);
        bottomLayout = findViewById(R.id.bottomLayout);
    }

    private boolean isSaving;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveTextView:
                System.out.println("saveTextView");
                bottomLayout.setVisibility(View.GONE);
                qrCardView.invalidate();
                qrCardView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (bottomLayout.getVisibility() == View.GONE) {
                            if (!isSaving) {
                                isSaving = true;
                                qrCardView.buildDrawingCache();
                                final Bitmap bmp = qrCardView.getDrawingCache(); // 获取图片
                                ThreadPoolUtils.getInstance().init().execute(new Runnable() {
                                    @Override
                                    public void run() {
//                                    savePicture(bmp, "my_qrcode.jpg");// 保存图片
                                        SaveBitmapUtils.saveBitmap2file(bmp, getContext());
                                        Message message = myHandler.obtainMessage(1);
                                        message.sendToTarget();
                                    }
                                });
                            }
                        }
                    }
                });
                break;
            case R.id.closeTextView:
                dismiss();
                break;
        }
    }

}
