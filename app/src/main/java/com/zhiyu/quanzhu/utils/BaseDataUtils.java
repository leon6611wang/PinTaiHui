package com.zhiyu.quanzhu.utils;

import android.os.Message;

import com.leon.chic.dao.CardDao;
import com.leon.chic.dao.IMUserDao;
import com.leon.chic.utils.LogUtils;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.AreaCity;
import com.zhiyu.quanzhu.model.bean.AreaProvince;
import com.zhiyu.quanzhu.model.bean.HobbyDaoParent;
import com.zhiyu.quanzhu.model.bean.IndustryParent;
import com.zhiyu.quanzhu.model.dao.AreaDao;
import com.zhiyu.quanzhu.model.dao.HobbyDao;
import com.zhiyu.quanzhu.model.dao.IndustryDao;
import com.zhiyu.quanzhu.model.result.AreaResult;
import com.zhiyu.quanzhu.model.result.CardFrendResult;
import com.zhiyu.quanzhu.model.result.HobbyDaoResult;
import com.zhiyu.quanzhu.model.result.IndustryResult;
import com.zhiyu.quanzhu.model.result.ZuiJinUserResult;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

public class BaseDataUtils {
    private final String TAG = "BaseDataUtils";
    private static BaseDataUtils utils;

    public static BaseDataUtils getInstance() {
        if (null == utils) {
            synchronized (BaseDataUtils.class) {
                utils = new BaseDataUtils();
            }
        }
        return utils;
    }

    public void initBaseData() {
//        ThreadPoolUtils.getInstance().init().execute(new Runnable() {
//            @Override
//            public void run() {
//                initCityData();
//            }
//        });
//        ThreadPoolUtils.getInstance().init().execute(new Runnable() {
//            @Override
//            public void run() {
//                initIndustryData();
//            }
//        });
//        ThreadPoolUtils.getInstance().init().execute(new Runnable() {
//            @Override
//            public void run() {
//                initHobbyData();
//            }
//        });
        ThreadPoolUtils.getInstance().init().execute(new Runnable() {
            @Override
            public void run() {
                initCardUserData();
            }
        });
    }

    private void initCityData() {
        cityList();
//        List<AreaProvince> list = AreaDao.getInstance().provinceList();
//        if (null == list || list.size() == 0) {
//            LogUtils.getInstance().show(TAG, "本地城市数据为空，立刻进行网络请求");
//            cityList();
//        } else {
//            LogUtils.getInstance().show(TAG, "本地城市数据不为空 " + list.size());
//        }
    }

    private void initIndustryData() {
        industryList();
//        List<IndustryParent> list = IndustryDao.getInstance().industryParentList();
//        if (null == list || list.size() == 0) {
//            LogUtils.getInstance().show(TAG, "本地行业数据为空，立刻进行网络请求");
//            industryList();
//        } else {
//            LogUtils.getInstance().show(TAG, "本地行业数据不为空 " + list.size());
//        }
    }

    private void initHobbyData() {
        hobbyList();
//        List<HobbyDaoParent> list = HobbyDao.getInstance().hobbyParentList();
//        if (null == list || list.size() == 0) {
//            LogUtils.getInstance().show(TAG, "本地爱好数据为空，立刻进行网络请求");
//            hobbyList();
//        } else {
//            LogUtils.getInstance().show(TAG, "本地爱好数据不为空 " + list.size());
//        }
    }

    private void initCardUserData() {
        cardUserList();
    }

    private AreaResult areaResult;

    /**
     * 地区列表
     */
    private void cityList() {
        RequestParams params = MyRequestParams.getInstance(BaseApplication.applicationContext).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CITYS);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("BaseData - 地区: "+result);
                areaResult = GsonUtils.GsonToBean(result, AreaResult.class);
                AreaDao.getInstance().saveAreaProvince(areaResult.getData().getCitys());
                for (final AreaProvince p : areaResult.getData().getCitys()) {
                    ThreadPoolUtils.getInstance().init().execute(new Runnable() {
                        @Override
                        public void run() {
                            AreaDao.getInstance().saveAreaCity(p.getChild());
                        }
                    });
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.getInstance().show(TAG, "cityList:" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private IndustryResult industryResult;

    /**
     * 行业列表
     */
    private void industryList() {
        final RequestParams params = MyRequestParams.getInstance(BaseApplication.applicationContext).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.HOBBY_LIST);
        params.addBodyParameter("type", "1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("BaseData - 行业: "+result);
                industryResult = GsonUtils.GsonToBean(result, IndustryResult.class);
                if (null != industryResult) {
                    IndustryDao.getInstance().saveIndustryParent(industryResult.getData().getList().get(0).getChild());
                    for (final IndustryParent parent : industryResult.getData().getList().get(0).getChild()) {
                        ThreadPoolUtils.getInstance().init().execute(new Runnable() {
                            @Override
                            public void run() {
                                IndustryDao.getInstance().saveIndustryChild(parent.getChild());
                            }
                        });
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.getInstance().show(TAG, "industryList:" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private HobbyDaoResult hobbyResult;

    private void hobbyList() {
        final RequestParams params = MyRequestParams.getInstance(BaseApplication.applicationContext).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.HOBBY_LIST);
        params.addBodyParameter("type", "2");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("BaseData - 兴趣爱好: "+result);
                hobbyResult = GsonUtils.GsonToBean(result, HobbyDaoResult.class);
                HobbyDao.getInstance().saveHobbyParentList(hobbyResult.getData().getList().get(0).getChild());
                for (HobbyDaoParent parent : hobbyResult.getData().getList().get(0).getChild()) {
                    HobbyDao.getInstance().saveHobbyChildList(parent.getChild());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.getInstance().show(TAG, "hobbyList:" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 名片好友列表
     */
    private void cardUserList() {
        RequestParams params = MyRequestParams.getInstance(BaseApplication.applicationContext).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CARD_USER_LIST);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("BaseData - 名片好友: "+result);
                CardDao.getInstance().clear(BaseApplication.getInstance());
                CardDao.getInstance().saveList(result, BaseApplication.getInstance());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.getInstance().show(TAG, "cardUserList:" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    public void initIMConversation(String ids) {
        getUserListAvatarNickName(ids);
    }


    private void getUserListAvatarNickName(String ids) {
        RequestParams params = MyRequestParams.getInstance(BaseApplication.applicationContext).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.USER_INFO_LIST);
        params.addBodyParameter("uids", ids);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("头像昵称: " + result);
                System.out.println("BaseData - 头像昵称: "+result);
                IMUserDao.getInstance().saveIMUserList(result, BaseApplication.getInstance());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
