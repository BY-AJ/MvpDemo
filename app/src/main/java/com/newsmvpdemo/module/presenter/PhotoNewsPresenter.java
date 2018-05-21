package com.newsmvpdemo.module.presenter;

import com.newsmvpdemo.api.RetrofitHelper;
import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.bean.PhotoInfo;
import com.newsmvpdemo.module.fragment.PhotoNewsFragment;

import java.util.List;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by ictcxq on 2018/3/27.
 */

public class PhotoNewsPresenter implements IBasePresenter{

    private PhotoNewsFragment mView;
    private String mNextSetId;

    public PhotoNewsPresenter(PhotoNewsFragment view) {
        mView = view;
    }

    @Override
    public void getData(boolean isRefresh) {
        RetrofitHelper.getPhotoList()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mView.<List<PhotoInfo>>bindToLife())
                .subscribe(new Subscriber<List<PhotoInfo>>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                    }
                    @Override
                    public void onError(Throwable e) {
                        mView.showNetError();
                    }
                    @Override
                    public void onNext(List<PhotoInfo> photoInfos) {
                        mView.loadData(photoInfos);
                        mNextSetId = photoInfos.get(photoInfos.size()-1).getSetid();
                    }
                });
    }

    @Override
    public void getMoreData() {
        RetrofitHelper.getPhotoMoreList(mNextSetId)
                .compose(mView.<List<PhotoInfo>>bindToLife())
                .subscribe(new Subscriber<List<PhotoInfo>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        mView.loadErrorData();
                    }
                    @Override
                    public void onNext(List<PhotoInfo> photoInfos) {
                        mView.loadMoreData(photoInfos);
                        mNextSetId = photoInfos.get(photoInfos.size()-1).getSetid();
                    }
                });
    }
}
