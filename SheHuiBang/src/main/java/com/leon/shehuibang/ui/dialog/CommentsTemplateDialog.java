package com.leon.shehuibang.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.leon.shehuibang.R;
import com.leon.shehuibang.model.MyResult;
import com.leon.shehuibang.model.bean.CommentsTemplate;
import com.leon.shehuibang.ui.adapter.CommentsTemplateAdapter;
import com.leon.shehuibang.ui.widget.SpaceItemDecoration;
import com.leon.shehuibang.utils.ConstantsUtils;
import com.leon.shehuibang.utils.GsonUtils;
import com.leon.shehuibang.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.leon.shehuibang.utils.ConstantsUtils.HOST;

public class CommentsTemplateDialog extends Dialog implements CommentsTemplateAdapter.OnTemplateAdapterClickListener {
    private View bottomView;
    private RecyclerView recycleView;
    private CommentsTemplateAdapter adapter;
    private List<CommentsTemplate> list;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CommentsTemplateDialog> weakReference;

        public MyHandler(CommentsTemplateDialog dialog) {
            weakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            CommentsTemplateDialog dialog = weakReference.get();
            switch (msg.what) {
                case 1:
                    dialog.adapter.setList(dialog.list);
                    break;
            }
        }
    }

    public CommentsTemplateDialog(@NonNull Context context, int themeResId, OnCommentsTemplateChooseListener listener) {
        super(context, themeResId);
        this.onCommentsTemplateChooseListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comments_template);
        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
//        params.height = ScreentUtils.getInstance().getScreenHeight(getContext()) / 5;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogRightShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        initViews();
        templateList();
    }

    private void initViews() {
        bottomView = findViewById(R.id.bottomView);
        bottomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        recycleView = findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        SpaceItemDecoration decoration = new SpaceItemDecoration((int) getContext().getResources().getDimension(R.dimen.dp_5));
        adapter = new CommentsTemplateAdapter(getContext());
        adapter.setOnTemplateAdapterClickListener(this);
        recycleView.addItemDecoration(decoration);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(adapter);
    }

    private void templateList() {
        RequestParams params = new RequestParams(ConstantsUtils.HOST+"comments/comments_template_list");
        params.addHeader("Content-Type", "application/json; charset=UTF-8");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyResult myResult = GsonUtils.GsonToBean2(result, MyResult.class);
                list = myResult.getComments_template_list();
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println(ex.toString());
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
    public void onTemplateeAdapterClick(CommentsTemplate template) {
        if (null != onCommentsTemplateChooseListener) {
            onCommentsTemplateChooseListener.onCommentsTemplateChoose(template);
        }
        dismiss();
    }

    private OnCommentsTemplateChooseListener onCommentsTemplateChooseListener;

    public interface OnCommentsTemplateChooseListener {
        void onCommentsTemplateChoose(CommentsTemplate template);
    }
}
