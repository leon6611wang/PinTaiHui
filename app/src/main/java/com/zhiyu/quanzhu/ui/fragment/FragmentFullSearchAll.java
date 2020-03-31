package com.zhiyu.quanzhu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.FullSearchAll;
import com.zhiyu.quanzhu.model.result.FullSearchAllResult;
import com.zhiyu.quanzhu.ui.activity.CircleInfoActivity;
import com.zhiyu.quanzhu.ui.adapter.FullSearchAllCircleListAdapter;
import com.zhiyu.quanzhu.ui.adapter.FullSearchArticleListAdapter;
import com.zhiyu.quanzhu.ui.adapter.FullSearchCardListAdapter;
import com.zhiyu.quanzhu.ui.adapter.FullSearchCircleListAdapter;
import com.zhiyu.quanzhu.ui.adapter.FullSearchFeedListAdapter;
import com.zhiyu.quanzhu.ui.adapter.FullSearchGoodsGridAdapter;
import com.zhiyu.quanzhu.ui.adapter.FullSearchShopListAdapter;
import com.zhiyu.quanzhu.ui.adapter.FullSearchVideoListAdapter;
import com.zhiyu.quanzhu.ui.widget.HorizontalListView;
import com.zhiyu.quanzhu.ui.widget.MyGridView;
import com.zhiyu.quanzhu.ui.widget.MyListView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

public class FragmentFullSearchAll extends Fragment implements View.OnClickListener {
    private View view;
    private HorizontalListView circleListView;
    private MyListView cardListView, articleListView, feedListView, videoListView, shopListView;
    private MyGridView goodsGridView;
    private FullSearchAllCircleListAdapter circleListAdapter;
    private FullSearchCardListAdapter cardListAdapter;
    private FullSearchArticleListAdapter articleListAdapter;
    private FullSearchFeedListAdapter feedListAdapter;
    private FullSearchVideoListAdapter videoListAdapter;
    private FullSearchGoodsGridAdapter goodsGridAdapter;
    private FullSearchShopListAdapter shopListAdapter;
    private TextView moreCircleTextView, moreCardTextView, moreArticleTextView, moreFeedTextView, moreVideoTextView, moreShopTextView, moreGoodsTextView;
    private CardView circleCardView, cardCardView, articleCardView, feedCardView, videoCardView, goodsCardView, shopCardView;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FragmentFullSearchAll> fullSearchAllWeakReference;

