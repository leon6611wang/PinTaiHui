package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

/**
 * yes or no dialog
 */
public class EditNoteNameDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView cancelTextView, confirmTextView;
    private EditText mEditText;
    private String noteName;

    public EditNoteNameDialog(@NonNull Context context, int themeResId, EditNoteNameListener listener) {
        super(context, themeResId);
        this.editNoteNameListener = listener;
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_note_name);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(context) / 5 * 4;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(R.drawable.shape_wait_dialog_bg);

        initViews();
    }

    private void initViews() {
        mEditText = findViewById(R.id.mEditText);
        cancelTextView = findViewById(R.id.cancelTextView);
        confirmTextView = findViewById(R.id.confirmTextView);
        cancelTextView.setOnClickListener(this);
        confirmTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelTextView:
                dismiss();
                break;
            case R.id.confirmTextView:
                noteName = mEditText.getText().toString().trim();
                if (!StringUtils.isNullOrEmpty(noteName)) {
                    if (null != editNoteNameListener) {
                        editNoteNameListener.editNoteName(noteName);
                    }
                    dismiss();
                } else {
                    MessageToast.getInstance(getContext()).show("请输入备注内容");
                }

                break;
        }
    }


    private EditNoteNameListener editNoteNameListener;

    public interface EditNoteNameListener {
        void editNoteName(String noteName);
    }

}
