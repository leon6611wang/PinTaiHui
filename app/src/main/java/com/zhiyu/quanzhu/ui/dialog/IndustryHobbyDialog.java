package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.HobbyDaoChild;
import com.zhiyu.quanzhu.model.bean.HobbyDaoParent;
import com.zhiyu.quanzhu.model.bean.IndustryHobby;
import com.zhiyu.quanzhu.model.dao.HobbyDao;
import com.zhiyu.quanzhu.model.result.IndustryHobbyResult;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.ThreadPoolUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 兴趣
 */
public class IndustryHobbyDialog extends Dialog implements View.OnClickListener {
    private List<String> parentList = new ArrayList<>();
    private List<String> childList = new ArrayList<>();
    private LoopView parentView, childView;
    private TextView cancelTextView, confirmTextView, titleTextView;
    private List<IndustryHobby> list;
    private IndustryHobby parent, child;
    private boolean isIndustry;
    private MyHandler myHandler = new MyHandler(this);
    private int childIndex;

    private static class MyHandler extends Handler {
        WeakReference<IndustryHobbyDialog> dialogWeakReference;

        public MyHandler(IndustryHobbyDialog dialog) {
            dialogWeakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            IndustryHobbyDialog dialog = dialogWeakReference.get();
            switch (msg.what) {
                case 1:
                    dialog.initData();
                    break;
            }
        }
    }

    public IndustryHobbyDialog(@NonNull Context context, int themeResId, boolean isIndustry, OnIndustryHobbySelectedListener listener) {
        super(context, themeResId);
        this.isIndustry = isIndustry;
        this.onIndustryHobbySelectedListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_industry_hobby);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
        params.height = ScreentUtils.getInstance().getScreenHeight(getContext()) / 5 * 2;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initViews();
        industryHobbyList();
    }

    private void initData() {
        for (IndustryHobby ih : list) {
            parentList.add(ih.getName());
        }
        if (null != list && list.size() > 0)
            parent = list.get(0);
        getChildList(0);
        if (null != list && list.size() > 0 && null != list.get(0).getChild() && list.get(0).getChild().size() > 0)
            child = list.get(0).getChild().get(0);
        parentView.setItems(parentList);
        if (parentList.size() > 0)
            parentView.setInitPosition(0);
        childView.setItems(childList);
        if (childList.size() > 0)
            childView.setInitPosition(0);
    }


    private void initViews() {
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(isIndustry ? "行业选择" : "兴趣选择");
        parentView = findViewById(R.id.parentView);
        parentView.setNotLoop();
        childView = findViewById(R.id.childView);
        childView.setNotLoop();
        parentView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                parent = list.get(index);
                getChildList(index);
                childView.setItems(childList);
                if (childIndex > childList.size() - 1) {
                    childIndex = 0;
                }
                childView.setCurrentPosition(childIndex);
                child = list.get(index).getChild().get(childIndex);
            }
        });
        childView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                childIndex = index;
                if (!TextUtils.isEmpty(childList.get(childIndex))) {
                    child = list.get(parentView.getSelectedItem()).getChild().get(childIndex);
                }
            }
        });
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
    }

    private void getChildList(int position) {
        childList.clear();
        for (IndustryHobby ih : list.get(position).getChild()) {
            childList.add(ih.getName());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelTextView:
                dismiss();
                break;
            case R.id.confirmTextView:
                if (null != onIndustryHobbySelectedListener) {
                    onIndustryHobbySelectedListener.onIndustryHobbySelected(parent, child);
                }
                dismiss();
                break;
        }
    }

    private OnIndustryHobbySelectedListener onIndustryHobbySelectedListener;

    public interface OnIndustryHobbySelectedListener {
        void onIndustryHobbySelected(IndustryHobby p, IndustryHobby c);
    }


    private IndustryHobbyResult industryHobbyResult;

    private void industryHobbyList() {
        final RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.HOBBY_LIST);
        params.addBodyParameter("type", isIndustry ? "1" : "2");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println((isIndustry ? "行业列表: " : "兴趣列表: ") + result);
                industryHobbyResult = GsonUtils.GsonToBean(result, IndustryHobbyResult.class);
                list = industryHobbyResult.getData().getList().get(0).getChild();
                int index = -1;
                if (null != list && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getName().equals("自定义")) {
                            if (null == list.get(i).getChild() || list.get(i).getChild().size() == 0) {
                                index = i;
                            }
                        }
                    }
                }
                if (index > -1) {
                    list.remove(index);
                }

                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("hobby list error: " + ex.toString());
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
