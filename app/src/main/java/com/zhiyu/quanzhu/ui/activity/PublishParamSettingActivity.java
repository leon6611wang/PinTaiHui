package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.Goods;
import com.zhiyu.quanzhu.model.bean.HobbyDaoChild;
import com.zhiyu.quanzhu.model.bean.HobbyDaoParent;
import com.zhiyu.quanzhu.model.bean.IndustryChild;
import com.zhiyu.quanzhu.model.bean.IndustryParent;
import com.zhiyu.quanzhu.model.bean.MyCircle;
import com.zhiyu.quanzhu.model.bean.Tag;
import com.zhiyu.quanzhu.ui.dialog.CircleSelectDialog;
import com.zhiyu.quanzhu.ui.dialog.HobbyDialog;
import com.zhiyu.quanzhu.ui.dialog.IndustryDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.SwitchButton;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.ImageUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 发布文章/视频参数设置
 */
public class PublishParamSettingActivity extends BaseActivity implements View.OnClickListener, PublishChooseGoodsActivity.OnChooseGoodsListener {
    private TextView titleTextView, circleTitleTextView, circleTextView, industryTextView, hobbyTextView, goodsTextView;
    private LinearLayout atCircleRootLayout, topCircleFeedRootLayout, onlyPublishCircleRootLayout, circleLayout, industryLayout, hobbyLayout, goodsLayout, backLayout;
    private SwitchButton personalFeedSwitchButton, circleFeedSwitchButton, circleSwitchButton;
    private CircleSelectDialog circleSelectDialog;
    private IndustryDialog industryDialog;
    private HobbyDialog hobbyDialog;
    private TextView publishButton;

    private int publishType;//1.文章，2.视频
    private int feeds_id;
    private String articleTitle, articleContent;
    private ArrayList<Tag> tagList = new ArrayList<>();
    private List<Integer> goodsList = new ArrayList<>();


    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<PublishParamSettingActivity> activityWeakReference;

        public MyHandler(PublishParamSettingActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PublishParamSettingActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (activity.baseResult.getCode() == 200) {
                        if (null != onPublishFinishListener) {
                            onPublishFinishListener.onPublishFinish();
                        }
                        activity.finish();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_param_setting);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        PublishChooseGoodsActivity.setOnChooseGoodsListener(this);
        getIntentData();
        initViews();
        initDialogs();
    }

