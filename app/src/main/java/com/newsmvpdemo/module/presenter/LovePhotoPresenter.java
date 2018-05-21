package com.newsmvpdemo.module.presenter;

import com.newsmvpdemo.local.table.WelfareInfo;
import com.newsmvpdemo.local.table.WelfareInfoDao;
import com.newsmvpdemo.module.base.ILocalPresenter;
import com.newsmvpdemo.module.event.LoveEvent;
import com.newsmvpdemo.module.fragment.LovePhotoFragment;
import com.newsmvpdemo.utils.RxBusHelper;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by ictcxq on 2018/3/27.
 */

public class LovePhotoPresenter implements ILocalPresenter<WelfareInfo>{

    private LovePhotoFragment mView;
    private WelfareInfoDao mDbDao;
    private RxBusHelper rxBusHelper;

    public LovePhotoPresenter(LovePhotoFragment mView, WelfareInfoDao dao, RxBusHelper rxBusHelper) {
        this.mView = mView;
        this.mDbDao = dao;
        this.rxBusHelper = rxBusHelper;
    }

    @Override
    public void insert(WelfareInfo data) {
    }

    @Override
    public void delete(WelfareInfo data) {
        data.setIsLove(false);
        if(!data.getIsLove() && !data.getIsDownload() && !data.getIsPraise()) {
            mDbDao.delete(data);
        }else {
            mDbDao.update(data);
        }
        // 如果收藏为0则显示无收藏
        if(mDbDao.queryBuilder().where(WelfareInfoDao.Properties.IsLove.eq(true)).count() == 0) {
            mView.loadErrorData();
        }
        rxBusHelper.post(new LoveEvent());
    }

    @Override
    public void update(List<WelfareInfo> list) {
    }

    @Override
    public void getData(boolean isRefresh) {
        mDbDao.queryBuilder().where(WelfareInfoDao.Properties.IsLove.eq(true))
                .rx()
                .list()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<WelfareInfo>>() {
                    @Override
                    public void call(List<WelfareInfo> welfareInfos) {
                        if(welfareInfos.size() == 0) {
                            mView.loadErrorData();
                        }else {
                            mView.loadData(welfareInfos);
                        }
                    }
                });
    }

    @Override
    public void getMoreData() {
    }
}
