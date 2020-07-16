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
    //    private String shareTitle;
//    private String shareContent;
//    private String shareUrl;
//    private String shareImageUrl;
//    private String shareType;
//    private String shareTypeId;
    private String content;

//    public String getShareTitle() {
//        return shareTitle;
//    }
//
//    public void setShareTitle(String shareTitle) {
//        this.shareTitle = shareTitle;
//    }
//
//    public String getShareContent() {
//        return shareContent;
//    }
//
//    public void setShareContent(String shareContent) {
//        this.shareContent = shareContent;
//    }
//
//    public String getShareUrl() {
//        return shareUrl;
//    }
//
//    public void setShareUrl(String shareUrl) {
//        this.shareUrl = shareUrl;
//    }
//
//    public String getShareImageUrl() {
//        return shareImageUrl;
//    }
//
//    public void setShareImageUrl(String shareImageUrl) {
//        this.shareImageUrl = shareImageUrl;
//    }
//
//    public String getShareType() {
//        return shareType;
//    }
//
//    public void setShareType(String shareType) {
//        this.shareType = shareType;
//    }
//
//    public String getShareTypeId() {
//        return shareTypeId;
//    }
//
//    public void setShareTypeId(String shareTypeId) {
//        this.shareTypeId = shareTypeId;
//    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public ShareMessage() {

    }

//    public ShareMessage(byte[] data) {
//        String jsonStr = null;
//        try {
//            jsonStr = new String(data, "UTF-8");
//        } catch (UnsupportedEncodingException e1) {
//            e1.printStackTrace();
//        }
//        ShareMessage om = GsonUtils.GsonToBean(jsonStr, ShareMessage.class);
//        setShareTitle(om.getShareTitle());
//        setShareUrl(om.getShareUrl());
//        setShareContent(om.getShareContent());
//        setShareImageUrl(om.getShareImageUrl());
//        setShareType(om.getShareType());
//        setShareTypeId(om.getShareTypeId());
//    }

    public ShareMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
//            if (jsonObj.has("shareTitle"))
//                setShareTitle(jsonObj.optString("shareTitle"));
//
//            if (jsonObj.has("shareContent"))
//                setShareContent(jsonObj.optString("shareContent"));
//
//            if (jsonObj.has("shareUrl"))
//                setShareUrl(jsonObj.optString("shareUrl"));
//
//            if (jsonObj.has("shareImageUrl"))
//                setShareImageUrl(jsonObj.optString("shareImageUrl"));
//
//            if (jsonObj.has("shareType"))
//                setShareType(jsonObj.optString("shareType"));
//
//            if (jsonObj.has("shareTypeId"))
//                setShareTypeId(jsonObj.optString("shareTypeId"));

            if (jsonObj.has("content"))
                setContent(jsonObj.optString("content"));

            if (jsonObj.has("title"))
                setContent(jsonObj.optString("title"));

        } catch (JSONException e) {
            Log.d("JSONException", e.getMessage());
        }
    }

    public ShareMessage(Parcel in) {
//        setShareTitle(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
//        setShareUrl(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
//        setShareImageUrl(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
//        setShareContent(ParcelUtils.readFromParcel(in));
//        setShareType(ParcelUtils.readFromParcel(in));
//        setShareTypeId(ParcelUtils.readFromParcel(in));
        setContent(ParcelUtils.readFromParcel(in));
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
//            jsonObj.put("shareTitle", this.getShareTitle());
//            jsonObj.put("shareUrl", this.getShareUrl());
//            jsonObj.put("shareImageUrl", this.getShareImageUrl());
//            jsonObj.put("shareContent", this.getShareContent());
//            jsonObj.put("shareType", this.getShareType());
//            jsonObj.put("shareTypeId", this.getShareTypeId());
            jsonObj.put("content", this.getContent());
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
//        ParcelUtils.writeToParcel(dest, getShareTitle());
//        ParcelUtils.writeToParcel(dest, getShareUrl());
//        ParcelUtils.writeToParcel(dest, getShareImageUrl());
//        ParcelUtils.writeToParcel(dest, getShareContent());
//        ParcelUtils.writeToParcel(dest, getShareType());
//        ParcelUtils.writeToParcel(dest, getShareTypeId());
        ParcelUtils.writeToParcel(dest, getContent());
    }


}