    private void getIntentData() {
        feeds_id = getIntent().getIntExtra("feeds_id", 0);
        publishType = getIntent().getIntExtra("publishType", 0);
        articleTitle = getIntent().getStringExtra("articleTitle");
        articleContent = getIntent().getStringExtra("articleContent");
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            try {
                tagList = (ArrayList<Tag>) bundle.getSerializable("tagList");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void initDialogs() {
        circleSelectDialog = new CircleSelectDialog(this, R.style.dialog, new CircleSelectDialog.OnCircleSeletedListener() {
            @Override
            public void onCircleSelected(MyCircle circle) {
                System.out.println("圈子选择: " + circle.getId() + " , " + circle.getName());
                circle_id = circle.getId();
                circleTextView.setText(circle.getName());
            }
        });
        industryDialog = new IndustryDialog(this, R.style.dialog, new IndustryDialog.OnHangYeChooseListener() {
            @Override
            public void onHangYeChoose(IndustryParent parent, IndustryChild child) {
                industry_parent = parent.getName();
                industry_child = child.getName();
                industryTextView.setText(industry_parent + "/" + industry_child);
            }
        });
        hobbyDialog = new HobbyDialog(this, R.style.dialog, new HobbyDialog.OnChooseHobbyListener() {
            @Override
            public void onChooseHobby(HobbyDaoParent parent, HobbyDaoChild child) {
                hobby_parent = parent.getName();
                hobby_child = child.getName();
                hobbyTextView.setText(hobby_parent + "/" + hobby_child);
            }
        });
    }

    private void initViews() {
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("发布设置");
        atCircleRootLayout = findViewById(R.id.atCircleRootLayout);
        topCircleFeedRootLayout = findViewById(R.id.topCircleFeedRootLayout);
        onlyPublishCircleRootLayout = findViewById(R.id.onlyPublishCircleRootLayout);
        publishButton = findViewById(R.id.publishButton);
        publishButton.setOnClickListener(this);
        circleLayout = findViewById(R.id.circleLayout);
        circleLayout.setOnClickListener(this);
        industryLayout = findViewById(R.id.industryLayout);
        industryLayout.setOnClickListener(this);
        hobbyLayout = findViewById(R.id.hobbyLayout);
        hobbyLayout.setOnClickListener(this);
        goodsLayout = findViewById(R.id.goodsLayout);
        goodsLayout.setOnClickListener(this);
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        circleTitleTextView = findViewById(R.id.circleTitleTextView);
        circleTitleTextView.setText("@圈子");
        circleTextView = findViewById(R.id.circleTextView);
        industryTextView = findViewById(R.id.industryTextView);
        hobbyTextView = findViewById(R.id.hobbyTextView);
        goodsTextView = findViewById(R.id.goodsTextView);
        personalFeedSwitchButton = findViewById(R.id.personalFeedSwitchButton);
        circleFeedSwitchButton = findViewById(R.id.circleFeedSwitchButton);
        circleSwitchButton = findViewById(R.id.circleSwitchButton);
        personalFeedSwitchButton.setOnSwitchButtonListener(new SwitchButton.OnSwitchButtonListener() {
            @Override
            public void onOpen(boolean isOpen) {
                is_user_top = isOpen ? 1 : 0;
            }
        });
        circleFeedSwitchButton.setOnSwitchButtonListener(new SwitchButton.OnSwitchButtonListener() {
            @Override
            public void onOpen(boolean isOpen) {
                is_sq_top = isOpen ? 1 : 0;
            }
        });
        circleSwitchButton.setOnSwitchButtonListener(new SwitchButton.OnSwitchButtonListener() {
            @Override
            public void onOpen(boolean isOpen) {
                is_sq = isOpen ? 1 : 0;
            }
        });
        personalFeedSwitchButton.open();
        showCircleParams(true);
    }

    private void showCircleParams(boolean isShow) {
        if (isShow) {
            atCircleRootLayout.setVisibility(View.VISIBLE);
            topCircleFeedRootLayout.setVisibility(View.VISIBLE);
            onlyPublishCircleRootLayout.setVisibility(View.VISIBLE);
        } else {
            atCircleRootLayout.setVisibility(View.GONE);
            topCircleFeedRootLayout.setVisibility(View.GONE);
            onlyPublishCircleRootLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.circleLayout:
                circleSelectDialog.show();
                break;
            case R.id.industryLayout:
                industryDialog.show();
                break;
            case R.id.hobbyLayout:
                hobbyDialog.show();
                break;
            case R.id.goodsLayout:
                Intent goodsIntent = new Intent(PublishParamSettingActivity.this, PublishChooseGoodsActivity.class);
                goodsIntent.putExtra("feeds_id", feeds_id);
                startActivity(goodsIntent);
                break;
            case R.id.publishButton:
                updateFeed();
                break;
        }
    }


    private BaseResult baseResult;
    private int circle_id;
    private String industry_parent, industry_child, hobby_parent, hobby_child;
    private int is_user_top;//是否发布到个人动态
    private int is_sq_top;//是否发布到商圈动态
    private int is_sq;//是否仅发布到商圈

    /**
     * 发布动态
     */
    private void updateFeed() {
        System.out.println("updateFeed feeds_id: "+feeds_id);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.UPDATE_FEED);
        params.addBodyParameter("type", String.valueOf(publishType));
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        params.addBodyParameter("p_industry", industry_parent);
        params.addBodyParameter("industry", industry_child);//行业
        params.addBodyParameter("p_hobby", hobby_parent);
        params.addBodyParameter("hobby", hobby_child);//兴趣
        params.addBodyParameter("is_user_top", String.valueOf(is_user_top));
        params.addBodyParameter("is_sq_top", String.valueOf(is_sq_top));
        params.addBodyParameter("is_sq", String.valueOf(is_sq));
        params.addBodyParameter("feeds_id", String.valueOf(feeds_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("updateFeed: "+result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
                System.out.println(baseResult.getMsg());
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

    @Override
    public void onChooseGoods(Set<Integer> idSet) {
        goodsTextView.setText("已选择 " + (null == idSet ? 0 : idSet.size()) + "件");
    }

    private static OnPublishFinishListener onPublishFinishListener;

    public static void setOnPublishFinishListener(OnPublishFinishListener listener) {
        onPublishFinishListener = listener;
    }

    public interface OnPublishFinishListener {
        void onPublishFinish();
    }
}
