package com.zhiyu.quanzhu.model.bean;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 聊天基础数据
 * id,name,headerpic
 */
public class ConversationData extends SugarRecord {
     String userId;
     String userName;
     String userHeaderPic;

    public ConversationData(String userId, String userName, String userHeaderPic) {
        this.userId = userId;
        this.userName = userName;
        this.userHeaderPic = userHeaderPic;
    }

    @Override
    public String toString() {
        return "userId: " + userId + " , userName: " + userName + " , userHeaderPic: " + userHeaderPic;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHeaderPic() {
        return userHeaderPic;
    }

    public void setUserHeaderPic(String userHeaderPic) {
        this.userHeaderPic = userHeaderPic;
    }
}
