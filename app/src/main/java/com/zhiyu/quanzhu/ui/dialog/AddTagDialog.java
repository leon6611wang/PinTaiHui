package com.zhiyu.quanzhu.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
                    if (dialog.is_hot == 1) {
                        dialog.createHotTagViews();
                    } else if (dialog.is_hot == 0) {
                        dialog.createMoreTagViews();
                    }

                    break;
                case 2:
                    if (null == dialog.tagResult2 || null == dialog.tagResult2.getData() || dialog.tagResult2.getData().size() == 0) {
                        dialog.craeteTagRootLayout.setVisibility(View.VISIBLE);
                        dialog.createCreateTagViews();
                    } else {
                        dialog.craeteTagRootLayout.setVisibility(View.GONE);
                    }
                    break;
                case 3:
                    Toast.makeText(dialog.getContext(), dialog.addTagResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (dialog.addTagResult.getCode() == 200) {
                        TagDao.getInstance().saveTag(dialog.addTagResult.getData().getData());
                        dialog.dismiss();
                    }
                    break;
            }
        }
    }

    public AddTagDialog(Activity aty, Context context, int themeResId) {
        super(context, themeResId);
        this.activity = aty;
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
        hotTag();

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
                    is_hot = 0;
                    tag_name = searchEditText.getText().toString().trim();
                    searchTag();
                    searchTags();
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
    private int is_hot = 1, page = 1;
    private TagResult tagResult;

    private void searchTags() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SEARCH_TAGS);
        params.addBodyParameter("tag_name", tag_name);
        params.addBodyParameter("is_hot", String.valueOf(is_hot));
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                tagResult = GsonUtils.GsonToBean(result, TagResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
                System.out.println("标签: " + tagResult.getData().size());
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

    private void search() {
        tag_name = searchEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(tag_name)) {
            is_hot = 1;
            page = 1;
            searchTags();
        }
    }

    private void hotTag() {
        tag_name = null;
        is_hot = 1;
        page = 1;
        searchTags();
    }

    private void createHotTagViews() {
        TagAdapter tagAdapter1 = new TagAdapter<Tag>(tagResult.getData()) {
            @Override
            public View getView(FlowLayout parent, int position, Tag tag) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
                TextView tagTextView = view.findViewById(R.id.labelTextView);
                tagTextView.setText("#" + tag.getName());
                return tagTextView;
            }
        };
        hotLayout.setAdapter(tagAdapter1);
        hotLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                selectedList = new ArrayList<>(selectPosSet);
                addSelectedTagViews();
            }
        });
    }

    private void createMoreTagViews() {
        TagAdapter tagAdapter1 = new TagAdapter<Tag>(tagResult.getData()) {
            @Override
            public View getView(FlowLayout parent, int position, Tag tag) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
                TextView tagTextView = view.findViewById(R.id.labelTextView);
                tagTextView.setText("#" + tag.getName());
                return tagTextView;
            }
        };
        moreLayout.setAdapter(tagAdapter1);
        moreLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
//                selectedList = new ArrayList<>(selectPosSet);
//                addSelectedTagViews();
            }
        });
    }

    private void createCreateTagViews() {
        List<Tag> tagList = new ArrayList<>();
        Tag tag = new Tag();
        tag.setName(tag_name);
        tagList.add(tag);
        TagAdapter tagAdapter1 = new TagAdapter<Tag>(tagList) {
            @Override
            public View getView(FlowLayout parent, int position, Tag tag) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
                TextView tagTextView = view.findViewById(R.id.labelTextView);
                tagTextView.setText("#" + tag.getName());
                return tagTextView;
            }
        };
        createLayout.setAdapter(tagAdapter1);
        createLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
//                selectedList = new ArrayList<>(selectPosSet);
//                addSelectedTagViews();
                addTag();
            }
        });
    }

    private List<Integer> selectedList;

    private void addSelectedTagViews() {
        List<Tag> selectedTagList = new ArrayList<>();
        for (Integer integer : selectedList) {
            selectedTagList.add(tagResult.getData().get(integer));
            TagDao.getInstance().saveTag(tagResult.getData().get(integer));
        }

        TagAdapter tagAdapter = new TagAdapter<Tag>(selectedTagList) {
            @Override
            public View getView(FlowLayout parent, int position, Tag tag) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_selected, parent, false);
                TextView tagTextView = view.findViewById(R.id.labelTextView);
                tagTextView.setText("#" + tag.getName());
                return tagTextView;
            }
        };
        selectedLayout.setAdapter(tagAdapter);
        selectedLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                List<Integer> selectedList = new ArrayList<>(selectPosSet);
                removeSelectedTagViews(selectedList);
                System.out.println(selectedList.toString());
            }
        });
    }

    private void removeSelectedTagViews(List<Integer> removedList) {
        List<Tag> selectedTagList = new ArrayList<>();
        for (Integer integer : removedList) {
            selectedList.remove(integer);
        }
        for (Integer integer : selectedList) {
            selectedTagList.add(tagResult.getData().get(integer));
        }
        TagAdapter tagAdapter = new TagAdapter<Tag>(selectedTagList) {
            @Override
            public View getView(FlowLayout parent, int position, Tag tag) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_selected, parent, false);
                TextView tagTextView = view.findViewById(R.id.labelTextView);
                tagTextView.setText("#" + tag.getName());
                return tagTextView;
            }
        };
        selectedLayout.setAdapter(tagAdapter);
        selectedLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                List<Integer> selectedList = new ArrayList<>(selectPosSet);
                removeSelectedTagViews(selectedList);
                System.out.println(selectedList.toString());
            }
        });
    }
}
