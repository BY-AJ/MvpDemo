package com.newsmvpdemo.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yb on 2018/3/4.
 * 读取assets目录下文件工具类
 */

public class AssetsHelper {

    private AssetsHelper() {
        throw new AssertionError();
    }

    /**
     * 读取 assets 文件
     * @param context 上下文
     * @param fileName 要读取文件名字
     * @return
     */
    public static String readData(Context context, String fileName) {
        InputStream inputStream = null;
        String data = null;
        try{
            //打开assets目录中的文本文件
            inputStream = context.getAssets().open(fileName);
            //inputStream.available()为文件中的总byte数
            byte[] bytes = new byte[inputStream.available()];
            //读取文件
            inputStream.read(bytes);
            //关闭数据流
            inputStream.close();
            //将bytes转为utf-8字符串
            data = new String(bytes,"utf-8");
        }catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
