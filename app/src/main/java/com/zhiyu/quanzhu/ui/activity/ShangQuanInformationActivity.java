package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.ShangQuan;
import com.zhiyu.quanzhu.ui.adapter.ShangQuanDongTaiRecyclerAdapter;
import com.zhiyu.quanzhu.ui.adapter.ShangQuanShangPinListAdapter;
import com.zhiyu.quanzhu.ui.adapter.ShangQuanTuiJianRecyclerViewAdapter;
import com.zhiyu.quanzhu.ui.dialog.ShangQuanApplyJoinDialog;
import com.zhiyu.quanzhu.ui.popupwindow.CircleInfoCirclerWindow;
import com.zhiyu.quanzhu.ui.popupwindow.CircleInfoUnjoinWindow;
import com.zhiyu.quanzhu.ui.popupwindow.CircleInfoJoinedWindow;
import com.zhiyu.quanzhu.ui.widget.MyScrollView;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 商圈详情
 */
public class ShangQuanInformationActivity extends BaseActivity implements MyScrollView.OnScrollListener ,View.OnClickListener{
    private MyScrollView mScrollView;
    private RecyclerView shangquanChengYuanRecyclerView;
    private ShangQuanTuiJianRecyclerViewAdapter shangQuanTuiJianRecyclerViewAdapter;
    private List<ShangQuan> shangQuanList = new ArrayList<>();
    private View menuRootLayout;
    private RecyclerView shangpinListView,shangquandongtaiRecyclerView;
    private ShangQuanShangPinListAdapter shangQuanShangPinListAdapter;
    private ShangQuanDongTaiRecyclerAdapter shangQuanDongTaiRecyclerAdapter;

    private LinearLayout backLayout,menuLayout;
    private TextView titleTextView;
    private TextView joinShangQuanTextView;

    private ShangQuanApplyJoinDialog shangQuanApplyJoinDialog;
    private long circle_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shang_quan_information);
        ScreentUtils.getInstance().setStatusBarLightMode(this,false);
        circle_id=getIntent().getLongExtra("circle_id",0l);
        initDialogs();
        initViews();

        initShangQuanTuiJian();
    }

    private void initViews() {
        backLayout=findViewById(R.id.backLayout);
        menuLayout=findViewById(R.id.menuLayout);
        titleTextView=findViewById(R.id.titleTextView);
        backLayout.setOnClickListener(this);
        menuLayout.setOnClickListener(this);
        titleTextView.setOnClickListener(this);
        joinShangQuanTextView=findViewById(R.id.joinShangQuanTextView);
        joinShangQuanTextView.setOnClickListener(this);

        shangpinListView=findViewById(R.id.shangpinListView);
        shangQuanShangPinListAdapter=new ShangQuanShangPinListAdapter();
        shangQuanShangPinListAdapter.setFragmentManager(this,getSupportFragmentManager());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        shangpinListView.setLayoutManager(mLayoutManager);
        shangpinListView.setAdapter(shangQuanShangPinListAdapter);
        shangpinListView.setHasFixedSize(true);
        shangpinListView.setNestedScrollingEnabled(false);

        shangquandongtaiRecyclerView=findViewById(R.id.shangquandongtaiRecyclerView);
        shangQuanDongTaiRecyclerAdapter=new ShangQuanDongTaiRecyclerAdapter();
        shangQuanDongTaiRecyclerAdapter.setContext(this);
        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(this);
        mLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        shangquandongtaiRecyclerView.setLayoutManager(mLayoutManager2);
        shangquandongtaiRecyclerView.setAdapter(shangQuanDongTaiRecyclerAdapter);
        shangquandongtaiRecyclerView.setHasFixedSize(true);
        shangquandongtaiRecyclerView.setNestedScrollingEnabled(false);

        menuRootLayout=findViewById(R.id.menuRootLayout);
        mScrollView = findViewById(R.id.mScrollView);
        mScrollView.setOnScrollListener(this);
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    private void initDialogs(){
        shangQuanApplyJoinDialog =new ShangQuanApplyJoinDialog(this,R.style.dialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backLayout:
                finish();
                break;
            case R.id.titleTextView:
                mScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mScrollView.fullScroll(ScrollView.FOCUS_UP);
                    }
                });
                break;
            case R.id.menuLayout:
                int member_shangquan_type=2;
                switch (member_shangquan_type){
                    case 0:
                        new CircleInfoUnjoinWindow(this).showAtBottom(menuLayout);
                        break;
                    case 1:
                        new CircleInfoJoinedWindow(this, new CircleInfoJoinedWindow.OnMenuSelectListener() {
                            @Override
                            public void onMenuSelect(int index, String menu) {

                            }
                        }).showAtBottom(menuLayout);
                        break;
                    case 2:
//                        new CircleInfoCirclerWindow(this).showAtBottom(menuLayout);
                        break;
                }
                break;
            case R.id.joinShangQuanTextView:
                shangQuanApplyJoinDialog.show();
                break;
        }
    }

    @Override
    public void onScroll(int scrollY) {
        if(scrollY>255){
            menuRootLayout.getBackground().setAlpha(255);
        }else{
            menuRootLayout.getBackground().mutate().setAlpha(scrollY);
        }
        Log.i("mScrollView", "scrollY: " + scrollY);
    }


    private void initShangQuanTuiJianDatas() {
        ShangQuan s1 = new ShangQuan();
        s1.setImage("https://c-ssl.duitang.com/uploads/item/201710/08/20171008151547_4h8mX.thumb.700_0.jpeg");
        s1.setName("商圈1");
        shangQuanList.add(s1);

        ShangQuan s2 = new ShangQuan();
        s2.setImage("https://c-ssl.duitang.com/uploads/item/201707/19/20170719211350_4PnBt.thumb.700_0.jpeg");
        s2.setName("商圈2");
        shangQuanList.add(s2);

        ShangQuan s3 = new ShangQuan();
        s3.setImage("https://c-ssl.duitang.com/uploads/item/201612/04/20161204000928_jfHPk.thumb.700_0.jpeg");
        s3.setName("商圈3");
        shangQuanList.add(s3);

        ShangQuan s4 = new ShangQuan();
        s4.setImage("https://c-ssl.duitang.com/uploads/item/201607/09/20160709202914_LYx4H.thumb.700_0.jpeg");
        s4.setName("商圈4");
        shangQuanList.add(s4);

        ShangQuan s5 = new ShangQuan();
        s5.setImage("https://c-ssl.duitang.com/uploads/item/201506/19/20150619182752_iTm5A.thumb.700_0.jpeg");
        s5.setName("商圈5");
        shangQuanList.add(s5);

        ShangQuan s6 = new ShangQuan();
        s6.setImage("https://c-ssl.duitang.com/uploads/item/201810/18/20181018164316_wddcq.thumb.700_0.jpg");
        s6.setName("商圈6");
        shangQuanList.add(s6);
    }

    private void initShangQuanTuiJian() {
        initShangQuanTuiJianDatas();
        shangquanChengYuanRecyclerView = findViewById(R.id.shangquanChengYuanRecyclerView);
        shangQuanTuiJianRecyclerViewAdapter = new ShangQuanTuiJianRecyclerViewAdapter(this);
        LinearLayoutManager ms = new LinearLayoutManager(this);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        shangquanChengYuanRecyclerView.setLayoutManager(ms);
        shangquanChengYuanRecyclerView.setAdapter(shangQuanTuiJianRecyclerViewAdapter);
        shangQuanTuiJianRecyclerViewAdapter.setData(shangQuanList);
    }
}
