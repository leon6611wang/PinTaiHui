package com.zhiyu.quanzhu.ui.widget.rongfrend;

import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * 自定义名片消息
 */
@MessageTag(value = "app:FrendMessage", flag = MessageTag.NONE)
public class FrendMessage extends MessageContent {
    private String image;//头像
    private String name;//姓名
    private String title;//职务

    public FrendMessage(){

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FrendMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("title"))
                setTitle(jsonObj.optString("title"));

            if (jsonObj.has("name"))
                setName(jsonObj.optString("name"));

            if (jsonObj.has("image"))
                setImage(jsonObj.optString("image"));

        } catch (JSONException e) {
            Log.d("JSONException", e.getMessage());
        }
    }

    public FrendMessage(Parcel in) {
        setTitle(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setName(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setImage(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("image", this.getImage());
            jsonObj.put("name", this.getName());
            jsonObj.put("title", this.getTitle());

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

    public static final Creator<FrendMessage> CREATOR = new Creator<FrendMessage>() {

        @Override
        public FrendMessage createFromParcel(Parcel source) {
            return new FrendMessage(source);
        }

        @Override
        public FrendMessage[] newArray(int size) {
            return new FrendMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, getTitle());
        ParcelUtils.writeToParcel(dest, getName());
        ParcelUtils.writeToParcel(dest, getImage());
    }
}
