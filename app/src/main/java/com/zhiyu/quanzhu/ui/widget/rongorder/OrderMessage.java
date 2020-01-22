package com.zhiyu.quanzhu.ui.widget.rongorder;

import android.os.Parcel;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zhiyu.quanzhu.utils.GsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * 自定义订单消息
 */
@MessageTag(value = "app:ShareMessage", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class OrderMessage extends MessageContent {
    private int all_count;//订单商品总数
    private float all_price;//订单商品总价
    private String buyer_name;//购买人姓名
    private String buyer_phone;//购买人电话
    private String buyer_address;//购买人地址
    private int order_status;//订单状态(未确认/已确认)
    private String order_status_desc;//订单状态说明(未确认/已确认)
    private String order_goods_list;//订单商品列表

    public int getAll_count() {
        return all_count;
    }

    public void setAll_count(int all_count) {
        this.all_count = all_count;
    }

    public float getAll_price() {
        return all_price;
    }

    public void setAll_price(float all_price) {
        this.all_price = all_price;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getBuyer_phone() {
        return buyer_phone;
    }

    public void setBuyer_phone(String buyer_phone) {
        this.buyer_phone = buyer_phone;
    }

    public String getBuyer_address() {
        return buyer_address;
    }

    public void setBuyer_address(String buyer_address) {
        this.buyer_address = buyer_address;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public String getOrder_status_desc() {
        return order_status_desc;
    }

    public void setOrder_status_desc(String order_status_desc) {
        this.order_status_desc = order_status_desc;
    }

    public String getOrder_goods_list() {
        return order_goods_list;
    }

    public void setOrder_goods_list(String order_goods_list) {
        this.order_goods_list = order_goods_list;
    }

    public OrderMessage() {

    }

    public OrderMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        OrderMessage om = GsonUtils.GsonToBean(jsonStr, OrderMessage.class);
        setAll_count(om.getAll_count());
        setAll_price(om.getAll_price());
        setBuyer_name(om.getBuyer_name());
        setBuyer_phone(om.getBuyer_phone());
        setBuyer_address(om.getBuyer_address());
        setOrder_status(om.getOrder_status());
        setOrder_status_desc(om.getOrder_status_desc());
        setOrder_goods_list(om.getOrder_goods_list());
    }

    public OrderMessage(Parcel in) {
        setAll_count(ParcelUtils.readIntFromParcel(in));//该类为工具类，消息属性
        setAll_price(ParcelUtils.readFloatFromParcel(in));//该类为工具类，消息属性
        setBuyer_name(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setBuyer_phone(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setBuyer_address(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setOrder_status(ParcelUtils.readIntFromParcel(in));//该类为工具类，消息属性
        setOrder_status_desc(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setOrder_goods_list(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("all_count", this.getAll_count());
            jsonObj.put("all_price", this.getAll_price());
            jsonObj.put("buyer_name", this.getBuyer_name());
            jsonObj.put("buyer_phone", this.getBuyer_phone());
            jsonObj.put("buyer_address", this.getBuyer_address());
            jsonObj.put("order_status", this.getOrder_status());
            jsonObj.put("order_status_desc", this.getOrder_status_desc());
            jsonObj.put("order_goods_list", this.getOrder_goods_list());

        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final Creator<OrderMessage> CREATOR = new Creator<OrderMessage>() {

        @Override
        public OrderMessage createFromParcel(Parcel source) {
            return new OrderMessage(source);
        }

        @Override
        public OrderMessage[] newArray(int size) {
            return new OrderMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, getAll_count());
        ParcelUtils.writeToParcel(dest, getAll_price());
        ParcelUtils.writeToParcel(dest, getBuyer_name());
        ParcelUtils.writeToParcel(dest, getBuyer_phone());
        ParcelUtils.writeToParcel(dest, getBuyer_address());
        ParcelUtils.writeToParcel(dest, getOrder_status());
        ParcelUtils.writeToParcel(dest, getOrder_status_desc());
        ParcelUtils.writeToParcel(dest, getOrder_goods_list());
    }


    /**
     * 订单商品
     */
    public class OrderGoods {
        private String goods_image;//商品图片
        private String goods_name;//商品名称
        private int goods_count;//商品数量

        public String getGoods_image() {
            return goods_image;
        }

        public void setGoods_image(String goods_image) {
            this.goods_image = goods_image;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public int getGoods_count() {
            return goods_count;
        }

        public void setGoods_count(int goods_count) {
            this.goods_count = goods_count;
        }

    }
}
