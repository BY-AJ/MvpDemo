package com.newsmvpdemo.module.presenter;

import com.newsmvpdemo.api.RetrofitHelper;
import com.newsmvpdemo.local.table.WelfareInfo;
import com.newsmvpdemo.local.table.WelfareInfoDao;
import com.newsmvpdemo.module.base.ILocalPresenter;
import com.newsmvpdemo.module.event.LoveEvent;
import com.newsmvpdemo.module.home.BigPhotoActivity;
import com.newsmvpdemo.utils.RxBusHelper;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by ictcxq on 2018/3/27.
 */

public class BigPhotoPresenter implements ILocalPresenter<WelfareInfo>{

    private final BigPhotoActivity mView;
    private final WelfareInfoDao mDbDao;
    private final List<WelfareInfo> mPhotoList;
    private final RxBusHelper mRxBus;
    private List<WelfareInfo> mDbLovedData;

    private int mPage =2;

    public BigPhotoPresenter(BigPhotoActivity view, WelfareInfoDao dao, List<WelfareInfo> mPhotoList, RxBusHelper rxBus) {
        this.mView = view;
        this.mDbDao = dao;
        this.mPhotoList = mPhotoList;
        this.mRxBus = rxBus;
        mDbLovedData = mDbDao.queryBuilder().list();
    }

    @Override
    public void insert(WelfareInfo data) {
        if(mDbLovedData.contains(data)) {
            mDbDao.update(data);
        }else {
            mDbDao.insert(data);
            mDbLovedData.add(data);
        }
        mRxBus.post(new LoveEvent());
    }

    @Override
    public void delete(WelfareInfo data) {
        if(!data.getIsLove() && !data.getIsDownload() && !data.getIsPraise()) {
            mDbDao.delete(data);
            mDbLovedData.remove(data);
        }else {
            mDbDao.update(data);
        }
        mRxBus.post(new LoveEvent());
    }

    @Override
    public void update(List<WelfareInfo> list) {
    }

    @Override
    public void getData(boolean isRefresh) {
        Observable.from(mPhotoList)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mTransformer)
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
                    public void onNext(List<WelfareInfo> welfareInfos) {
                        mView.loadData(welfareInfos);
                    }
                });
    }

    @Override
    public void getMoreData() {
        //未实现，还没理解
        RetrofitHelper.getWelfarePhoto(mPage)
                .compose(mTransformer)
                .subscribe(new Subscriber<List<WelfareInfo>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(List<WelfareInfo> welfareInfos) {
                        mView.loadMoreData(welfareInfos);
                        mPage++;
                    }
                });
    }

    /**
     * 统一变换
     */
    private Observable.Transformer<WelfareInfo, List<WelfareInfo>> mTransformer = new Observable.Transformer<WelfareInfo, List<WelfareInfo>>() {
        @Override
        public Observable<List<WelfareInfo>> call(Observable<WelfareInfo> listObservable) {
            return listObservable
                    .doOnNext(new Action1<WelfareInfo>() {
                        WelfareInfo tmpBean;

                        @Override
                        public void call(WelfareInfo bean) {
                            // 判断数据库是否有数据，有则设置对应参数
                            if (mDbLovedData.contains(bean)) {
                                tmpBean = mDbLovedData.get(mDbLovedData.indexOf(bean));
                                bean.setIsLove(tmpBean.getIsLove());
                                bean.setIsPraise(tmpBean.getIsPraise());
                                bean.setIsDownload(tmpBean.getIsDownload());
                            }
                        }
                    })
                    .toList()
                    .compose(mView.<List<WelfareInfo>>bindToLife());
        }
    };

}
