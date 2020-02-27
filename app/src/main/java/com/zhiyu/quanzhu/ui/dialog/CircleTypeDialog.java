package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * 圈子性质:行业/兴趣
 */
public class CircleTypeDialog extends Dialog implements View.OnClickListener{
    private TextView industryTextView,hobbyTextView,cancelTextView;
    public CircleTypeDialog(@NonNull Context context, int themeResId,OnCircleTypeListener listener) {
        super(context, themeResId);
        this.onCircleTypeListener=listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_circle_type);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initViews();
    }

    private void initViews(){
        industryTextView=findViewById(R.id.industryTextView);
        industryTextView.setOnClickListener(this);
        hobbyTextView=findViewById(R.id.hobbyTextView);
        hobbyTextView.setOnClickListener(this);
        cancelTextView=findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.industryTextView:
                if(null!=onCircleTypeListener){
                    onCircleTypeListener.onCircleType(1,"行业");
                }
                dismiss();
                break;
            case R.id.hobbyTextView:
                if(null!=onCircleTypeListener){
                    onCircleTypeListener.onCircleType(2,"兴趣");
                }
                dismiss();
                break;
            case R.id.cancelTextView:
                dismiss();
                break;
        }
    }
    private OnCircleTypeListener onCircleTypeListener;
    public interface OnCircleTypeListener{
        void onCircleType(int type,String desc);
    }
}
