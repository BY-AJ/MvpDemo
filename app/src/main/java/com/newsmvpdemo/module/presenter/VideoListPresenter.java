package com.newsmvpdemo.module.presenter;

import com.newsmvpdemo.api.RetrofitHelper;
import com.newsmvpdemo.local.table.VideoInfo;
import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.view.IVideoListView;

import java.util.List;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by yb on 2018/3/16.
 */

public class VideoListPresenter implements IBasePresenter{

    private IVideoListView mView;
    private String mVideoId;
    private int page = 0;

    public VideoListPresenter(IVideoListView mView, String mVideoId) {
        this.mView = mView;
        this.mVideoId = mVideoId;
    }

    @Override
    public void getData(boolean isRefresh) {
        RetrofitHelper.getVideoList(mVideoId,page)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mView.<List<VideoInfo>>bindToLife())
                .subscribe(new Subscriber<List<VideoInfo>>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                    }
                    @Override
                    public void onError(Throwable e) {
                        mView.showNetError();
                    }
                    @Override
                    public void onNext(List<VideoInfo> videoInfos) {
                        mView.loadData(videoInfos);
                    }
                });

    }

    @Override
    public void getMoreData() {

    }
}
