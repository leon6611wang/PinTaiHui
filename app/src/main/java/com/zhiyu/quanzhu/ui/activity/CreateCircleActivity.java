package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcw.library.imagepicker.ImagePicker;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.AreaCity;
import com.zhiyu.quanzhu.model.bean.AreaProvince;
import com.zhiyu.quanzhu.model.bean.HobbyDaoChild;
import com.zhiyu.quanzhu.model.bean.HobbyDaoParent;
import com.zhiyu.quanzhu.model.bean.IndustryChild;
import com.zhiyu.quanzhu.model.bean.IndustryHobby;
import com.zhiyu.quanzhu.model.bean.IndustryParent;
import com.zhiyu.quanzhu.model.bean.UploadImage;
import com.zhiyu.quanzhu.model.result.CircleDetailResult;
import com.zhiyu.quanzhu.model.result.CircleInfoResult;
import com.zhiyu.quanzhu.ui.adapter.ImageGridRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.CircleTypeDialog;
import com.zhiyu.quanzhu.ui.dialog.HobbyDialog;
import com.zhiyu.quanzhu.ui.dialog.IndustryDialog;
import com.zhiyu.quanzhu.ui.dialog.IndustryHobbyDialog;
import com.zhiyu.quanzhu.ui.dialog.ProvinceCityDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;
import com.zhiyu.quanzhu.ui.widget.SwitchButton;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GlideLoader;
import com.zhiyu.quanzhu.utils.GridSpacingItemDecoration;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.PriceParseUtils;
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


/**
 * 创建圈子
 */
