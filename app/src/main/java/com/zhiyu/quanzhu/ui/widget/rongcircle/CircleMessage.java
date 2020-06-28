package com.zhiyu.quanzhu.ui.widget.rongcircle;

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
@MessageTag(value = "app:CircleMessage", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class CircleMessage extends MessageContent {
    private String name;
    private String user_name;
    private String avatar;
    private String city_name;
    private String two_industry;
    private String days;
    private String id;
    private String thumb;
    private String pnum;
    private String fnum;
    private String descirption;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getTwo_industry() {
        return two_industry;
    }

    public void setTwo_industry(String two_industry) {
        this.two_industry = two_industry;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getPnum() {
        return pnum;
    }

    public void setPnum(String pnum) {
        this.pnum = pnum;
    }

    public String getFnum() {
        return fnum;
    }

    public void setFnum(String fnum) {
        this.fnum = fnum;
    }

    public String getDescirption() {
        return descirption;
    }

    public void setDescirption(String descirption) {
        this.descirption = descirption;
    }

    public CircleMessage() {

    }

    public CircleMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("user_name"))
                setUser_name(jsonObj.optString("user_name"));

            if (jsonObj.has("name"))
                setName(jsonObj.optString("name"));

            if (jsonObj.has("avatar"))
                setAvatar(jsonObj.optString("avatar"));

            if (jsonObj.has("city_name"))
                setCity_name(jsonObj.optString("city_name"));

            if (jsonObj.has("two_industry"))
                setTwo_industry(jsonObj.optString("two_industry"));

            if (jsonObj.has("thumb"))
                setThumb(jsonObj.optString("thumb"));

            if (jsonObj.has("descirption"))
                setDescirption(jsonObj.optString("descirption"));

            if (jsonObj.has("id"))
                setId(jsonObj.optString("id"));

            if (jsonObj.has("pnum"))
                setPnum(jsonObj.optString("pnum"));

            if (jsonObj.has("fnum"))
                setFnum(jsonObj.optString("fnum"));

            if (jsonObj.has("days"))
                setDays(jsonObj.optString("days"));
        } catch (JSONException e) {
            Log.d("JSONException", e.getMessage());
        }
    }

    public CircleMessage(Parcel in) {
        setUser_name(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setName(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setAvatar(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setCity_name(ParcelUtils.readFromParcel(in));
        setTwo_industry(ParcelUtils.readFromParcel(in));
        setDays(ParcelUtils.readFromParcel(in));
        setId(ParcelUtils.readFromParcel(in));
        setThumb(ParcelUtils.readFromParcel(in));
        setPnum(ParcelUtils.readFromParcel(in));
        setDescirption(ParcelUtils.readFromParcel(in));
        setFnum(ParcelUtils.readFromParcel(in));
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("user_name", this.getUser_name());
            jsonObj.put("name", this.getName());
            jsonObj.put("avatar", this.getAvatar());
            jsonObj.put("city_name", this.getCity_name());
            jsonObj.put("two_industry", this.getTwo_industry());
            jsonObj.put("days", this.getDays());
            jsonObj.put("id", this.getId());
            jsonObj.put("thumb", this.getThumb());
            jsonObj.put("pnum", this.getPnum());
            jsonObj.put("descirption", this.getDescirption());
            jsonObj.put("fnum", this.getFnum());


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

    public static final Creator<CircleMessage> CREATOR = new Creator<CircleMessage>() {

        @Override
        public CircleMessage createFromParcel(Parcel source) {
            return new CircleMessage(source);
        }

        @Override
        public CircleMessage[] newArray(int size) {
            return new CircleMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, getUser_name());
        ParcelUtils.writeToParcel(dest, getName());
        ParcelUtils.writeToParcel(dest, getAvatar());
        ParcelUtils.writeToParcel(dest, getCity_name());
        ParcelUtils.writeToParcel(dest, getTwo_industry());
        ParcelUtils.writeToParcel(dest, getDays());
        ParcelUtils.writeToParcel(dest, getId());
        ParcelUtils.writeToParcel(dest, getThumb());
        ParcelUtils.writeToParcel(dest, getPnum());
        ParcelUtils.writeToParcel(dest, getFnum());
        ParcelUtils.writeToParcel(dest, getDescirption());

    }
}
