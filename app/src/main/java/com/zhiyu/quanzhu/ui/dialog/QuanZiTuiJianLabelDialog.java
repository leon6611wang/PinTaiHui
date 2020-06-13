package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leon.chic.utils.SPUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.QuanZiTuiJianDaoHang;
import com.zhiyu.quanzhu.model.result.QuanZiTuiJianDaoHangResult;
import com.zhiyu.quanzhu.ui.adapter.QuanZiTuiJianTuiJianLabelRecyclerAdapter;
import com.zhiyu.quanzhu.ui.adapter.QuanZiTuiJianWoDeLabelRecyclerAdapter;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class QuanZiTuiJianLabelDialog extends Dialog implements View.OnClickListener, QuanZiTuiJianWoDeLabelRecyclerAdapter.OnLabelDeleteListener,
        QuanZiTuiJianWoDeLabelRecyclerAdapter.OnLabelGoToListener, QuanZiTuiJianTuiJianLabelRecyclerAdapter.OnLabelAddListener {
    private Context context;
    private LinearLayout closeLayout;
    private RecyclerView wodeRecyclerView, tuijianRecyclerView;
    private QuanZiTuiJianWoDeLabelRecyclerAdapter wodeAdapter;
    private QuanZiTuiJianTuiJianLabelRecyclerAdapter tuijianAdapter;
    private List<String> wodelabel_list = new ArrayList<>();
    private List<String> tuijianlabel_list = new ArrayList<>();
    private TextView editButtonTextView;
    private String cityName;

    public QuanZiTuiJianLabelDialog(@NonNull Context context, int themeResId, OnDaoHangCallbackListener listener) {
        super(context, themeResId);
        this.context = context;
        this.onDaoHangCallbackListener = listener;
        cityName = SPUtils.getInstance().getLocationCity(BaseApplication.applicationContext);
    }

    public void setCityName(String city_name) {
        this.cityName = city_name;
        allDaoHangList();
        myDaoHangList();
    }

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<QuanZiTuiJianLabelDialog> dialogWeakReference;

        public MyHandler(QuanZiTuiJianLabelDialog dialog) {
            dialogWeakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            QuanZiTuiJianLabelDialog dialog = dialogWeakReference.get();
            switch (msg.what) {
                case 1:
                    dialog.tuijianAdapter.setList(dialog.allList);
                    break;
                case 2:
                    dialog.wodeAdapter.setList(dialog.myList);
                    break;
                case 3:
                    Toast.makeText(dialog.context, dialog.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (dialog.baseResult.getCode() == 200) {
                        dialog.myList.add(dialog.daoHang);
                        dialog.wodeAdapter.setList(dialog.myList);
                        dialog.allList.remove(dialog.daoHang);
                        dialog.tuijianAdapter.setList(dialog.allList);
                    }
                    break;
                case 4:
                    Toast.makeText(dialog.context, dialog.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (dialog.baseResult.getCode() == 200) {
                        dialog.myList.remove(dialog.daoHang);
                        dialog.wodeAdapter.setList(dialog.myList);
                        dialog.allList.add(dialog.daoHang);
                        dialog.tuijianAdapter.setList(dialog.allList);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_quanzi_tuijian_label);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(context);
        params.height = ScreentUtils.getInstance().getScreenHeight(context);
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initViews();

    }

    @Override
    public void show() {
        super.show();
    }



    @Override
    public void dismiss() {
        super.dismiss();
        if (null != onDaoHangCallbackListener) {
            onDaoHangCallbackListener.onDaoHangCallback(myList);
        }
    }


    private void initViews() {
        closeLayout = findViewById(R.id.closeLayout);
        closeLayout.setOnClickListener(this);
        wodeRecyclerView = findViewById(R.id.wodeRecyclerView);
        tuijianRecyclerView = findViewById(R.id.tuijianRecyclerView);
        wodeAdapter = new QuanZiTuiJianWoDeLabelRecyclerAdapter(context);
        wodeAdapter.setOnLabelDeleteListener(this);
        wodeAdapter.setOnLabelGoToListener(this);
        tuijianAdapter = new QuanZiTuiJianTuiJianLabelRecyclerAdapter();
        tuijianAdapter.setOnLabelAddListener(this);
        GridLayoutManager wodeGridManager = new GridLayoutManager(context, 4);
        GridLayoutManager tuijianGridManager = new GridLayoutManager(context, 4);
        wodeRecyclerView.setLayoutManager(wodeGridManager);
        wodeRecyclerView.setAdapter(wodeAdapter);
        tuijianRecyclerView.setLayoutManager(tuijianGridManager);
        tuijianRecyclerView.setAdapter(tuijianAdapter);
        editButtonTextView = findViewById(R.id.editButtonTextView);
        editButtonTextView.setOnClickListener(this);
    }

    private boolean isEdit = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeLayout:
                dismiss();
                break;
            case R.id.editButtonTextView:
                if (!isEdit) {
                    isEdit = true;
                    wodeAdapter.setIsEdit(true);
                    editButtonTextView.setText("确定");
                } else {
                    isEdit = false;
                    wodeAdapter.setIsEdit(false);
                    editButtonTextView.setText("编辑");
                }
                break;
        }
    }


    @Override
    public void onLabelDelete(int position) {
        daoHang = myList.get(position);
        deleteDaoHang(daoHang);
    }

    @Override
    public void onLabelGoTo(int position) {
        Log.i("TuiJianLabel", "goto position: " + position);
    }

    private QuanZiTuiJianDaoHang daoHang;

    @Override
    public void onLabelAdd(int position) {
        daoHang = allList.get(position);
        addDaoHang(daoHang);
    }


    private QuanZiTuiJianDaoHangResult daoHangResult;
    private List<QuanZiTuiJianDaoHang> allList;

    private void allDaoHangList() {
        if (null != allList) {
            allList.clear();
        }
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ALL_DAO_HANG_LIST);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                daoHangResult = GsonUtils.GsonToBean(result, QuanZiTuiJianDaoHangResult.class);
                if (null != daoHangResult && null != daoHangResult.getData() && null != daoHangResult.getData().getList()) {
                    allList = daoHangResult.getData().getList();
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
                System.out.println("all daohang allList: " + daoHangResult.getData().getList().size());
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

    private QuanZiTuiJianDaoHangResult daoHangResult2;
    private List<QuanZiTuiJianDaoHang> myList;

    private void myDaoHangList() {
        if (null != myList) {
            myList.clear();
        }
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.MY_DAO_HANG_LIST);
        params.addBodyParameter("city_name", cityName);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                daoHangResult2 = GsonUtils.GsonToBean(result, QuanZiTuiJianDaoHangResult.class);
                if (null != daoHangResult2 && null != daoHangResult2.getData() && null != daoHangResult2.getData().getList()) {
                    myList = daoHangResult2.getData().getList();
                }
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
                System.out.println("我的导航列表: " + result);
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

    private BaseResult baseResult;

    private void addDaoHang(QuanZiTuiJianDaoHang daoHang) {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ADD_DAO_HANG);
        params.addBodyParameter("id", String.valueOf(daoHang.getId()));
        params.addBodyParameter("name", daoHang.getName());
        params.addBodyParameter("parent_id", String.valueOf(daoHang.getParent_id()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(3);
                message.sendToTarget();
                System.out.println("add daohang: " + result);
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

    private void deleteDaoHang(QuanZiTuiJianDaoHang daoHang) {
        System.out.println("id: " + daoHang.getId());
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.DELETE_DAO_HANG);
        params.addBodyParameter("id", String.valueOf(daoHang.getId()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(4);
                message.sendToTarget();
                System.out.println("delete daohang: " + result);
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

    private OnDaoHangCallbackListener onDaoHangCallbackListener;

    public interface OnDaoHangCallbackListener {
        void onDaoHangCallback(List<QuanZiTuiJianDaoHang> list);
    }
}
