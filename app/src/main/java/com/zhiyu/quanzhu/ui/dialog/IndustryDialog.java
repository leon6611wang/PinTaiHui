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
import com.zhiyu.quanzhu.model.bean.IndustryChild;
import com.zhiyu.quanzhu.model.bean.IndustryParent;
import com.zhiyu.quanzhu.model.dao.IndustryDao;
import com.zhiyu.quanzhu.model.result.IndustryResult;
import com.zhiyu.quanzhu.ui.activity.MingPianGuangChangActivity;
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
import java.util.concurrent.CountDownLatch;

/**
 * 行业
 */
public class IndustryDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private LoopView parentView, childView;
    private List<String> parentList = new ArrayList<>();
    private List<String> childList = new ArrayList<>();
    private String parent, child;
    private TextView cancelTextView, confirmTextView;
    private List<IndustryParent> industryParentList;
    private List<IndustryChild> industryChildList;
    private IndustryParent industryParent;
    private IndustryChild industryChild;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<IndustryDialog> dialogWeakReference;

        public MyHandler(IndustryDialog dialog) {
            dialogWeakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            IndustryDialog dialog = dialogWeakReference.get();
            switch (msg.what) {
                case 1:
                    dialog.initData();
                    break;
            }
        }
    }

    public IndustryDialog(@NonNull Context context, int themeResId, OnHangYeChooseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.onHangYeChooseListener = listener;
        industryParentList = IndustryDao.getInstance().industryParentList();
    }

    public void setIndustry(String pIndustry, String cIndustry) {
        int pIndex = -1, cIndex = -1;
        if (null != parentList && parentList.size() > 0) {
            for (int i = 0; i < parentList.size(); i++) {
                if (parentList.get(i).equals(pIndustry)) {
                    pIndex = i;
                    break;
                }
            }
            if (pIndex > -1) {
                parentView.setCurrentPosition(pIndex);
                industryParent = industryParentList.get(pIndex);
                industryChildList = IndustryDao.getInstance().industryChildList(industryParentList.get(pIndex).getId());
                if (null != industryChildList && industryChildList.size() > 0) {
                    childList.clear();
                    for (IndustryChild child : industryChildList) {
                        childList.add(child.getName());
                    }
                }
                childView.setItems(childList);
            }
        }
        if (null != childList && childList.size() > 0) {
            for (int i = 0; i < childList.size(); i++) {
                if (childList.get(i).equals(cIndustry)) {
                    cIndex = i;
                    break;
                }
            }
            if (cIndex > -1) {
                childView.setCurrentPosition(cIndex);
                industryChild = industryChildList.get(cIndex);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_industry);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext);
        params.height = ScreentUtils.getInstance().getScreenHeight(mContext) / 5 * 2;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initViews();
        industryList();
//        if (null != industryParentList && industryParentList.size() > 0) {
//            initData();
//        } else {
//            industryList();
//        }
    }

    private void initData() {
        industryParentList = IndustryDao.getInstance().industryParentList();
        if (null != industryParentList && industryParentList.size() > 0) {
            industryParent = industryParentList.get(0);
            for (IndustryParent parent : industryParentList) {
                parentList.add(parent.getName());
            }
        }

        industryChildList = IndustryDao.getInstance().industryChildList(industryParentList.get(0).getId());
        if (null != industryChildList && industryChildList.size() > 0) {
            industryChild = industryChildList.get(0);
            for (IndustryChild child : industryChildList) {
                childList.add(child.getName());
            }
        }

        parentView.setItems(parentList);
        parentView.setInitPosition(0);
        childView.setItems(childList);
        childView.setInitPosition(0);
        parent = parentList.get(parentView.getSelectedItem());
        child = childList.get(childView.getSelectedItem());

    }

    private void initViews() {
        parentView = findViewById(R.id.parentView);
        parentView.setNotLoop();
        childView = findViewById(R.id.childView);
        childView.setNotLoop();

        parentView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (!TextUtils.isEmpty(parentList.get(index))) {
                    parent = parentList.get(index);
                    industryParent = IndustryDao.getInstance().industryParent(parent);
                    if (null != industryChildList) {
                        industryChildList.clear();
                    }
                    if (null != childList) {
                        childList.clear();
                    }
                    industryChildList = IndustryDao.getInstance().industryChildList(industryParentList.get(index).getId());
                    if (null != industryChildList && industryChildList.size() > 0) {
                        for (IndustryChild child : industryChildList) {
                            childList.add(child.getName());
                        }
                    }
                    childView.setItems(childList);
                    if (null != industryChildList && industryChildList.size() > 0)
                        industryChild = industryChildList.get(0);
                }
            }
        });


        childView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (null != childList && childList.size() > 0)
                    if (!TextUtils.isEmpty(childList.get(index))) {
                        child = childList.get(index);
                        industryChild = IndustryDao.getInstance().industryChild(child);
                    }
            }
        });
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelTextView:
                dismiss();
                break;
            case R.id.confirmTextView:
                if (null == industryChildList || industryChildList.size() == 0) {
                    MessageToast.getInstance(getContext()).show("当前行业无二级分类，请选择其他行业");
                } else {
                    if (null != onHangYeChooseListener) {
                        if (null != industryParent && null != industryChild) {
                            onHangYeChooseListener.onHangYeChoose(industryParent, industryChild);
                            dismiss();
                        }
                    }
                }


                break;
        }
    }

    private OnHangYeChooseListener onHangYeChooseListener;

    public interface OnHangYeChooseListener {
        void onHangYeChoose(IndustryParent parent, IndustryChild child);
    }

    private IndustryResult industryResult;

    /**
     * 行业列表
     */
    private void industryList() {
        final RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.HOBBY_LIST);
        params.addBodyParameter("type", "1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("industry list: " + result);
                industryResult = GsonUtils.GsonToBean(result, IndustryResult.class);
                if (null != industryResult) {
                    IndustryDao.getInstance().saveIndustryParent(industryResult.getData().getList().get(0).getChild());
                    final CountDownLatch cdl = new CountDownLatch(industryResult.getData().getList().get(0).getChild().size());
                    for (final IndustryParent parent : industryResult.getData().getList().get(0).getChild()) {
                        ThreadPoolUtils.getInstance().init().execute(new Runnable() {
                            @Override
                            public void run() {
                                IndustryDao.getInstance().saveIndustryChild(parent.getChild());
                                cdl.countDown();
                            }
                        });
                    }
                    try {
                        cdl.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("industry list error: " + ex.toString());
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
