package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcw.library.imagepicker.ImagePicker;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.result.ReportDetailResult;
import com.zhiyu.quanzhu.ui.adapter.ShenSuPingZhengImageRecyclerViewAdapter;
import com.zhiyu.quanzhu.ui.adapter.ShenSuShenSuImageRecyclerViewAdapter;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GlideLoader;
import com.zhiyu.quanzhu.utils.GridSpacingItemDecoration;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.UploadImageUtils;
import com.zhiyu.quanzhu.utils.VideoUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ShenSuActivity extends BaseActivity implements View.OnClickListener, ShenSuShenSuImageRecyclerViewAdapter.OnShenSuImageListener {
    private LinearLayout backLayout;
    private TextView titleTextView, reasonTextView, remarkTextView, resultTextView, confirmTextView;
    private MyRecyclerView pingzhengImageRecyclerView, shensuImageRecyclerView;
    private ShenSuPingZhengImageRecyclerViewAdapter pingZhengImageRecyclerViewAdapter;
    private ShenSuShenSuImageRecyclerViewAdapter shenSuImageRecyclerViewAdapter;
    private EditText reasonEditText;
    private int id;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<ShenSuActivity> activityWeakReference;

        public MyHandler(ShenSuActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ShenSuActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.reasonTextView.setText(activity.reportDetailResult.getData().getReason());
                    activity.remarkTextView.setText(activity.reportDetailResult.getData().getRemark());
                    activity.resultTextView.setText(activity.reportDetailResult.getData().getResult_msg());
                    activity.pingZhengImageRecyclerViewAdapter.setList(activity.reportDetailResult.getData().getPics());
                    break;
                case 2:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.finish();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shen_su);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        id = getIntent().getIntExtra("id", 0);
        initViews();
        reportDetail();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("我要申诉");
        reasonTextView = findViewById(R.id.reasonTextView);
        remarkTextView = findViewById(R.id.remarkTextView);
        resultTextView = findViewById(R.id.resultTextView);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
        pingzhengImageRecyclerView = findViewById(R.id.pingzhengImageRecyclerView);
        shensuImageRecyclerView = findViewById(R.id.shensuImageRecyclerView);
        reasonEditText = findViewById(R.id.reasonEditText);
        pingZhengImageRecyclerViewAdapter = new ShenSuPingZhengImageRecyclerViewAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(3, (int) getResources().getDimension(R.dimen.dp_6), false);
        pingzhengImageRecyclerView.setLayoutManager(gridLayoutManager);
        pingzhengImageRecyclerView.setAdapter(pingZhengImageRecyclerViewAdapter);
        pingzhengImageRecyclerView.addItemDecoration(gridSpacingItemDecoration);
        mImageList.add("add");
        shenSuImageRecyclerViewAdapter = new ShenSuShenSuImageRecyclerViewAdapter(this);
        shenSuImageRecyclerViewAdapter.setOnShenSuImageListener(this);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 3);
        GridSpacingItemDecoration gridSpacingItemDecoration2 = new GridSpacingItemDecoration(3, (int) getResources().getDimension(R.dimen.dp_6), false);
        shensuImageRecyclerView.setLayoutManager(gridLayoutManager2);
        shensuImageRecyclerView.setAdapter(shenSuImageRecyclerViewAdapter);
        shensuImageRecyclerView.addItemDecoration(gridSpacingItemDecoration2);
        shensuImageRecyclerView.setAdapter(shenSuImageRecyclerViewAdapter);
        shenSuImageRecyclerViewAdapter.setList(mImageList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.confirmTextView:
                shenSu();
                break;
        }
    }

    @Override
    public void addImage() {
        selectImages();
    }

    private final int REQUEST_SELECT_IMAGES_CODE = 1001;
    private ArrayList<String> mImageList = new ArrayList<>();

    private void selectImages() {
        mImageList.remove("add");
        ImagePicker.getInstance()
                .setTitle("图片选择")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(false)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(9)//设置最大选择图片数目(默认为1，单选)
                .setImagePaths(mImageList)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(ShenSuActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            mImageList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            uploadImages();
            mImageList.add("add");
            shenSuImageRecyclerViewAdapter.setList(mImageList);
        }
    }

    private LinkedHashMap<String, String> map = new LinkedHashMap<>();

    private void uploadImages() {
        for (final String path : mImageList) {
            if (!map.containsKey(path)) {
                UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLEFEES, path, new UploadImageUtils.OnUploadCallback() {
                    @Override
                    public void onUploadSuccess(String name) {
                        map.put(path, name);
                    }
                });
            }
        }
    }

    private ReportDetailResult reportDetailResult;

    private void reportDetail() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.REPORT_DETAIL);
        params.addBodyParameter("id", String.valueOf(id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                reportDetailResult = GsonUtils.GsonToBean(result, ReportDetailResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private BaseResult baseResult;

    private void shenSu() {
        List<String> list=new ArrayList<>();
        if(null!=mImageList&&mImageList.size()>0){
            for(String path:mImageList){
                list.add(map.get(path));
            }
        }
//        System.out.println("pics: "+GsonUtils.GsonString(list));
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SHEN_SU);
        params.addBodyParameter("id", String.valueOf(id));
        params.addBodyParameter("reason", reasonEditText.getText().toString().trim());
        params.addBodyParameter("pics", GsonUtils.GsonString(list));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("申诉: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("申诉: " + ex.toString());
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
