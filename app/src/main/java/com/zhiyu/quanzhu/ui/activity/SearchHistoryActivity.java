package com.zhiyu.quanzhu.ui.activity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.SearchHistory;
import com.zhiyu.quanzhu.model.dao.SearchHistoryDao;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SearchHistoryActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    private TagFlowLayout tagFlowLayout, reShouTagFlowLayout;
    private List<String> search_history_list = new ArrayList<>();
    private List<String> hot_search_list = new ArrayList<>();
    private TextView cancelTextView, clearSearchHistoryTextView;
    private EditText searchEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        hot_search_list.add("搜索的历史记录");
        hot_search_list.add("2019");
        hot_search_list.add("中美贸易");
        hot_search_list.add("GDP增长率");
        hot_search_list.add("印度拒绝");
        hot_search_list.add("知乎精选");
        hot_search_list.add("洞朗");
        hot_search_list.add("武器");
        initViews();
        initSimple();
    }

    private TagAdapter tagAdapter1, tagAdapter2;

    private void initViews() {
        tagFlowLayout = findViewById(R.id.tagFlowLayout);
        reShouTagFlowLayout = findViewById(R.id.reShouTagFlowLayout);
        tagAdapter1 = new TagAdapter<String>(search_history_list) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history_label, parent, false);
                TextView labelTextView = view.findViewById(R.id.labelTextView);
                labelTextView.setText(s);
                return labelTextView;
            }
        };
        tagFlowLayout.setAdapter(tagAdapter1);
        tagAdapter2 = new TagAdapter<String>(hot_search_list) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history_label, parent, false);
                TextView labelTextView = view.findViewById(R.id.labelTextView);
                labelTextView.setText(s);
                return labelTextView;
            }
        };
        reShouTagFlowLayout.setAdapter(tagAdapter2);

        tagFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                for (Integer index : selectPosSet) {
                    Log.i("tagSelect", search_history_list.get(index));
                    SearchHistory searchHistory = new SearchHistory();
                    searchHistory.setName(search_history_list.get(index));
                    searchHistory.setTime(new Date().getTime());
                    SearchHistoryDao.getInstance().add(searchHistory);
                }
            }
        });
        clearSearchHistoryTextView = findViewById(R.id.clearSearchHistoryTextView);
        clearSearchHistoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchHistoryDao.getInstance().deleteAll();
                search_history_list.clear();
                tagAdapter1.notifyDataChanged();
            }
        });
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchEdit = findViewById(R.id.searchEdit);
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(searchEdit.getText().toString())) {
                        SearchHistory searchHistory = new SearchHistory();
                        searchHistory.setName(searchEdit.getText().toString().trim());
                        searchHistory.setTime(new Date().getTime());
                        SearchHistoryDao.getInstance().add(searchHistory);
                        search_history_list.add(0, searchEdit.getText().toString().trim());
                        tagAdapter1.notifyDataChanged();

                        searchEdit.clearFocus();
                        return true;
                    }
                    return true;
                }
                return false;
            }
        });
    }


    private final int PERMISSION_STORAGE_CODE = 10001;
    private final String PERMISSION_STORAGE_MSG = "此app需要获取SD卡读取权限";
    private final String[] PERMISSION_STORAGE = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    private boolean hasPermissions(Context context, String... permissions) {
        return EasyPermissions.hasPermissions(context, permissions);
    }

    /**
     * 是否有SD卡权限
     *
     * @param context
     * @return
     */
    private boolean hasStoragePermission(Context context) {
        return hasPermissions(context, PERMISSION_STORAGE);
    }

    @AfterPermissionGranted(PERMISSION_STORAGE_CODE)
    private void initSimple() {
        if (hasStoragePermission(this)) {
            //有权限
            BaseApplication.initXUtils();
            List<SearchHistory> searchHistoryList = SearchHistoryDao.getInstance().queryAll();
            if (null != searchHistoryList && searchHistoryList.size() > 0) {
                for (SearchHistory searchHistory : searchHistoryList) {
                    search_history_list.add(searchHistory.getName());
                }
                tagAdapter1.notifyDataChanged();
            }
        } else {
            //申请权限
            EasyPermissions.requestPermissions(this, PERMISSION_STORAGE_MSG, PERMISSION_STORAGE_CODE, PERMISSION_STORAGE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        //将结果传入EasyPermissions中
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        BaseApplication.initXUtils();
        List<SearchHistory> searchHistoryList = SearchHistoryDao.getInstance().queryAll();
        if (null != searchHistoryList && searchHistoryList.size() > 0) {
            for (SearchHistory searchHistory : searchHistoryList) {
                search_history_list.add(searchHistory.getName());
            }
            tagAdapter1.notifyDataChanged();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
