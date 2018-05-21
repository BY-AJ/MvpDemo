package com.newsmvpdemo.api;

import android.content.Context;
import android.widget.Toast;

import com.newsmvpdemo.utils.ExceptionHandler;
import com.newsmvpdemo.utils.NetUtil;

import rx.Subscriber;

/**
 * Created by ictcxq on 2018/4/12.
 * Subscriber基类,可以在这里处理client网络连接状况
 */

public abstract class HttpSubscriber<T> extends Subscriber<T>{

    private Context mContext;

    public HttpSubscriber(Context context) {
        mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!NetUtil.isNetworkAvailable(mContext)) {
            Toast.makeText(mContext,"当前网络不可用，请检查网络情况",Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof Exception) {
            //访问获得对应的Exception
            ExceptionHandler.ResponseThrowable responseThrowable = ExceptionHandler.handleException(e);
            onError(responseThrowable.code, responseThrowable.message);
        } else {
            //将Throwable 和 未知错误的status code返回
            ExceptionHandler.ResponseThrowable responseThrowable = new ExceptionHandler.ResponseThrowable(e, ExceptionHandler.ERROR.UNKNOWN);
            onError(responseThrowable.code, responseThrowable.message);
        }
    }

    @Override
    public void onNext(T t) {

    }

    public abstract void onError(int errType, String errMessage);
}
