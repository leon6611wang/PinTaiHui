package com.zhiyu.quanzhu.ui.popupwindow;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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
 * 圈子成员管理菜单
 */
public class CircleMemberManageTopMenuWindow extends PopupWindow {
    private Context context;
    private View view;
    private int windowHeight;
    private int dp_10, dp_100, dp_15, dp_40, dp_150, dp_50;
    private int screenWidth;
    //自己的身份
    // 0圈主 1管理员 2成员
    private int my_role = -1;
    //用户的身份
    //0圈主 1管理员 2成员
    private int user_role = -1;
    private LinearLayout menu1Layout, menu2Layout, menu3Layout;
    private TextView menu1TextView, menu2TextView, menu3TextView;
    private int layoutCount = 0;
    private int operationType = -1;// 0 T人 1撤销管理员 2转让群 3注销群,4:删除圈子， 5:添加管理
    private int tuid, circle_id;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CircleMemberManageTopMenuWindow> windowWeakReference;

        public MyHandler(CircleMemberManageTopMenuWindow window) {
            windowWeakReference = new WeakReference<>(window);
        }

        @Override
        public void handleMessage(Message msg) {
            CircleMemberManageTopMenuWindow window = windowWeakReference.get();
            switch (msg.what) {
                case 1:
                    MessageToast.getInstance(window.context).show(window.baseResult.getMsg());
                    if (null != window.circleMemberManageTopListener) {
                        window.circleMemberManageTopListener.onComplete();
                    }
                    break;
                case 2:
                    MessageToast.getInstance(window.context).show("服务器内部错误，请稍后重试.");
                    break;
                case 3:
                    int height = (Integer) msg.obj;
                    window.showTop(height);
                    break;
            }
        }
    }

    public CircleMemberManageTopMenuWindow(Context context, int my_role, int user_role, int circle_id, int tuid, CircleMemberManageTopListener listener) {
        super(context);
        this.circleMemberManageTopListener = listener;
        this.context = context;
        this.my_role = my_role;
        this.user_role = user_role;
        this.circle_id = circle_id;
        this.tuid = tuid;
        screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        dp_10 = (int) context.getResources().getDimension(R.dimen.dp_10);
        dp_15 = (int) context.getResources().getDimension(R.dimen.dp_15);
        dp_40 = (int) context.getResources().getDimension(R.dimen.dp_60);
        dp_50 = (int) context.getResources().getDimension(R.dimen.dp_50);
        dp_100 = (int) context.getResources().getDimension(R.dimen.dp_100);
        dp_150 = (int) context.getResources().getDimension(R.dimen.dp_150);
        initalize();
    }

    private void initalize() {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.popup_circle_member_manage_top, null);
        setContentView(view);
        initWindow();
        initViews();
        viewRole();
    }

    private void initViews() {
        menu1Layout = view.findViewById(R.id.menu1Layout);
        menu2Layout = view.findViewById(R.id.menu2Layout);
        menu3Layout = view.findViewById(R.id.menu3Layout);
        menu1TextView = view.findViewById(R.id.menu1TextView);
        menu2TextView = view.findViewById(R.id.menu2TextView);
        menu3TextView = view.findViewById(R.id.menu3TextView);
    }

    private void viewRole() {
        switch (my_role) {
            case 0:
                menu1Layout.setVisibility(View.VISIBLE);
                menu2Layout.setVisibility(View.VISIBLE);
                menu3Layout.setVisibility(View.VISIBLE);
                menu1Layout.setOnClickListener(new OnAddFrendClick());
                menu2Layout.setOnClickListener(new OnTUserClick());
                menu1TextView.setText("添加圈友");
                menu2TextView.setText("请出圈聊");
                switch (user_role) {
                    case 1:
                        menu3TextView.setText("撤销管理");
                        menu3Layout.setOnClickListener(new OnRemoveManageClick());
                        break;
                    case 2:
                        menu3TextView.setText("添加管理");
                        menu3Layout.setOnClickListener(new OnAddManageClick());
                        break;
                }
                break;
            case 1:
                switch (user_role) {
                    case 0:
                        menu1Layout.setVisibility(View.GONE);
                        menu2Layout.setVisibility(View.GONE);
                        menu3Layout.setVisibility(View.VISIBLE);
                        menu3TextView.setText("添加圈友");
                        menu3Layout.setOnClickListener(new OnAddFrendClick());
                        break;
                    case 1:
                        menu1Layout.setVisibility(View.GONE);
                        menu2Layout.setVisibility(View.GONE);
                        menu3Layout.setVisibility(View.VISIBLE);
                        menu3TextView.setText("添加圈友");
                        menu3Layout.setOnClickListener(new OnAddFrendClick());
                        break;
                    case 2:
                        menu1Layout.setVisibility(View.VISIBLE);
                        menu2Layout.setVisibility(View.GONE);
                        menu3Layout.setVisibility(View.VISIBLE);
                        menu1Layout.setOnClickListener(new OnAddFrendClick());
                        menu3Layout.setOnClickListener(new OnTUserClick());
                        menu1TextView.setText("添加圈友");
                        menu3TextView.setText("请出圈聊");
                        break;
                }
                break;
            case 2:
                menu1Layout.setVisibility(View.GONE);
                menu2Layout.setVisibility(View.GONE);
                menu3Layout.setVisibility(View.VISIBLE);
                menu3TextView.setText("添加圈友");
                menu3Layout.setOnClickListener(new OnAddFrendClick());
                break;
        }
    }

    private void initWindow() {
        this.setWidth(dp_100);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        backgroundAlpha((Activity) context, 1.0f);//0.0-1.0
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha((Activity) context, 1f);
            }
        });
    }

    //设置添加屏幕的背景透明度
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }


    public void showAtTop(View v, int height) {
        showAsDropDown(v, -dp_40, -(v.getHeight() / 2 + dp_50 * layoutCount - dp_50 / 2 + dp_15 + height));
    }

    private void showTop(int height) {
        System.out.println("showTop height: " + height);
//        showAsDropDown(itemView, -dp_40, -(itemView.getHeight() / 4 + dp_50 * layoutCount + dp_15 + 495));
    }

    /**
     * 添加好友
     */
    private class OnAddFrendClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            dismiss();
            System.out.println("添加圈友");
            addFrend();
        }
    }

    /**
     * T人
     */
    private class OnTUserClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            dismiss();
            System.out.println(" T人");
            operationType = 0;
            operation();
        }
    }

    /**
     * 撤销管理
     */
    private class OnRemoveManageClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            dismiss();
            System.out.println("撤销管理");
            operationType = 1;
            operation();
        }
    }

    /**
     * 添加管理
     */
    private class OnAddManageClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            dismiss();
            System.out.println("添加管理");
            operationType = 5;
            operation();
        }
    }

    /**
     * 操作
     */
    private void operation() {
        System.out.println("circle_id: " + circle_id + " , tuid: " + tuid + " , operationType:　" + operationType);
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CIRCLE_USER_LIST_OPERATION);
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        params.addBodyParameter("tuid", String.valueOf(tuid));
        params.addBodyParameter("type", String.valueOf(operationType));// 0 T人 1撤销管理员 2转让群 3注销群
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private BaseResult baseResult;

    private void addFrend() {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CARD_EXCHANGE);
        params.addBodyParameter("tuid", String.valueOf(tuid));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private CircleMemberManageTopListener circleMemberManageTopListener;

    public interface CircleMemberManageTopListener {
        void onComplete();
    }
}
