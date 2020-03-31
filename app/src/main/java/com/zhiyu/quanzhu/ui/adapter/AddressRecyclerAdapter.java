package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.Address;
import com.zhiyu.quanzhu.ui.activity.AddEditAddressActivity;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.ui.toast.FailureToast;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

public class AddressRecyclerAdapter extends RecyclerView.Adapter<AddressRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Address> list;
    private YNDialog ynDialog;
    private int deletePosition;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<AddressRecyclerAdapter> addressRecyclerAdapterWeakReference;

        public MyHandler(AddressRecyclerAdapter adapter) {
            addressRecyclerAdapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            AddressRecyclerAdapter adapter = addressRecyclerAdapterWeakReference.get();
            switch (msg.what) {
                case 1:
                    MessageToast.getInstance(adapter.context).show(adapter.baseResult.getMsg());
                    break;
                case 2:
                    FailureToast.getInstance(adapter.context).show();
                    break;
            }
        }
    }

    public void setList(List<Address> addressList) {
        this.list = addressList;
        notifyDataSetChanged();
    }

    public AddressRecyclerAdapter(Context context) {
        this.context = context;
        initDialogs();
    }

    private void initDialogs() {
        ynDialog = new YNDialog(context, R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                deleteAddress();
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, phonenumTextView, locationTextView, defaultTextView;
        ImageView editImageView, deleteImageView;
        CardView addressCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phonenumTextView = itemView.findViewById(R.id.phonenumTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            editImageView = itemView.findViewById(R.id.editImageView);
            deleteImageView = itemView.findViewById(R.id.deleteImageView);
            defaultTextView = itemView.findViewById(R.id.defaultTextView);
            addressCardView=itemView.findViewById(R.id.addressCardView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.editImageView.setOnClickListener(new EditOnClick(position));
        holder.deleteImageView.setOnClickListener(new DeleteOnClick(position));
        holder.nameTextView.setText(list.get(position).getName());
        holder.phonenumTextView.setText(list.get(position).getPhone());
        holder.locationTextView.setText(list.get(position).getProvince_name() + " " + list.get(position).getCity_name() + " " + list.get(position).getAddress());
        holder.defaultTextView.setVisibility(list.get(position).getIs_def() == 1 ? View.VISIBLE : View.GONE);
        holder.addressCardView.setOnClickListener(new OnSelectAddressClickListener(position));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private class EditOnClick implements View.OnClickListener {
        private int position;

        public EditOnClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            System.out.println("address_id: " + list.get(position).get_id());
            Intent editIntent = new Intent(context, AddEditAddressActivity.class);
            editIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            editIntent.putExtra("address_id", list.get(position).get_id());
            context.startActivity(editIntent);
        }
    }

    class DeleteOnClick implements View.OnClickListener {
        private int position;

        public DeleteOnClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            ynDialog.show();
            deletePosition = position;
        }
    }

    private BaseResult baseResult;

    private void deleteAddress() {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ADDRESS_DELETE);
        params.addBodyParameter("id", list.get(deletePosition).get_id());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
                System.out.println("deleteAddress: " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
                System.out.println("deleteAddress: " + ex.toString().trim());

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private class OnSelectAddressClickListener implements View.OnClickListener{
        private int position;

        public OnSelectAddressClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(null!=onSelectAddressListener){
                onSelectAddressListener.onSelectAddress(list.get(position));
            }
        }
    }

    private OnSelectAddressListener onSelectAddressListener;
    public void setOnSelectAddressListener(OnSelectAddressListener listener){
        this.onSelectAddressListener=listener;
    }
    public interface OnSelectAddressListener{
        void onSelectAddress(Address address);
    }

}
