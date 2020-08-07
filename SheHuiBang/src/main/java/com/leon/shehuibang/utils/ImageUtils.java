package com.leon.shehuibang.utils;

import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 图片处理工具类
 */
public class ImageUtils {
    private static ImageUtils utils;

    public static ImageUtils getInstance() {
        if (null == utils) {
            synchronized (ImageUtils.class) {
                utils = new ImageUtils();
            }
        }
        return utils;
    }



    public int[] getLocalImageWidthHeight(String imgPath) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        //只请求图片宽高，不解析图片像素(请求图片属性但不申请内存，解析bitmap对象，该对象不占内存)
        opts.inJustDecodeBounds = true;
        //String path = Environment.getExternalStorageDirectory() + "/dog.jpg";
        BitmapFactory.decodeFile(imgPath, opts);
        int imageWidth = opts.outWidth;
        int imageHeight = opts.outHeight;
        int[] wh = new int[2];
        wh[0] = imageWidth;
        wh[1] = imageHeight;
        return wh;
    }

    public  int getImageSize(String path) {
        FileInputStream fis= null;
        int size=0;
        try{
            File f= new File(path);
            fis= new FileInputStream(f);
            size=fis.available();
        }catch(Exception e){
            e.printStackTrace();
        } finally{
            if (null!=fis){
                try {
                    fis.close();
                } catch (IOException e) {
                   e.printStackTrace();
                }
            }
        }
        return size;
    }
}
