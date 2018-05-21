package com.newsmvpdemo.module.presenter;

import com.newsmvpdemo.local.dao.NewsTypeDao;
import com.newsmvpdemo.local.table.DaoSession;
import com.newsmvpdemo.local.table.NewsTypeInfo;
import com.newsmvpdemo.local.table.NewsTypeInfoDao;
import com.newsmvpdemo.module.event.ChannelEvent;
import com.newsmvpdemo.module.home.ChannelActivity;
import com.newsmvpdemo.module.view.IChannelView;
import com.newsmvpdemo.utils.RxBusHelper;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by yb on 2018/3/8.
 */

public class ChannelPresenter implements IChannelPresenter<NewsTypeInfo> {

    private final IChannelView mView;
    private final NewsTypeInfoDao mDbDao;
    private final RxBusHelper mRxBus;

    public ChannelPresenter(ChannelActivity mView, DaoSession daoSession, RxBusHelper rxBus) {
        this.mView = mView;
        mDbDao = daoSession.getNewsTypeInfoDao();
        this.mRxBus = rxBus;
    }

    @Override
    public void getData(boolean isRefresh) {
        //1.先获取数据库中已有的新闻类型对象
        final List<NewsTypeInfo> checkList = mDbDao.queryBuilder().list();
        final List<String> typeList = new ArrayList<>();
        for (NewsTypeInfo bean:checkList) {
            typeList.add(bean.getTypeId());
        }
        //2.将所有的新闻类型对象作为一个个事件向下传递
        Observable.from(NewsTypeDao.getAllChannels())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //过滤掉数据库已有的新闻类型对象
                .filter(new Func1<NewsTypeInfo, Boolean>() {
                    @Override
                    public Boolean call(NewsTypeInfo newsTypeInfo) {
                        return !typeList.contains(newsTypeInfo.getTypeId());
                    }
                })
                //将过滤后的所有新闻类型，转换成一个集合
                .toList()
                //注册观察者
                .subscribe(new Subscriber<List<NewsTypeInfo>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(List<NewsTypeInfo> unCkeckList) {
                        //通知适配器去更新数据
                        mView.loadData(checkList,unCkeckList);
                    }
                });
    }

    @Override
    public void getMoreData() {
    }

    @Override
    public void insert(final NewsTypeInfo data) {
        mDbDao.rx().insert(data)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<NewsTypeInfo>() {
                    @Override
                    public void onCompleted() {
                        //发送一个添加事件，通知所有注册者去接受这个事件处理
                        mRxBus.post(new ChannelEvent(ChannelEvent.ADD_EVENT,data));
                    }
                    @Override
                    public void onError(Throwable e) {
                        Logger.d(e.getMessage());
                    }
                    @Override
                    public void onNext(NewsTypeInfo newsTypeInfo) {
                    }
                });
    }

    @Override
    public void delete(final NewsTypeInfo data) {
        mDbDao.rx().delete(data)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        //发送一个删除事件，通知所有注册者去接受这个事件处理
                        mRxBus.post(new ChannelEvent(ChannelEvent.DEL_EVENT,data));
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(Void aVoid) {
                    }
                });
    }

    @Override
    public void update(final List<NewsTypeInfo> list) {
        Observable.from(list)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mDbDao.deleteAll();
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<NewsTypeInfo>() {
                    @Override
                    public void onCompleted() {
                        mRxBus.post(new ChannelEvent(ChannelEvent.UPDATE_EVENT,list));
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(NewsTypeInfo newsTypeInfo) {
                        newsTypeInfo.setId(null);
                        mDbDao.save(newsTypeInfo);
                    }
                });
    }

}
