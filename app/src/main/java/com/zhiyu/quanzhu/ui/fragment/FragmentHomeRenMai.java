package com.zhiyu.quanzhu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.result.CardResult;
import com.zhiyu.quanzhu.ui.activity.MingPianGuangChangActivity;
import com.zhiyu.quanzhu.ui.activity.MyCardInformationActivity;
import com.zhiyu.quanzhu.ui.adapter.RenMaiRecyclerAdapter;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class FragmentHomeRenMai extends Fragment {
    private View view;
    private MyRecyclerView mRecyclerView;
    private RenMaiRecyclerAdapter adapter;
    private View headerView, headerLayout, headerlayoutView;
    private ArrayList<Integer> list = new ArrayList<>();
    private ImageView sousuoImageView, guangchangImageView, sousuoImageLayout, guangchangImageLayout;
    private int totalDy;
    private TextView mingpianinfoTextView;
    private CircleImageView headerImageView;
    private TextView nameTextView,positionTextView,companyTextView,viewCountTextView,priseCountTextView,collectCountTextView;
    private MyHandler myHandler=new MyHandler(this);
    private static class MyHandler extends Handler{
        WeakReference<FragmentHomeRenMai> fragmentHomeRenMaiWeakReference;
        public MyHandler(FragmentHomeRenMai fragmentHomeRenMai){
            fragmentHomeRenMaiWeakReference=new WeakReference<>(fragmentHomeRenMai);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentHomeRenMai fragment=fragmentHomeRenMaiWeakReference.get();
            switch (msg.what){
                case 1:
                    if(null!=fragment.cardResult.getData().getDetail()){
                        Glide.with(fragment.getContext()).load(fragment.cardResult.getData().getDetail().getCard_thumb()).into(fragment.headerImageView);
                        fragment.nameTextView.setText(fragment.cardResult.getData().getDetail().getCard_name());
                        fragment.positionTextView.setText(fragment.cardResult.getData().getDetail().getOccupation());
                        fragment.companyTextView.setText(fragment.cardResult.getData().getDetail().getCompany());
                        fragment.viewCountTextView.setText(String.valueOf(fragment.cardResult.getData().getDetail().getView()));
                        fragment.priseCountTextView.setText(String.valueOf(fragment.cardResult.getData().getDetail().getPrise()));
                        fragment.collectCountTextView.setText(String.valueOf(fragment.cardResult.getData().getDetail().getCollect()));
                    }

                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_renmai, container, false);
        initDatas();
        initViews();
        cardIndex();
        cardFrends();
        return view;
    }

    private void initDatas() {
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
    }

    private void initViews() {
        headerView = LayoutInflater.from(getContext()).inflate(R.layout.header_renmai_recyclerview, null);
        headerImageView=headerView.findViewById(R.id.headerImageView);
        nameTextView=headerView.findViewById(R.id.nameTextView);
        positionTextView=headerView.findViewById(R.id.positionTextView);
        companyTextView=headerView.findViewById(R.id.companyTextView);
        viewCountTextView=headerView.findViewById(R.id.viewCountTextView);
        priseCountTextView=headerView.findViewById(R.id.priseCountTextView);
        collectCountTextView=headerView.findViewById(R.id.collectCountTextView);

        mingpianinfoTextView = headerView.findViewById(R.id.mingpianinfoTextView);
        mingpianinfoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mymingpianIntent = new Intent(getActivity(), MyCardInformationActivity.class);
                mymingpianIntent.putExtra("card_id",cardResult.getData().getDetail().getId());
                mymingpianIntent.putExtra("uid",cardResult.getData().getDetail().getUid());
                startActivity(mymingpianIntent);
            }
        });
        headerlayoutView = headerView.findViewById(R.id.headerlayoutView);
        sousuoImageView = headerView.findViewById(R.id.sousuoImageView);
        sousuoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        guangchangImageView = headerView.findViewById(R.id.guangchangImageView);
        guangchangImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guangchangIntent = new Intent(getActivity(), MingPianGuangChangActivity.class);
                startActivity(guangchangIntent);
            }
        });
        sousuoImageLayout = view.findViewById(R.id.sousuoImageView);
        sousuoImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        guangchangImageLayout = view.findViewById(R.id.guangchangImageView);
        guangchangImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guangchangIntent = new Intent(getActivity(), MingPianGuangChangActivity.class);
                startActivity(guangchangIntent);
            }
        });
        headerLayout = view.findViewById(R.id.headerLayout);
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        adapter = new RenMaiRecyclerAdapter(getContext());
        adapter.setHeaderView(headerView);
        adapter.addDatas(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                totalDy -= dy;
                headerLayoutChange();
            }
        });
    }


    private void headerLayoutChange() {
        if (Math.abs(totalDy) > 0) {
            headerlayoutView.setVisibility(View.INVISIBLE);
            headerLayout.setVisibility(View.VISIBLE);
            headerLayout.setBackgroundColor(getResources().getColor(R.color.text_color_yellow));
            float alpha = (float) Math.abs(totalDy) / (float) 300;
            headerLayout.setAlpha(alpha);
        } else {
            headerlayoutView.setVisibility(View.VISIBLE);
            headerLayout.setVisibility(View.INVISIBLE);
            headerLayout.setAlpha(0);
        }
    }

    private CardResult cardResult;

    private void cardIndex() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CARD_INDEX);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                cardResult = GsonUtils.GsonToBean(result, CardResult.class);
                Message message=myHandler.obtainMessage(1);
                message.sendToTarget();
                System.out.println("名片首页:" +result);
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

    private void cardFrends(){
        RequestParams params=MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL+ConstantsUtils.CARD_USER_LIST);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("名片好友:"+result);
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
}
