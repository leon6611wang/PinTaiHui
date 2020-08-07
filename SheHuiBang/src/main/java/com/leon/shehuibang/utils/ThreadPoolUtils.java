package com.leon.shehuibang.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程工具类
 */
public class ThreadPoolUtils {
    private static ThreadPoolUtils utils;

    public static ThreadPoolUtils getInstance() {
        if (null == utils) {
            synchronized (ThreadPoolUtils.class) {
                utils = new ThreadPoolUtils();
            }
        }
        return utils;
    }

    public ExecutorService init() {
        ExecutorService executorService = null;
        try {
            executorService = Executors.newFixedThreadPool(4);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executorService;
    }

}
