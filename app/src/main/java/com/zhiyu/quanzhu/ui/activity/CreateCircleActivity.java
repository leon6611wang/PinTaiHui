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
import com.zhiyu.quanzhu.model.bean.IndustryParent;
import com.zhiyu.quanzhu.model.bean.UploadImage;
import com.zhiyu.quanzhu.model.result.CircleDetailResult;
import com.zhiyu.quanzhu.ui.adapter.ImageGridRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.CircleTypeDialog;
import com.zhiyu.quanzhu.ui.dialog.HobbyDialog;
import com.zhiyu.quanzhu.ui.dialog.IndustryDialog;
import com.zhiyu.quanzhu.ui.dialog.ProvinceCityDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;
import com.zhiyu.quanzhu.ui.widget.SwitchButton;
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
    private IndustryDialog industryDialog;
    private HobbyDialog hobbyDialog;
    private int circleType = 1;
    private long circle_id;
    private String name, province_name, city_name, descirption, logo, thumb, two_industry, three_industry, video;
    private int type, province, city, is_verify, is_price, price, status;
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
                case 1:
                    activity.circleEditable();
                    String title = null;
                    String buttonString = null;
                    String remark = null;
                    switch (activity.circleDetailResult.getData().getStatus()) {
                        case -2:
                            title = "圈子审核失败";
                            remark = (StringUtils.isNullOrEmpty(activity.circleDetailResult.getData().getRemark())) ? "审核失败，可修改信息重新提交审核" : activity.circleDetailResult.getData().getRemark();
                            buttonString = "重新提交审核";
                            break;
                        case -1:
                            title = "圈子审核中";
                            buttonString = "撤销审核";
                            remark = (StringUtils.isNullOrEmpty(activity.circleDetailResult.getData().getRemark())) ? "正在审核中，数据不可修改" : activity.circleDetailResult.getData().getRemark();
                            break;
                        case 2:
                            title = "圈子被后台下架";
                            buttonString = "申请重新上架";
                            remark = (StringUtils.isNullOrEmpty(activity.circleDetailResult.getData().getRemark())) ? "已被下架，可修改数据重新申请上架" : activity.circleDetailResult.getData().getRemark();
                            break;
                        case 3:
                            title = "圈子被后台禁用";
                            buttonString = "申请解除禁用";
                            remark = (StringUtils.isNullOrEmpty(activity.circleDetailResult.getData().getRemark())) ? "已被禁用，可修改数据重新申请" : activity.circleDetailResult.getData().getRemark();
                            break;
                        case 4:
                            title = "圈子被圈主解散";
                            buttonString = "删除圈子";
                            remark = (StringUtils.isNullOrEmpty(activity.circleDetailResult.getData().getRemark())) ? "已被圈助解散，可直接删除" : activity.circleDetailResult.getData().getRemark();
                            break;
                    }
                    activity.titleTextView.setText(title);
                    activity.confirmTextView.setText(buttonString);
                    activity.jinggaoTextView.setText(remark);
                    activity.mingchengEditText.setText(activity.circleDetailResult.getData().getName());
                    activity.name = activity.circleDetailResult.getData().getName();
                    activity.type = activity.circleDetailResult.getData().getType();
                    activity.xingzhiTextView.setText(activity.circleDetailResult.getData().getType() == 1 ? "行业" : "兴趣");
                    activity.two_industry = activity.circleDetailResult.getData().getTwo_industry();
                    activity.three_industry = activity.circleDetailResult.getData().getThree_industry();
                    activity.province = activity.circleDetailResult.getData().getProvince();
                    activity.province_name = activity.circleDetailResult.getData().getProvince_name();
                    activity.city = activity.circleDetailResult.getData().getCity();
                    activity.city_name = activity.circleDetailResult.getData().getCity_name();
                    activity.is_verify = activity.circleDetailResult.getData().getIs_verify();
                    activity.is_price = activity.circleDetailResult.getData().getIs_price();
                    activity.price = activity.circleDetailResult.getData().getPrice();
                    activity.descirption = activity.circleDetailResult.getData().getDescirption();
                    activity.imgs = activity.circleDetailResult.getData().getImgs();
                    activity.logo = activity.circleDetailResult.getData().getLogo();
                    activity.video = activity.circleDetailResult.getData().getVideo();

                    activity.hangyeTextView.setText(activity.circleDetailResult.getData().getTwo_industry() + "/" + activity.circleDetailResult.getData().getThree_industry());
                    activity.chengshiTextView.setText(activity.circleDetailResult.getData().getProvince_name() + " " + activity.circleDetailResult.getData().getCity_name());
                    switch (activity.circleDetailResult.getData().getIs_verify()) {
                        case 0:
                            activity.shenheSwitchButton.close();
                            break;
                        case 1:
                            activity.shenheSwitchButton.open();
                            break;
                    }
                    switch (activity.circleDetailResult.getData().getIs_price()) {
                        case 0:
                            activity.shoufeiSwitchButton.close();
                            break;
                        case 1:
                            activity.shoufeiSwitchButton.open();
                            break;
                    }
                    activity.jineEditText.setText(((float) activity.circleDetailResult.getData().getPrice() / 100f) + "元");
                    activity.jieshaoEditText.setText(activity.circleDetailResult.getData().getDescirption());
                    Glide.with(activity).load(activity.circleDetailResult.getData().getLogo()).into(activity.logoImageView);
                    activity.list.addAll(activity.circleDetailResult.getData().getImgs());
                    activity.imageGridRecyclerAdapter.setData(activity.list);
                    break;
                case 2://圈子创建成功
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if(200==activity.baseResult.getCode()){
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
        industryDialog = new IndustryDialog(this, R.style.dialog, new IndustryDialog.OnHangYeChooseListener() {
            @Override
            public void onHangYeChoose(IndustryParent parent, IndustryChild child) {
                two_industry = parent.getName();
                three_industry = child.getName();
                hangyeTextView.setText(parent.getName() + "/" + child.getName());
            }
        });
        hobbyDialog = new HobbyDialog(this, R.style.dialog, new HobbyDialog.OnChooseHobbyListener() {
            @Override
            public void onChooseHobby(HobbyDaoParent parent, HobbyDaoChild child) {
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
            case R.id.rightLayout:

                break;
            case R.id.xingzhiLayout:
                circleTypeDialog.show();
                break;
            case R.id.chengshiLayout:
                cityDialog.show();
                break;
            case R.id.hangyeLayout:
                switch (type) {
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
                if (!StringUtils.isNullOrEmpty(jine)) {
                    price = Integer.parseInt(jine) * 100;
                }
                if (null != imgs && imgs.size() > 0) {
                    thumb = imgs.get(0);
                    imgs.remove(0);
                }
                if (isCreate) {
                    createCircle();
                } else {
                    switch (circleDetailResult.getData().getStatus()) {
                        case -2:

                            break;
                        case -1:

                            break;
                        case 1:

                            break;
                        case 2:

                            break;
                        case 3:

                            break;
                        case 4:

                            break;
                    }
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
            Glide.with(CreateCircleActivity.this).load(mVideoList.get(0)).error(R.drawable.image_error).into(uploadvideoImageView);
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

    private void uploadImages() {
        for (final String path : list) {
            if (!map.containsKey(path)) {
                UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLEFEES, path, new UploadImageUtils.OnUploadCallback() {
                    @Override
                    public void onUploadSuccess(String name) {
                        map.put(path, name);
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).equals(path)) {
                                list.get(i).replace(path, name);
                            }
                        }
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
        imgs.remove(position);
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
        if (null != list && list.size() > 0) {
            logo = thumb = list.get(0);
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
        params.addBodyParameter("imgs", GsonUtils.GsonString(list));
        params.addBodyParameter("two_industry", two_industry);
        params.addBodyParameter("three_industry", three_industry);
        params.addBodyParameter("province", String.valueOf(province));
        params.addBodyParameter("province_name", province_name);
        params.addBodyParameter("city", String.valueOf(city));
        params.addBodyParameter("city_name", city_name);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult=GsonUtils.GsonToBean(result,BaseResult.class);
                Message message=myHandler.obtainMessage(2);
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

    private CircleDetailResult circleDetailResult;

    /**
     * 圈子详情
     */
    private void circleDetail() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CIRCLE_DETAIL);
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                circleDetailResult = GsonUtils.GsonToBean(result, CircleDetailResult.class);
                if (null != circleDetailResult.getData())
                    isEdit = (circleDetailResult.getData().getStatus() == -1) ? false : true;
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