public class CreateCircleActivity extends BaseActivity implements View.OnClickListener, ImageGridRecyclerAdapter.OnAddImageListener,
        ImageGridRecyclerAdapter.OnDeleteImageListener {
    private LinearLayout backLayout, rightLayout;
    private TextView titleTextView, rightTextView, jinggaoTextView;
    private boolean isCreate = true;//是否创建
    private boolean isEdit = false;//是否可编辑(审核中不可编辑，其他状态可编辑)
    private EditText mingchengEditText, jineEditText, jieshaoEditText;
    private LinearLayout xingzhiLayout, chengshiLayout, hangyeLayout, moneyLayout;
    private TextView xingzhiTextView, chengshiTextView, hangyeTextView, confirmTextView, industryTextView;
    private SwitchButton shenheSwitchButton, shoufeiSwitchButton;
    private RoundImageView logoImageView;
    private ImageView uploadvideoImageView;
    private RecyclerView mRecyclerView;
    private ImageGridRecyclerAdapter imageGridRecyclerAdapter;
    private ArrayList<String> list = new ArrayList<>();
    private CircleTypeDialog circleTypeDialog;
    private ProvinceCityDialog cityDialog;
    private IndustryHobbyDialog industryDialog;
    private IndustryHobbyDialog hobbyDialog;
    private int circleType = 1;
    private long circle_id;
    private String name, province_name, city_name, descirption, logo, thumb, two_industry, three_industry, video;
    private int type, province, city, is_verify, is_price, price, status, two_industry_id, three_industry_id;
    private String videoUrl;
    private int videoWidth, videoHeight;
    private List<String> imgs = new ArrayList<>();

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CreateCircleActivity> activityWeakReference;

        public MyHandler(CreateCircleActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CreateCircleActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 99:
                    activity.confirmTextView.setClickable(true);
                    MessageToast.getInstance(activity).show("服务器内部错误，请稍后再试.");
                    break;
                case 1:
                    activity.circleEditable();
                    String title = null;
                    String buttonString = null;
                    String remark = null;
                    switch (activity.circleInfoResult.getData().getStatus()) {
                        case -2:
                            title = "圈子审核失败";
                            remark = (StringUtils.isNullOrEmpty(activity.circleInfoResult.getData().getRemark())) ? "审核失败，可修改信息重新提交审核" : activity.circleInfoResult.getData().getRemark();
                            buttonString = "重新提交审核";
                            break;
                        case -1:
                            title = "圈子审核中";
                            buttonString = "撤销审核";
                            remark = (StringUtils.isNullOrEmpty(activity.circleInfoResult.getData().getRemark())) ? "正在审核中，数据不可修改" : activity.circleInfoResult.getData().getRemark();
                            break;
                        case 2:
                            title = "圈子被后台下架";
                            buttonString = "申请重新上架";
                            remark = (StringUtils.isNullOrEmpty(activity.circleInfoResult.getData().getRemark())) ? "已被下架，可修改数据重新申请上架" : activity.circleInfoResult.getData().getRemark();
                            break;
                        case 3:
                            title = "圈子被后台禁用";
                            buttonString = "申请解除禁用";
                            remark = (StringUtils.isNullOrEmpty(activity.circleInfoResult.getData().getRemark())) ? "已被禁用，可修改数据重新申请" : activity.circleInfoResult.getData().getRemark();
                            break;
                        case 4:
                            title = "圈子被圈主解散";
                            buttonString = "删除圈子";
                            remark = (StringUtils.isNullOrEmpty(activity.circleInfoResult.getData().getRemark())) ? "已被圈助解散，可直接删除" : activity.circleInfoResult.getData().getRemark();
                            break;
                    }
                    activity.titleTextView.setText(title);
                    activity.confirmTextView.setText(buttonString);
                    activity.jinggaoTextView.setText(remark);
                    activity.mingchengEditText.setText(activity.circleInfoResult.getData().getName());
                    activity.name = activity.circleInfoResult.getData().getName();
                    activity.type = activity.circleInfoResult.getData().getType();
                    activity.xingzhiTextView.setText(activity.circleInfoResult.getData().getType() == 1 ? "行业" : "兴趣");
                    activity.two_industry = activity.circleInfoResult.getData().getTwo_industry();
                    activity.three_industry = activity.circleInfoResult.getData().getThree_industry();
                    activity.province = activity.circleInfoResult.getData().getProvince();
                    activity.province_name = activity.circleInfoResult.getData().getProvince_name();
                    activity.city = activity.circleInfoResult.getData().getCity();
                    activity.city_name = activity.circleInfoResult.getData().getCity_name();
                    activity.is_verify = activity.circleInfoResult.getData().isIs_verify() ? 1 : 0;
                    activity.is_price = activity.circleInfoResult.getData().getIs_price();
                    activity.price = activity.circleInfoResult.getData().getPrice();
                    activity.descirption = activity.circleInfoResult.getData().getDescirption();
                    activity.imgList = activity.circleInfoResult.getData().getImgs();
                    activity.logo = activity.circleInfoResult.getData().getLogo();
                    activity.videoUrl = activity.circleInfoResult.getData().getVideo();
                    if (!StringUtils.isNullOrEmpty(activity.videoUrl)) {
                        Glide.with(activity).load(activity.videoUrl).error(R.drawable.image_error).into(activity.uploadvideoImageView);
                    }
                    activity.hangyeTextView.setText(activity.circleInfoResult.getData().getTwo_industry() + "/" + activity.circleInfoResult.getData().getThree_industry());
                    activity.chengshiTextView.setText(activity.circleInfoResult.getData().getProvince_name() + " " + activity.circleInfoResult.getData().getCity_name());
                    switch (activity.is_verify) {
                        case 0:
                            activity.shenheSwitchButton.close();
                            break;
                        case 1:
                            activity.shenheSwitchButton.open();
                            break;
                    }
                    switch (activity.circleInfoResult.getData().getIs_price()) {
                        case 0:
                            activity.shoufeiSwitchButton.close();
                            break;
                        case 1:
                            activity.shoufeiSwitchButton.open();
                            break;
                    }
                    activity.jineEditText.setText(PriceParseUtils.getInstance().parsePrice(activity.price) + "元");
                    activity.jieshaoEditText.setText(activity.circleInfoResult.getData().getDescirption());
                    Glide.with(activity).load(activity.circleInfoResult.getData().getLogo()).error(R.drawable.image_error).into(activity.logoImageView);
                    activity.list.remove("add");
                    activity.list.addAll(activity.circleInfoResult.getData().getImgs());
                    activity.list.add("add");
                    activity.imageGridRecyclerAdapter.setData(activity.list);
                    break;
                case 2://圈子创建成功
                    activity.confirmTextView.setClickable(true);
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
        setContentView(R.layout.activity_create_circle);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        circle_id = getIntent().getLongExtra("circle_id", 0l);
        if (circle_id > 0l) {
            isCreate = false;
        }
        initViews();
        initDialogs();
        if (!isCreate) {
            circleDetail();
        }
        circleEditable();
    }

    private void initDialogs() {
        circleTypeDialog = new CircleTypeDialog(this, R.style.dialog, new CircleTypeDialog.OnCircleTypeListener() {
            @Override
            public void onCircleType(int t, String desc) {
                circleType = t;
                type = t;
                xingzhiTextView.setText(desc);
                industryTextView.setText(desc);
            }
        });
        cityDialog = new ProvinceCityDialog(this, R.style.dialog, new ProvinceCityDialog.OnCityChooseListener() {
            @Override
            public void onCityChoose(AreaProvince p, AreaCity c) {
                province = p.getCode();
                province_name = p.getName();
                city = (int) c.getCode();
                city_name = c.getName();
                chengshiTextView.setText(p.getName() + " " + c.getName());
            }
        });
        industryDialog = new IndustryHobbyDialog(this, R.style.dialog, true, new IndustryHobbyDialog.OnIndustryHobbySelectedListener() {
            @Override
            public void onIndustryHobbySelected(IndustryHobby parent, IndustryHobby child) {
                two_industry = parent.getName();
                two_industry_id = parent.getId();
                three_industry = child.getName();
                three_industry_id = child.getId();
                hangyeTextView.setText(parent.getName() + "/" + child.getName());
            }
        });
        hobbyDialog = new IndustryHobbyDialog(this, R.style.dialog, false, new IndustryHobbyDialog.OnIndustryHobbySelectedListener() {
            @Override
            public void onIndustryHobbySelected(IndustryHobby parent, IndustryHobby child) {
                two_industry = parent.getName();
                three_industry = child.getName();
                hangyeTextView.setText(parent.getName() + "/" + child.getName());
            }
        });
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(isCreate ? "创建圈子" : "圈子审核中");
        rightTextView = findViewById(R.id.rightTextView);
        rightLayout.setVisibility(isCreate ? View.INVISIBLE : View.VISIBLE);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setText(isCreate ? "提交审核" : "撤销审核");
        confirmTextView.setOnClickListener(this);
        jinggaoTextView = findViewById(R.id.jinggaoTextView);

        mingchengEditText = findViewById(R.id.mingchengEditText);
        jineEditText = findViewById(R.id.jineEditText);
        jieshaoEditText = findViewById(R.id.jieshaoEditText);
        xingzhiLayout = findViewById(R.id.xingzhiLayout);
        chengshiLayout = findViewById(R.id.chengshiLayout);
        hangyeLayout = findViewById(R.id.hangyeLayout);
        moneyLayout = findViewById(R.id.moneyLayout);
        xingzhiTextView = findViewById(R.id.xingzhiTextView);
        xingzhiTextView.setText("行业");
        chengshiTextView = findViewById(R.id.chengshiTextView);
        hangyeTextView = findViewById(R.id.hangyeTextView);
        industryTextView = findViewById(R.id.industryTextView);
        shenheSwitchButton = findViewById(R.id.shenheSwitchButton);
        shoufeiSwitchButton = findViewById(R.id.shoufeiSwitchButton);
        shenheSwitchButton.setOnSwitchButtonListener(new SwitchButton.OnSwitchButtonListener() {
            @Override
            public void onOpen(boolean isOpen) {
                is_verify = isOpen ? 1 : 0;
            }
        });
        shoufeiSwitchButton.setOnSwitchButtonListener(new SwitchButton.OnSwitchButtonListener() {
            @Override
            public void onOpen(boolean isOpen) {
                is_price = isOpen ? 1 : 0;
                moneyLayout.setVisibility(isOpen ? View.VISIBLE : View.GONE);
            }
        });
        chengshiLayout = findViewById(R.id.chengshiLayout);
        hangyeLayout = findViewById(R.id.hangyeLayout);
        shoufeiSwitchButton = findViewById(R.id.shoufeiSwitchButton);
        logoImageView = findViewById(R.id.logoImageView);
        list.add("add");
        mRecyclerView = findViewById(R.id.mRecyclerView);
        imageGridRecyclerAdapter = new ImageGridRecyclerAdapter(this);
        imageGridRecyclerAdapter.setData(list);
        imageGridRecyclerAdapter.setOnDeleteImageListener(this);
        imageGridRecyclerAdapter.setOnAddImageListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(3, (int) getResources().getDimension(R.dimen.dp_10), false);
        mRecyclerView.setAdapter(imageGridRecyclerAdapter);
        mRecyclerView.addItemDecoration(gridSpacingItemDecoration);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        uploadvideoImageView = findViewById(R.id.uploadvideoImageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.xingzhiLayout:
                circleTypeDialog.show();
                break;
            case R.id.chengshiLayout:
                cityDialog.show();
                break;
            case R.id.hangyeLayout:
                switch (circleType) {
                    case 1:
                        industryDialog.show();
                        break;
                    case 2:
                        hobbyDialog.show();
                        break;
                }
                break;
            case R.id.uploadvideoImageView:
                selectVideo();
                break;
            case R.id.logoImageView:

                break;
            case R.id.confirmTextView:
                name = mingchengEditText.getText().toString().trim();
                descirption = jieshaoEditText.getText().toString().trim();
                String jine = jineEditText.getText().toString().trim();
                if (jine.contains("元")) {
                    jine = jine.replace("元", "");
                }
                if (!StringUtils.isNullOrEmpty(jine)) {
                    price = (int) Float.parseFloat(jine) * 100;
                }
                if (null != imgs && imgs.size() > 0) {
                    thumb = imgs.get(0);
                    imgs.remove(0);
                }
                if (isCreate) {
                    createCircle();
                } else {
                    switch (circleInfoResult.getData().getStatus()) {
                        case -2://圈子申请已被驳回，请点击重新申请
                            operation_type = 2;
                            break;
                        case -1://圈子审核中，可点击查看详情
                            operation_type = 1;
                            break;
                        case 1:

                            break;
                        case 2://圈子已被下架，请根据系统通知调整
                            operation_type = 4;
                            break;
                        case 3://圈子已被禁用，可向平台申诉解封
                            operation_type = 3;
                            break;
                        case 4://圈子已被圈主解散，可长按删除

                            break;
                    }
                    updateCircle();
                }
                break;
        }
    }

    private void selectImages() {
        list.remove("add");
        ImagePicker.getInstance()
                .setTitle("图片选择")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(false)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(9)//设置最大选择图片数目(默认为1，单选)
                .setImagePaths(list)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(CreateCircleActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    private void selectVideo() {
        ImagePicker.getInstance()
                .setTitle("视频选择")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(false)//设置是否展示图片
                .showVideo(true)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                .setImagePaths(mVideoList)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(CreateCircleActivity.this, REQUEST_SELECT_VIDEO_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    private ArrayList<String> mVideoList = new ArrayList<>();
    private final int REQUEST_SELECT_IMAGES_CODE = 1001;
    private final int REQUEST_SELECT_VIDEO_CODE = 1002;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            list = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            uploadImages();
            list.add("add");
            imageGridRecyclerAdapter.setData(list);
        }

        if (requestCode == REQUEST_SELECT_VIDEO_CODE && resultCode == RESULT_OK) {
            mVideoList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            Glide.with(CreateCircleActivity.this).load(mVideoList.get(0)).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                    .fallback(R.drawable.image_error).into(uploadvideoImageView);
            UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLEFEES, mVideoList.get(0), new UploadImageUtils.OnUploadCallback() {
                @Override
                public void onUploadSuccess(String name) {
                    videoUrl = name;
                    VideoUtils.getInstance().getVideoWidthAndHeightAndVideoTimes(videoUrl, new VideoUtils.OnCaculateVideoWidthHeightListener() {
                        @Override
                        public void onVideoWidthHeight(float w, float h, float vt) {
                            videoWidth = (int) w;
                            videoHeight = (int) h;
                        }
                    });
                }
            });

        }
    }

    private LinkedHashMap<String, String> map = new LinkedHashMap<>();
    private List<String> imgList = new ArrayList<>();

    private void uploadImages() {
        System.out.println("list size: " + list.size());
        for (final String path : list) {
            if (!map.containsKey(path)) {
                UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLEFEES, path, new UploadImageUtils.OnUploadCallback() {
                    @Override
                    public void onUploadSuccess(String name) {
                        map.put(path, name);
                        imgList.add(name);
                        imgs.add(name);
//                        for (int i = 0; i < list.size(); i++) {
//                            if (list.get(i).equals(path)) {
//                                list.get(i).replace(path, name);
//                            }
//                        }
                    }
                });
            }
        }
    }


    @Override
    public void onAddImage() {
        selectImages();
    }

    @Override
    public void onDelete(int position) {
        System.out.println("position: " + position);
        System.out.println("1 imgs: " + imgList.size());
        imgList.remove(position);
        System.out.println("2 imgs: " + imgList.size());
        list.remove(position);
        imageGridRecyclerAdapter.setData(list);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void setEditTextEditable(EditText editText, boolean editable) {
        editText.setFocusable(editable);
        editText.setFocusableInTouchMode(editable);
        editText.setLongClickable(editable);
        editText.setInputType(editable ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_NULL);
    }


    private BaseResult baseResult;

    /**
     * 创建圈子
     */
    private void createCircle() {
        confirmTextView.setClickable(false);
        if (null != imgList && imgList.size() > 0) {
            logo = thumb = imgList.get(0);
        }
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ADD_CIRCLE);
        params.addBodyParameter("circle_name", name);
        params.addBodyParameter("descirption", descirption);
        params.addBodyParameter("logo", logo);
        params.addBodyParameter("thumb", thumb);
        params.addBodyParameter("type", String.valueOf(circleType));
        params.addBodyParameter("is_price", String.valueOf(is_price));
        params.addBodyParameter("price", String.valueOf(price));
        params.addBodyParameter("is_verify", String.valueOf(is_verify));
        params.addBodyParameter("video", videoUrl);
        params.addBodyParameter("imgs", GsonUtils.GsonString(imgList));
        params.addBodyParameter("two_industry", two_industry);
        params.addBodyParameter("three_industry", three_industry);
        params.addBodyParameter("two_industry_id", String.valueOf(two_industry_id));
        params.addBodyParameter("three_industry_id", String.valueOf(three_industry_id));
        params.addBodyParameter("province", String.valueOf(province));
        params.addBodyParameter("province_name", province_name);
        params.addBodyParameter("city", String.valueOf(city));
        params.addBodyParameter("city_name", city_name);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("创建圈子: "+result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
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

    //1撤销审核 2提交审核 3申请解除禁用 4申请重新上架
    private int operation_type = 0;

    private void updateCircle() {
        confirmTextView.setClickable(false);
        System.out.println("operation_type: " + operation_type);
        System.out.println("imgs size: " + imgList.size());
        if (null != imgList && imgList.size() > 0) {
            logo = thumb = imgList.get(0);
        }
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.UPDATE_CIRCLE);
        params.addBodyParameter("circle_name", name);
        params.addBodyParameter("descirption", descirption);
        params.addBodyParameter("logo", logo);
        params.addBodyParameter("thumb", thumb);
        params.addBodyParameter("type", String.valueOf(circleType));
        params.addBodyParameter("is_price", String.valueOf(is_price));
        params.addBodyParameter("price", String.valueOf(price));
        params.addBodyParameter("is_verify", String.valueOf(is_verify));
        params.addBodyParameter("video", videoUrl);
        params.addBodyParameter("imgs", GsonUtils.GsonString(imgList));
        params.addBodyParameter("two_industry", two_industry);
        params.addBodyParameter("three_industry", three_industry);
        params.addBodyParameter("two_industry_id", String.valueOf(two_industry_id));
        params.addBodyParameter("three_industry_id", String.valueOf(three_industry_id));
        params.addBodyParameter("province", String.valueOf(province));
        params.addBodyParameter("province_name", province_name);
        params.addBodyParameter("city", String.valueOf(city));
        params.addBodyParameter("operation_type", String.valueOf(operation_type));
        params.addBodyParameter("city_name", city_name);
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
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

    private CircleInfoResult circleInfoResult;

    /**
     * 圈子详情
     */
    private void circleDetail() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CIRCLE_BASE);
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                circleInfoResult = GsonUtils.GsonToBean(result, CircleInfoResult.class);
                if (null != circleInfoResult.getData()){
                    isEdit = (circleInfoResult.getData().getStatus() == -1) ? false : true;
                    two_industry_id=circleInfoResult.getData().getTwo_industry_id();
                    three_industry_id=circleInfoResult.getData().getThree_industry_id();
                }

                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println(ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void circleEditable() {
        jinggaoTextView.setVisibility(isCreate ? View.GONE : View.VISIBLE);

        if (isCreate || isEdit) {
            setEditTextEditable(jieshaoEditText, true);
        } else {
            setEditTextEditable(jieshaoEditText, false);
        }
        if (isCreate || isEdit) {
            setEditTextEditable(jineEditText, true);
            jineEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else {
            setEditTextEditable(jineEditText, false);
        }

        if (isCreate || isEdit) {
            setEditTextEditable(mingchengEditText, true);
        } else {
            setEditTextEditable(mingchengEditText, false);
        }
        if (isCreate || isEdit)
            xingzhiLayout.setOnClickListener(this);

        if (isCreate || isEdit)
            chengshiLayout.setOnClickListener(this);

        if (isCreate || isEdit)
            hangyeLayout.setOnClickListener(this);
        if (isCreate || isEdit) {
            shenheSwitchButton.setCanSwitch(true);
        } else {
            shenheSwitchButton.setCanSwitch(false);
        }
        if (isCreate || isEdit) {
            shoufeiSwitchButton.setCanSwitch(true);
        } else {
            shoufeiSwitchButton.setCanSwitch(false);
        }
        if (isCreate) {
            shenheSwitchButton.close();
            shoufeiSwitchButton.close();
        }
        if (isCreate || isEdit)
            logoImageView.setOnClickListener(this);
        if (isCreate || isEdit)
            imageGridRecyclerAdapter.setOnAddImageListener(this);
        if (isCreate || isEdit)
            imageGridRecyclerAdapter.setOnDeleteImageListener(this);
        if (isCreate || isEdit)
            uploadvideoImageView.setOnClickListener(this);
    }

}
