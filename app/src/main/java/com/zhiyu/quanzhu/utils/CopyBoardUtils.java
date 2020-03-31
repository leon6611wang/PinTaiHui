package com.zhiyu.quanzhu.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * 复制到剪贴板工具类
 */
public class CopyBoardUtils {
    private static CopyBoardUtils utils;

    public static CopyBoardUtils getInstance() {
        if (null == utils) {
            synchronized (CopyBoardUtils.class) {
                utils = new CopyBoardUtils();
            }
        }
        return utils;
    }

    public boolean copy(Context context, String copyStr) {
        try {
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", copyStr);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
