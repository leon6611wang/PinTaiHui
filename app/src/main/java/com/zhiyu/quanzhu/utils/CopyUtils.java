package com.zhiyu.quanzhu.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * 复制内容到剪贴板
 */
public class CopyUtils {
    private static CopyUtils utils;
    private ClipboardManager cm;
    private ClipData mClipData;

    public static CopyUtils getInstance() {
        if (null == utils) {
            synchronized (CopyUtils.class) {
                utils = new CopyUtils();
            }
        }
        return utils;
    }

    public void copy(Context context, String s) {
        //获取剪贴板管理器：
        cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        mClipData = ClipData.newPlainText("Label", s);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }
}
