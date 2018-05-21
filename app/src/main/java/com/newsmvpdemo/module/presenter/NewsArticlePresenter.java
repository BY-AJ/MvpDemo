package com.newsmvpdemo.module.presenter;

import com.newsmvpdemo.api.RetrofitHelper;
import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.bean.NewsDetailInfo;
import com.newsmvpdemo.module.view.INewsArticleView;
import com.newsmvpdemo.utils.ListUtils;

import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by ictcxq on 2018/3/11.
 */

public class NewsArticlePresenter implements IBasePresenter{

    private static final String HTML_IMG_TEMPLATE = "<img src=\"http\" />";

    private INewsArticleView mView;
    private String mNewsId;

    public NewsArticlePresenter(INewsArticleView mView, String mNewsId) {
        this.mView = mView;
        this.mNewsId = mNewsId;
    }

    @Override
    public void getData(boolean isRefresh) {
        RetrofitHelper.getNewsDetail(mNewsId)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .doOnNext(new Action1<NewsDetailInfo>() {
                    @Override
                    public void call(NewsDetailInfo newsDetailInfo) {
                        handleRichTextWithImg(newsDetailInfo);
                    }
                })
                .compose(mView.<NewsDetailInfo>bindToLife())
                .subscribe(new Subscriber<NewsDetailInfo>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                    }
                    @Override
                    public void onError(Throwable e) {
                        mView.showNetError();
                    }
                    @Override
                    public void onNext(NewsDetailInfo newsDetailInfo) {
                        mView.loadData(newsDetailInfo);
                    }
                });
    }

    /**
     * 处理富文本包含图片的情况
     */
    private void handleRichTextWithImg(NewsDetailInfo newsDetailInfo) {
        if(!ListUtils.isEmpty(newsDetailInfo.getImg())) {
            String body = newsDetailInfo.getBody();
            for (NewsDetailInfo.ImgEntity imgEntity:newsDetailInfo.getImg()) {
                String ref = imgEntity.getRef();
                String src = imgEntity.getSrc();
                String img = HTML_IMG_TEMPLATE.replace("http", src);
                body = body.replaceAll(ref,img);
            }
            newsDetailInfo.setBody(body);
        }
    }

    @Override
    public void getMoreData() {
    }
}
