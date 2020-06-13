package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.ShopType;
import com.zhiyu.quanzhu.model.result.ShopTypeResult;
import com.zhiyu.quanzhu.ui.toast.FailureToast;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 店铺类型
 */
public class ShopTypeDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private LoopView shopTypeView;
    private TextView cancelTextView, confirmTextView;
    private List<String> typeList = new ArrayList<>();
    private ShopType shopType;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<ShopTypeDialog> dialogWeakReference;

        public MyHandler(ShopTypeDialog dialog) {
            dialogWeakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            ShopTypeDialog dialog = dialogWeakReference.get();
            switch (msg.what) {
                case 1:
                    dialog.shopTypeView.setItems(dialog.typeList);
                    dialog.shopTypeView.setInitPosition(0);
                    dialog.shopType=dialog.shopTypeResult.getData().get(0);
                    break;
                case 2:
                    MessageToast.getInstance(dialog.getContext()).show(dialog.shopTypeResult.getMsg());
                    break;
                case 3:
                    FailureToast.getInstance(dialog.getContext()).show();
                    break;
            }
        }
    }


    public ShopTypeDialog(@NonNull Context context, int themeResId, OnShopTypeSelectListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.onShopTypeSelectListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shop_type);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext);
        params.height = ScreentUtils.getInstance().getScreenHeight(mContext) / 5 * 2;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initViews();
        shopTypeList();
    }


    private void initViews() {
        shopTypeView = findViewById(R.id.shopTypeView);
        shopTypeView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                shopType = shopTypeResult.getData().get(index);
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
                if (null != onShopTypeSelectListener) {
                    onShopTypeSelectListener.onShopTypeSelect(shopType);
                }
                dismiss();
                break;
        }
    }

    private ShopTypeResult shopTypeResult;

    private void shopTypeList() {
        typeList.clear();
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SHOP_TYPE_LIST);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                shopTypeResult = GsonUtils.GsonToBean(result, ShopTypeResult.class);
                if (shopTypeResult.getCode() == 200 && null != shopTypeResult && null != shopTypeResult.getData() && shopTypeResult.getData().size() > 0) {
                    for (ShopType shopType : shopTypeResult.getData()) {
                        typeList.add(shopType.getName());
                    }
                    Message message = myHandler.obtainMessage(1);
                    message.sendToTarget();
                } else {
                    Message message = myHandler.obtainMessage(2);
                    message.sendToTarget();
                }

                System.out.println("shopTypeList: " + shopTypeResult.getData().size());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(3);
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

    private OnShopTypeSelectListener onShopTypeSelectListener;

    public interface OnShopTypeSelectListener {
        void onShopTypeSelect(ShopType shopType);
    }
}