        public MyHandler(FragmentFullSearchAll fragment) {
            fullSearchAllWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentFullSearchAll fragment = fullSearchAllWeakReference.get();
            switch (msg.what) {
                case 1:
                    if (null != fragment.allResult.getData() && null != fragment.allResult.getData().getCircle_list() && fragment.allResult.getData().getCircle_list().size() > 0) {
                        fragment.circleCardView.setVisibility(View.VISIBLE);
                        fragment.circleListAdapter.setList(fragment.allResult.getData().getCircle_list());
                    } else {
                        fragment.circleCardView.setVisibility(View.GONE);
                    }

                    if (null != fragment.allResult.getData() && null != fragment.allResult.getData().getCards_list() && fragment.allResult.getData().getCards_list().size() > 0) {
                        fragment.cardCardView.setVisibility(View.VISIBLE);
                        fragment.cardListAdapter.setList(fragment.allResult.getData().getCards_list());
                    } else {
                        fragment.cardCardView.setVisibility(View.GONE);
                    }
                    if (null != fragment.allResult.getData() && null != fragment.allResult.getData().getArticle_list() && fragment.allResult.getData().getArticle_list().size() > 0) {
                        fragment.articleCardView.setVisibility(View.VISIBLE);
                        fragment.articleListAdapter.setList(fragment.allResult.getData().getArticle_list());
                    } else {
                        fragment.articleCardView.setVisibility(View.GONE);
                    }
                    if (null != fragment.allResult.getData() && null != fragment.allResult.getData().getFeeds_list() && fragment.allResult.getData().getFeeds_list().size() > 0) {
                        fragment.feedCardView.setVisibility(View.VISIBLE);
                        fragment.feedListAdapter.setList(fragment.allResult.getData().getFeeds_list());
                    } else {
                        fragment.feedCardView.setVisibility(View.GONE);
                    }
                    if (null != fragment.allResult.getData() && null != fragment.allResult.getData().getVideo_list() && fragment.allResult.getData().getVideo_list().size() > 0) {
                        fragment.videoCardView.setVisibility(View.VISIBLE);
                        fragment.videoListAdapter.setList(fragment.allResult.getData().getVideo_list());
                    } else {
                        fragment.videoCardView.setVisibility(View.GONE);
                    }
                    if (null != fragment.allResult.getData() && null != fragment.allResult.getData().getGoods_list() && fragment.allResult.getData().getGoods_list().size() > 0) {
                        fragment.goodsCardView.setVisibility(View.VISIBLE);
                        fragment.goodsGridAdapter.setList(fragment.allResult.getData().getGoods_list());
                    } else {
                        fragment.goodsCardView.setVisibility(View.GONE);
                    }
                    if (null != fragment.allResult.getData() && null != fragment.allResult.getData().getShop_list() && fragment.allResult.getData().getShop_list().size() > 0) {
                        fragment.shopCardView.setVisibility(View.VISIBLE);
                        fragment.shopListAdapter.setList(fragment.allResult.getData().getShop_list());
                    } else {
                        fragment.shopCardView.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_full_search_all, null);
        initViews();
        return view;
    }

    private void initViews() {
        circleCardView = view.findViewById(R.id.circleCardView);
        circleCardView.setVisibility(View.GONE);
        cardCardView = view.findViewById(R.id.cardCardView);
        cardCardView.setVisibility(View.GONE);
        articleCardView = view.findViewById(R.id.articleCardView);
        articleCardView.setVisibility(View.GONE);
        feedCardView = view.findViewById(R.id.feedCardView);
        feedCardView.setVisibility(View.GONE);
        videoCardView = view.findViewById(R.id.videoCardView);
        videoCardView.setVisibility(View.GONE);
        goodsCardView = view.findViewById(R.id.goodsCardView);
        goodsCardView.setVisibility(View.GONE);
        shopCardView = view.findViewById(R.id.shopCardView);
        shopCardView.setVisibility(View.GONE);
        circleListView = view.findViewById(R.id.circleListView);
        cardListView = view.findViewById(R.id.cardListView);
        articleListView = view.findViewById(R.id.articleListView);
        feedListView = view.findViewById(R.id.feedListView);
        videoListView = view.findViewById(R.id.videoListView);
        goodsGridView = view.findViewById(R.id.goodsGridView);
        shopListView = view.findViewById(R.id.shopListView);
        circleListAdapter = new FullSearchAllCircleListAdapter();
        circleListView.setAdapter(circleListAdapter);
        cardListAdapter = new FullSearchCardListAdapter();
        cardListView.setAdapter(cardListAdapter);
        articleListAdapter = new FullSearchArticleListAdapter(getActivity(), getContext());
        articleListView.setAdapter(articleListAdapter);
        feedListAdapter = new FullSearchFeedListAdapter(getActivity(), getContext());
        feedListView.setAdapter(feedListAdapter);
        videoListAdapter = new FullSearchVideoListAdapter(getActivity(), getContext());
        videoListView.setAdapter(videoListAdapter);
        goodsGridAdapter = new FullSearchGoodsGridAdapter(getContext());
        goodsGridView.setAdapter(goodsGridAdapter);
        shopListAdapter = new FullSearchShopListAdapter(getContext());
        shopListView.setAdapter(shopListAdapter);
        moreCircleTextView = view.findViewById(R.id.moreCircleTextView);
        moreCircleTextView.setOnClickListener(this);
        moreCardTextView = view.findViewById(R.id.moreCardTextView);
        moreCardTextView.setOnClickListener(this);
        moreArticleTextView = view.findViewById(R.id.moreArticleTextView);
        moreArticleTextView.setOnClickListener(this);
        moreFeedTextView = view.findViewById(R.id.moreFeedTextView);
        moreFeedTextView.setOnClickListener(this);
        moreVideoTextView = view.findViewById(R.id.moreVideoTextView);
        moreVideoTextView.setOnClickListener(this);
        moreShopTextView = view.findViewById(R.id.moreShopTextView);
        moreShopTextView.setOnClickListener(this);
        moreGoodsTextView = view.findViewById(R.id.moreGoodsTextView);
        moreGoodsTextView.setOnClickListener(this);
        circleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent circleInfoIntent = new Intent(getContext(), CircleInfoActivity.class);
                circleInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                circleInfoIntent.putExtra("circle_id",allResult.getData().getCircle_list().get(position).getId());
                getContext().startActivity(circleInfoIntent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.moreCircleTextView:
                if (null != onSelectPageListener) {
                    onSelectPageListener.onSelectPage(1);
                }
                break;
            case R.id.moreCardTextView:
                if (null != onSelectPageListener) {
                    onSelectPageListener.onSelectPage(2);
                }
                break;
            case R.id.moreArticleTextView:
                if (null != onSelectPageListener) {
                    onSelectPageListener.onSelectPage(3);
                }
                break;
            case R.id.moreFeedTextView:
                if (null != onSelectPageListener) {
                    onSelectPageListener.onSelectPage(4);
                }
                break;
            case R.id.moreVideoTextView:
                if (null != onSelectPageListener) {
                    onSelectPageListener.onSelectPage(5);
                }
                break;
            case R.id.moreGoodsTextView:
                if (null != onSelectPageListener) {
                    onSelectPageListener.onSelectPage(6);
                }
                break;
            case R.id.moreShopTextView:
                if (null != onSelectPageListener) {
                    onSelectPageListener.onSelectPage(7);
                }
                break;

        }
    }

    private String searchContext;
    private boolean isRequested = false;

    public void setSearchContext(String search) {
        this.searchContext = search;
        isRequested = false;
        if (isUserVisible) {
            search();
        }
    }

    private boolean isUserVisible = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isUserVisible = isVisibleToUser;
        if (isVisibleToUser && !isRequested) {
            search();
        }
    }

