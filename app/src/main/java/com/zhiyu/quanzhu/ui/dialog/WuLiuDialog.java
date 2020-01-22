package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.WuLiu;
import com.zhiyu.quanzhu.ui.adapter.WuLiuRecyclerAdapter;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WuLiuDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private RecyclerView mRecyclerView;
    private ImageView closeImageView;
    private WuLiuRecyclerAdapter adapter;
    private List<WuLiu> list=new ArrayList<>();

    public WuLiuDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public WuLiuDialog(@NonNull Context context, int themeResId, OnChoosePhotoListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.onChoosePhotoListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wuliu);
        initData();
        initViews();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext)/20*18;
        params.height = ScreentUtils.getInstance().getScreenHeight(mContext) / 5 * 3;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.CENTER);
//        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void initData(){
        list.add(new WuLiu("上海仓库发货","2019-11-12 08:23:11"));
        list.add(new WuLiu("嘉兴中转站中转，下一站：杭州中转站","2019-11-13 02:17:41"));
        list.add(new WuLiu("杭州中转站分发","2019-11-13 04:40:23"));
        list.add(new WuLiu("黄山中转站","2019-11-13 09:37:15"));
        list.add(new WuLiu("池州中转站，下一站安庆中转站","2019-11-13 13:46:12"));
        list.add(new WuLiu("合肥站，正在分发到包河区中转站","2019-11-13 21:37:20"));
        list.add(new WuLiu("正在分发到滨湖新区烟墩街道","2019-11-14 04:23:50"));
        list.add(new WuLiu("正在派送，派送员宋江，电话:18757597425","2019-11-14 09:17:31"));
        list.add(new WuLiu("蓝鼎观湖苑快递已签收，快递在2栋102，取件码:208564","2019-11-14 11：27：43"));
        Collections.reverse(list);
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter=new WuLiuRecyclerAdapter(mContext);
        adapter.setData(list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        closeImageView = findViewById(R.id.closeImageView);
        closeImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeImageView:
                dismiss();
                break;
        }
    }

    private OnChoosePhotoListener onChoosePhotoListener;

    public interface OnChoosePhotoListener {
        void xiangce();

        void paizhao();
    }
}
