package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
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
public class CalendarDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    private TextView cancelTextView, confirmTextView;
    private LoopView yearView, monthView, dayView;
    private List<String> yearList = new ArrayList<>();
    private List<String> monthList = new ArrayList<>();
    private List<String> dayList = new ArrayList<>();
    private int currentYear, currentMonth, currentDay, yearIndex, monthIndex, dayIndex, selectYear, selectMonth, selectDay;

    public CalendarDialog(@NonNull Context context, int themeResId, boolean isFirstDay, OnCalendarListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.onCalendarListener = listener;
        currentYear = CalendarUtils.getInstance().getCurrentYear();
        currentMonth = CalendarUtils.getInstance().getCurrentMonth();
        if (isFirstDay) {
            currentDay = 1;
        } else {
            currentDay = CalendarUtils.getInstance().getCurrentDay();
        }
        selectYear = currentYear;
        selectMonth = currentMonth;
        selectDay = currentDay;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext);
        params.height = ScreentUtils.getInstance().getScreenHeight(mContext) / 5 * 2;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initData();
        initViews();
        setDayView(currentYear, currentMonth);

        if (null != onCalendarListener) {
            onCalendarListener.onCalendar(selectYear, selectMonth, selectDay);
        }
    }

    private void initData() {
        for (int i = 2000; i < 2050 + 1; i++) {
            yearList.add(String.valueOf(i));
            if (i == currentYear) {
                yearIndex = (i - 2000);
            }
        }

        for (int i = 1; i < 12 + 1; i++) {
            monthList.add(String.valueOf(i));
            if (i == currentMonth) {
                monthIndex = i - 1;
            }
        }

    }


    private void initViews() {
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
        yearView = findViewById(R.id.yearView);
        monthView = findViewById(R.id.monthView);
        dayView = findViewById(R.id.dayView);
        yearView.setItems(yearList);
        yearView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (!TextUtils.isEmpty(yearList.get(index))) {
                    selectYear = Integer.parseInt(yearList.get(index));
                    setDayView(selectYear, selectMonth);
                }
                if (null != onCalendarListener) {
                    onCalendarListener.onCalendar(selectYear, selectMonth, selectDay);
                }
            }
        });
        monthView.setItems(monthList);
        monthView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (!TextUtils.isEmpty(monthList.get(index))) {
                    selectMonth = Integer.parseInt(monthList.get(index));
                    setDayView(selectYear, selectMonth);
                }
                if (null != onCalendarListener) {
                    onCalendarListener.onCalendar(selectYear, selectMonth, selectDay);
                }
            }
        });
    }

    private void setDayView(int year, int month) {
        if (null != dayList && dayList.size() > 0) {
            dayList.clear();
        }
        int days = CalendarUtils.getInstance().getDaysByYearMonth(year, month);

        for (int i = 1; i < days + 1; i++) {
            dayList.add(String.valueOf(i));
            if (i == currentDay) {
                dayIndex = i - 1;
            }
        }

        dayView.setCurrentPosition(dayIndex);
        dayView.setItems(dayList);
        dayView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (!TextUtils.isEmpty(dayList.get(index))) {
                    selectDay = Integer.parseInt(dayList.get(index));
                }
                if (null != onCalendarListener) {
                    onCalendarListener.onCalendar(selectYear, selectMonth, selectDay);
                }
            }
        });
    }

    @Override
    public void show() {
        super.show();
        System.out.println("currentDay: "+currentDay+" , dayIndex: "+dayIndex);
        setCurrentDate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelTextView:
                dismiss();
                break;
            case R.id.confirmTextView:
                if (null != onCalendarListener) {
                    onCalendarListener.onCalendar(selectYear, selectMonth, selectDay);
                }
                dismiss();
                break;
        }
    }

    private void setCurrentDate() {
        yearView.setCurrentPosition(yearIndex);
        monthView.setCurrentPosition(monthIndex);
        dayView.setCurrentPosition(dayIndex);
    }

    private OnCalendarListener onCalendarListener;

    public interface OnCalendarListener {
        void onCalendar(int year, int month, int day);
    }
}
