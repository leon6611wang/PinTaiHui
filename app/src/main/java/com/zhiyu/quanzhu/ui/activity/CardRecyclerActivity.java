package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.adapter.card.CardAdapter;
import com.zhiyu.quanzhu.ui.adapter.card.CardBean;
import com.zhiyu.quanzhu.ui.adapter.card.CardHelperCallback;
import com.zhiyu.quanzhu.ui.adapter.card.CardManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardRecyclerActivity extends BaseActivity implements CardHelperCallback.OnItemTouchCallbackListener
        , CardAdapter.CardItemCallback {

    private RecyclerView recyclerView;
    private List<CardBean> mCardBeanList;
    private CardAdapter mCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_recyclerview);
        init();

    }


    //加载更多
    public void loadMore() {
        List<CardBean> cardBeenList = new ArrayList<>();

        CardBean bean0 = new CardBean();
        bean0.setName("1");
        bean0.setBallYear("球龄：11年");
        bean0.setTeam("球队：俄克拉荷马雷霆");
        bean0.setPic("https://c-ssl.duitang.com/uploads/item/201801/04/20180104034252_KFyWT.jpeg");

        CardBean bean1 = new CardBean();
        bean1.setName("2");
        bean1.setBallYear("球龄：20年");
        bean1.setTeam("球队：洛杉矶湖人");
        bean1.setPic("https://c-ssl.duitang.com/uploads/item/201802/07/20180207131800_WBEja.jpeg");

        CardBean bean2 = new CardBean();
        bean2.setName("3");
        bean2.setBallYear("球龄：15年");
        bean2.setTeam("球队：洛杉矶湖人");
        bean2.setPic("https://c-ssl.duitang.com/uploads/item/201803/14/20180314215342_szpnd.jpg");

        CardBean bean3 = new CardBean();
        bean3.setName("4");
        bean3.setBallYear("球龄：9年");
        bean3.setTeam("球队：俄克拉荷马雷霆");
        bean3.setPic("https://c-ssl.duitang.com/uploads/item/201903/13/20190313003146_CBack.jpeg");

        CardBean bean4 = new CardBean();
        bean4.setName("5");
        bean4.setBallYear("球龄：16年");
        bean4.setTeam("球队：芝加哥公牛");
        bean4.setPic("https://c-ssl.duitang.com/uploads/item/201512/02/20151202143926_RTjkK.jpeg");

        CardBean bean5 = new CardBean();
        bean5.setName("6");
        bean5.setBallYear("球龄：15年");
        bean5.setTeam("球队：迈阿密热火");
        bean5.setPic("https://c-ssl.duitang.com/uploads/item/201509/28/20150928205633_5uNjs.jpeg");

        CardBean bean6 = new CardBean();
        bean6.setName("7");
        bean6.setBallYear("球龄：21年");
        bean6.setTeam("球队：达拉斯小牛");
        bean6.setPic("https://c-ssl.duitang.com/uploads/item/201607/27/20160727215901_FEf3U.jpeg");

        CardBean bean7 = new CardBean();
        bean7.setName("8");
        bean7.setBallYear("球龄：7年");
        bean7.setTeam("球队：波特兰开拓者");
        bean7.setPic("https://c-ssl.duitang.com/uploads/item/201712/17/20171217231240_eLGUx.jpeg");

        CardBean bean8 = new CardBean();
        bean8.setName("9");
        bean8.setBallYear("球龄：10年");
        bean8.setTeam("球队：金州勇士");
        bean8.setPic("https://c-ssl.duitang.com/uploads/item/201802/23/20180223102258_jenzt.jpg");

        CardBean bean9 = new CardBean();
        bean9.setName("10");
        bean9.setBallYear("球龄：7年");
        bean9.setTeam("球队：波士顿凯尔特人");
        bean9.setPic("https://c-ssl.duitang.com/uploads/item/201811/15/20181115213128_lojkw.jpg");
        cardBeenList.add(bean0);
        cardBeenList.add(bean1);
        cardBeenList.add(bean2);
        cardBeenList.add(bean3);
        cardBeenList.add(bean4);
        cardBeenList.add(bean5);
        cardBeenList.add(bean6);
        cardBeenList.add(bean7);
        cardBeenList.add(bean8);
        cardBeenList.add(bean9);

        Collections.reverse(cardBeenList);
        mCardBeanList.addAll(0, cardBeenList);
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView);
        mCardBeanList = new ArrayList<>();
        loadMore();
        CardManager manager = new CardManager(this);
        recyclerView.setLayoutManager(manager);
        mCardAdapter = new CardAdapter(this, mCardBeanList);
        mCardAdapter.setCardItemCallback(this);
        recyclerView.setAdapter(mCardAdapter);
        initTouch();
    }

    public void initTouch() {
        CardHelperCallback itemTouchHelpCallback = new CardHelperCallback();
        itemTouchHelpCallback.setListener(this);
        ItemTouchHelper helper = new ItemTouchHelper(itemTouchHelpCallback);
        //将ItemTouchHelper和RecyclerView进行绑定
        helper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onSwiped(int position, int direction) {
        if (direction == CardManager.MAX_COUNT) {//左滑
            //Toast.makeText(this,"left",Toast.LENGTH_SHORT).show();
        } else {//右滑
            //Toast.makeText(this,"right",Toast.LENGTH_SHORT).show();
        }
        if (mCardBeanList != null) {
            for (int i = 0; i < recyclerView.getChildCount(); i++) {
                View view = recyclerView.getChildAt(i);
                view.setAlpha(1);
            }
            mCardBeanList.remove(position);
            //加载更多
            if (mCardBeanList.size() < CardManager.MAX_COUNT) {
                loadMore();
            }
            mCardAdapter.notifyDataSetChanged();
        }
    }

    //点击事件
    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, mCardBeanList.get(position).getName(), Toast.LENGTH_SHORT).show();
        Log.i("zs", mCardBeanList.get(position).getName());
    }


}
