package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.ShareMessageContent;
import com.zhiyu.quanzhu.ui.adapter.InnerShareQuanLiaoAdapter;
import com.zhiyu.quanzhu.ui.adapter.InnerShareQuanYouAdapter;
import com.zhiyu.quanzhu.ui.adapter.InnerShareZuiJinAdapter;
import com.zhiyu.quanzhu.ui.adapter.ViewPagerAdapter;
import com.zhiyu.quanzhu.ui.dialog.ShareInnerDialog;
import com.zhiyu.quanzhu.ui.fragment.FragmentShareInnerQuanLiao;
import com.zhiyu.quanzhu.ui.fragment.FragmentShareInnerQuanYou;
import com.zhiyu.quanzhu.ui.fragment.FragmentShareInnerZuiJin;
import com.zhiyu.quanzhu.ui.listener.ShareInnerSelectListener;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.rongshare.ShareMessage;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.push.RongPushClient;

public class ShareInnerActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, ShareInnerSelectListener {
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private TextView titleTextView;
    private LinearLayout backLayout, rightLayout, zuijinlayout, quanyoulayout, quanliaolayout, titlelayout;
    private TextView zuijintextview, quanyoutextview, quanliaotextview;
    private View zuijinlineview, quanyoulineview, quanliaolineview;
    private ShareInnerDialog shareInnerDialog;
    private String title, desc, webUrl, imageUrl, type;
    private int feed_type;
    private int type_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_inner);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("desc");
        webUrl = getIntent().getStringExtra("webUrl");
        imageUrl = getIntent().getStringExtra("imageUrl");
        type = getIntent().getStringExtra("type");
        feed_type=getIntent().getIntExtra("feed_type",0);
        System.out.println("share type: " + type);
        type_id = getIntent().getIntExtra("share_id", 0);
        setShareInnerSelectListener();
        initDialogs();
        initViews();
    }

    private void initDialogs() {
        shareInnerDialog = new ShareInnerDialog(this, R.style.dialog, new ShareInnerDialog.OnShareInnerListener() {
            @Override
            public void onShareInner() {
                List<Integer> list0 = ((FragmentShareInnerZuiJin) fragmentList.get(0)).getSelectedIdList();
                for (Integer id : list0) {
                    share(String.valueOf(id), true);
                }
                List<Integer> list1 = ((FragmentShareInnerQuanYou) fragmentList.get(1)).getSelectedIdList();
                for (Integer id : list1) {
                    share(String.valueOf(id), true);
                }
                List<Integer> list2 = ((FragmentShareInnerQuanLiao) fragmentList.get(2)).getSelectedIdList();
                for (Integer id : list2) {
                    share(String.valueOf(id), false);
                }
                MessageToast.getInstance(ShareInnerActivity.this).show("分享成功");
                finish();
            }
        });
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        zuijinlayout = findViewById(R.id.zuijinlayout);
        zuijinlayout.setOnClickListener(this);
        quanyoulayout = findViewById(R.id.quanyoulayout);
        quanyoulayout.setOnClickListener(this);
        quanliaolayout = findViewById(R.id.quanliaolayout);
        quanliaolayout.setOnClickListener(this);
        zuijintextview = findViewById(R.id.zuijintextview);
        quanyoutextview = findViewById(R.id.quanyoutextview);
        quanliaotextview = findViewById(R.id.quanliaotextview);
        zuijinlineview = findViewById(R.id.zuijinlineview);
        quanyoulineview = findViewById(R.id.quanyoulineview);
        quanliaolineview = findViewById(R.id.quanliaolineview);

        mViewPager = findViewById(R.id.mViewPager);
        fragmentList.add(new FragmentShareInnerZuiJin());
        fragmentList.add(new FragmentShareInnerQuanYou());
        fragmentList.add(new FragmentShareInnerQuanLiao());
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(3);
    }

    private String shareInnerDialogAvatar, shareInnerDialogName, shareInnerDialogContent;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.rightLayout:
                shareInnerDialog.show();
                shareInnerDialog.setShareContent(shareInnerDialogAvatar,
                        shareInnerDialogName, StringUtils.isNullOrEmpty(title) ? "[链接]圈助注册" : title);
                break;
            case R.id.zuijinlayout:
                barChange(0);
                break;
            case R.id.quanyoulayout:
                barChange(1);
                break;
            case R.id.quanliaolayout:
                barChange(2);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        barChange(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void barChange(int position) {
        zuijintextview.setTextColor(getResources().getColor(R.color.text_color_shareinner_gray));
        quanyoutextview.setTextColor(getResources().getColor(R.color.text_color_shareinner_gray));
        quanliaotextview.setTextColor(getResources().getColor(R.color.text_color_shareinner_gray));
        zuijinlineview.setVisibility(View.INVISIBLE);
        quanyoulineview.setVisibility(View.INVISIBLE);
        quanliaolineview.setVisibility(View.INVISIBLE);
        switch (position) {
            case 0:
                zuijintextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                zuijinlineview.setVisibility(View.VISIBLE);
                break;
            case 1:
                quanyoutextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                quanyoulineview.setVisibility(View.VISIBLE);
                break;
            case 2:
                quanliaotextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                quanliaolineview.setVisibility(View.VISIBLE);
                break;
        }
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_silent, R.anim.dialog_dimiss);
    }

    private void share(String mTargetId, boolean isPrivate) {
        ShareMessage shareMessage = new ShareMessage();
        ShareMessageContent shareMessageContent = new ShareMessageContent();
        shareMessageContent.setShareUrl(webUrl);
        shareMessageContent.setShareImageUrl(imageUrl);
        shareMessageContent.setShareTitle(title);
        shareMessageContent.setShareContent(desc);
        shareMessageContent.setShareType(type);
        shareMessageContent.setShareTypeId(String.valueOf(type_id));
        shareMessage.setContent(GsonUtils.GsonString(shareMessageContent));
        RongIM.getInstance().sendMessage(Message.obtain(mTargetId, isPrivate ? Conversation.ConversationType.PRIVATE : Conversation.ConversationType.GROUP, shareMessage),
                "您有一条新分享消息", null, new IRongCallback.ISendMessageCallback() {
                    @Override
                    public void onAttached(Message message) {
                        System.out.println("消息发送Attached");
                    }

                    @Override
                    public void onSuccess(Message message) {
                        System.out.println("消息发送成功");
                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        System.out.println("消息发送失败");
                    }
                });
    }

    private void setShareInnerSelectListener() {
        InnerShareZuiJinAdapter.setShareInnerSelectListener(this);
        InnerShareQuanYouAdapter.setShareInnerSelectListener(this);
        InnerShareQuanLiaoAdapter.setShareInnerSelectListener(this);
    }

    private List<SelectUser> selectUserList = new ArrayList<>();

    @Override
    public void onSelect(String avatar, String name, boolean select, boolean isCircle) {
        boolean has = false;
        int position = 0;
        for (int i = 0; i < selectUserList.size(); i++) {
            if (selectUserList.get(i).getAvatar().equals(avatar) && selectUserList.get(i).getName().equals(name)) {
                has = true;
                position = i;
                break;
            }
        }
        if (has) {
            selectUserList.remove(position);
        } else {
            SelectUser user = new SelectUser();
            user.setAvatar(avatar);
            user.setCircle(isCircle);
            user.setName(name);
            user.setSelect(select);
            selectUserList.add(user);
        }
        if (selectUserList.size() > 1) {
            shareInnerDialogAvatar = null;
            shareInnerDialogName = "";
            for (int i = 0; i < selectUserList.size(); i++) {
                shareInnerDialogName += selectUserList.get(i).getName();
                if (i < selectUserList.size() - 1) {
                    shareInnerDialogName += "、";
                }
            }
        } else if (selectUserList.size() == 1) {
            shareInnerDialogAvatar = selectUserList.get(0).getAvatar();
            shareInnerDialogName = selectUserList.get(0).getName();
        }
        titleTextView.setText("发送给(" + selectUserList.size() + ")");
    }

    class SelectUser {
        private String avatar;
        private String name;
        private boolean select;
        private boolean isCircle;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public boolean isCircle() {
            return isCircle;
        }

        public void setCircle(boolean circle) {
            isCircle = circle;
        }
    }
}
