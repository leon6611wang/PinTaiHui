package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lcw.library.imagepicker.ImagePicker;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.ComplaintReason;
import com.zhiyu.quanzhu.ui.adapter.ComplaintImagesGridAdapter;
import com.zhiyu.quanzhu.ui.adapter.ComplaintReasonRecyclerAdapter;
import com.zhiyu.quanzhu.ui.adapter.ImageGridAdapter;
import com.zhiyu.quanzhu.ui.widget.MyGridView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GlideLoader;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.UploadImageUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投诉
 */
public class ComplaintActivity extends BaseActivity implements View.OnClickListener, ComplaintImagesGridAdapter.OnAddImagesListener {
    private LinearLayout backLayout;
    private TextView titleText;
    private RecyclerView reasonRecyclerView;
    private ComplaintReasonRecyclerAdapter reasonAdapter;
    private List<ComplaintReason> list = new ArrayList<>();
    private MyGridView imageGridView;
    private ComplaintImagesGridAdapter imageGridAdapter;
    private final int REQUEST_SELECT_IMAGES_CODE = 1001;
    private ArrayList<String> mImageList = new ArrayList<>();
    private Map<String, String> map = new HashMap<>();
    private int report_id;
    private String module_type;
    private String remark;
    private EditText remarkEditText;
    private TextView reportTextView;
    private List<String> uploadImgList = new ArrayList<>();
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<ComplaintActivity> activityWeakReference;

        public MyHandler(ComplaintActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ComplaintActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    Toast.makeText(activity, activity.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (activity.baseResult.getCode() == 200) {
                        activity.finish();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        report_id = getIntent().getIntExtra("report_id", 0);
        module_type = getIntent().getStringExtra("module_type");
//        System.out.println("report_id: "+report_id+" , module_type: "+module_type);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initList();
        initViews();
    }

    private void initList() {
        list.add(new ComplaintReason("辱骂、歧视、挑衅、泄露他人隐私等"));
        list.add(new ComplaintReason("垃圾广告信息"));
        list.add(new ComplaintReason("色情、暴力、血腥、政治敏感等违法内容"));
        list.add(new ComplaintReason("迷信活动"));
        list.add(new ComplaintReason("其他"));

        mImageList.add("add");
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleText = findViewById(R.id.titleTextView);
        titleText.setText("投诉");
        reasonRecyclerView = findViewById(R.id.reasonRecyclerView);
        reasonAdapter = new ComplaintReasonRecyclerAdapter(this);
        reasonAdapter.setList(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reasonRecyclerView.setLayoutManager(linearLayoutManager);
        reasonRecyclerView.setAdapter(reasonAdapter);
        imageGridView = findViewById(R.id.imageGridView);
        imageGridAdapter = new ComplaintImagesGridAdapter(this);
        imageGridAdapter.setData(mImageList);
        imageGridAdapter.setOnAddImagesListener(this);
        imageGridView.setAdapter(imageGridAdapter);
        reportTextView = findViewById(R.id.reportTextView);
        reportTextView.setOnClickListener(this);
        remarkEditText = findViewById(R.id.remarkEditText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.reportTextView:
                String reason = null;
                remark = remarkEditText.getText().toString().trim();
                if (map.size() > 0) {
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        String mapValue = entry.getValue();
                        uploadImgList.add(mapValue);
                    }
                }
                List<ComplaintReason> reasonList = reasonAdapter.getList();
                for (ComplaintReason complaintReason : reasonList) {
                    if (complaintReason.isSelected()) {
                        reason = complaintReason.getReason();
                    }
                }
                if (TextUtils.isEmpty(reason)) {
                    Toast.makeText(this, "请选择投诉理由", Toast.LENGTH_SHORT).show();
                } else {
                    report(reason);
                }
                break;
        }
    }

    @Override
    public void onAddImages() {
        selectImages();
    }

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
                .start(ComplaintActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            mImageList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            uploadImages();
            mImageList.add("add");
            imageGridAdapter.setData(mImageList);
        }
    }

    private void uploadImages() {
        for (final String path : mImageList) {
            if (!map.containsKey(path)) {
                UploadImageUtils.getInstance().uploadFile(UploadImageUtils.REPORT, path, new UploadImageUtils.OnUploadCallback() {
                    @Override
                    public void onUploadSuccess(String name) {
                        map.put(path, name);
                    }
                });
            }
        }
    }

    private BaseResult baseResult;

    private void report(final String reason) {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.REPORT);
        params.addBodyParameter("report_id", String.valueOf(report_id));
        params.addBodyParameter("module_type", module_type);
        params.addBodyParameter("reason", reason);
        params.addBodyParameter("remark", remark);
        params.addBodyParameter("pics", GsonUtils.GsonString(uploadImgList));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("投诉/举报: "+result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("投诉/举报: "+ex.toString());
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
