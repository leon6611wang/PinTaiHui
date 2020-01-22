package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.adapter.QuanZiTuiJianTuiJianLabelRecyclerAdapter;
import com.zhiyu.quanzhu.ui.adapter.QuanZiTuiJianWoDeLabelRecyclerAdapter;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

public class QuanZiTuiJianLabelDialog extends Dialog implements View.OnClickListener, QuanZiTuiJianWoDeLabelRecyclerAdapter.OnLabelDeleteListener,
        QuanZiTuiJianWoDeLabelRecyclerAdapter.OnLabelGoToListener, QuanZiTuiJianTuiJianLabelRecyclerAdapter.OnLabelAddListener {
    private Context context;
    private LinearLayout closeLayout;
    private RecyclerView wodeRecyclerView, tuijianRecyclerView;
    private QuanZiTuiJianWoDeLabelRecyclerAdapter wodeAdapter;
    private QuanZiTuiJianTuiJianLabelRecyclerAdapter tuijianAdapter;
    private List<String> wodelabel_list = new ArrayList<>();
    private List<String> tuijianlabel_list = new ArrayList<>();
    private TextView editButtonTextView;

    public QuanZiTuiJianLabelDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_quanzi_tuijian_label);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(context);
        params.height = ScreentUtils.getInstance().getScreenHeight(context);
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initDatas();
        initViews();
    }

    private void initDatas() {
        wodelabel_list.add("关注");
        wodelabel_list.add("推荐");
        wodelabel_list.add("视频");
        wodelabel_list.add("娱乐");
        wodelabel_list.add("财经");
        wodelabel_list.add("科技");
        wodelabel_list.add("汽车");
        wodelabel_list.add("体育");

        tuijianlabel_list.add("公开课");
        tuijianlabel_list.add("段子");
        tuijianlabel_list.add("讲讲");
        tuijianlabel_list.add("体育");
        tuijianlabel_list.add("房产");
        tuijianlabel_list.add("时尚");
        tuijianlabel_list.add("图片");
        tuijianlabel_list.add("科技");
        tuijianlabel_list.add("美容");
        tuijianlabel_list.add("信息");
        tuijianlabel_list.add("互联网");
        tuijianlabel_list.add("野外生存");
        tuijianlabel_list.add("抖音");
        tuijianlabel_list.add("新时代");

    }

    private void initViews() {
        closeLayout = findViewById(R.id.closeLayout);
        closeLayout.setOnClickListener(this);
        wodeRecyclerView = findViewById(R.id.wodeRecyclerView);
        tuijianRecyclerView = findViewById(R.id.tuijianRecyclerView);
        wodeAdapter = new QuanZiTuiJianWoDeLabelRecyclerAdapter(context);
        wodeAdapter.setList(wodelabel_list);
        wodeAdapter.setOnLabelDeleteListener(this);
        wodeAdapter.setOnLabelGoToListener(this);
        tuijianAdapter = new QuanZiTuiJianTuiJianLabelRecyclerAdapter();
        tuijianAdapter.setList(tuijianlabel_list);
        tuijianAdapter.setOnLabelAddListener(this);
        GridLayoutManager wodeGridManager = new GridLayoutManager(context, 4);
        GridLayoutManager tuijianGridManager = new GridLayoutManager(context, 4);
        wodeRecyclerView.setLayoutManager(wodeGridManager);
        wodeRecyclerView.setAdapter(wodeAdapter);
        tuijianRecyclerView.setLayoutManager(tuijianGridManager);
        tuijianRecyclerView.setAdapter(tuijianAdapter);
        editButtonTextView = findViewById(R.id.editButtonTextView);
        editButtonTextView.setOnClickListener(this);
    }

    private boolean isEdit = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeLayout:
                dismiss();
                break;
            case R.id.editButtonTextView:
                if (!isEdit) {
                    isEdit = true;
                    wodeAdapter.setIsEdit(true);
                    editButtonTextView.setText("编辑");
                } else {
                    isEdit = false;
                    wodeAdapter.setIsEdit(false);
                    editButtonTextView.setText("确定");
                }
                break;
        }
    }


    @Override
    public void onLabelDelete(int position) {
        tuijianlabel_list.add(wodelabel_list.get(position));
        wodelabel_list.remove(position);
        wodeAdapter.setList(wodelabel_list);
        tuijianAdapter.setList(tuijianlabel_list);
        Log.i("TuiJianLabel", "delete position: " + position);
    }

    @Override
    public void onLabelGoTo(int position) {
        Log.i("TuiJianLabel", "goto position: " + position);
    }

    @Override
    public void onLabelAdd(int position) {
        String label = tuijianlabel_list.get(position);
        if (!wodelabel_list.contains(label)) {
            wodelabel_list.add(label);
            wodeAdapter.setList(wodelabel_list);
            tuijianlabel_list.remove(position);
            tuijianAdapter.setList(tuijianlabel_list);
        }else{
            Toast.makeText(context,label+" 已关注",Toast.LENGTH_SHORT).show();
        }
        Log.i("TuiJianLabel", "add position: " + position);
    }
}
