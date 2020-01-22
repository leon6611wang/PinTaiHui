package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.GoodsNorm;
import com.zhiyu.quanzhu.model.bean.GoodsNormGroup;
import com.zhiyu.quanzhu.ui.dialog.ShangPinInformationGuiGeDialog;
import com.zhiyu.quanzhu.ui.listener.OnShangPinInformationGuiGePiPeiListener;
import com.zhiyu.quanzhu.ui.widget.LabelsView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zhy.view.flowlayout.TagView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShangPinInformationGuiGeRecyclerAdapter extends RecyclerView.Adapter<ShangPinInformationGuiGeRecyclerAdapter.ViewHolder> implements OnShangPinInformationGuiGePiPeiListener {
    private Context context;
    private int dp_15;
    private List<GoodsNormGroup> guiGeList;
    private Map<Integer, GoodsNorm> map = new HashMap<>();
    private List<Integer> pipeiList;
    private ShangPinInformationGuiGeDialog dialog;
    private MyHandler myHandler;

    private static class MyHandler extends Handler {
        WeakReference<ShangPinInformationGuiGeRecyclerAdapter> shangPinInformationGuiGeRecyclerAdapterWeakReference;

        public MyHandler(ShangPinInformationGuiGeRecyclerAdapter adapter) {
            shangPinInformationGuiGeRecyclerAdapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            ShangPinInformationGuiGeRecyclerAdapter adapter = shangPinInformationGuiGeRecyclerAdapterWeakReference.get();
            switch (msg.what) {
                case 1:
//                    Bundle bundle=msg.getData();
//                    int parentPosition=bundle.getInt("parentPosition",-1);
//                    int childPosition=bundle.getInt("childPosition",-1);
//                    ArrayList<Integer> list=bundle.getIntegerArrayList("list");

                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    public void setData(List<GoodsNormGroup> list) {
        guiGeList = list;
        notifyDataSetChanged();
    }

    public ShangPinInformationGuiGeRecyclerAdapter(Context context, ShangPinInformationGuiGeDialog d) {
        this.context = context;
        this.myHandler = new MyHandler(this);
        this.dialog = d;
        this.dialog.setOnShangPinInformationGuiGePiPeiListener(this);
        dp_15 = (int) context.getResources().getDimension(R.dimen.dp_15);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private LabelsView labelsView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            labelsView = itemView.findViewById(R.id.labelsView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shangpin_information_guige, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.nameTextView.setText(guiGeList.get(position).getName());
        holder.labelsView.setLabels(guiGeList.get(position).getList(), new LabelsView.LabelTextProvider<GoodsNorm>() {
            @Override
            public CharSequence getLabelText(TextView label, int child_position, GoodsNorm data) {
                // label就是标签项，在这里可以对标签项单独设置一些属性，比如文本样式等。
                if (guiGeList.get(position).getList().get(child_position).isSelectable()) {
                    if (guiGeList.get(position).getList().get(child_position).isSelected()) {
                        label.setBackground(context.getResources().getDrawable(R.drawable.shape_guige_selected));
                        label.setTextColor(context.getResources().getColor(R.color.text_color_yellow));
                    } else {
                        label.setBackground(context.getResources().getDrawable(R.drawable.shape_guige_unselected));
                        label.setTextColor(context.getResources().getColor(R.color.text_color_grey));
                    }
                } else {
                    label.setBackground(context.getResources().getDrawable(R.drawable.shape_guige_unselected));
                    label.setTextColor(context.getResources().getColor(R.color.text_color_yilingqu));
                }
                //根据data和position返回label需要显示的数据。
                return data.getNorms_name() + "-" + data.getNorms_id();
            }
        });
        //标签的点击监听
        holder.labelsView.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int child_position) {
                //label是被点击的标签，data是标签所对应的数据，position是标签的位置。
                if (guiGeList.get(position).getList().get(child_position).isSelectable()) {
                    if (null != onGuiGeSelectedListener) {
                        onGuiGeSelectedListener.onGuiGeSelected(position, child_position);
                    }
                }
            }
        });
        //标签的选中监听
        holder.labelsView.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                //label是被选中的标签，data是标签所对应的数据，isSelect是是否选中，position是标签的位置。
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == guiGeList ? 0 : guiGeList.size();
    }

    @Override
    public void onPiPei(int parentPosition, int childPosition, List<Integer> pipeiList) {
        this.pipeiList = pipeiList;
    }

    private OnGuiGeSelectedListener onGuiGeSelectedListener;

    public void setOnGuiGeSelectedListener(OnGuiGeSelectedListener listener) {
        this.onGuiGeSelectedListener = listener;
    }

    public interface OnGuiGeSelectedListener {
        void onGuiGeSelected(int parentPosition, int childPosition);
    }


}
