package com.zhiyu.quanzhu.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.Tag;
import com.zhiyu.quanzhu.model.dao.TagDao;
import com.zhiyu.quanzhu.model.result.AddTagResult;
import com.zhiyu.quanzhu.model.result.TagResult;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SoftKeyboardUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 添加标签
 */
public class AddTagDialog extends Dialog implements View.OnClickListener {
    private TextView backTextView, confirmTextView, cancelTextView, searchTextView;
    private EditText searchEditText;
    private LinearLayout searchTextLayout, searchEditLayout, selectTagLayout, addTagLayout, craeteTagRootLayout;
    private TagFlowLayout selectedLayout, historyLayout, hotLayout, createLayout, moreLayout;
    private ImageView deleteHistoryImageView;
    private Activity activity;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<AddTagDialog> dialogWeakReference;

        public MyHandler(AddTagDialog dialog) {
            dialogWeakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            AddTagDialog dialog = dialogWeakReference.get();
            switch (msg.what) {
                case 1:
                    int isHot = (Integer) msg.obj;
                    switch (isHot) {
                        case 0:
                            dialog.createMoreTagViews();
                            break;
                        case 1:
                            dialog.createHotTagViews();
                            dialog.createHistoryTagViews();
                            break;
                    }
                    break;
                case 2:

                    break;
                case 3:

                    break;
            }
        }
    }

    public AddTagDialog(Activity aty, Context context, int themeResId, OnTagsSelectedListener listener) {
        super(context, themeResId);
        this.activity = aty;
        this.onTagsSelectedListener = listener;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_tag);
        initViews();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
        params.height = ScreentUtils.getInstance().getScreenHeight(getContext()) / 5 * 3;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        createSelectedTagViews();
