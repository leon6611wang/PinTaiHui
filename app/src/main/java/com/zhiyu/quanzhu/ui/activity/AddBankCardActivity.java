package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.Bank;
import com.zhiyu.quanzhu.ui.dialog.BankListDialog;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class AddBankCardActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private BankListDialog bankListDialog;
    private LinearLayout banklayout;
    private TextView banknametextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bankcard);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initDialogs();
        initViews();
    }

    private void initDialogs() {
        bankListDialog = new BankListDialog(this, R.style.dialog,new BankListDialog.OnChooseBankListener(){
            @Override
            public void onChooseBank(Bank bank) {
                banknametextview.setText(bank.getBankName());
            }
        });
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("新增银行卡");
        banklayout=findViewById(R.id.banklayout);
        banklayout.setOnClickListener(this);
        banknametextview=findViewById(R.id.banknametextview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.banklayout:
                bankListDialog.show();
                break;
        }
    }
}
