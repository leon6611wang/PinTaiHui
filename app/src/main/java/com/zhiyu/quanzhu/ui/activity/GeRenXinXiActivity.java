package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.AreaCity;
import com.zhiyu.quanzhu.model.bean.AreaProvince;
import com.zhiyu.quanzhu.ui.dialog.GenderDialog;
import com.zhiyu.quanzhu.ui.dialog.ProvinceCityDialog;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class GeRenXinXiActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleText;
    private LinearLayout genderlayout, citylayout, shouhuodizhilayout;

    private GenderDialog genderDialog;
    private ProvinceCityDialog provinceCityDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenxinxi);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initDialogs();
        initViews();
    }

    private void initDialogs() {
        genderDialog = new GenderDialog(this, R.style.dialog);
        provinceCityDialog = new ProvinceCityDialog(this, R.style.dialog, new ProvinceCityDialog.OnCityChooseListener() {
            @Override
            public void onCityChoose(AreaProvince province, AreaCity city) {

            }
        });
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleText = findViewById(R.id.titleTextView);
        titleText.setText("个人信息");
        genderlayout = findViewById(R.id.genderlayout);
        genderlayout.setOnClickListener(this);
        citylayout = findViewById(R.id.citylayout);
        citylayout.setOnClickListener(this);
        shouhuodizhilayout = findViewById(R.id.shouhuodizhilayout);
        shouhuodizhilayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.genderlayout:
                genderDialog.show();
                break;
            case R.id.citylayout:
                provinceCityDialog.show();
                break;
            case R.id.shouhuodizhilayout:
                Intent dizhiIntent = new Intent(this, ShouHuoDiZhiGuanLiActivity.class);
                startActivity(dizhiIntent);
                break;
        }
    }
}