    private OnSelectPageListener onSelectPageListener;

    public void setOnSelectPageListener(OnSelectPageListener listener) {
        this.onSelectPageListener = listener;
    }

    public interface OnSelectPageListener {
        void onSelectPage(int position);
    }

    //0全部 1圈子 2名片 3动态 4商品 5店铺
    private final String TYPE = "0";
    private int page = 1;
    private FullSearchAllResult allResult;

    private void search() {
        if (!StringUtils.isNullOrEmpty(searchContext)) {
            isRequested = true;
            RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FULL_SEARCH);
            params.addBodyParameter("page", String.valueOf(page));
            params.addBodyParameter("type", TYPE);
            params.addBodyParameter("keywords", searchContext);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    allResult = GsonUtils.GsonToBean(result, FullSearchAllResult.class);
                    Message message = myHandler.obtainMessage(1);
                    message.sendToTarget();
//                    String tag = "fullsearchall";
//                    Log.i(tag,"circle: "+allResult.getData().getCircle_list().size()+" , card: "+allResult.getData().getCards_list().size()+" , article: "+
//                    allResult.getData().getArticle_list().size()+" , feed: "+allResult.getData().getFeeds_list().size()+" , video: "+allResult.getData().getVideo_list().size()+" , goods: "+
//                    allResult.getData().getVideo_list().size()+" , shop: "+allResult.getData().getShop_list().size());
//                    if (result.length() > 4000) {
//                        for (int i = 0; i < result.length(); i += 4000) {
//                            //当前截取的长度<总长度则继续截取最大的长度来打印
//                            if (i + 4000 < result.length()) {
//                                Log.i(tag, result.substring(i, i + 4000));
//                            } else {
//                                //当前截取的长度已经超过了总长度，则打印出剩下的全部信息
//                                Log.i(tag, result.substring(i, result.length()));
//                            }
//                        }
//                    } else {
//                        //直接打印
//                        Log.i(tag, result);
//                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    System.out.println("full search all: " + ex.toString());
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
}
