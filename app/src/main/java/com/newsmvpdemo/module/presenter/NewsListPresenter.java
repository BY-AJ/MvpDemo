package com.newsmvpdemo.module.presenter;

import com.newsmvpdemo.adapter.entity.NewsMultiItem;
import com.newsmvpdemo.api.NewsUtils;
import com.newsmvpdemo.api.RetrofitHelper;
import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.bean.NewsInfo;
import com.newsmvpdemo.module.fragment.NewsListFragment;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;


/**
 * Created by yb on 2018/3/5.
 */

public class NewsListPresenter implements IBasePresenter{

    private final NewsListFragment mView;
    private final String mNewsId;

    private int mPage = 0 ;

    public NewsListPresenter(NewsListFragment view, String newsId) {
        this.mView = view;
        this.mNewsId = newsId;
    }

    @Override
    public void getData(boolean isRefresh) {
        RetrofitHelper.getNewsList(mNewsId,mPage)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();//预加载
                    }
                })
                .filter(new Func1<NewsInfo, Boolean>() {
                    @Override
                    public Boolean call(NewsInfo newsInfo) {
                        if(NewsUtils.isAbNews(newsInfo)) {
                            mView.loadAdData(newsInfo);
                        }
                        //过滤,如果是广告则忽略，不是广告继续往下发送数据
                        return !NewsUtils.isAbNews(newsInfo);
                    }
                })
                .compose(mTransformer)
                .subscribe(new Subscriber<List<NewsMultiItem>>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                    }
                    @Override
                    public void onError(Throwable e) {
                        mView.showNetError();
                        Logger.d(e.getMessage());
                    }
                    @Override
                    public void onNext(List<NewsMultiItem> newsMultiItems) {
                        mView.loadData(newsMultiItems);
                        mPage++;
                    }
                });
    }

    @Override
    public void getMoreData() {
        RetrofitHelper.getNewsList(mNewsId,mPage)
                .compose(mTransformer)
                .subscribe(new Subscriber<List<NewsMultiItem>>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {
                        mView.loadErrorData();
                    }
                    @Override
                    public void onNext(List<NewsMultiItem> newsMultiItems) {
                        mView.loadMoreData(newsMultiItems);
                        mPage++;
                    }
                });
    }

    /**
     * 统一转换
     * 做从一个具体类型转换到另一个类型来创建一个实例
     */
    private Observable.Transformer<NewsInfo,List<NewsMultiItem>> mTransformer = new Observable.Transformer<NewsInfo, List<NewsMultiItem>>() {
        @Override
        public Observable<List<NewsMultiItem>> call(Observable<NewsInfo> newsInfoObservable) {
            return newsInfoObservable
                    .map(new Func1<NewsInfo,NewsMultiItem>() {
                        @Override
                        public NewsMultiItem call(NewsInfo newsInfo) {
                            if(NewsUtils.isNewsPhotoSet(newsInfo.getSkipType())) {
                                return new NewsMultiItem(NewsMultiItem.ITEM_TYPE_PHOTO_SET,newsInfo);
                            }
                            return new NewsMultiItem(NewsMultiItem.ITEM_TYPE_NORMAL,newsInfo);
                        }
                    })
                    .toList()
                    .compose( mView.<List<NewsMultiItem>>bindToLife());
        }
    };
}
