package com.zhiyu.quanzhu.ui.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.ShopInfoGoodsType;
import com.zhiyu.quanzhu.model.result.ShopInfoGoodsTypeResult;
import com.zhiyu.quanzhu.ui.adapter.ShopInfoGoodsTypeGridAdapter;
import com.zhiyu.quanzhu.ui.adapter.ShopInfoGoodsTypeListAdapter;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

/**
 * 店铺详情-商品分类
 */
public class ShopInfoGoodsTypeWindow extends PopupWindow implements ShopInfoGoodsTypeGridAdapter.OnGoodsTypeSelectListener {
    private Context context;
    private int popupWidth, popupHeight;
    private ListView mListView;
    private ShopInfoGoodsTypeListAdapter adapter;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<ShopInfoGoodsTypeWindow> windowWeakReference;

        public MyHandler(ShopInfoGoodsTypeWindow window) {
            windowWeakReference = new WeakReference<>(window);
        }

        @Override
        public void handleMessage(Message msg) {
            ShopInfoGoodsTypeWindow window = windowWeakReference.get();
            switch (msg.what) {
                case 1:
                    window.adapter.setList(window.goodsTypeResult.getData().getList());
                    break;
            }
        }
    }

    public ShopInfoGoodsTypeWindow(Context context,OnGoodsTypeSelectListener listener) {
        this.context = context;
        this.onGoodsTypeSelectListener=listener;
    }

    public void setData(int height, String shop_id) {
        this.popupHeight = height;
        this.shop_id = shop_id;
        ShopInfoGoodsTypeGridAdapter.setOnGoodsTypeSelectListener(this);
        initalize();
        shopGoodsType();
    }

    private void initalize() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popupwindow_shop_info_goods_type, null);
        mListView = view.findViewById(R.id.mListView);
        adapter = new ShopInfoGoodsTypeListAdapter(context);
        mListView.setAdapter(adapter);
        setContentView(view);
        initWindow();
    }

    private void initWindow() {
        popupWidth = ScreentUtils.getInstance().getScreenWidth(context);
//        popupHeight = ScreentUtils.getInstance().getScreenHeight(context) / 2;
        this.setWidth(popupWidth);
        this.setHeight(popupHeight);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
//        this.setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
        this.update();
        //实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
//        this.setBackgroundDrawable(dw);
//        backgroundAlpha((Activity) context, 0.8f);//0.0-1.0
//        this.setOnDismissListener(new OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                backgroundAlpha((Activity) context, 1f);
//            }
//        });

        this.setAnimationStyle(R.style.popupwindow_shop_info_goods_type);
    }

    //设置添加屏幕的背景透明度
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    @Override
    public void onGoodsTypeSelectListener(ShopInfoGoodsType goodsType) {
        if (null != goodsTypeResult && null != goodsTypeResult.getData().getList() && goodsTypeResult.getData().getList().size() > 0) {
            for (ShopInfoGoodsType type : goodsTypeResult.getData().getList()) {
                if (null != type && null != type.getChild() && type.getChild().size() > 0) {
                    for (ShopInfoGoodsType type1 : type.getChild()) {
                        type1.setSelected(false);
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
        if(null!=onGoodsTypeSelectListener){
            onGoodsTypeSelectListener.onGoodsTypeSelect(goodsType);
            dismiss();
        }
    }

    public void showAtBottom(View view) {
        //弹窗位置设置
        showAsDropDown(view, Math.abs((view.getWidth() - getWidth()) / 2), 10);
    }

    public void showAtUp(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        //在控件上方显示    向上移动y轴是负数
        showAtLocation(view, Gravity.NO_GRAVITY, (location[0] + view.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
    }

    private String shop_id;

    private ShopInfoGoodsTypeResult goodsTypeResult;

    private void shopGoodsType() {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SHOP_GOODS_TYPE);
        params.addBodyParameter("shop_id", shop_id);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("店铺商品分类: " + result);
                goodsTypeResult = GsonUtils.GsonToBean(result, ShopInfoGoodsTypeResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
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

    private OnGoodsTypeSelectListener onGoodsTypeSelectListener;
    public interface OnGoodsTypeSelectListener{
        void onGoodsTypeSelect(ShopInfoGoodsType goodsType);
    }
}
