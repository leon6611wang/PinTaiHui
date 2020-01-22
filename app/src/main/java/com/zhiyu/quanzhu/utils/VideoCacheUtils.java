package com.zhiyu.quanzhu.utils;

import android.content.Context;

import com.zhiyu.quanzhu.model.bean.QuanZiTuiJian;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * 视频缓存工具类
 */
public class VideoCacheUtils {
    private static VideoCacheUtils utils;

    public static VideoCacheUtils getInstance() {
        if (null == utils) {
            synchronized (VideoCacheUtils.class) {
                utils = new VideoCacheUtils();
            }
        }
        if (null == list || list.size() == 0) {
            initList();
        }
        return utils;
    }

    private static List<String> list = new ArrayList<>();

    private static void initList() {
        list.add("https://flv3.bn.netease.com/711b98bb9cbd792f7bac148032e18f6107a0268e2b8d7fd051fd60fa1bc65a4f43e75bd652aece1fea99c76ceb097aff24962323fcafe073346002ed8585771ea852f9432516f82f2d5ea129290c2b7e5a424d5a6521b1ef843caa35f5a744167cccf29ff6e80c5bbf1adc5603b897073adad4e69ff930e7.mp4");
        list.add("https://flv3.bn.netease.com/711b98bb9cbd792f9910d9a87be03514f9ec4f97e5a133197bbc163d603a201a0da90786ec24bd71e912c930fe8770cbae4e1f6436eafe802bde12920afac89e251cf9eb4988a2710b85cf5969e115383ced9a1079b0002052a311820e646330b0dff247aeaeeaae90d4f9dcace01c348ec7d5c617ac670b.mp4");
        list.add("https://flv3.bn.netease.com/711b98bb9cbd792f2f1dcc6249744d2e4476d9b10e809671ea559d1a36aaef54bf88b25cb02a13a396efe1c4d14949f0448b260748a86a17249c77c1ca12efc29b86d3ef1c1062d6603a03b0198ea5a5f64a955b7261134042db390a67d55ea939a568834d42fa6b549fc4695c549d56d56cefcedf9ea2d0.mp4");
        list.add("https://flv3.bn.netease.com/711b98bb9cbd792fe399cdef59e1f46bb76b1c442ee15c9022b7b95ff52045bbaaa3131cd8dee7312bd9c7aeab76ca4643326823cd178f009a73601366186e5bc1d56486ffaf7c03e72e672f1b5e76e84ebb57a44c8481e96f513f1a19e044e126e34168054d62ec04a25a5303fa9a6d81b8448a210bf473.mp4");
        list.add("https://flv3.bn.netease.com/711b98bb9cbd792f068aad75bf0c1e7722ad1e7200b5f516a3c51ce7539ac4668592f08a796f785a13f40b5342dce5120c4de08f06689b4d2c61905b20f100c7c1ba7b6adb361b4dd10135827e8e60b09e1cd800889966e46731fb014acaf46e14a85c340bd5be700c0e8e49db67f4afabf336a8bd2e8b9b.mp4");
        list.add("https://flv3.bn.netease.com/711b98bb9cbd792f93a69277945c6cd63181f1676991d35c1858ddee299485262384db5ad80436d2097da38008222e42218a179350b63cecd1d969367d80129ea034e85681646e4f4b6cad828eebd2cb3bcb017dd5000d974cae2073549025a38c0b28ca873369c8574646aa4401caf7e4786bf2da7a652f.mp4");
        list.add("https://flv3.bn.netease.com/711b98bb9cbd792ffad532c32a9b6eb7bb94d46aa3cf0c5a2a868a7378603e4a282db88d4082eaabdb05724b83f8c5cb96946523870ebcc4f5c9f2cb360e1501a13a479a063586ab72651cb480bcbd4bcd78c356c99e2aca00334a35d0d23c584fcfbc73dd2d416e2dd08e37ce514a447a01ec92e20e80b1.mp4");
        list.add("https://flv3.bn.netease.com/711b98bb9cbd792f468676891707912650e3b01a9fbac1c5258b05ab68ed2fe94b5c33f410621fdb23c7a42952758bc77b0daeba5b871942879d2c815b2a408e23a3cdcfc15c44e2ea577f4d04b877165bafc811987388d3b552425cc3cc954964d883fc888b25506b03f7f3452ad41f137f27a6f1744ab1.mp4");
        list.add("https://flv3.bn.netease.com/711b98bb9cbd792fa1fdcf290a0e04e280519b4734b14fb0294159b65f95d22cbc894f7eec7105f1af858f3bbd719b8de082abb1b4a79e9fe577436198b6d818aa7877d6c4c3d9782e798663f83c3d5405867d0338264d7d2a59e963a8c575ed41d724ca0cf6583b65b297684d661c7c9865764a42c1402d.mp4");
        list.add("https://flv3.bn.netease.com/711b98bb9cbd792f3ee87bbcafc243d44da9aed76ee5bb969aa7fb15273b32123182d48b8a8c5742e6a59d62fceb09dd7855d7b8dbc89d429d382e23b29d5bbac432ae2c951463d187932feb80df2a3405286ccd245c079f79dc7b848d0014ad74a1d37bb65a824fa0d1e0c348341aec4aa57e3a71a837a9.mp4");
    }

