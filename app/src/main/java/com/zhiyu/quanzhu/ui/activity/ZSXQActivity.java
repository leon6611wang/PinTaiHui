package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.leon.chic.zsxq.ConstantsUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.UUID;

public class ZSXQActivity extends BaseActivity  implements View.OnClickListener{

    private TextView reqButton,contentTextView;
    private MyHandler myHandler=new MyHandler(this);
    private static class MyHandler extends Handler{
        WeakReference<ZSXQActivity> activityWeakReference;
        public MyHandler(ZSXQActivity activity){
            activityWeakReference=new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ZSXQActivity activity=activityWeakReference.get();
            switch (msg.what){
                case 1:
                    String result=(String)msg.obj;
                    activity.contentTextView.setText(result);
                    break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zsxq);
        ScreentUtils.getInstance().setStatusBarLightMode(this,true);
        initViews();
    }

    private void initViews(){
        reqButton=findViewById(R.id.reqButton);
        reqButton.setOnClickListener(this);
        contentTextView=findViewById(R.id.contentTextView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reqButton:
                req();
                break;
        }
    }

    private void req(){
        String uuid= UUID.randomUUID().toString();
        System.out.println("uuid: "+uuid);
        RequestParams params = new RequestParams(ConstantsUtils.ZHUTI_WENDA_INFO);
        params.addHeader("Authorization", ConstantsUtils.TOKEN);
        params.addHeader("Content-Type","application/json; charset=UTF-8");
//        params.addHeader("Accept-Encoding","gzip,delate,br");
        params.addHeader("Connection","keep-alive");
        params.addHeader("X-Version","1.10.43");
        params.addHeader("Accept","*/*");
        params.addHeader("User-Agent","xiaomiquan/4.16.0 iOS/phone/13.3.1 iPhone Mobile");
        params.addHeader("Accept-Language","zh-Hans-US;q=1,en;q=0.9");
        params.addHeader("X-Request-Id",uuid);
//        params.addBodyParameter("count","30");
//        params.addBodyParameter("filter","answered");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                Message message=myHandler.obtainMessage(1);
                message.obj=result;
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("error: "+ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
