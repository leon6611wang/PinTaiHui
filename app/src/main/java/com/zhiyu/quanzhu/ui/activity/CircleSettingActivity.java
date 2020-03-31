package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.AreaCity;
import com.zhiyu.quanzhu.model.bean.AreaProvince;
import com.zhiyu.quanzhu.model.bean.HobbyDaoChild;
import com.zhiyu.quanzhu.model.bean.HobbyDaoParent;
import com.zhiyu.quanzhu.model.bean.IndustryChild;
import com.zhiyu.quanzhu.model.bean.IndustryParent;
import com.zhiyu.quanzhu.ui.dialog.ChoosePhotoDialog;
import com.zhiyu.quanzhu.ui.dialog.CircleTypeDialog;
import com.zhiyu.quanzhu.ui.dialog.HobbyDialog;
import com.zhiyu.quanzhu.ui.dialog.IndustryDialog;
import com.zhiyu.quanzhu.ui.dialog.ProvinceCityDialog;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * 圈子设置
 * 圈主
 */
public class CircleSettingActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout, xingzhiLayout, chengshiLayout, hangyeLayout;
    private TextView titleTextView, xingzhiTextView, chengshiTextView, industryTextView, hangyeTextView, confirmTextView;
    private EditText mingchengEditText;
    private CircleTypeDialog circleTypeDialog;
    private ProvinceCityDialog cityDialog;
    private IndustryDialog industryDialog;
    private HobbyDialog hobbyDialog;
    private int circleType = 1;
    private long circle_id;
    private String name, province_name, city_name, descirption, logo, thumb, two_industry, three_industry, video;
    private int type, province, city, is_verify, is_price, price, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_setting);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
        initDialogs();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("更改圈子信息");
        mingchengEditText = findViewById(R.id.mingchengEditText);
        xingzhiLayout = findViewById(R.id.xingzhiLayout);
        xingzhiLayout.setOnClickListener(this);
        xingzhiTextView = findViewById(R.id.xingzhiTextView);
        chengshiLayout = findViewById(R.id.chengshiLayout);
        chengshiLayout.setOnClickListener(this);
        chengshiTextView = findViewById(R.id.chengshiTextView);
        hangyeLayout = findViewById(R.id.hangyeLayout);
        hangyeLayout.setOnClickListener(this);
        industryTextView = findViewById(R.id.industryTextView);
        hangyeTextView = findViewById(R.id.hangyeTextView);
        confirmTextView = findViewById(R.id.confirmTextView);
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

        }
    }

    private void initDialogs() {
        circleTypeDialog = new CircleTypeDialog(this, R.style.dialog, new CircleTypeDialog.OnCircleTypeListener() {
            @Override
            public void onCircleType(int t, String desc) {
                circleType = t;
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
}
