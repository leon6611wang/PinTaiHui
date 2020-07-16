package com.zhiyu.quanzhu.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.lcw.library.imagepicker.ImagePicker;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.UploadImage;
import com.zhiyu.quanzhu.ui.adapter.CircleBannerSrotAdapter;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GlideLoader;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.ImageUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.ThreadPoolUtils;
import com.zhiyu.quanzhu.utils.UploadImageUtils;
import com.zhiyu.quanzhu.utils.recyclerTouchHelper.ItemTouchHelperCallback;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class CircleBannerSortDialog extends Dialog implements CircleBannerSrotAdapter.OnBannerOperationListener, View.OnClickListener {
    private Activity activity;
    private RecyclerView recyclerView;
    private LinearLayout closeLayout;
    private CircleBannerSrotAdapter adapter;
    private List<String> list;
    private CircleBannerSortMenuDialog circleBannerSortMenuDialog;
    private LoadingDialog loadingDialog;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CircleBannerSortDialog> weakReference;

        public MyHandler(CircleBannerSortDialog dialog) {
            weakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            CircleBannerSortDialog dialog = weakReference.get();
            switch (msg.what) {
                case 99:
                    MessageToast.getInstance(dialog.getContext()).show("服务器内部错误，请稍后再试");
                    break;
                case 1:
                    dialog.loadingDialog.dismiss();
                    if (200 != dialog.baseResult.getCode()) {
                        MessageToast.getInstance(dialog.getContext()).show(dialog.baseResult.getMsg());
                    }
                    dialog.dismiss();
                    break;
            }
        }
    }

    public CircleBannerSortDialog(Activity aty, Context context, int themeResId) {
        super(context, themeResId);
        this.activity = aty;
        initDialog();
    }

    private int replaceImagePosition = -1;

    private void initDialog() {
        circleBannerSortMenuDialog = new CircleBannerSortMenuDialog(getContext(), R.style.dialog, new CircleBannerSortMenuDialog.OnCircleBannerSortMenuListener() {
            @Override
            public void onCircleBannerSortMenu(int index, int position) {
                switch (index) {
                    case 1://更换图片
                        selectImage();
                        replaceImagePosition = position;
                        break;
                    case 2://删除图片
                        list.remove(position);
                        adapter.setList(list);
                        break;
                }
            }
        });
        loadingDialog = new LoadingDialog(getContext(), R.style.dialog);
    }

    private long circle_id;

    public void setList(List<String> imageList, long circle_id) {
        this.circle_id = circle_id;
        if (null == list || list.size() == 0) {
            this.list = imageList;
            this.list.add("add");
            adapter.setList(list);
        } else {
            list = new ArrayList<>();
        }
    }

    @Override
    public void onBannerClick(int position) {
        replaceImagePosition = -1;
        circleBannerSortMenuDialog.show();
        circleBannerSortMenuDialog.setPosition(position);
    }

    @Override
    public void onAddBannerClick() {
        if (list.size() < 10) {
            selectImage();
        } else {
            MessageToast.getInstance(getContext()).show("圈子轮播图最多9张哦~");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_circle_banner_sort);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
        params.height = ScreentUtils.getInstance().getScreenHeight(getContext());
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initViews();
        this.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    updateCircle();
//                    return true;
//                }
                return true;
            }
        });
    }

    private void initViews() {
        closeLayout = findViewById(R.id.closeLayout);
        closeLayout.setOnClickListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new CircleBannerSrotAdapter(getContext());
        adapter.setOnBannerOperationListener(this);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        ItemTouchHelperCallback helperCallback = new ItemTouchHelperCallback(adapter);
        helperCallback.setSwipeEnable(false);
        helperCallback.setDragEnable(true);
        ItemTouchHelper helper = new ItemTouchHelper(helperCallback);
        helper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeLayout:
                updateCircle();
                break;
        }
    }

    private ArrayList<String> mImageList = new ArrayList<>();
    private final int REQUEST_SELECT_IMAGES_CODE = 1001;

    private void selectImage() {
        mImageList.clear();
        ImagePicker.getInstance()
                .setTitle("图片选择")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(false)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(10 - list.size())//设置最大选择图片数目(默认为1，单选)
                .setImagePaths(mImageList)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(activity, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    public void setActivityForResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            loadingDialog.show();
            mImageList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            for (String path : mImageList)
                uploadImage(path);
        }
    }

    private void uploadImage(final String path) {
        UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLE, path, new UploadImageUtils.OnUploadCallback() {
            @Override
            public void onUploadSuccess(String name) {
                UploadImage uploadImage = ImageUtils.getInstance().getUploadImage(path, name);
                loadingDialog.dismiss();
                if (replaceImagePosition > -1) {
                    list.set(replaceImagePosition, uploadImage.getFile());
                    adapter.setList(list);
                } else if (replaceImagePosition == -1) {
                    list.add(0, uploadImage.getFile());
                    adapter.setList(list);
                }
            }
        });
    }


    private BaseResult baseResult;

    private void updateCircle() {
        if(null!=loadingDialog){
            loadingDialog.show();
        }
        List<String> list = adapter.getList();
        if (null == list || list.size() == 0) {
            return;
        }
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals("add")) {
                index = i;
            }
        }
        if (index > -1) {
            list.remove(index);
        }
//        System.out.println("circle_id: " + circle_id + " , imgs: " + GsonUtils.GsonString(list));
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.UPDATE_CIRCLE);
        params.addBodyParameter("imgs", GsonUtils.GsonString(list));
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
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
