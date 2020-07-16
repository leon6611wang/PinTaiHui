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
import com.zhiyu.quanzhu.model.bean.IndustryChild;
import com.zhiyu.quanzhu.model.dao.HobbyDao;
import com.zhiyu.quanzhu.model.dao.IndustryDao;
import com.zhiyu.quanzhu.model.result.HobbyDaoResult;
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
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 兴趣
 */
public class HobbyDialog extends Dialog implements View.OnClickListener {
    private List<String> parentList = new ArrayList<>();
    private List<String> childList = new ArrayList<>();
    private LoopView parentView, childView;
    private String parent, child;
    private TextView cancelTextView, confirmTextView;
    private List<HobbyDaoParent> hobbyParentList;
    private List<HobbyDaoChild> hobbyChildList;
    private HobbyDaoParent hobbyDaoParent;
    private HobbyDaoChild hobbyDaoChild;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<HobbyDialog> dialogWeakReference;

        public MyHandler(HobbyDialog dialog) {
            dialogWeakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            HobbyDialog dialog = dialogWeakReference.get();
            switch (msg.what) {
                case 99:
                    MessageToast.getInstance(dialog.getContext()).show("服务器内部错误，请稍后再试");
                    break;
                case 1:
                    dialog.initData();
                    break;
                case 2:
                    dialog.notifyData();
                    break;
            }
        }
    }

    public HobbyDialog(@NonNull Context context, int themeResId, OnChooseHobbyListener listener) {
        super(context, themeResId);
        this.onChooseHobbyListener = listener;
        ThreadPoolUtils.getInstance().init().execute(new Runnable() {
            @Override
            public void run() {
                hobbyParentList = HobbyDao.getInstance().hobbyParentList();
            }
        });

    }

    public void setHobby(String pHobby, String cHobby) {
        int pIndex = -1, cIndex = -1;
        if (null != parentList && parentList.size() > 0) {
            for (int i = 0; i < parentList.size(); i++) {
                if (parentList.get(i).equals(pHobby)) {
                    pIndex = i;
                    break;
                }
            }
            if (pIndex > -1) {
                parentView.setCurrentPosition(pIndex);
                hobbyDaoParent = hobbyParentList.get(pIndex);
                hobbyChildList = HobbyDao.getInstance().hobbyChildList(hobbyParentList.get(pIndex).getId());
                if (null != hobbyChildList && hobbyChildList.size() > 0) {
                    childList.clear();
                    for (HobbyDaoChild child : hobbyChildList) {
                        childList.add(child.getName());
                    }
                }
                childView.setItems(childList);
            }
        }
        if (null != childList && childList.size() > 0) {
            for (int i = 0; i < childList.size(); i++) {
                if (childList.get(i).equals(cHobby)) {
                    cIndex = i;
                    break;
                }
            }
            if (cIndex > -1) {
                childView.setCurrentPosition(cIndex);
                hobbyDaoChild = hobbyChildList.get(cIndex);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_hobby);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
        params.height = ScreentUtils.getInstance().getScreenHeight(getContext()) / 5 * 2;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initViews();
        hobbyList();
//        if (null != hobbyParentList && hobbyParentList.size() > 0) {
//            System.out.println("initData");
//            initData();
//        } else {
//            System.out.println("hobbyList");
//            hobbyList();
//        }
    }

    private void initData() {
        ThreadPoolUtils.getInstance().init().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("startTime: " + new Date().getTime());
                if (null != hobbyParentList && hobbyParentList.size() > 0) {
                    hobbyDaoParent = hobbyParentList.get(0);
                    for (HobbyDaoParent parent : hobbyParentList) {
                        parentList.add(parent.getName());
                    }
                }

                hobbyChildList = HobbyDao.getInstance().hobbyChildList(hobbyParentList.get(0).getId());
                if (null != hobbyChildList && hobbyChildList.size() > 0) {
                    hobbyDaoChild = hobbyChildList.get(0);
                    for (HobbyDaoChild child : hobbyChildList) {
                        childList.add(child.getName());
                    }
                }
                System.out.println("endTime: " + new Date().getTime());
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }
        });

    }

    private void notifyData() {
        parentView.setItems(parentList);
        parentView.setInitPosition(0);
        childView.setItems(childList);
        childView.setInitPosition(0);
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
                    hobbyDaoParent = HobbyDao.getInstance().getHobbyParent(parent);
                    if (null != hobbyChildList) {
                        hobbyChildList.clear();
                    }
                    if (null != childList) {
                        childList.clear();
                    }
                    hobbyChildList = HobbyDao.getInstance().hobbyChildList(hobbyParentList.get(index).getId());
                    if (null != hobbyChildList && hobbyChildList.size() > 0) {
                        hobbyDaoChild = hobbyChildList.get(0);
                        for (HobbyDaoChild child : hobbyChildList) {
                            childList.add(child.getName());
                        }
                    }
                    childView.setItems(childList);
                    if (null != hobbyChildList && hobbyChildList.size() > 0)
                        hobbyDaoChild = hobbyChildList.get(0);
                }
            }
        });
        childView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (!TextUtils.isEmpty(childList.get(index))) {
                    child = childList.get(index);
                    hobbyDaoChild = HobbyDao.getInstance().getHobbyChild(child);
                }
            }
        });
        if (null != childList && childList.size() > 0)
            child = childList.get(childView.getSelectedItem());
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
                if (null == hobbyChildList || hobbyChildList.size() == 0) {
                    MessageToast.getInstance(getContext()).show("当前兴趣无二级分类，请选择其他兴趣");
                } else {
                    if (null != onChooseHobbyListener) {
                        if (null != hobbyDaoParent && null != hobbyChildList) {
                            onChooseHobbyListener.onChooseHobby(hobbyDaoParent, hobbyDaoChild);
                            dismiss();
                        } else {
                            MessageToast.getInstance(getContext()).show("请选择兴趣");
                        }
                    }
                }

                break;
        }
    }

    private OnChooseHobbyListener onChooseHobbyListener;

    public interface OnChooseHobbyListener {
        void onChooseHobby(HobbyDaoParent parent, HobbyDaoChild child);
    }


    private HobbyDaoResult hobbyResult;

    private void hobbyList() {
        final RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.HOBBY_LIST);
        params.addBodyParameter("type", "2");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("hobby list: " + result);
                hobbyResult = GsonUtils.GsonToBean(result, HobbyDaoResult.class);
                hobbyParentList = hobbyResult.getData().getList().get(0).getChild();
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
//                System.out.println("hobby list error: " + ex.toString());
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
