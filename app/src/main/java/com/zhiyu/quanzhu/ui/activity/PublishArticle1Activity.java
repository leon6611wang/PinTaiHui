package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.dialog.AddTagDialog;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * 发布文章
 * 第一步
 */
public class PublishArticle1Activity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private AddTagDialog addTagDialog;
    private LinearLayout addTagLayout, editContentLayout;
    private TextView addTagTextView, nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_article_1);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initDialogs();
        initViews();
    }

    private void initDialogs() {
        addTagDialog = new AddTagDialog(this, this, R.style.dialog);
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("发布文章");
        addTagLayout = findViewById(R.id.addTagLayout);
        addTagLayout.setOnClickListener(this);
        addTagTextView = findViewById(R.id.addTagTextView);
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);
        editContentLayout = findViewById(R.id.editContentLayout);
        editContentLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.addTagLayout:
                addTagDialog.show();
                break;
            case R.id.nextButton:
                Intent paramIntent = new Intent(PublishArticle1Activity.this, PublishParamSettingActivity.class);
                startActivity(paramIntent);
                break;
            case R.id.editContentLayout:
                Intent editContentIntent = new Intent(PublishArticle1Activity.this, PublishArticle2Activity.class);
                startActivity(editContentIntent);
                break;
        }
    }
}