//        createHistoryTagViews();
//        createHotTagViews();
//        createAddTagViews();
//        createMoreTagViews();
        historyTagList = TagDao.getInstance().tagList();

        tagList(0);
        tagList(1);

    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (null != onTagsSelectedListener) {
            onTagsSelectedListener.onTagsSelected(selectedTagList);
        }
    }

    private void initViews() {
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
        backTextView = findViewById(R.id.backTextView);
        backTextView.setOnClickListener(this);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
        searchTextLayout = findViewById(R.id.searchTextLayout);
        searchEditLayout = findViewById(R.id.searchEditLayout);
        selectTagLayout = findViewById(R.id.selectTagLayout);
        addTagLayout = findViewById(R.id.addTagLayout);
        searchTextView = findViewById(R.id.searchTextView);
        searchTextView.setOnClickListener(this);
        searchEditText = findViewById(R.id.searchEditText);
        selectedLayout = findViewById(R.id.selectedLayout);
        deleteHistoryImageView = findViewById(R.id.deleteHistoryImageView);
        deleteHistoryImageView.setOnClickListener(this);
        historyLayout = findViewById(R.id.historyLayout);
        hotLayout = findViewById(R.id.hotLayout);
        createLayout = findViewById(R.id.createLayout);
        moreLayout = findViewById(R.id.moreLayout);
        craeteTagRootLayout = findViewById(R.id.craeteTagRootLayout);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //关闭软键盘
                    SoftKeyboardUtil.hideSoftKeyboard(activity);
                    tag_name = searchEditText.getText().toString().trim();
                    searchTag();
                    return true;
                }
                return false;
            }
        });

        contentChange(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmTextView:
                dismiss();
                break;
            case R.id.cancelTextView:
                dismiss();
                break;
            case R.id.backTextView:
                contentChange(0);
                break;
            case R.id.searchTextView:
                contentChange(1);
                break;
            case R.id.deleteHistoryImageView:
                hotTagList.addAll(historyTagList);
                TagDao.getInstance().clearTags();
                historyTagList.clear();
                createHistoryTagViews();
                createHotTagViews();
                Set<Integer> selectedSet = new HashSet<>();
                for (Tag tag : selectedTagList) {
                    for (int i = 0; i < hotTagList.size(); i++) {
                        if (tag.getId() == hotTagList.get(i).getId()) {
                            selectedSet.add(i);
                        }
                    }
                }
                hotAdapter.setSelectedList(selectedSet);
                break;
        }
    }

    private void contentChange(int index) {
        searchTextLayout.setVisibility(View.GONE);
        searchEditLayout.setVisibility(View.GONE);
        selectTagLayout.setVisibility(View.GONE);
        addTagLayout.setVisibility(View.GONE);
        switch (index) {
            case 0:
                searchTextLayout.setVisibility(View.VISIBLE);
                selectTagLayout.setVisibility(View.VISIBLE);
                break;
            case 1:
                searchEditLayout.setVisibility(View.VISIBLE);
                addTagLayout.setVisibility(View.VISIBLE);
                searchEditText.setFocusable(true);
                break;
        }
    }


    private String tag_name;
    private int page = 1;
    private TagResult tagResult;

    /**
     * 热门标签列表
     */
    private void tagList(final int is_hot) {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SEARCH_TAGS);
        params.addBodyParameter("tag_name", tag_name);
        params.addBodyParameter("is_hot", String.valueOf(is_hot));
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("tagList: " + result);
                tagResult = GsonUtils.GsonToBean(result, TagResult.class);
                switch (is_hot) {
                    case 0:
                        moreTagList = tagResult.getData();
                        break;
                    case 1:
                        hotTagList = tagResult.getData();
                        List<Tag> list = new ArrayList<>();
                        if (null != hotTagList && null != historyTagList) {
                            for (int i = 0; i < hotTagList.size(); i++) {
                                Tag hotTag = hotTagList.get(i);
                                for (int j = 0; j < historyTagList.size(); j++) {
                                    Tag historyTag = historyTagList.get(j);
                                    if (hotTag.getId() == historyTag.getId()) {
                                        list.add(hotTag);
                                    }
                                }
                            }

                            for (Tag tag : list)
                                hotTagList.remove(tag);
                        }
                        break;
                }
                Message message = myHandler.obtainMessage(1);
                message.obj = is_hot;
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

    private TagResult tagResult2;

    private void searchTag() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SEARCH_TAG);
        params.addBodyParameter("tag_name", tag_name);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                tagResult2 = GsonUtils.GsonToBean(result, TagResult.class);
                Message message = myHandler.obtainMessage(2);
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

    private AddTagResult addTagResult;

    /**
     * 添加标签
     */
    private void addTag() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ADD_TAG);
        params.addBodyParameter("tag_name", tag_name);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("addTag: " + result);
                addTagResult = GsonUtils.GsonToBean(result, AddTagResult.class);
                Message message = myHandler.obtainMessage(3);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("addTag error: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private TagAdapter hotAdapter;
    private List<Tag> hotTagList;
    private List<Integer> selectedList;

    /**
     * 创建热门标签
     */
    private void createHotTagViews() {
        hotAdapter = new TagAdapter<Tag>(hotTagList) {
            @Override
            public View getView(FlowLayout parent, int position, Tag tag) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
                TextView tagTextView = view.findViewById(R.id.labelTextView);
                tagTextView.setText("#" + tag.getName());
                return tagTextView;
            }
        };
        hotLayout.setMaxSelectCount(5);
        hotLayout.setAdapter(hotAdapter);
        hotLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                boolean clickable = false;
                hotTagList.get(position).setSelected(!hotTagList.get(position).isSelected());
                if (!hotTagList.get(position).isSelected()&&selectedTagList.size()>0) {
                    clickable = true;
                    selectedTagList.remove(hotTagList.get(position));
                    createSelectedTagViews();
                } else {
                    if (selectedTagList.size() < 5) {

                        if (null != selectedTagList && selectedTagList.size() > 0) {
                            if (hotTagList.get(position).isSelected()) {
                                selectedTagList.add(hotTagList.get(position));
                            } else {
                                selectedTagList.remove(hotTagList.get(position));
                            }
                        } else {
                            selectedTagList.add(hotTagList.get(position));
                        }
                        createSelectedTagViews();

                        if (hotTagList.get(position).isSelected()) {
                            TagDao.getInstance().saveTag(hotTagList.get(position));
                            historyTagList.add(hotTagList.get(position));
                            if (null != historyTagList && historyTagList.size() > 0) {
                                boolean has = false;
                                for (Tag tag : historyTagList) {
                                    if (tag.getId() == hotTagList.get(position).getId()) {
                                        has = true;
                                    }
                                }
                                if (!has) {
                                    historyTagList.add(hotTagList.get(position));
                                }
                            } else {
                                historyTagList.add(hotTagList.get(position));
                            }
                            createHistoryTagViews();
                            Set<Integer> selectedSet = new HashSet<>();
                            for (Tag tag : selectedTagList) {
                                for (int i = 0; i < historyTagList.size(); i++) {
                                    if (tag.getId() == historyTagList.get(i).getId()) {
                                        selectedSet.add(i);
                                    }
                                }
                            }
                            historyAdapter.setSelectedList(selectedSet);

                        }

                        System.out.println("position: " + position);
                        if (hotTagList.get(position).isSelected()) {
                            hotTagList.remove(position);
                            createHotTagViews();
                        }

                        clickable = false;
                    } else {
                        clickable = true;
                        Toast.makeText(getContext(), "最多可选五个标签", Toast.LENGTH_SHORT).show();
                        Set<Integer> selectedSet = new HashSet<>();
                        for (Tag tag : selectedTagList) {
                            for (int i = 0; i < hotTagList.size(); i++) {
                                if (tag.getId() == hotTagList.get(i).getId()) {
                                    selectedSet.add(i);
                                }
                            }
                        }
                        hotAdapter.setSelectedList(selectedSet);
                    }

                }

                return clickable;
            }
        });
    }


    private TagAdapter historyAdapter;
    private List<Tag> historyTagList = new ArrayList<>();

    /**
     * 创建历史标签
     */
    private void createHistoryTagViews() {
        historyAdapter = new TagAdapter<Tag>(historyTagList) {
            @Override
            public View getView(FlowLayout parent, int position, Tag tag) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
                TextView tagTextView = view.findViewById(R.id.labelTextView);
                tagTextView.setText("#" + tag.getName());
                return tagTextView;
            }
        };
        historyLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                historyTagList.get(position).setSelected(!historyTagList.get(position).isSelected());
                boolean clickable;
                if (!historyTagList.get(position).isSelected()&&selectedTagList.size()>0) {
                    clickable = true;
                    selectedTagList.remove(historyTagList.get(position));
                    createSelectedTagViews();
                } else {
                    if (selectedTagList.size() < 5 && historyTagList.get(position).isSelected()) {
                        if (null != selectedTagList && selectedTagList.size() > 0) {
                            if (historyTagList.get(position).isSelected()) {
                                selectedTagList.add(historyTagList.get(position));
                            } else {
                                selectedTagList.remove(historyTagList.get(position));
                            }
                        } else {
                            selectedTagList.add(historyTagList.get(position));
                        }
                        createSelectedTagViews();


                        clickable = false;
                    } else {
                        Toast.makeText(getContext(), "最多可选五个标签", Toast.LENGTH_SHORT).show();
                        Set<Integer> selectedSet = new HashSet<>();
                        for (Tag tag : selectedTagList) {
                            for (int i = 0; i < historyTagList.size(); i++) {
                                if (tag.getId() == historyTagList.get(i).getId()) {
                                    selectedSet.add(i);
                                }
                            }
                        }
                        historyAdapter.setSelectedList(selectedSet);
                        clickable = true;
                    }

                }


                return clickable;
            }
        });
        historyLayout.setAdapter(historyAdapter);
    }

    private TagAdapter moreAdapter;
    private List<Tag> moreTagList;

    /**
     * 创建更多标签
     */
    private void createMoreTagViews() {
        moreAdapter = new TagAdapter<Tag>(moreTagList) {
            @Override
            public View getView(FlowLayout parent, int position, Tag tag) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
                TextView tagTextView = view.findViewById(R.id.labelTextView);
                tagTextView.setText("#" + tag.getName());
                return tagTextView;
            }
        };
        moreLayout.setAdapter(moreAdapter);
        moreLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
