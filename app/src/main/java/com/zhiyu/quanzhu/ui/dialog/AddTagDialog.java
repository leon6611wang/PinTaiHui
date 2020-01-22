package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.Tag;
import com.zhiyu.quanzhu.model.result.TagResult;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;


/**
 * 添加标签
 */
public class AddTagDialog extends Dialog implements View.OnClickListener {
    private TextView backTextView, confirmTextView, cancelTextView, searchTextView;
    private EditText searchEditText;
    private LinearLayout searchTextLayout, searchEditLayout, selectTagLayout, addTagLayout;
    private TagFlowLayout selectedLayout, historyLayout, hotLayout;
    private ImageView deleteHistoryImageView;

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
                    }

                    break;
            }
        }
    }

    public AddTagDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
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


        searchEditText.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容

                } else {
                    // 此处为失去焦点时的处理内容
                }
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
                break;
        }
    }


    private String tag_name;
    private int is_hot, page = 1;
    private TagResult tagResult;

    private void searchTag() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SEARCH_TAG);
        params.addBodyParameter("tag_name", tag_name);
        params.addBodyParameter("is_hot", String.valueOf(is_hot));
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                tagResult = GsonUtils.GsonToBean(result, TagResult.class);
                Message message=myHandler.obtainMessage(1);
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

    private void search() {
        tag_name = searchEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(tag_name)) {
            is_hot = 1;
            page = 1;
            searchTag();
        }
    }

    private void hotTag() {
        tag_name = null;
        is_hot = 1;
        page = 1;
        searchTag();
    }

    private void createHotTagViews() {
        TagAdapter tagAdapter1 = new TagAdapter<Tag>(tagResult.getData()) {
            @Override
            public View getView(FlowLayout parent, int position, Tag tag) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history_label, parent, false);
                TextView labelTextView = view.findViewById(R.id.labelTextView);
                labelTextView.setText(tag.getName());
                return labelTextView;
            }
        };
        hotLayout.setAdapter(tagAdapter1);
    }
}
