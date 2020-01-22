package com.zhiyu.quanzhu.ui.widget.rongshare;

import android.os.Parcel;
import android.util.Log;

import com.zhiyu.quanzhu.utils.GsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * 自定义分享消息
 */
@MessageTag(value = "app:ShareMessage", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class ShareMessage extends MessageContent {
   private String title;
   private String description;
   private String url;
   private String image_url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public ShareMessage() {

    }

    public ShareMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        ShareMessage om = GsonUtils.GsonToBean(jsonStr, ShareMessage.class);
        setTitle(om.getTitle());
        setUrl(om.getUrl());
        setImage_url(om.getImage_url());
    }

    public ShareMessage(Parcel in) {
        setTitle(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setUrl(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setImage_url(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setDescription(ParcelUtils.readFromParcel(in));
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("title", this.getTitle());
            jsonObj.put("url", this.getUrl());
            jsonObj.put("image_url", this.getImage_url());
            jsonObj.put("description", this.getDescription());

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

    public static final Creator<ShareMessage> CREATOR = new Creator<ShareMessage>() {

        @Override
        public ShareMessage createFromParcel(Parcel source) {
            return new ShareMessage(source);
        }

        @Override
        public ShareMessage[] newArray(int size) {
            return new ShareMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, getTitle());
        ParcelUtils.writeToParcel(dest, getUrl());
        ParcelUtils.writeToParcel(dest, getImage_url());
        ParcelUtils.writeToParcel(dest, getDescription());
    }


}