//                selectedList = new ArrayList<>(selectPosSet);
//                createSelectedTagViews();
            }
        });
    }


    private TagAdapter addAdapter;
    private List<Tag> addTagList;

    /**
     * 创建新建标签
     */
    private void createAddTagViews() {
        addAdapter = new TagAdapter<Tag>(addTagList) {
            @Override
            public View getView(FlowLayout parent, int position, Tag tag) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
                TextView tagTextView = view.findViewById(R.id.labelTextView);
                tagTextView.setText("#" + tag.getName());
                return tagTextView;
            }
        };
        createLayout.setAdapter(addAdapter);
    }

    private List<Tag> selectedTagList = new ArrayList<>();
    private TagAdapter selectedAdapter;

    /**
     * 创建已选标签
     */
    private void createSelectedTagViews() {
        selectedAdapter = new TagAdapter<Tag>(selectedTagList) {
            @Override
            public View getView(FlowLayout parent, int position, Tag tag) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_selected, parent, false);
                TextView tagTextView = view.findViewById(R.id.labelTextView);
                tagTextView.setText("#" + tag.getName());
                return tagTextView;
            }
        };
        selectedLayout.setAdapter(selectedAdapter);
        selectedLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                selectedTagList.remove(position);
                createSelectedTagViews();

                Set<Integer> selectedSet = new HashSet<>();
                for (Tag tag : selectedTagList) {
                    for (int i = 0; i < historyTagList.size(); i++) {
                        if (tag.getId() == historyTagList.get(i).getId()) {
                            selectedSet.add(i);
                        }
                    }
                }
                System.out.println("set size: " + selectedSet.size());
                historyAdapter.setSelectedList(selectedSet);

                return false;
            }
        });
    }


    private OnTagsSelectedListener onTagsSelectedListener;

    public interface OnTagsSelectedListener {
        void onTagsSelected(List<Tag> list);
    }
}
