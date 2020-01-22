package com.zhiyu.quanzhu.ui.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.Conversation;
import com.zhiyu.quanzhu.model.bean.ConversationData;
import com.zhiyu.quanzhu.model.dao.ConversationDao;
import com.zhiyu.quanzhu.model.dao.helper.DataBaseHelper;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.List;

public class DaoTestActivity extends BaseActivity implements View.OnClickListener {
    private TextView saveTV, deleteTV, updateTV, selectTV, resultTV;
    private ImageView imageIV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dao_test);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
        initDB();

    }

    private SQLiteDatabase db;

    private void initDB() {
        //新建了一个名为test_db的数据库
        DataBaseHelper databaseHelper = new DataBaseHelper(this, "test_db", null, 1);
        db = databaseHelper.getWritableDatabase();
    }

    private void initViews() {
        saveTV = findViewById(R.id.saveTV);
        saveTV.setOnClickListener(this);
        deleteTV = findViewById(R.id.deleteTV);
        deleteTV.setOnClickListener(this);
        updateTV = findViewById(R.id.updateTV);
        updateTV.setOnClickListener(this);
        selectTV = findViewById(R.id.selectTV);
        selectTV.setOnClickListener(this);
        resultTV = findViewById(R.id.resultTV);
        imageIV = findViewById(R.id.imageIV);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveTV:
                Conversation c = new Conversation();
                c.setUser_id("2");
                c.setUser_name("bryce");
                c.setHeader_pic("https://c-ssl.duitang.com/uploads/item/201802/03/20180203204046_yskxu.thumb.700_0.jpg");
                ConversationDao.getDao(this).save(c);
                break;
            case R.id.deleteTV:
                ConversationDao.getDao(this).delete("1");
                break;
            case R.id.updateTV:
               Conversation c2=new Conversation();
               c2.setUser_name("杀手2047");
               c2.setHeader_pic("https://img.52z.com/upload/news/image/20181211/20181211104335_91332.jpg");
               c2.setUser_id("2");
               ConversationDao.getDao(this).update(c2);
                break;
            case R.id.selectTV:
                Conversation c3=ConversationDao.getDao(this).selectById("2");
                resultTV.setText(c3.toString());
                Glide.with(this).load(c3.getHeader_pic()).into(imageIV);
                break;
        }
    }
}
