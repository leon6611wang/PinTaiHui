package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.WindowManager;

import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.CalendarUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 日历
 * 年月日
 */
public class EndDateDialog extends Dialog {
    private Context mContext;
    private LoopView yearView, monthView, dayView;
    private List<String> yearList = new ArrayList<>();
    private List<String> monthList = new ArrayList<>();
    private List<String> dayList = new ArrayList<>();
    private int startYear, startMonth, startDay, yearIndex, monthIndex, dayIndex, selectYear, selectMonth, selectDay;

    public EndDateDialog(@NonNull Context context, int themeResId, OnCalendarListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.onCalendarListener = listener;
//        startYear = CalendarUtils.getInstance().getCurrentYear();
//        startMonth = CalendarUtils.getInstance().getCurrentMonth();
//        startDay = CalendarUtils.getInstance().getCurrentDay();
//        selectYear = startYear;
//        selectMonth = startMonth;
//        selectDay = startDay;
    }


    public void setStartDate(int year, int month, int day, int sYear, int sMonth, int sDay) {
        startYear = year;
        startMonth = month;
        startDay = day;
        selectYear = sYear;
        selectMonth = sMonth;
        selectDay = sDay;
//        System.out.println("select: " + selectYear + " , " + selectMonth + " , " + selectDay);
        for (int i = 0; i < yearList.size(); i++) {
            if (Integer.parseInt(yearList.get(i)) == selectYear) {
                yearIndex = i;
            }
        }
        for (int i = 1; i < monthList.size(); i++) {
            if (i == selectMonth) {
                monthIndex = i - 1;
            }
        }
        dayIndex = selectDay - 1;
        if (dayIndex < 0) {
            dayIndex = 0;
        }
//        System.out.println("index: " + yearIndex + " , " + monthIndex + " , " + dayIndex);
        yearView.setCurrentPosition(yearIndex);
        monthView.setCurrentPosition(monthIndex);
        dayView.setCurrentPosition(dayIndex);
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
        setDayView(startYear, startMonth);
    }

    private void initData() {
        for (int i = 2000; i < 2050 + 1; i++) {
            yearList.add(String.valueOf(i));
        }

        for (int i = 1; i < 12 + 1; i++) {
            monthList.add(String.valueOf(i));
        }

    }


    private void initViews() {
        yearView = findViewById(R.id.yearView);
        monthView = findViewById(R.id.monthView);
        dayView = findViewById(R.id.dayView);
        dayView.setNotLoop();
        yearView.setItems(yearList);
        yearView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (!TextUtils.isEmpty(yearList.get(index))) {
                    selectYear = Integer.parseInt(yearList.get(index));
                    if (selectYear < startYear) {
                        yearView.setCurrentPosition(yearIndex);
                        monthView.setCurrentPosition(monthIndex);
                        dayView.setCurrentPosition(dayIndex);
                        selectYear =  Integer.parseInt(yearList.get(yearIndex));
                        selectMonth = Integer.parseInt(monthList.get(monthIndex));
                        selectDay = Integer.parseInt(dayList.get(dayIndex));
                        MessageToast.getInstance(getContext()).show("截止时间不能小于开始时间");
                    } else {
                        setDayView(selectYear, selectMonth);
                    }
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
                    if (selectYear == startYear && selectMonth < startMonth) {
                        yearView.setCurrentPosition(yearIndex);
                        monthView.setCurrentPosition(monthIndex);
                        dayView.setCurrentPosition(dayIndex);
                        selectMonth = Integer.parseInt(monthList.get(monthIndex));
                        selectYear = Integer.parseInt(yearList.get(yearIndex));
                        selectDay = Integer.parseInt(dayList.get(dayIndex));
                        MessageToast.getInstance(getContext()).show("截止时间不能小于时间");
                    } else {
                        setDayView(selectYear, selectMonth);
                    }
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
        }

        dayView.setItems(dayList);
        dayView.setCurrentPosition(0);
        dayView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (!TextUtils.isEmpty(dayList.get(index))) {
                    selectDay = Integer.parseInt(dayList.get(index));
                    if (selectYear == startYear && selectMonth == startMonth && selectDay < startDay) {
                        yearView.setCurrentPosition(yearIndex);
                        monthView.setCurrentPosition(monthIndex);
                        dayView.setCurrentPosition(dayIndex);
                        selectDay = Integer.parseInt(dayList.get(dayIndex));
                        selectMonth = Integer.parseInt(monthList.get(monthIndex));
                        selectYear = Integer.parseInt(yearList.get(yearIndex));
                        MessageToast.getInstance(getContext()).show("截止时间不能小于开始时间");
                    }
                }
                if (null != onCalendarListener) {
                    onCalendarListener.onCalendar(selectYear, selectMonth, selectDay);
                }
            }
        });
    }


    private void setCurrentDate() {
        yearView.setCurrentPosition(yearIndex);
        monthView.setCurrentPosition(monthIndex);
    }

    private OnCalendarListener onCalendarListener;

    public interface OnCalendarListener {
        void onCalendar(int year, int month, int day);
    }
}
