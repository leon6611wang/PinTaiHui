package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.adapter.ManageReplyRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.AddReplyDialog;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

public class ManageReplyActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleText;
    private RecyclerView mRecyclerView;
    private ManageReplyRecyclerAdapter adapter;
    private List<String> list = new ArrayList<>();
    private TextView addReplyTextView;
    private AddReplyDialog addReplyDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_reply);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        list.add("北京");
        list.add("上海");
        list.add("天津");
        list.add("重庆");
        list.add("安徽");
        list.add("河北");
        list.add("江苏");
        list.add("浙江");
        list.add("河南");
        list.add("湖北");
        list.add("山东");
        list.add("辽宁");
        list.add("山西");
        list.add("宁夏");
        list.add("陕西");
        list.add("新疆");
        list.add("吉林");
        list.add("福建");
        list.add("四川");
        list.add("贵州");
        list.add("云南");
        list.add("广东");
        list.add("广西");
        list.add("湖南");
        list.add("内蒙古");
        list.add("西藏");
        initViews();
        initDialogs();
    }

    private void initDialogs(){
        addReplyDialog=new AddReplyDialog(this,R.style.dialog);
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleText = findViewById(R.id.titleTextView);
        titleText.setText("管理回复");
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new ManageReplyRecyclerAdapter(this);
        adapter.setList(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        addReplyTextView = findViewById(R.id.addReplyTextView);
        addReplyTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.addReplyTextView:
                addReplyDialog.show();
                break;
        }
    }
}
