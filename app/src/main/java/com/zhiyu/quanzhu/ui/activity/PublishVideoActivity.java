package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.IndustryChild;
import com.zhiyu.quanzhu.model.bean.IndustryParent;
import com.zhiyu.quanzhu.ui.dialog.AddTagDialog;
import com.zhiyu.quanzhu.ui.dialog.IndustryDialog;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * 发布视频
 */
public class PublishVideoActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout backLayout;
    private TextView titleTextView;
    private FrameLayout addVideoLayout;
    private float ratio=0.69565f;
    private int screenWidth,layoutWidth,layoutHeight,dp_15,dp_25;
    private LinearLayout.LayoutParams ll;
    private LinearLayout tagLayout;
    private EditText introductionEditText;
    private TextView tagTextView,nextButton;
    private AddTagDialog addTagDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_video);
        ScreentUtils.getInstance().setStatusBarLightMode(this,true);
        initViews();
        initDialogs();
    }

    private void initDialogs(){
        addTagDialog=new AddTagDialog(this,this,R.style.dialog);
    }

    private void initViews(){
        screenWidth=ScreentUtils.getInstance().getScreenWidth(this);
        dp_15=(int) getResources().getDimension(R.dimen.dp_15);
        dp_25=(int) getResources().getDimension(R.dimen.dp_25);
        layoutWidth=screenWidth-dp_15*2;
        layoutHeight=Math.round(layoutWidth*ratio);
        ll=new LinearLayout.LayoutParams(layoutWidth,layoutHeight);
        ll.gravity= Gravity.CENTER;
        ll.leftMargin=dp_15;
        ll.rightMargin=dp_15;
        ll.topMargin=dp_25;
        addVideoLayout=findViewById(R.id.addVideoLayout);
        addVideoLayout.setLayoutParams(ll);
        backLayout=findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView=findViewById(R.id.titleTextView);
        titleTextView.setText("发布视频");
        tagLayout=findViewById(R.id.tagLayout);
        tagLayout.setOnClickListener(this);
        introductionEditText=findViewById(R.id.introductionEditText);
        tagTextView=findViewById(R.id.tagTextView);
        nextButton=findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backLayout:
                finish();
                break;
            case R.id.tagLayout:
                addTagDialog.show();
                break;
            case R.id.nextButton:
                Intent paramsIntent=new Intent(PublishVideoActivity.this,PublishParamSettingActivity.class);
                startActivity(paramsIntent);
                break;
        }
    }
}
