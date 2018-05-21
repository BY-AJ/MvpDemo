package com.newsmvpdemo.module.presenter;

import com.newsmvpdemo.api.RetrofitHelper;
import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.bean.PhotoSetInfo;
import com.newsmvpdemo.module.view.IPhotoSetView;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by ictcxq on 2018/3/13.
 */

public class PhotoSetPresenter implements IBasePresenter{

    private IPhotoSetView mView;
    private String mPhotoId;

    public PhotoSetPresenter(IPhotoSetView mView, String mPhotoId) {
        this.mView = mView;
        this.mPhotoId = mPhotoId;
    }

    @Override
    public void getData(boolean isRefresh) {
        RetrofitHelper.getPhotoSet(mPhotoId)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mView.<PhotoSetInfo>bindToLife())
                .subscribe(new Subscriber<PhotoSetInfo>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                    }
                    @Override
                    public void onError(Throwable e) {
                        mView.showNetError();
                    }
                    @Override
                    public void onNext(PhotoSetInfo photoSetInfo) {
                        mView.loadData(photoSetInfo);
                    }
                });
    }

    @Override
    public void getMoreData() {

    }
}
