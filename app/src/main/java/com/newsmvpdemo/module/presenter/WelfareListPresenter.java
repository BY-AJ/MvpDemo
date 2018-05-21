package com.newsmvpdemo.module.presenter;

import com.newsmvpdemo.api.RetrofitHelper;
import com.newsmvpdemo.local.table.WelfareInfo;
import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.fragment.WelfareListFragment;
import com.newsmvpdemo.utils.ImageLoader;

import java.util.List;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ictcxq on 2018/3/27.
 */

public class WelfareListPresenter implements IBasePresenter{

    private WelfareListFragment mView;

    private int mPage = 1;

    public WelfareListPresenter(WelfareListFragment view) {
        mView =view;
    }

    @Override
    public void getData(boolean isRefresh) {
        RetrofitHelper.getWelfarePhoto(mPage)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mTransFormer)
                .subscribe(new Subscriber<List<WelfareInfo>>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                    }
                    @Override
                    public void onError(Throwable e) {
                        mView.showNetError();
                    }
                    @Override
                    public void onNext(List<WelfareInfo> welfarePhotoInfos) {
                        mView.loadData(welfarePhotoInfos);
                        mPage++;
                    }
                });
    }

    @Override
    public void getMoreData() {
        RetrofitHelper.getWelfarePhoto(mPage)
                .compose(mTransFormer)
                .subscribe(new Subscriber<List<WelfareInfo>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        mView.loadErrorData();
                    }
                    @Override
                    public void onNext(List<WelfareInfo> welfarePhotoInfos) {
                        mView.loadMoreData(welfarePhotoInfos);
                        mPage++;
                    }
                });
    }

    private Observable.Transformer<WelfareInfo,List<WelfareInfo>> mTransFormer = new Observable.Transformer<WelfareInfo, List<WelfareInfo>>() {
        @Override
        public Observable<List<WelfareInfo>> call(Observable<WelfareInfo> welfarePhotoInfoObservable) {
            return welfarePhotoInfoObservable
                    .observeOn(Schedulers.io())
                    .filter(new Func1<WelfareInfo, Boolean>() {
                        @Override
                        public Boolean call(WelfareInfo welfarePhotoInfo) {
                            try {
                                welfarePhotoInfo.setPixel(ImageLoader.calePhotoSize(mView.getContext(),welfarePhotoInfo.getUrl()));
                                return true;
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                                return false;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                return false;
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .toList()
                    .compose(mView.<List<WelfareInfo>>bindToLife());
        }
    };
}
