package com.zhiyu.quanzhu.base;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zhiyu.quanzhu.utils.AppManager;
import com.zhiyu.quanzhu.utils.StatusBarUtils;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;


public class BaseActivity extends AppCompatActivity {
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将Activity实例添加到AppManager的堆栈
        AppManager.getAppManager().addActivity(this);
        setStatusBar();
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);
        methodRequiresTwoPermission();

    }

    protected void setStatusBar() {
        //这里做了两件事情，1.使状态栏透明并使contentView填充到状态栏 2.预留出状态栏的位置，防止界面上的控件离顶部靠的太近。这样就可以实现开头说的第二种情况的沉浸式状态栏了
        StatusBarUtils.setTransparent(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //将Activity实例从AppManager的堆栈中移除
        AppManager.getAppManager().finishActivity(this);
        unregisterReceiver(networkChangeReceiver);
    }

    /**
     * 监听网络状态
     */
    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                Log.i("myNetWork", "networkInfo.getType(): " + networkInfo.getType() + " , TYPE_MOBILE: " + TYPE_MOBILE + " , TYPE_WIFI: " + TYPE_WIFI);
                switch (networkInfo.getType()) {
                    case TYPE_MOBILE:
                        Log.i("myNetWork", "正在使用2G/3G/4G网络");
//                        Toast.makeText(context, "正在使用2G/3G/4G网络", Toast.LENGTH_SHORT).show();
                        break;
                    case TYPE_WIFI:
                        Log.i("myNetWork", "正在使用wifi上网");
//                        Toast.makeText(context, "正在使用wifi上网", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            } else {
                Log.i("myNetWork", "当前无网络连接");
//                Toast.makeText(context, "当前无网络连接", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private final int EXTERNAL_STORAGE_CODE = 1001;

    @AfterPermissionGranted(EXTERNAL_STORAGE_CODE)
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            //如果已经获取权限，在这里做一些事情
            if (null != onExternalStorageListener) {
                onExternalStorageListener.onExternalStorage();
            }
        } else {
            //如果没有获取到权限，在这里获取权限，其中RC_CAMERA_AND_LOCATION是自己定义的一个唯一标识int值
            EasyPermissions.requestPermissions(this, "请求SD卡读写权限",
                    EXTERNAL_STORAGE_CODE, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private static OnExternalStorageListener onExternalStorageListener;

    public static void setOnExternalStorageListener(OnExternalStorageListener listener) {
        onExternalStorageListener = listener;
    }

    public interface OnExternalStorageListener {
        void onExternalStorage();
    }

}
