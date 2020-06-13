package com.zhiyu.quanzhu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leon.chic.dao.CardDao;
import com.leon.chic.dao.MessageDao;
import com.leon.chic.utils.LogUtils;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.MyCardFriend;
import com.zhiyu.quanzhu.model.result.CardResult;
import com.zhiyu.quanzhu.ui.activity.MingPianGuangChangActivity;
import com.zhiyu.quanzhu.ui.activity.CardInformationActivity;
import com.zhiyu.quanzhu.ui.adapter.CardLetterListAdapter;
import com.zhiyu.quanzhu.ui.adapter.RenMaiRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.MyQRCodeDialog;
import com.zhiyu.quanzhu.ui.dialog.ShareDialog;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.SoftKeyboardUtil;
import com.zhiyu.quanzhu.utils.ThreadPoolUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class FragmentHomeRenMai extends Fragment implements View.OnClickListener {
    private View view;
    private MyRecyclerView mRecyclerView;
    private RenMaiRecyclerAdapter adapter;
    private View headerView, headerLayout;
    private ImageView sousuoImageView, guangchangImageView;
    private LinearLayout wallLayout2, searchLayout2;
    private EditText searchEditText2;
    private ImageView clearImageView2;
    private TextView cancelTextView2;
    private TextView qrCodeTextView, shareTextView;
    private ListView letterListView;
    private CardLetterListAdapter letterAdapter;
    private List<String> letterList = new ArrayList<>();
    private int totalDy;
    private TextView mingpianinfoTextView;
    private CircleImageView headerImageView;
    private TextView nameTextView, positionTextView, companyTextView, viewCountTextView, priseCountTextView, collectCountTextView;
    private MyQRCodeDialog qrCodeDialog;
    private ShareDialog shareDialog;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FragmentHomeRenMai> fragmentHomeRenMaiWeakReference;

        public MyHandler(FragmentHomeRenMai fragmentHomeRenMai) {
            fragmentHomeRenMaiWeakReference = new WeakReference<>(fragmentHomeRenMai);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentHomeRenMai fragment = fragmentHomeRenMaiWeakReference.get();
            switch (msg.what) {
                case 1:
                    if (null != fragment.cardResult && null != fragment.cardResult.getData() && null != fragment.cardResult.getData().getDetail()) {
                        Glide.with(fragment.getContext()).load(fragment.cardResult.getData().getDetail().getCard_thumb()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                                .fallback(R.drawable.image_error).into(fragment.headerImageView);
                        fragment.nameTextView.setText(fragment.cardResult.getData().getDetail().getCard_name());
                        fragment.positionTextView.setText(fragment.cardResult.getData().getDetail().getOccupation());
                        fragment.companyTextView.setText(fragment.cardResult.getData().getDetail().getCompany());
                        fragment.viewCountTextView.setText(String.valueOf(fragment.cardResult.getData().getDetail().getView()));
                        fragment.priseCountTextView.setText(String.valueOf(fragment.cardResult.getData().getDetail().getPrise()));
                        fragment.collectCountTextView.setText(String.valueOf(fragment.cardResult.getData().getDetail().getCollect()));
                    }

                    break;
                case 2:
                    fragment.initCardFriendDataView();
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_renmai, container, false);
        initDialogs();
        initData();
        initViews();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ThreadPoolUtils.getInstance().init().execute(new Runnable() {
            @Override
            public void run() {
                cardIndex();
            }
        });
        initCardData();
    }


    private void initDialogs() {
        qrCodeDialog = new MyQRCodeDialog(getContext(), R.style.dialog);
        shareDialog = new ShareDialog(getActivity(), getContext(), R.style.dialog, new ShareDialog.OnShareListener() {
            @Override
            public void onShare(int position, String desc) {

            }
        });
    }

    private ArrayList<MyCardFriend> cardList = new ArrayList<>();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mRecyclerView.smoothScrollToPosition(0);
            ThreadPoolUtils.getInstance().init().execute(new Runnable() {
                @Override
                public void run() {
                    cardIndex();
                }
            });
        }
        if (isVisibleToUser && (null == cardList || cardList.size() == 0)) {
            initCardData();
        }
    }


    private String cardFriend;

    private void initCardData() {
        cardFriend = CardDao.getInstance().cardList(BaseApplication.getInstance(), new MessageDao.OnCardMessageChangeListener() {
            @Override
            public void onCardMessageChange() {
                cardFriend = CardDao.getInstance().cardChangeList(BaseApplication.getInstance());
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }
        });
        Message message = myHandler.obtainMessage(2);
        message.sendToTarget();
    }

    private void initCardFriendDataView() {
        List<MyCardFriend> list = GsonUtils.getObjectList(cardFriend, MyCardFriend.class);
        if (list.size() == 0) {
            MyCardFriend friend = new MyCardFriend();
            friend.setEmpty(true);
            list.add(friend);
        }
        if (null != cardList && cardList.size() > 0) {
            cardList.clear();
            adapter.clearDatas();
        }
        cardList.addAll(list);
        adapter.addDatas(cardList);
    }

    private void initData() {
        letterList.add("a");
        letterList.add("b");
        letterList.add("c");
        letterList.add("d");
        letterList.add("e");
        letterList.add("f");
        letterList.add("g");
        letterList.add("h");
        letterList.add("i");
        letterList.add("j");
        letterList.add("k");
        letterList.add("l");
        letterList.add("m");
        letterList.add("n");
        letterList.add("o");
        letterList.add("p");
        letterList.add("q");
        letterList.add("r");
        letterList.add("s");
        letterList.add("t");
        letterList.add("u");
        letterList.add("v");
        letterList.add("w");
        letterList.add("x");
        letterList.add("y");
        letterList.add("z");

    }

    private void initViews() {
        headerView = LayoutInflater.from(getContext()).inflate(R.layout.header_renmai_recyclerview, null);
        qrCodeTextView = headerView.findViewById(R.id.qrCodeTextView);
        qrCodeTextView.setOnClickListener(this);
        shareTextView = headerView.findViewById(R.id.shareTextView);
        shareTextView.setOnClickListener(this);
        headerImageView = headerView.findViewById(R.id.headerImageView);
        nameTextView = headerView.findViewById(R.id.nameTextView);
        positionTextView = headerView.findViewById(R.id.positionTextView);
        companyTextView = headerView.findViewById(R.id.companyTextView);
        viewCountTextView = headerView.findViewById(R.id.viewCountTextView);
        priseCountTextView = headerView.findViewById(R.id.priseCountTextView);
        collectCountTextView = headerView.findViewById(R.id.collectCountTextView);
        mingpianinfoTextView = headerView.findViewById(R.id.mingpianinfoTextView);
        mingpianinfoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mymingpianIntent = new Intent(getActivity(), CardInformationActivity.class);
                mymingpianIntent.putExtra("card_id", cardResult.getData().getDetail().getId());
                mymingpianIntent.putExtra("uid", cardResult.getData().getDetail().getUid());
                startActivity(mymingpianIntent);
            }
        });
        wallLayout2 = view.findViewById(R.id.wallLayout);
        searchLayout2 = view.findViewById(R.id.searchLayout);
        sousuoImageView = view.findViewById(R.id.sousuoImageView);
        sousuoImageView.setOnClickListener(this);
        guangchangImageView = view.findViewById(R.id.guangchangImageView);
        guangchangImageView.setOnClickListener(this);
        searchEditText2 = view.findViewById(R.id.searchEditText2);
        searchEditText2.setOnEditorActionListener(new OnEditAction());
        clearImageView2 = view.findViewById(R.id.clearImageView);
        clearImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSearchText();
            }
        });
        cancelTextView2 = view.findViewById(R.id.cancelTextView);
        cancelTextView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchChange();
            }
        });
        headerLayout = view.findViewById(R.id.headerLayout);
        headerLayout.getBackground().setAlpha(0);
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        adapter = new RenMaiRecyclerAdapter(getContext());
        adapter.setHeaderView(headerView);
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

        letterListView = view.findViewById(R.id.letterListView);
        letterAdapter = new CardLetterListAdapter();
        letterListView.setAdapter(letterAdapter);
        letterAdapter.setLetterList(letterList);
        letterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String letter = letterList.get(position);
                if (null != cardList && cardList.size() > 0) {
                    int index = getCardListIndex(letter);
                    if (index > -1) {
                        mRecyclerView.smoothScrollToPosition(index + 1);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qrCodeTextView:
                qrCodeDialog.show();
                qrCodeDialog.setCard(cardResult.getData().getDetail());
                break;
            case R.id.shareTextView:
                shareDialog.show();
                break;
            case R.id.guangchangImageView:
                Intent intent = new Intent(getContext(), MingPianGuangChangActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                break;
            case R.id.sousuoImageView:
                searchChange();
                break;
        }
    }

    private int getCardListIndex(String letter) {
        int index = -1;
        for (int i = 0; i < cardList.size(); i++) {
            if (cardList.get(i).getLetter().equals(letter)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void headerLayoutChange() {
        int y = Math.abs(totalDy);
        if (y > 0) {
            if (y > 300) {
                y = 300;
            }
            float alpha = (float) y / (float) 300;
            int a = Math.round(alpha * 255);
            headerLayout.getBackground().setAlpha(a);
        } else {
            headerLayout.getBackground().setAlpha(0);
        }
    }

    private boolean isSearchStatus;

    private void searchChange() {
        isSearchStatus = !isSearchStatus;
        if (isSearchStatus) {
            wallLayout2.setVisibility(View.GONE);
            searchLayout2.setVisibility(View.VISIBLE);
        } else {
            wallLayout2.setVisibility(View.VISIBLE);
            searchLayout2.setVisibility(View.GONE);
        }
    }

    private void clearSearchText() {
        searchEditText2.setText(null);
    }

    private String search = null;

    private class OnEditAction implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                SoftKeyboardUtil.hideSoftKeyBoard(getActivity().getWindow());
                search = searchEditText2.getText().toString().trim();
                searchCardData();
                return true;
            }
            return false;
        }
    }

    private void searchCardData() {
        if (!StringUtils.isNullOrEmpty(search)) {
            cardFriend = CardDao.getInstance().searchCardListSQL(BaseApplication.getInstance(), search);
            Message message = myHandler.obtainMessage(2);
            message.sendToTarget();
        } else {
            initCardData();
        }

    }

    private CardResult cardResult;

    private void cardIndex() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CARD_INDEX);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("名片首页:" + result);
                cardResult = GsonUtils.GsonToBean(result, CardResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("名片首页:" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shareDialog.setQQShareCallback(requestCode, resultCode, data);
    }
}
