package com.newsmvpdemo.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseBooleanArray;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.newsmvpdemo.local.table.WelfareInfo;
import com.newsmvpdemo.local.table.WelfareInfoDao;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yb on 2018/3/28.
 * 图片下载工具类
 */

public final class DownloadUtils {

    /***采用稀疏数组来记录图片下载的状态***/
    // 记录下载完的图片
    private static SparseBooleanArray sDlPhotos = new SparseBooleanArray();
    // 记录下载中的图片
    private static SparseBooleanArray sDoDlPhotos = new SparseBooleanArray();


    private DownloadUtils() {
        throw new RuntimeException("DownloadUtils cannot be initialized!");
    }

    /**
     * 初始化操作，记录已经下载的图片
     * @param dbDao
     */
    public static void init(WelfareInfoDao dbDao) {
        dbDao.queryBuilder().rx().list()
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<List<WelfareInfo>, Observable<WelfareInfo>>() {
                    @Override
                    public Observable<WelfareInfo> call(List<WelfareInfo> welfareInfos) {
                        return Observable.from(welfareInfos);//转换，将集合转换成单个对象一一发送
                    }
                })
                .filter(new Func1<WelfareInfo, Boolean>() {
                    @Override
                    public Boolean call(WelfareInfo welfareInfo) {
                        return welfareInfo.getIsDownload();//过滤，判断图片是否已经下载
                    }
                })
                .subscribe(new Action1<WelfareInfo>() {
                    @Override
                    public void call(WelfareInfo welfareInfo) {
                        sDlPhotos.put(welfareInfo.getUrl().hashCode(),true);//存储记录
                    }
                });
    }

    /**
     * 图片下载
     * @param context 上下文
     * @param url 图片Url地址
     * @param id 图片id
     * @param listener 下载监听器
     */
    public static void downloadOrDeletePhoto(final Context context, final String url, final String id, final OnCompletedListener listener) {
        //1.图片已经下载，是否删除
        if(sDlPhotos.get(url.hashCode(),false)) {
            DialogHelper.deleteDialog(context, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FileUtils.deleteFile(PreferencesUtils.getSavePath(context)+File.separator
                            +"picture"+File.separator + id + ".jpg");
                    listener.onDeleted(url);
                    cleanDownloadPhoto(url);
                }
            });
            return;
        }

        //2.判断图片正在下载，直接结束操作
        if(sDoDlPhotos.get(url.hashCode(),false)) {
            ToastUtils.showToast("正在下载...");
            return;
        }

        //3.开始下载图片
        sDoDlPhotos.put(url.hashCode(),true);
        ToastUtils.showToast("正在下载...");
        Observable.just(url)//发送一个事件给下游
                .map(new Func1<String,Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        File file = null;
                        try {
                            //直接从Glide缓存中获取下载好的图片
                            file = Glide.with(context).load(url).downloadOnly(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        return FileUtils.copyFile(file.getPath(),PreferencesUtils.getSavePath(context)+File.separator
                                +"picture"+File.separator + id + ".jpg");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean isCompleted) {
                        if(isCompleted) {
                            ToastUtils.showToast("下载完成");
                            sDlPhotos.put(url.hashCode(),true);
                            if(listener!= null) {
                                listener.onCompleted(url);
                            }
                        }else {
                            ToastUtils.showToast("下载失败");
                        }
                        sDoDlPhotos.put(url.hashCode(),false);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtils.showToast("下载失败");
                        sDoDlPhotos.put(url.hashCode(), false);
                    }
                });
    }

    /**
     * 清除下载记录
     * @param url
     */
    public static void cleanDownloadPhoto(String url) {
        sDlPhotos.put(url.hashCode(),false);
        sDoDlPhotos.put(url.hashCode(),false);
    }

    /**
     * 下载监听器
     */
    public interface OnCompletedListener {
        /**
         * 下载完成
         * @param url
         */
        void onCompleted(String url);

        /**
         * 删除
         * @param url
         */
        void onDeleted(String url);
    }
}
