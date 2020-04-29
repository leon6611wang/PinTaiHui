package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.CalendarUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 日历
 * 年月日
 */
public class TimeDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private TextView cancelTextView, confirmTextView;
    private LoopView dayView, hourView, minuteView;
    private List<String> dayList = new ArrayList<>();
    private List<String> hourList = new ArrayList<>();
    private List<String> minuteList = new ArrayList<>();
    private String hour, minute;
    private boolean isCurrentDay;
    private int dayIndex;

    public TimeDialog(@NonNull Context context, int themeResId, boolean isCurrentDay, OnTimeListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.isCurrentDay = isCurrentDay;
        this.onTimeListener = listener;
        this.dayIndex=isCurrentDay?0:1;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_time);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext);
        params.height = ScreentUtils.getInstance().getScreenHeight(mContext) / 5 * 2;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initData();
        initViews();
    }

    private void initData() {
        dayList.add("当");
        dayList.add("次");

        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                hourList.add("0" + String.valueOf(i));
            } else {
                hourList.add(String.valueOf(i));
            }
        }

        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minuteList.add("0" + String.valueOf(i));
            } else {
                minuteList.add(String.valueOf(i));
            }

        }
    }


    private void initViews() {
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
        dayView = findViewById(R.id.dayView);
        hourView = findViewById(R.id.hourView);
        minuteView = findViewById(R.id.minuteView);
        dayView.setItems(dayList);
        dayView.setCurrentPosition(dayIndex);
        dayView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                dayIndex = index;
                if (isCurrentDay) {
                    dayView.setCurrentPosition(0);
                }
            }
        });
        hourView.setItems(hourList);
        hourView.setCurrentPosition(0);
        hour = hourList.get(0);
        hourView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                hour = hourList.get(index);
            }
        });
        minuteView.setItems(minuteList);
        minuteView.setCurrentPosition(0);
        minute = minuteList.get(0);
        minuteView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                minute = minuteList.get(index);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelTextView:
                dismiss();
                break;
            case R.id.confirmTextView:
                if (null != onTimeListener) {
                    onTimeListener.onTime(hour, minute, dayIndex == 1);
                }
                dismiss();
                break;
        }
    }


    private OnTimeListener onTimeListener;

    public interface OnTimeListener {
        void onTime(String hour, String minute, boolean isNextDay);
    }
}
