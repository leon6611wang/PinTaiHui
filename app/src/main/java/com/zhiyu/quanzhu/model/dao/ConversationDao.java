package com.zhiyu.quanzhu.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zhiyu.quanzhu.model.bean.Conversation;

import java.util.ArrayList;
import java.util.List;

public class ConversationDao extends SQLiteOpenHelper {
    private static ConversationDao dao;
    private static SQLiteDatabase db;

    public static ConversationDao getDao(Context context) {
        if (null == dao) {
            synchronized (ConversationDao.class) {
                dao = new ConversationDao(context, "quanzhu_db", null, 1);
                db = dao.getWritableDatabase();
            }
        }
        return dao;
    }

    public ConversationDao(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table conversation(user_id varchar(20),user_name varchar(20),header_pic varchar(100))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static SQLiteDatabase initDB(Context context) {
        //新建了一个名为test_db的数据库
        ConversationDao databaseHelper = new ConversationDao(context, "quanzhu_db", null, 1);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        return db;
    }

    public void save(Conversation conversation) {
        ContentValues values = new ContentValues();
        values.put("user_id", conversation.getUser_id());
        values.put("user_name", conversation.getUser_name());
        values.put("header_pic", conversation.getHeader_pic());
        db.insert("conversation", null, values);
    }

    public void save(String user_id, String user_name, String header_pic) {
        ContentValues values = new ContentValues();
        values.put("user_id", user_id);
        values.put("user_name", user_name);
        values.put("header_pic", header_pic);
        db.insert("conversation", null, values);
    }

    public void update(Conversation conversation) {
        ContentValues values = new ContentValues();
        values.put("user_name", conversation.getUser_name());
        values.put("header_pic", conversation.getHeader_pic());
        db.update("conversation", values, "user_id = ?", new String[]{conversation.getUser_id()});
    }

    public void update(String user_id, String user_name, String header_pic) {
        ContentValues values = new ContentValues();
        values.put("user_name", user_name);
        values.put("header_pic", header_pic);
        db.update("conversation", values, "user_id = ?", new String[]{user_id});
    }

    public void delete(Conversation conversation) {
        db.delete("conversation", "user_id=?", new String[]{conversation.getUser_id()});
    }

    public void delete(String user_id) {
        db.delete("conversation", "user_id=?", new String[]{user_id});
    }

    public List<Conversation> selectAll() {
        List<Conversation> list = new ArrayList<>();
        //创建游标对象
        Cursor cursor = db.query("conversation", new String[]{"user_id", "user_name", "header_pic"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Conversation conversation = new Conversation();
            String user_id = cursor.getString(cursor.getColumnIndex("user_id"));
            String user_name = cursor.getString(cursor.getColumnIndex("user_name"));
            String header_pic = cursor.getString(cursor.getColumnIndex("header_pic"));
            conversation.setUser_id(user_id);
            conversation.setUser_name(user_name);
            conversation.setHeader_pic(header_pic);
            list.add(conversation);
        }
        return list;
    }

    public Conversation selectById(String user_id) {
        Conversation conversation = new Conversation();
        Cursor cursor = db.query("conversation", new String[]{"user_id", "user_name", "header_pic"}, "user_id = ?", new String[]{user_id}, null, null, null);
        while (cursor.moveToNext()) {
            conversation = new Conversation();
            String id = cursor.getString(cursor.getColumnIndex("user_id"));
            String user_name = cursor.getString(cursor.getColumnIndex("user_name"));
            String header_pic = cursor.getString(cursor.getColumnIndex("header_pic"));
            conversation.setUser_id(user_id);
            conversation.setUser_name(user_name);
            conversation.setHeader_pic(header_pic);
        }
        return conversation;
    }

}
