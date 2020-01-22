package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.activity.ManageReplyActivity;
import com.zhiyu.quanzhu.ui.adapter.ConversationQuickReplyRecyclerAdapter;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

public class ConversationQuickReplyDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private TextView manageReplyTextView;
    private List<String> list = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ConversationQuickReplyRecyclerAdapter adapter;

    public ConversationQuickReplyDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_conversation_quick_reply);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext);
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        initViews();
    }

    private void initViews() {
        manageReplyTextView = findViewById(R.id.manageReplyTextView);
        manageReplyTextView.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new ConversationQuickReplyRecyclerAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.manageReplyTextView:
                Intent manageIntent = new Intent(getContext(), ManageReplyActivity.class);
                manageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(manageIntent);
                break;
        }
    }


}