    public void cacheListVideo(final Context context, final List<QuanZiTuiJian> list, final OnCacheListVideoListener onCacheListVideoListener) {
        ThreadPoolUtils.getInstance().init().execute(new Runnable() {
            @Override
            public void run() {
                if (null != list && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        final int position = i;
                        if (list.get(i).getType() == 2 || list.get(i).getType() == 3) {
                            downloadVideo(context, list.get(position).getContent().getVideo_url(), new OnSaveVideoPathListener() {
                                @Override
                                public void onSavePath(String path) {
                                    if (null != onCacheListVideoListener) {
                                        onCacheListVideoListener.onCacheListVideo(position, path);
                                    }
                                }
                            });
//                            boolean isCached = videoIsCached(context, list.get(i).getContent().getVideo_url(), new OnIsCachedListener() {
//                                @Override
//                                public void onCached(String path) {
//                                    if (null != onCacheListVideoListener) {
//                                        onCacheListVideoListener.onCacheListVideo(position, path);
//                                    }
//                                }
//                            });
//                            if (!isCached) {
//                                saveVideo(context, list.get(i).getContent().getVideo_url(), new OnSaveVideoPathListener() {
//                                    @Override
//                                    public void onSavePath(String path) {
//                                        if (null != onCacheListVideoListener) {
//                                            onCacheListVideoListener.onCacheListVideo(position, path);
//                                        }
//                                    }
//                                });
//                            }
                        }
                    }
                }
            }
        });

    }

    private void saveVideo(final Context context, final String video_url, final OnSaveVideoPathListener listener) {
        ThreadPoolUtils.getInstance().init().execute(new Runnable() {
            @Override
            public void run() {
                int lastIndex = video_url.lastIndexOf(".");
                String video_type = video_url.substring(lastIndex);
                String fileName = MD5Utils.getMD5String(video_url) + video_type;
                try {
                    URL url = new URL(video_url);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(5000);
                    con.setConnectTimeout(5000);
                    con.setRequestProperty("Charset", "UTF-8");
                    con.setRequestMethod("GET");
                    if (con.getResponseCode() == 200) {
                        InputStream is = con.getInputStream();//获取输入流
                        FileOutputStream fileOutputStream = null;//文件输出流
                        if (is != null) {
                            fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                            byte[] buf = new byte[1024];
                            int ch;
                            while ((ch = is.read(buf)) != -1) {
                                fileOutputStream.write(buf, 0, ch);//将获取到的流写入文件中
                            }
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                        File file = context.getFileStreamPath(fileName);
                        if (null != listener) {
                            listener.onSavePath(file.getAbsolutePath());
                        }
                        System.out.println("视频下载完成 path: " + file.getAbsolutePath() + " , size: " + file.getName());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("视频下载出错:" + e.toString());
                }
            }
        });
    }

    private boolean videoIsCached(Context context, String video_url, OnIsCachedListener onIsCachedListener) {
        boolean isCached = false;
        try {
            int lastIndex = video_url.lastIndexOf(".");
            String video_type = video_url.substring(lastIndex);
            String fileName = MD5Utils.getMD5String(video_url) + video_type;
            File file = context.getFileStreamPath(fileName);
            if (!file.exists()) {
                isCached = false;
            } else {
                isCached = true;
                if (null != onIsCachedListener) {
                    onIsCachedListener.onCached(file.getAbsolutePath());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isCached;
    }

    public interface OnSaveVideoPathListener {
        void onSavePath(String path);
    }

    public interface OnCacheListVideoListener {
        void onCacheListVideo(int position, String path);
    }

    public interface OnIsCachedListener {
        void onCached(String path);
    }

    private void downloadVideo(Context context, String url, final OnSaveVideoPathListener onSaveVideoPathListener) {
        url = "http://101.37.246.157/img/a.mp4";
        String path = context.getExternalCacheDir().getPath();
        int lastIndex = url.lastIndexOf(".");
        String video_type = url.substring(lastIndex);
        String fileName = MD5Utils.getMD5String(url) + video_type;
        path = path + "/" + fileName;
        File videoFile = new File(path);
        if (videoFile.exists()) {
            if (null != onSaveVideoPathListener) {
                onSaveVideoPathListener.onSavePath(path);
            }
//            System.out.println("该视频已存在，无需下载,path: " + path);
        } else {
//            System.out.println("开始下载: path: " + path);
            RequestParams params = new RequestParams(url);
            // 为RequestParams设置文件下载后的保存路径
            params.setSaveFilePath(path);
            // 下载完成后自动为文件命名
            params.setAutoRename(false);
            x.http().get(params, new Callback.ProgressCallback<File>() {
                @Override
                public void onSuccess(File result) {
                    if (null != onSaveVideoPathListener) {
                        onSaveVideoPathListener.onSavePath(result.getAbsolutePath());
                    }
//                    System.out.println("下载成功:" + result.getAbsolutePath());
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
//                    System.out.println("onError " + ex.toString());
                }

                @Override
                public void onCancelled(CancelledException cex) {
//                System.out.println("onCancelled");
                }

                @Override
                public void onFinished() {
//                System.out.println("onFinished");
                }

                @Override
                public void onWaiting() {
//                System.out.println("onWaiting");
                }

                @Override
                public void onStarted() {
//                System.out.println("onStarted");
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
//                System.out.println("onLoading " + (current / total) + "%");
                }
            });
        }
    }
}
