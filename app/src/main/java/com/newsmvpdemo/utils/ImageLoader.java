package com.newsmvpdemo.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Created by ictcxq on 2018/3/27.
 */

public final class ImageLoader {

    private ImageLoader() {
        throw new RuntimeException();
    }

    public static void loadCenterCrop(Context context, String url, ImageView view, int defaultResId) {
        if ( NetUtil.isWifiConnected(context)) {
            Glide.with(context).load(url).centerCrop().dontAnimate().placeholder(defaultResId).into(view);
        } else {
            view.setImageResource(defaultResId);
        }
    }

    public static void loadFitCenter(Context context, String url, ImageView view, int defaultResId) {
        if (NetUtil.isWifiConnected(context)) {
            Glide.with(context).load(url).fitCenter().dontAnimate().placeholder(defaultResId).into(view);
        } else {
            view.setImageResource(defaultResId);
        }
    }

    /**
     *  计算图片分辨率
     * @param context
     * @param url
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static String calePhotoSize(Context context,String url) throws ExecutionException, InterruptedException {
        File file = Glide.with(context)
                .load(url)
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get();
        BitmapFactory.Options options = new BitmapFactory.Options();
        //加载的时候不解析图片，而是获取图片的相关信息
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(),options);
        return options.outWidth +"*"+ options.outHeight;
    }
}
