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
import android.widget.TextView;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.activity.ShareInnerActivity;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.ShareUtils;
import com.zhiyu.quanzhu.utils.ThreadPoolUtils;
import com.zhiyu.quanzhu.utils.WXUtils;

import static com.zhiyu.quanzhu.utils.WXUtils.WECHAT_FRIEND;
import static com.zhiyu.quanzhu.utils.WXUtils.WECHAT_MOMENT;

public class ShareDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private Activity activity;
    private TextView shareInnerView, sharePengYouQuanView, shareWeiXinView, shareQQView, copyLinkView;
    private TextView cancelTextView;
    private String url = "https://sports.163.com/19/1224/14/F15SC47P0005877U.html";
    private String title = "摆烂不走心?勇士本赛季首次连胜 库里汤神乐开花了";
    private String description = "今天在主场，勇士可谓打得很过瘾，早早建立了领先优势，虽然森林狼一度迫近比分，但勇士最终还是击败了森林狼，而这也让他们迎来两连胜（上一场赢了鹈鹕），这也是他们本赛季第一次连胜，场边看球的库里和汤神也笑得很开心。";
    private String imageUrl = "http://cms-bucket.ws.126.net/2019/1224/4884a1e5j00q304xh002ac000iw00anc.jpg?imageView&thumbnail=550x0";

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shareInnerView:
                Intent intent = new Intent(context, ShareInnerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            case R.id.sharePengYouQuanView:
                ThreadPoolUtils.getInstance().init().execute(new Runnable() {
                    @Override
                    public void run() {
                        WXUtils.WxUrlShare(context, "https://xw.qq.com/partner/gdtadf/20191220A0KBN4/20191220A0KBN400?ADTAG=gdtadf&pgv_ref=gdtadf", "《庆余年》全集遭泄露，公安机关已立案，网友：这下不用超前点映了", "热播剧《庆余年》46集全集在网上突然被泄露。有越来越多的网友，发现有人在社交网站上传播热播剧《庆余年》全集，甚至有人晒出了相关截图，而当前爱奇艺、腾讯视频两大平台才更新至33集。", "https://inews.gtimg.com/newsapp_bt/0/11019914276/641", WECHAT_MOMENT);
                    }
                });
                break;
            case R.id.shareWeiXinView:
                ThreadPoolUtils.getInstance().init().execute(new Runnable() {
                    @Override
                    public void run() {
                        WXUtils.WxUrlShare(context, "https://xw.qq.com/partner/gdtadf/20191220A0KBN4/20191220A0KBN400?ADTAG=gdtadf&pgv_ref=gdtadf", "《庆余年》全集遭泄露，公安机关已立案，网友：这下不用超前点映了", "热播剧《庆余年》46集全集在网上突然被泄露。有越来越多的网友，发现有人在社交网站上传播热播剧《庆余年》全集，甚至有人晒出了相关截图，而当前爱奇艺、腾讯视频两大平台才更新至33集。", "https://inews.gtimg.com/newsapp_bt/0/11019914276/641", WECHAT_FRIEND);
                    }
                });
                break;
            case R.id.shareQQView:
                ShareUtils.getInstance(activity).shareToQQ(shareListener);
                break;
            case R.id.copyLinkView:
                if (null != onShareListener) {
                    onShareListener.onShare(5, "link");
                    dismiss();
                }
                break;
            case R.id.cancelTextView:
                dismiss();
                break;
        }
    }

    public void setQQShareCallback(int requestCode, int resultCode, Intent data) {
        System.out.println("ShareDialog qqShareCallback " + "requestCode: " + requestCode + " , resultCode: " + resultCode);
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
