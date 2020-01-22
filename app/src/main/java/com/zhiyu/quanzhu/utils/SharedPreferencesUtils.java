package com.zhiyu.quanzhu.utils;

import android.content.Context;
import android.content.SharedPreferences;

import io.rong.imlib.model.Conversation;

public class SharedPreferencesUtils {
    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";
    private static final String USER_PHONE_NUMBER = "user_phone_number";
    private static final String USER_HEADER_PIC = "user_header_pic";
    private static final String USER_TOKEN = "user_token";
    private static final String USER_SP = "user_sp";
    private static SharedPreferences user_sp;
    private static SharedPreferences.Editor user_editor;
    private static SharedPreferencesUtils utils;

    public static SharedPreferencesUtils getInstance(Context context) {
        if (null == utils) {
            synchronized (SharedPreferencesUtils.class) {
                utils = new SharedPreferencesUtils();
                user_sp = context.getSharedPreferences(USER_SP, Context.MODE_PRIVATE);
                user_editor = user_sp.edit();

                conversation_sp = context.getSharedPreferences(CONVERSATION_TYPE_SP, Context.MODE_PRIVATE);
                conversation_editor = conversation_sp.edit();

                app_update_sp = context.getSharedPreferences(APP_UPDATE_SP, Context.MODE_PRIVATE);
                app_update_editor = app_update_sp.edit();

            }
        }


        return utils;
    }

    public void saveUser(String user_id, String user_name, String header_pic, String phone_number) {
        user_editor.putString(USER_ID, user_id);
        user_editor.putString(USER_NAME, user_name);
        user_editor.putString(USER_PHONE_NUMBER, phone_number);
        user_editor.putString(USER_HEADER_PIC, header_pic);
        user_editor.commit();
    }

    public void clearUser(){
        user_editor.putString(USER_ID, "");
        user_editor.putString(USER_NAME, "");
        user_editor.putString(USER_PHONE_NUMBER, "");
        user_editor.putString(USER_HEADER_PIC, "");
        user_editor.putString(USER_TOKEN, "");
        user_editor.commit();
    }

    public void saveUserId(String user_id) {
        user_editor.putString(USER_ID, user_id);
        user_editor.commit();
    }

    public void saveUserName(String user_name) {
        user_editor.putString(USER_NAME, user_name);
        user_editor.commit();
    }

    public void saveUserHeaderPic(String header_pic) {
        user_editor.putString(USER_HEADER_PIC, header_pic);
        user_editor.commit();
    }

    public void saveUserPhoneNumber(String phone_number) {
        user_editor.putString(USER_PHONE_NUMBER, phone_number);
        user_editor.commit();
    }

    public void saveUserToken(String user_token) {
        user_editor.putString(USER_TOKEN, user_token);
        user_editor.commit();
    }


    public String getUserId() {
        String userId = user_sp.getString(USER_ID, null);
        return userId;
    }

    public String getUserName() {
        String userName = user_sp.getString(USER_NAME, null);
        return userName;
    }

    public String getUserPhoneNumber() {
        String phoneNumber = user_sp.getString(USER_PHONE_NUMBER, null);
        return phoneNumber;
    }

    public String getUserHeaderPic() {
        String headerPic = user_sp.getString(USER_HEADER_PIC, null);
        return headerPic;
    }

    public String getUserToken() {
        String token = user_sp.getString(USER_TOKEN, null);
        return token;
    }

    /**
     * 聊天室类型:私聊，群聊
     */
    private static final String CONVERSATION_TYPE_SP = "conversation_type_sp";
    private static final String CONVERSATION_TYPE = "conversation_type";
    private static SharedPreferences conversation_sp;
    private static SharedPreferences.Editor conversation_editor;
    public static final String IM_GROUP = Conversation.ConversationType.GROUP.getName().toLowerCase();
    public static final String IM_PRIVATE = Conversation.ConversationType.PRIVATE.getName().toLowerCase();

    public void setConversationType(String conversationType) {
        conversation_editor.putString(CONVERSATION_TYPE, conversationType);
        conversation_editor.commit();
    }

    public String getConversationType() {
        return conversation_sp.getString(CONVERSATION_TYPE, null);
    }

    /**
     * app更新
     */
    private static final String APP_UPDATE_SP = "app_update_sp";
    private static final String APP_VERSION = "app_version";
    //忽略当前版本
    private static final String APP_NEGLECT = "app_neglect";
    //必须更新
    private static final String APP_NECESSARY = "app_necessary";
    private static SharedPreferences app_update_sp;
    private static SharedPreferences.Editor app_update_editor;

    public void saveAppVersion(String version, boolean necessary) {
        app_update_editor.putString(APP_VERSION, version);
        app_update_editor.putBoolean(APP_NECESSARY, necessary);
        app_update_editor.putBoolean(APP_NEGLECT,false);
        app_update_editor.commit();
    }

    public void saveAppNeglect(boolean neglect) {
        app_update_editor.putBoolean(APP_NEGLECT, neglect);
        app_update_editor.commit();
    }

    public String getAppVersion() {
        String version = app_update_sp.getString(APP_VERSION, null);
        return version;
    }

    public boolean getAppNecessary() {
        boolean necessary = app_update_sp.getBoolean(APP_NECESSARY, false);
        return necessary;
    }

    public boolean getNeglect() {
        boolean neglect = app_update_sp.getBoolean(APP_NEGLECT, false);
        return neglect;
    }

}
