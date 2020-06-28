package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.PurseRecordParent;
import com.zhiyu.quanzhu.model.result.PurseRecordResult;
import com.zhiyu.quanzhu.model.result.PurseResult;
import com.zhiyu.quanzhu.ui.adapter.MyWalletExpandableListAdapter;
import com.zhiyu.quanzhu.ui.dialog.CalendarDialog;
import com.zhiyu.quanzhu.ui.dialog.EndDateDialog;
import com.zhiyu.quanzhu.ui.dialog.NotificationDialog;
import com.zhiyu.quanzhu.ui.dialog.StartDateDialog;
import com.zhiyu.quanzhu.utils.CalendarUtils;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.PriceParseUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.ThreadPoolUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * 我的钱包(重构)
 */
public class MyPurseActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout headerLayout, backLayout, rightLayout;
    private View headerView;
    private ExpandableListView mExpandableListView;
    private MyWalletExpandableListAdapter adapter;
    private View mHeaderView, mMenuView, headerlayout, menulayout;
    private int[] menulocation = new int[2];
    private int menulayoutY = 0, headerviewheight;
    private LinearLayout quanbuView, shouruView, zhichuView, quanbuLayout, shouruLayout, zhichuLayout;
    private TextView quanbuTextView, shouruTextView, zhichuTextView, quanbuTextLayout, shouruTextLayout, zhichuTextLayout;
    private View quanbuLineView, shouruLineView, zhichuLineView, quanbuLineLayout, shouruLineLayout, zhichuLineLayout;
    private TextView startDateTextView, endDateTextView, tixianTextView;
    private TextView allMoneyTextView, frozenMoneyTextView, outMoneyTextView, inMoneyTextView;
    private StartDateDialog startDateDialog;
    private EndDateDialog endDateDialog;
    private PtrFrameLayout ptrLayout;
    private NotificationDialog notificationDialog;
    private ImageView notificationImageView;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<MyPurseActivity> activityWeakReference;

        public MyHandler(MyPurseActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MyPurseActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.allMoneyTextView.setText(activity.formatTosepara(activity.purseResult.getData().getMoney() / 100));
                    activity.frozenMoneyTextView.setText(PriceParseUtils.getInstance().parsePrice(activity.purseResult.getData().getFrozen_money()));
                    activity.outMoneyTextView.setText(PriceParseUtils.getInstance().parsePrice(activity.purseResult.getData().getOut_money()));
                    activity.inMoneyTextView.setText(PriceParseUtils.getInstance().parsePrice(activity.purseResult.getData().getIn_money()));
                    break;
                case 2:
                    activity.ptrLayout.refreshComplete();
                    switch (activity.currentMenuPosition) {
                        case 0:
                            activity.adapter.setList(activity.allList);
                            break;
                        case 1:
                            activity.adapter.setList(activity.inList);
                            break;
                        case 2:
                            activity.adapter.setList(activity.outList);
                            break;
                    }

                    activity.expandList();
                    break;
                case 3:

                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purse);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        currentYear = CalendarUtils.getInstance().getCurrentYear();
        currentMonth = CalendarUtils.getInstance().getCurrentMonth();
        currentDay = CalendarUtils.getInstance().getCurrentDay();
        startYear = CalendarUtils.getInstance().getCurrentYear();
        startMonth = CalendarUtils.getInstance().getCurrentMonth();
        startDay = 1;
        initDialogs();
        initPtr();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        purseInformation();
        purseList();
    }

    private int startYear, startMonth, startDay, endYear, endMonth, endDay, currentYear, currentMonth, currentDay;

    private void initDialogs() {
        startDateDialog = new StartDateDialog(this, R.style.dialog, new StartDateDialog.OnCalendarListener() {
            @Override
            public void onCalendar(int year, int month, int day) {
                startYear = year;
                startMonth = month;
                startDay = day;
                startDateTextView.setText(month + "月" + day + "日 " + year);
            }
        });
        endDateDialog = new EndDateDialog(this, R.style.dialog, new EndDateDialog.OnCalendarListener() {
            @Override
            public void onCalendar(int year, int month, int day) {
                endYear = year;
                endMonth = month;
                endDay = day;
                endDateTextView.setText(month + "月" + day + "日 " + year);
            }
        });
        notificationDialog = new NotificationDialog(this, R.style.dialog);
    }

    private void initPtr() {
        ptrLayout = findViewById(R.id.ptr_frame_layout);
        ptrLayout.setHeaderView(new MyPtrRefresherHeader(this));
        ptrLayout.addPtrUIHandler(new MyPtrHandlerHeader(this, ptrLayout));
        ptrLayout.setFooterView(new MyPtrRefresherFooter(this));
        ptrLayout.addPtrUIHandler(new MyPtrHandlerFooter(this, ptrLayout));
        ptrLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                switch (currentMenuPosition) {
                    case 0:
                        allPage++;
                        break;
                    case 1:
                        inPage++;
                        break;
                    case 2:
                        outPage++;
                        break;
                }
                purseList();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

            }
        });
        ptrLayout.setMode(PtrFrameLayout.Mode.LOAD_MORE);
    }

    private void initViews() {
        headerView = LayoutInflater.from(this).inflate(R.layout.header_qianbao_recyclerview, null);
        backLayout = headerView.findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        rightLayout = headerView.findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        allMoneyTextView = headerView.findViewById(R.id.allMoneyTextView);
        frozenMoneyTextView = headerView.findViewById(R.id.frozenMoneyTextView);
        outMoneyTextView = headerView.findViewById(R.id.outMoneyTextView);
        inMoneyTextView = headerView.findViewById(R.id.inMoneyTextView);
        notificationImageView = headerView.findViewById(R.id.notificationImageView);
        notificationImageView.setOnClickListener(this);

        startDateTextView = headerView.findViewById(R.id.startDateTextView);
        startDateTextView.setText(currentMonth + "月" + 1 + "日 " + currentYear);
        startDateTextView.setOnClickListener(this);
        endDateTextView = headerView.findViewById(R.id.endDateTextView);
        endYear=currentYear;
        endMonth=currentMonth;
        endDay=CalendarUtils.getInstance().getCurrentDay();
        endDateTextView.setText(endMonth + "月" +endDay + "日 " + endYear);
        endDateTextView.setOnClickListener(this);
        quanbuView = findViewById(R.id.quanbuView);
        quanbuView.setOnClickListener(this);
        shouruView = findViewById(R.id.shouruView);
        shouruView.setOnClickListener(this);
        zhichuView = findViewById(R.id.zhichuView);
        zhichuView.setOnClickListener(this);
        quanbuLayout = headerView.findViewById(R.id.quanbuLayout);
        quanbuLayout.setOnClickListener(this);
        shouruLayout = headerView.findViewById(R.id.shouruLayout);
        shouruLayout.setOnClickListener(this);
        zhichuLayout = headerView.findViewById(R.id.zhichuLayout);
        zhichuLayout.setOnClickListener(this);
        tixianTextView = headerView.findViewById(R.id.tixianTextView);
        tixianTextView.setOnClickListener(this);
        quanbuTextView = findViewById(R.id.quanbuTextView);
        quanbuTextLayout = headerView.findViewById(R.id.quanbuTextLayout);
        shouruTextView = findViewById(R.id.shouruTextView);
        shouruTextLayout = headerView.findViewById(R.id.shouruTextLayout);
        zhichuTextView = findViewById(R.id.zhichuTextView);
        zhichuTextLayout = headerView.findViewById(R.id.zhichuTextLayout);
        quanbuLineView = findViewById(R.id.quanbuLineView);
        quanbuLineLayout = headerView.findViewById(R.id.quanbuLineLayout);
        shouruLineView = findViewById(R.id.shouruLineView);
        shouruLineLayout = headerView.findViewById(R.id.shouruLineLayout);
        zhichuLineView = findViewById(R.id.zhichuLineView);
        zhichuLineLayout = headerView.findViewById(R.id.zhichuLineLayout);
        headerlayout = headerView.findViewById(R.id.headerlayout);
        menulayout = headerView.findViewById(R.id.menulayout);
        mHeaderView = findViewById(R.id.mHeaderView);
        mMenuView = findViewById(R.id.mMenuView);
        mExpandableListView = findViewById(R.id.mExpandableListView);
        mExpandableListView.addHeaderView(headerView);
        adapter = new MyWalletExpandableListAdapter();
        mExpandableListView.setAdapter(adapter);

        mExpandableListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mCurrentfirstVisibleItem = firstVisibleItem;
                View firstView = view.getChildAt(0);
                if (null != firstView) {
                    ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
                    if (null == itemRecord) {
                        itemRecord = new ItemRecod();
                    }
                    itemRecord.height = firstView.getHeight();
                    itemRecord.top = firstView.getTop();
                    recordSp.append(firstVisibleItem, itemRecord);
                    totalDy = getScrollY();//滚动距离
                    barChange();
                }
            }
        });
        mHeaderView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        mHeaderView.getViewTreeObserver().removeOnPreDrawListener(this);
                        headerviewheight = mHeaderView.getHeight(); // 获取高度
                        return true;
                    }
                });
        headerlayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        headerlayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        int[] headerlocation = new int[2];
                        headerlayout.getLocationOnScreen(headerlocation);
                        return true;
                    }
                });
        menulayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        menulayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        menulayout.getLocationInWindow(menulocation);
                        menulayoutY = menulocation[1];
                        return true;
                    }
                });

