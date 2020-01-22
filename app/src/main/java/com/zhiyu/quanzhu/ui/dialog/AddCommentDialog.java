package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.CommentChild;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * 新增评论/回复评论
 */
public class AddCommentDialog extends Dialog implements View.OnClickListener {
    private EditText mEditText;
    private TextView confirmTextView;

    public AddCommentDialog(@NonNull Context context, int themeResId, OnAddCommentListener listener) {
        super(context, themeResId);
        this.onAddCommentListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_comment);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(R.drawable.shape_wait_dialog_bg);
        initViews();
    }

    private void initViews() {
        mEditText = findViewById(R.id.mEditText);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
    }
    private int id;
    private String name;
    public void setCommentData(int commentId,String userName){
        this.id=commentId;
        this.name=userName;
        mEditText.setHint("@"+name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmTextView:
                String comment = mEditText.getText().toString().trim();
                if (null != onAddCommentListener && !TextUtils.isEmpty(comment)) {
                    this.onAddCommentListener.onAddComment(comment,id);
                }
                mEditText.setText(null);
                dismiss();
                break;
        }
    }

    private OnAddCommentListener onAddCommentListener;

    public interface OnAddCommentListener {
        void onAddComment(String comment,int commentId);
    }
}
