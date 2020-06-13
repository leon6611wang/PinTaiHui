package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.CommentStar;
import com.zhiyu.quanzhu.model.bean.OrderGoods;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class OrderGoodsCommentsAdapter extends RecyclerView.Adapter<OrderGoodsCommentsAdapter.ViewHolder> {
    private Context context;
    private List<OrderGoods> list;

    public void setList(List<OrderGoods> goodsList) {
        this.list = goodsList;
        notifyDataSetChanged();
    }

    public void setUrlList(int position, List<String> urlList) {
        System.out.println("position: " + position + " , list: " + urlList.toString());
        list.get(position).setUrlList(urlList);
    }

    public List<OrderGoods> getList() {
        return list;
    }

    public OrderGoodsCommentsAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MyRecyclerView mRecyclerView;
        MyRecyclerView starRecyclerview;
        OrderGoodsCommentsStarAdapter adapter;
        List<CommentStar> list = new ArrayList<>();
        TextView scoreTextView, goodsNameTextView,
                goodsNormsTextView;
        NiceImageView goodsImageImageView;
        ImageGridRecyclerAdapter imgAdapter;
        EditText commentContentEditText;

        public ViewHolder(View itemView) {
            super(itemView);
            list.add(new CommentStar(false));
            list.add(new CommentStar(false));
            list.add(new CommentStar(false));
            list.add(new CommentStar(false));
            list.add(new CommentStar(false));
            mRecyclerView = itemView.findViewById(R.id.mRecyclerView);
            starRecyclerview = itemView.findViewById(R.id.starRecyclerview);
            adapter = new OrderGoodsCommentsStarAdapter(context);
            adapter.setList(list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            starRecyclerview.setLayoutManager(layoutManager);
            starRecyclerview.setAdapter(adapter);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
            goodsImageImageView = itemView.findViewById(R.id.goodsImageImageView);
            goodsNameTextView = itemView.findViewById(R.id.goodsNameTextView);
            goodsNormsTextView = itemView.findViewById(R.id.goodsNormsTextView);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(3, (int) context.getResources().getDimension(R.dimen.dp_15), false);
            imgAdapter = new ImageGridRecyclerAdapter(context);
            mRecyclerView.setAdapter(imgAdapter);
            mRecyclerView.addItemDecoration(decoration);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            commentContentEditText = itemView.findViewById(R.id.commentContentEditText);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_goods_comments, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getGoods_img()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.goodsImageImageView);
        holder.goodsNameTextView.setText(list.get(position).getGoods_name());
        holder.goodsNormsTextView.setText(list.get(position).getGoods_normas_name());
        holder.imgAdapter.setData(list.get(position).getCommentImageList());
        holder.commentContentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int lineCount = holder.commentContentEditText.getLineCount();
                if (lineCount > 10) {
                    //发现输入的内容大于最大行数，则删除多余的内容
                    String str = holder.commentContentEditText.getText().toString();
                    str = str.substring(0, str.length() - 1);
                    holder.commentContentEditText.setText(str);
                    holder.commentContentEditText.setSelection(holder.commentContentEditText.getText().length());
                    MessageToast.getInstance(context).show("商品评价最多十行哦~");
                }
                list.get(position).setCommentContent(holder.commentContentEditText.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.adapter.setOnCommentStarListener(new OrderGoodsCommentsStarAdapter.OnCommentStarListener() {
            @Override
            public void onStar(int sum) {
                list.get(position).setScore(sum);
                holder.scoreTextView.setText(sum + ".0");
            }
        });
        holder.imgAdapter.setOnAddImageListener(new ImageGridRecyclerAdapter.OnAddImageListener() {
            @Override
            public void onAddImage() {
                if (null != onAddImageListener) {
                    onAddImageListener.onAddImage(position, list.get(position).getCommentImageList());
                }
            }
        });
        holder.imgAdapter.setOnDeleteImageListener(new ImageGridRecyclerAdapter.OnDeleteImageListener() {
            @Override
            public void onDelete(int childPosition) {
                list.get(position).getCommentImageList().remove(childPosition);
                list.get(position).getUrlList().remove(childPosition);
                holder.imgAdapter.setData(list.get(position).getCommentImageList());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private OnAddImageListener onAddImageListener;

    public void setOnAddImageListener(OnAddImageListener listener) {
        this.onAddImageListener = listener;
    }

    public interface OnAddImageListener {
        void onAddImage(int position, ArrayList<String> list);
    }

}
