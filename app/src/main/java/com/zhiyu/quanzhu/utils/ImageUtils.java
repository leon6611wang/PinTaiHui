package com.zhiyu.quanzhu.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.MessageImage;
import com.zhiyu.quanzhu.model.bean.UploadImage;

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

    public UploadImage getUploadImage(String path, String url) {
        UploadImage image = new UploadImage();
        BitmapFactory.Options options = new BitmapFactory.Options();
        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        int width = options.outWidth;
        int height = options.outHeight;
        image.setFile(url);
        image.setWidth(width);
        image.setHeight(height);
        return image;
    }

    public MessageImage getMessageImage(String path, String url) {
        MessageImage image = new MessageImage();
        BitmapFactory.Options options = new BitmapFactory.Options();
        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        int width = options.outWidth;
        int height = options.outHeight;
        image.setUrl(url);
        image.setWidth(width);
        image.setHeight(height);
        return image;
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
