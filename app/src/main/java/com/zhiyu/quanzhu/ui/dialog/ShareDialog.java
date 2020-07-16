package com.zhiyu.quanzhu.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.Share;
import com.zhiyu.quanzhu.ui.activity.ShareInnerActivity;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.CopyUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.ShareUtils;
import com.zhiyu.quanzhu.utils.ThreadPoolUtils;
import com.zhiyu.quanzhu.utils.WXUtils;

import static com.zhiyu.quanzhu.utils.WXUtils.WECHAT_FRIEND;
import static com.zhiyu.quanzhu.utils.WXUtils.WECHAT_MOMENT;

public class ShareDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private Activity activity;
    private LinearLayout innerShareLayout;
    private TextView shareInnerView, sharePengYouQuanView, shareWeiXinView, shareQQView, copyLinkView;
    private TextView cancelTextView;
    private String url;
    private String title;
    private String description;
    private String imageUrl;
    private int share_id;
    private String type;
    private int feed_type;

    public void setShare(Share share, int id) {
        this.title = share.getTitle();
        this.url = share.getWeb_url() + id;
        this.type = share.getType_desc();
        this.feed_type=share.getType();
        this.description = share.getContent();
        if (share.getImage_url().contains(".mp4") || share.getImage_url().contains(".MP4") ||
                share.getImage_url().contains(".RMVB") || share.getImage_url().contains(".rmvb")) {
            this.imageUrl = share.getImage_url() + "/w/100/h/100";
        } else {
            this.imageUrl = share.getImage_url() + "?imageView2/1/w/100/h/100/q/85";
        }

        this.share_id = id;
    }

    public void hideInnerShare(){
        if(null!=innerShareLayout){
            innerShareLayout.setVisibility(View.GONE);
        }
    }

    public ShareDialog(Activity aty, Context context, int themeResId, OnShareListener listener) {
        super(context, themeResId);
        this.activity = aty;
        this.context = context;
        this.onShareListener = listener;
    }

    public ShareDialog(Activity activity, Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(context);
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        initViews();
    }

    private void initViews() {
        shareInnerView = findViewById(R.id.shareInnerView);
        shareInnerView.setOnClickListener(this);
        sharePengYouQuanView = findViewById(R.id.sharePengYouQuanView);
        sharePengYouQuanView.setOnClickListener(this);
        shareWeiXinView = findViewById(R.id.shareWeiXinView);
        shareWeiXinView.setOnClickListener(this);
        shareQQView = findViewById(R.id.shareQQView);
        shareQQView.setOnClickListener(this);
        copyLinkView = findViewById(R.id.copyLinkView);
        copyLinkView.setOnClickListener(this);
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
        innerShareLayout=findViewById(R.id.innerShareLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shareInnerView:
                Intent intent = new Intent(context, ShareInnerActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("desc", description);
                intent.putExtra("webUrl", url);
                intent.putExtra("imageUrl", imageUrl);
                intent.putExtra("share_id", share_id);
                intent.putExtra("type",type);
                intent.putExtra("feed_type",feed_type);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            case R.id.sharePengYouQuanView:
                ThreadPoolUtils.getInstance().init().execute(new Runnable() {
                    @Override
                    public void run() {
                        WXUtils.WxUrlShare(context, url, title, description, imageUrl, WECHAT_MOMENT);
                    }
                });
                break;
            case R.id.shareWeiXinView:
//                System.out.println("share_image_url: " + imageUrl);
                ThreadPoolUtils.getInstance().init().execute(new Runnable() {
                    @Override
                    public void run() {
                        WXUtils.WxUrlShare(context, url, title, description, imageUrl, WECHAT_FRIEND);
                    }
                });
                break;
            case R.id.shareQQView:
                ShareUtils.getInstance(activity).shareToQQ(title, description, imageUrl, url, shareListener);
                break;
            case R.id.copyLinkView:
                CopyUtils.getInstance().copy(context, url);
                MessageToast.getInstance(context).show("复制成功");
                dismiss();
                break;
            case R.id.cancelTextView:
                dismiss();
                break;
        }
    }

    public void setQQShareCallback(int requestCode, int resultCode, Intent data) {
//        System.out.println("ShareDialog qqShareCallback " + "requestCode: " + requestCode + " , resultCode: " + resultCode);
        ShareUtils.getInstance(activity).setQQShareCallback(requestCode, resultCode, data);
    }

    private OnShareListener onShareListener;

    public interface OnShareListener {
        void onShare(int position, String desc);
    }


    //授权登录监听（最下面是返回结果）
    private IUiListener shareListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            MessageToast.getInstance(getContext()).show("分享成功");
        }

        @Override
        public void onError(UiError uiError) {
            MessageToast.getInstance(getContext()).show("分享失败");
        }

        @Override
        public void onCancel() {
            MessageToast.getInstance(getContext()).show("分享取消");
        }
    };


}
