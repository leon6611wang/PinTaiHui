package com.leon.chic.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {
    private final String TAG = "SPUtils";
    private static SPUtils utils;

    public static SPUtils getInstance() {
        if (null == utils) {
            synchronized (SPUtils.class) {
                utils = new SPUtils();
            }
        }
        return utils;
    }

    /**
     * 用户信息
     */
    private final String USER_ID = "user_id";
    private final String USER_NAME = "user_name";
    private final String USER_AVATAR = "user_avatar";
    private final String USER_PHONE_NUM = "user_phone_num";
    private final String USER_IM_TOKEN = "user_im_token";
    private final String USER_TOKEN = "user_token";
    private final String USER_BIND_PHONE = "user_bind_phone";
    private final String USER_HAS_PWD = "user_has_pwd";
    private final String USER_FILL_PROFILE = "user_fill_profile";
    private final String USER_CHOOSE_INTEREST = "user_choose_interest";
    private final String USER_IS_LOGIN = "user_is_login";
    private final String USER_BIND_TYPE = "user_bind_type";
    private SharedPreferences user_sp;
    private SharedPreferences.Editor user_editor;


    private void initUserSp(Context context) {
        if (null == user_sp) {
            user_sp = context.getSharedPreferences("user_sp", Context.MODE_PRIVATE);
        }
        if (null == user_editor) {
            user_editor = user_sp.edit();
        }
    }

    //1 qq 2 wx 3 phone 4 mini
    public static final int QQ = 1;
    public static final int WX = 2;
    public static final int PHONE = 3;
    public static final int MINI = 4;

    public void setUserBindType(Context context, int type) {
        initUserSp(context);
        user_editor.putInt(USER_BIND_TYPE, type);
        user_editor.commit();
    }

    public int getUserBindType(Context context) {
        initUserSp(context);
        return user_sp.getInt(USER_BIND_TYPE, 0);
    }

    public void userHasPwd(Context context) {
        initUserSp(context);
        user_editor.putBoolean(USER_HAS_PWD, true);
        user_editor.commit();
    }

    public void userLogin(Context context) {
        initUserSp(context);
        user_editor.putBoolean(USER_IS_LOGIN, true);
        user_editor.commit();
    }

    public void userLogout(Context context) {
        initUserSp(context);
        user_editor.putBoolean(USER_IS_LOGIN, false);
        user_editor.commit();
        clearUserSP(context);
    }


    public boolean userIsLogin(Context context) {
        initUserSp(context);
        return user_sp.getBoolean(USER_IS_LOGIN, false);
    }

    public void userFillProfile(Context context) {
        initUserSp(context);
        user_editor.putBoolean(USER_FILL_PROFILE, true);
        user_editor.commit();
    }

    public void userBindPhone(Context context) {
        initUserSp(context);
        user_editor.putBoolean(USER_BIND_PHONE, true);
        user_editor.commit();
    }

    public void userChooseInterest(Context context) {
        initUserSp(context);
        user_editor.putBoolean(USER_CHOOSE_INTEREST, true);
        user_editor.commit();
    }

    public boolean getUserHasPwd(Context context) {
        initUserSp(context);
        return user_sp.getBoolean(USER_HAS_PWD, false);
    }

    public boolean getUserFillProfile(Context context) {
        initUserSp(context);
        return user_sp.getBoolean(USER_FILL_PROFILE, false);
    }

    public boolean getUserBindPhone(Context context) {
        initUserSp(context);
        return user_sp.getBoolean(USER_BIND_PHONE, false);
    }

    public boolean getUserChooseInterest(Context context) {
        initUserSp(context);
        return user_sp.getBoolean(USER_CHOOSE_INTEREST, false);
    }

    public void saveUserInfoStatus(Context context, boolean pwd, boolean phone, boolean profile, boolean interest) {
        initUserSp(context);
        user_editor.putBoolean(USER_HAS_PWD, pwd);
        user_editor.putBoolean(USER_BIND_PHONE, phone);
        user_editor.putBoolean(USER_FILL_PROFILE, profile);
        user_editor.putBoolean(USER_CHOOSE_INTEREST, interest);
        user_editor.commit();
    }

    public void saveUserPhoneNum(Context context, String phoneNum) {
        initUserSp(context);
        user_editor.putString(USER_PHONE_NUM, phoneNum);
        user_editor.commit();
    }

    public String getUserPhoneNum(Context context) {
        initUserSp(context);
        return user_sp.getString(USER_PHONE_NUM, null);
    }

    public void saveUserInfo(Context context, int user_id, String user_name, String user_avatar) {
//        System.out.println("saveUserInfo: " + user_id + " , " + user_name + " , " + user_avatar);
        initUserSp(context);
        user_editor.putInt(USER_ID, user_id);
        user_editor.putString(USER_NAME, user_name);
        user_editor.putString(USER_AVATAR, user_avatar);
        user_editor.commit();
    }

    public void saveIMToken(Context context, String im_token) {
        initUserSp(context);
        user_editor.putString(USER_IM_TOKEN, im_token);
        user_editor.commit();
    }

    public String getIMToken(Context context) {
        initUserSp(context);
        return user_sp.getString(USER_IM_TOKEN, null);
    }

    public void logout(Context context) {
        initUserSp(context);
        user_editor.putInt(USER_ID, 0);
        user_editor.putString(USER_NAME, null);
        user_editor.putString(USER_TOKEN, null);
        user_editor.putString(USER_AVATAR, null);
        user_editor.putString(USER_IM_TOKEN, null);
        user_editor.putBoolean(USER_HAS_PWD, false);
        user_editor.putBoolean(USER_BIND_PHONE, false);
        user_editor.putBoolean(USER_FILL_PROFILE, false);
        user_editor.putBoolean(USER_CHOOSE_INTEREST, false);
        user_editor.commit();
        isLogin(context);
    }

    public int getUserId(Context context) {
        initUserSp(context);
        int user_id = user_sp.getInt(USER_ID, 0);
        return user_id;
    }

    public void saveUserId(Context context, int userId) {
        initUserSp(context);
        user_editor.putInt(USER_ID, userId);
        user_editor.commit();
    }

    public String getUserName(Context context) {
        initUserSp(context);
        return user_sp.getString(USER_NAME, null);
    }

    public String getUserAvatar(Context context) {
        initUserSp(context);
        return user_sp.getString(USER_AVATAR, null);
    }

    public void saveUserAvatar(Context context, String avatar) {
        initUserSp(context);
        user_editor.putString(USER_AVATAR, avatar);
        user_editor.commit();
    }

    public void saveUserName(Context context, String name) {
        initUserSp(context);
        user_editor.putString(USER_NAME, name);
        user_editor.commit();
    }

    public boolean isLogin(Context context) {
        initUserSp(context);
        String userToken = user_sp.getString(USER_TOKEN, null);
        boolean islogin = false;
        if (null != userToken && !"".equals(userToken)) {
            islogin = true;
        }
        return islogin;
    }

    public void saveUserToken(Context context, String userToken) {
        initUserSp(context);
        user_editor.putString(USER_TOKEN, userToken);
        user_editor.commit();
    }

    public String getUserToken(Context context) {
        initUserSp(context);
        return user_sp.getString(USER_TOKEN, null);
    }

    public void clearUserSP(Context context) {
        initUserSp(context);
        user_editor.putInt(USER_ID, 0);
        user_editor.putString(USER_NAME, null);
        user_editor.putString(USER_TOKEN, null);
        user_editor.putString(USER_AVATAR, null);
        user_editor.putString(USER_IM_TOKEN, null);
        user_editor.putBoolean(USER_HAS_PWD, false);
        user_editor.putBoolean(USER_BIND_PHONE, false);
        user_editor.putBoolean(USER_FILL_PROFILE, false);
        user_editor.putBoolean(USER_CHOOSE_INTEREST, false);
        user_editor.commit();
    }

    /**
     * 引导页
     */
    private final String GUIDE_SHOW = "guide_show";
    private SharedPreferences guide_sp;
    private SharedPreferences.Editor guide_editor;

    private void initGuideSp(Context context) {
        if (null == guide_sp) {
            guide_sp = context.getSharedPreferences("guide_sp", Context.MODE_PRIVATE);
        }
        if (null == guide_editor) {
            guide_editor = guide_sp.edit();
        }
    }

    public void saveGuide(Context context) {
        initGuideSp(context);
        guide_editor.putBoolean(GUIDE_SHOW, true);
        guide_editor.commit();
    }

    public boolean guideShow(Context context) {
        initGuideSp(context);
        return guide_sp.getBoolean(GUIDE_SHOW, false);
    }

    private static final String LOCATION_SP = "location_sp";
    private static final String LOCATION_PROVINCE = "location_province";
    private static final String LOCATION_CITY = "location_city";
    private static SharedPreferences location_sp;
    private static SharedPreferences.Editor location_editor;

    private void initLocationSP(Context context) {
        if (null == location_sp) {
            location_sp = context.getSharedPreferences(LOCATION_SP, Context.MODE_PRIVATE);
            location_editor = location_sp.edit();
        }
    }

    public void saveLocation(Context context, String province, String city) {
        initLocationSP(context);
        location_editor.putString(LOCATION_PROVINCE, province);
        location_editor.putString(LOCATION_CITY, city);
        location_editor.commit();
    }

    public String getLocationProvince(Context context) {
        initLocationSP(context);
        String province_name = location_sp.getString(LOCATION_PROVINCE, null);
//        System.out.println("province_name: " + province_name);
        return province_name;
    }

    public String getLocationCity(Context context) {
        initLocationSP(context);
        String city_name = location_sp.getString(LOCATION_CITY, null);
//        System.out.println("city_name: " + city_name);
        return city_name;
    }

    /**
     * 消息免打扰
     */
    private SharedPreferences silence_sp;
    private SharedPreferences.Editor silence_editor;
    private final String SILENCE_SP = "silence_sp";
    private final String SILENCE_EDITOR = "silence_editor";
    private final String SILENCE_TOTAL = "silence_total";
    private final String SILENCE_XIAOMISHU = "silence_xiaomishu";
    private final String SILENCE_QUANYOU = "silence_quanyou";
    private final String SILENCE_QUANZI = "silence_quanzi";
    private final String SILENCE_ZHIFU = "silence_zhifu";
    private final String SILENCE_KAQUAN = "silence_kaquan";
    private final String SILENCE_DIANPU = "silence_dianpu";
    private final String SILENCE_TOUSU = "silence_tousu";

    private void initSilenceSp(Context context) {
        if (null == silence_sp) {
            silence_sp = context.getSharedPreferences(SILENCE_SP, Context.MODE_PRIVATE);
            silence_editor = silence_sp.edit();
        }
    }

    public void setSilenceTotal(Context context) {
        initSilenceSp(context);
        silence_editor.putBoolean(SILENCE_TOTAL, true);
        silence_editor.commit();
    }

    private boolean silenceTotal(Context context) {
        initSilenceSp(context);
        return silence_sp.getBoolean(SILENCE_TOTAL, false);
    }

    public void setSilenceXiaomishu(Context context) {
        initSilenceSp(context);
        silence_editor.putBoolean(SILENCE_XIAOMISHU, true);
        silence_editor.commit();
    }

    private boolean silenceXiaomishu(Context context) {
        initSilenceSp(context);
        return silence_sp.getBoolean(SILENCE_XIAOMISHU, false);
    }

    public void setSilenceQuanyou(Context context) {
        initSilenceSp(context);
        silence_editor.putBoolean(SILENCE_QUANYOU, true);
        silence_editor.commit();
    }

    private boolean silenceQuanyou(Context context) {
        initSilenceSp(context);
        return silence_sp.getBoolean(SILENCE_QUANYOU, false);
    }

    public void setSilenceQuanzi(Context context) {
        initSilenceSp(context);
        silence_editor.putBoolean(SILENCE_QUANZI, true);
        silence_editor.commit();
    }

    private boolean silenceQuanzi(Context context) {
        initSilenceSp(context);
        return silence_sp.getBoolean(SILENCE_QUANZI, false);
    }

    public void setSilenceZhifu(Context context) {
        initSilenceSp(context);
        silence_editor.putBoolean(SILENCE_ZHIFU, true);
        silence_editor.commit();
    }

    private boolean silenceZhifu(Context context) {
        initSilenceSp(context);
        return silence_sp.getBoolean(SILENCE_ZHIFU, false);
    }

    public void setSilenceKaquan(Context context) {
        initSilenceSp(context);
        silence_editor.putBoolean(SILENCE_KAQUAN, true);
        silence_editor.commit();
    }

    private boolean silenceKaquan(Context context) {
        initSilenceSp(context);
        return silence_sp.getBoolean(SILENCE_KAQUAN, false);
    }

    public void setSilenceDianpu(Context context) {
        initSilenceSp(context);
        silence_editor.putBoolean(SILENCE_DIANPU, true);
        silence_editor.commit();
    }

    private boolean silenceDianpu(Context context) {
        initSilenceSp(context);
        return silence_sp.getBoolean(SILENCE_DIANPU, false);
    }

    public void setSilenceTousu(Context context) {
        initSilenceSp(context);
        silence_editor.putBoolean(SILENCE_TOUSU, true);
        silence_editor.commit();
    }

    private boolean silenceTousu(Context context) {
        initSilenceSp(context);
        return silence_sp.getBoolean(SILENCE_TOUSU, false);
    }

    public void setSilence(Context context, int s0, int s1, int s2, int s3, int s4, int s5, int s6, int s7) {
        initSilenceSp(context);
        silence_editor.putBoolean(SILENCE_XIAOMISHU, s0 == 1);
        silence_editor.putBoolean(SILENCE_QUANYOU, s1 == 1);
        silence_editor.putBoolean(SILENCE_QUANZI, s2 == 1);
        silence_editor.putBoolean(SILENCE_ZHIFU, s3 == 1);
        silence_editor.putBoolean(SILENCE_KAQUAN, s4 == 1);
        silence_editor.putBoolean(SILENCE_DIANPU, s5 == 1);
        silence_editor.putBoolean(SILENCE_TOUSU, s6 == 1);
        silence_editor.putBoolean(SILENCE_TOTAL, s7 == 1);
        silence_editor.commit();
    }

    public boolean messageSilence(Context context, int system_type) {
        boolean isSilence = silenceTotal(context);
        if (!isSilence) {
            switch (system_type) {
                case MessageTypeUtils.XIAO_MI_SHU:
                    isSilence = silenceXiaomishu(context);
                    break;
                case MessageTypeUtils.QUAN_YOU_QING_QIU:
                    isSilence = silenceQuanyou(context);
                    break;
                case MessageTypeUtils.QUAN_ZI_SHEN_HE:
                    isSilence = silenceQuanzi(context);
                    break;
                case MessageTypeUtils.FU_KUAN_TONG_ZHI:
                    isSilence = silenceZhifu(context);
                    break;
                case MessageTypeUtils.KA_QUAN_TONG_ZHI:
                    isSilence = silenceKaquan(context);
                    break;
                case MessageTypeUtils.GUAN_ZHU_DIAN_PU:
                    isSilence = silenceDianpu(context);
                    break;
                case MessageTypeUtils.TOU_SU_FAN_KUI:
                    isSilence = silenceTousu(context);
                    break;
            }
        }
        return isSilence;
    }


}