//        mExpandableListView.scrollTo(0, 500);
    }

    //展开列表
    private void expandList() {
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            mExpandableListView.collapseGroup(i);
            mExpandableListView.expandGroup(i);
        }
    }


    private SparseArray recordSp = new SparseArray(0);
    private int mCurrentfirstVisibleItem = 0;
    private int totalDy;

    private int getScrollY() {
        int height = 0;
        for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
            ItemRecod itemRecod = (ItemRecod) recordSp.get(i);
            if (null != itemRecod)
                height += itemRecod.height;
        }
        ItemRecod itemRecod = (ItemRecod) recordSp.get(mCurrentfirstVisibleItem);
        if (null == itemRecod) {
            itemRecod = new ItemRecod();
        }
        return height - itemRecod.top;
    }

    class ItemRecod {
        int height = 0;
        int top = 0;
    }

    private void barChange() {
        if (Math.abs(totalDy) > 0) {
            headerlayout.setVisibility(View.INVISIBLE);
            mHeaderView.setVisibility(View.VISIBLE);
            mHeaderView.setBackgroundColor(getResources().getColor(R.color.text_color_yellow));
            float alpha = (float) Math.abs(totalDy) / (float) 300;
            mHeaderView.setAlpha(alpha);
        } else {
            headerlayout.setVisibility(View.VISIBLE);
            mHeaderView.setVisibility(View.INVISIBLE);
            mHeaderView.setAlpha(0);
        }

        if (menulayoutY > 0) {
            if (Math.abs(totalDy) >= (menulayoutY - headerviewheight)) {
                mMenuView.setVisibility(View.VISIBLE);
            } else {
                mMenuView.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void menuChange(int position) {
        currentMenuPosition = position;
        switch (currentMenuPosition) {
            case 0:
                if (null == allList || allList.size() == 0) {
                    purseList();
                } else {
                    adapter.setList(allList);
                }
                break;
            case 1:
                if (null == inList || inList.size() == 0) {
                    purseList();
                } else {
                    adapter.setList(inList);
                }
                break;
            case 2:
                if (null == outList || outList.size() == 0) {
                    purseList();
                } else {
                    adapter.setList(outList);
                }
                break;
        }

        quanbuTextView.setTextColor(getResources().getColor(R.color.text_color_gray));
        quanbuTextLayout.setTextColor(getResources().getColor(R.color.text_color_gray));
        shouruTextView.setTextColor(getResources().getColor(R.color.text_color_gray));
        shouruTextLayout.setTextColor(getResources().getColor(R.color.text_color_gray));
        zhichuTextView.setTextColor(getResources().getColor(R.color.text_color_gray));
        zhichuTextLayout.setTextColor(getResources().getColor(R.color.text_color_gray));
        quanbuLineView.setVisibility(View.INVISIBLE);
        quanbuLineLayout.setVisibility(View.INVISIBLE);
        shouruLineView.setVisibility(View.INVISIBLE);
        shouruLineLayout.setVisibility(View.INVISIBLE);
        zhichuLineView.setVisibility(View.INVISIBLE);
        zhichuLineLayout.setVisibility(View.INVISIBLE);
        switch (position) {
            case 0:
                quanbuTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                quanbuTextLayout.setTextColor(getResources().getColor(R.color.text_color_yellow));
                quanbuLineView.setVisibility(View.VISIBLE);
                quanbuLineLayout.setVisibility(View.VISIBLE);
                break;
            case 1:
                shouruTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                shouruTextLayout.setTextColor(getResources().getColor(R.color.text_color_yellow));
                shouruLineView.setVisibility(View.VISIBLE);
                shouruLineLayout.setVisibility(View.VISIBLE);
                break;
            case 2:
                zhichuTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                zhichuTextLayout.setTextColor(getResources().getColor(R.color.text_color_yellow));
                zhichuLineView.setVisibility(View.VISIBLE);
                zhichuLineLayout.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quanbuView:
                menuChange(0);
                break;
            case R.id.quanbuLayout:
                menuChange(0);
                break;
            case R.id.shouruView:
                menuChange(1);
                break;
            case R.id.shouruLayout:
                menuChange(1);
                break;
            case R.id.zhichuView:
                menuChange(2);
                break;
            case R.id.zhichuLayout:
                menuChange(2);
                break;
            case R.id.startDateTextView:
                startDateDialog.show();
                break;
            case R.id.endDateTextView:
                endDateDialog.show();
                System.out.println("start: "+startYear + " , " + startMonth + " , " + startDay);
                endDateDialog.setStartDate(startYear, startMonth, startDay,endYear,endMonth,endDay);
                break;
            case R.id.tixianTextView:
                Intent tixianIntent = new Intent(this, WithdrawActivity.class);
                startActivity(tixianIntent);
                break;
            case R.id.notificationImageView:
                notificationDialog.show();
                notificationDialog.setContent(purseResult.getData().getDesc());
                break;
            case R.id.backLayout:
                finish();
                break;
            case R.id.rightLayout:
                Intent bondAccountIntent = new Intent(MyPurseActivity.this, WithdrawSettingActivity.class);
                bondAccountIntent.putExtra("is_ali", purseResult.getData().isIs_ali());
                bondAccountIntent.putExtra("is_wechar", purseResult.getData().isIs_wechar());
                startActivityForResult(bondAccountIntent, 1031);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean is_ali = false;
        boolean is_wechar = false;
        if (requestCode == 1031) {
            if (null != data) {
                if (data.hasExtra("isAlipay")) {
                    is_ali = data.getBooleanExtra("isAlipay", false);
                }
                if (data.hasExtra("isWechat")) {
                    is_wechar = data.getBooleanExtra("isWechat", false);
                }
            }

            purseResult.getData().setIs_ali(is_ali);
            purseResult.getData().setIs_wechar(is_wechar);
        }
    }

    private int currentMenuPosition = 0;
    private PurseResult purseResult;

    //钱包详情
    private void purseInformation() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.MY_PURSE_INFORMATION);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                purseResult = GsonUtils.GsonToBean(result, PurseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private PurseRecordResult purseRecordResult;
    private int page = 1;
    private List<PurseRecordParent> allList = new ArrayList<>();
    private List<PurseRecordParent> inList = new ArrayList<>();
    private List<PurseRecordParent> outList = new ArrayList<>();
    private int allPage = 1, inPage = 1, outPage = 1;

    /**
     * 收入/支出 记录
     */
    private void purseList() {
        switch (currentMenuPosition) {
            case 0:
                page = allPage;
                break;
            case 1:
                page = inPage;
                break;
            case 2:
                page = outPage;
                break;
        }
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.MY_PURSE_RECORD_LIST);
        params.addBodyParameter("type", String.valueOf(currentMenuPosition));//1收入 2支出
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("start_time", "");
        params.addBodyParameter("end_time", "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("钱包记录: " + result);
                purseRecordResult = GsonUtils.GsonToBean(result, PurseRecordResult.class);
                if (page == 1) {
                    switch (currentMenuPosition) {
                        case 0:
                            allList = purseRecordResult.getData().getList();
                            break;
                        case 1:
                            inList = purseRecordResult.getData().getList();
                            break;
                        case 2:
                            outList = purseRecordResult.getData().getList();
                            break;
                    }
                } else {
                    switch (currentMenuPosition) {
                        case 0:
                            allList.addAll(purseRecordResult.getData().getList());
                            break;
                        case 1:
                            inList.addAll(purseRecordResult.getData().getList());
                            break;
                        case 2:
                            outList.addAll(purseRecordResult.getData().getList());
                            break;
                    }
                }
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("钱包记录: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private String formatTosepara(long money) {
        DecimalFormat df = new DecimalFormat("#,###.00");
        String money_str = df.format(money);
        if (money_str.equals(".00")) {
            money_str = "0.00";
        }
        return money_str;
    }


}
