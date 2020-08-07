package com.leon.shehuibang.utils;

import android.content.Context;

import com.wgd.gdcp.gdcplibrary.GDCompressC;
import com.wgd.gdcp.gdcplibrary.GDCompressImageListener;
import com.wgd.gdcp.gdcplibrary.GDConfig;

import java.io.File;

public class ThumbnailUtils {

    private static ThumbnailUtils utils;

    public static ThumbnailUtils getInstance() {
        if (null == utils) {
            synchronized (ThumbnailUtils.class) {
                if (null == utils) {
                    utils = new ThumbnailUtils();
                }
            }
        }
        return utils;
    }

    private final int MAX_SIZE = 1024 * 1000 * 20;
    private final int MIN_SIZE = 1024 * 500;

    public void thumbnailImage(final String path, final Context context, final OnThumbnailListener listener) {
        ThreadPoolUtils.getInstance().init().execute(new Runnable() {
            @Override
            public void run() {
                int fileSize = ImageUtils.getInstance().getImageSize(path);
                if (fileSize <= MIN_SIZE) {
                    System.out.println("当前小于500k,无需压缩");
                    if (null != listener) {
                        listener.onThumbnail(path);
                    }
                } else if (fileSize > MIN_SIZE && fileSize <= MAX_SIZE) {
                    System.out.println("当前小于20M,开始压缩");
                    int index = path.indexOf(".");
                    String p0 = path.substring(0, index);
                    String p1 = path.substring(index + 1, path.length());
                    final String thumb_path = p0 + "_thumb." + p1;
                    File thumb_file = new File(thumb_path);
                    if (!thumb_file.exists()) {
                        thumb_file.getParentFile().mkdirs();
                        try {
                            thumb_file.createNewFile();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    new GDCompressC(context,
                            new GDConfig()
                                    .setmPath(path)//要压缩图片的原路径
                                    .setSavePath(thumb_path)//压缩图片的保存路径，如果不设置将替换原文件
//                        .setChangeWH(true)//是否要进行调整图片分辨率以压缩到更小
//                        .setWidth(720)//需要调整分辨率的时候有效，压缩后的宽度（按比例计算后的，而不是直接使用这个）
//                        .setHeight(1280)//需要调整分辨率的时候有效，压缩后的高度（按比例计算后的，而不是直接使用这个）
                            , new GDCompressImageListener() {
                        @Override
                        public void OnSuccess(String path) {
                            System.out.println("压缩成功");
                            if (null != listener) {
                                listener.onThumbnail(thumb_path);
                            }
                        }

                        @Override
                        public void OnError(int code, String errorMsg) {
                            System.out.println("压缩失败");
                            if (null != listener) {
                                listener.onThumbnail(path);
                            }
                        }
                    });
                } else if (fileSize > MAX_SIZE) {
                    System.out.println("当前大于20M,无法压缩");
                    if (null != listener) {
                        listener.onThumbnail(path);
                    }
                }
            }
        });

    }

    public interface OnThumbnailListener {
        void onThumbnail(String path);
    }

}
