package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.BikeInsurance;
import com.zhiyu.quanzhu.model.bean.BikeShop;
import com.zhiyu.quanzhu.model.bean.BikeUser;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Date;

/**
 * 测试类
 */
public class InsuranceActivity extends BaseActivity implements View.OnClickListener{
    TextView addShopBtn,addUserBtn,addInsuranceBtn,shopListBtn,userListBtn,shopListManageBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);

         addShopBtn = findViewById(R.id.addShopBtn);
        addShopBtn.setOnClickListener(this);
        addUserBtn = findViewById(R.id.addUserBtn);
        addUserBtn.setOnClickListener(this);
        addInsuranceBtn = findViewById(R.id.addInsuranceBtn);
        addInsuranceBtn.setOnClickListener(this);
        shopListBtn = findViewById(R.id.shopListBtn);
        shopListBtn.setOnClickListener(this);
        userListBtn = findViewById(R.id.userListBtn);
        userListBtn.setOnClickListener(this);
        shopListManageBtn = findViewById(R.id.shopListManageBtn);
        shopListManageBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addShopBtn:
                addShop();
                break;
            case R.id.addUserBtn:
                addUser();
                break;
            case R.id.addInsuranceBtn:
                addInsurance();
                break;
            case  R.id.shopListBtn:
                shopList();
                break;
            case R.id.userListBtn:
                userList();
                break;
            case R.id.shopListManageBtn:

                break;
        }
    }

    private void addShop() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams("http://www.hzlslpweb.cn/bike/shop/insertShop");
        BikeShop shop = new BikeShop();
        shop.setLongitude(118.78f);
        shop.setLatitude(32.04f);
        shop.setSalesValume(1234.5f);
        shop.setShop_address("杭州市滨江区江虹国际创意园6号楼301");
        shop.setShop_name("邦威");
        shop.setShopkeeper_name("柯南");
        shop.setShopkeeper_phone_number("18757591055");
        params.setAsJsonContent(true);
        params.setBodyContent(GsonUtils.GsonString(shop));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("success: " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("error: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    private void addUser() {
        RequestParams params = new RequestParams("http://121.41.89.35:8080/bike/shop/insertUser");
        BikeUser user=new BikeUser();
        user.setAddress("fasdfasdf");
        user.setBike_code("adsfasdf");
        user.setId_number("asdfasdfas");
        user.setPhone_number("fasdfas");
        user.setUser_name("asdfasdf");

        params.setAsJsonContent(true);
        params.setBodyContent(GsonUtils.GsonString(user));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("success: " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("error: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    private void addInsurance() {
        RequestParams params = new RequestParams("http://121.41.89.35:8080/bike/shop/insertInsurance");
        BikeInsurance insurance=new BikeInsurance();
        insurance.setMoney(100.35f);
        insurance.setShop_id(1);
        insurance.setUser_id(1);
        insurance.setCreate_time(new Date().getTime());
        insurance.setEnd_time(new Date().getTime()+30*24*60*60*1000);

        params.setAsJsonContent(true);
        params.setBodyContent(GsonUtils.GsonString(insurance));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("success: " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("error: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void userList() {
        RequestParams params = new RequestParams("http://121.41.89.35:8080/bike/shop/userList");
       params.addBodyParameter("page","1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("success: " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("error: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void shopList() {
        RequestParams params = new RequestParams("http://121.41.89.35:8080/bike/shop/shopList");
        params.addBodyParameter("page","1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("success: " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("error: " + ex.toString());
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
