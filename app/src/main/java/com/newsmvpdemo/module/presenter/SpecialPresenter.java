package com.newsmvpdemo.module.presenter;

import com.newsmvpdemo.adapter.entity.SpecialItem;
import com.newsmvpdemo.api.RetrofitHelper;
import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.bean.NewsItemInfo;
import com.newsmvpdemo.module.bean.SpecialInfo;
import com.newsmvpdemo.module.view.ISpecialView;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by yb on 2018/3/12.
 */

public class SpecialPresenter implements IBasePresenter{

    private ISpecialView mView;
    private String mSpecialId;

    public SpecialPresenter(ISpecialView mView, String id) {
        this.mView = mView;
        this.mSpecialId = id;
    }

    @Override
    public void getData(boolean isRefresh) {
        RetrofitHelper.getSpecial(mSpecialId)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .flatMap(new Func1<SpecialInfo, Observable<SpecialItem>>() {
                    @Override
                    public Observable<SpecialItem> call(SpecialInfo specialInfo) {
                        mView.loadBanner(specialInfo);
                        return convertSpecialBeanToItem(specialInfo);
                    }
                })
                .toList()
                .compose(mView.<List<SpecialItem>>bindToLife())
                .subscribe(new Subscriber<List<SpecialItem>>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                    }
                    @Override
                    public void onError(Throwable e) {
                        mView.showNetError();
                    }
                    @Override
                    public void onNext(List<SpecialItem> specialItems) {
                        mView.loadData(specialItems);
                    }
                });
    }

    private Observable<SpecialItem> convertSpecialBeanToItem(final SpecialInfo specialBean) {
        // 这边 +1 是接口数据还有个 topicsplus 的字段可能是穿插在 topics 字段列表中间。这里没有处理 topicsplus
        final SpecialItem[] specialItems = new SpecialItem[specialBean.getTopics().size() + 1];
        return Observable.from(specialBean.getTopics())
                // 获取头部
                .doOnNext(new Action1<SpecialInfo.TopicsEntity>() {
                    @Override
                    public void call(SpecialInfo.TopicsEntity topicsEntity) {
                        specialItems[topicsEntity.getIndex()-1] = new SpecialItem(true,
                                        topicsEntity.getIndex()+"/"+specialItems.length+" "+topicsEntity.getTname());
                    }
                })
                //排序
                .toSortedList(new Func2<SpecialInfo.TopicsEntity, SpecialInfo.TopicsEntity, Integer>() {
                    @Override
                    public Integer call(SpecialInfo.TopicsEntity topicsEntity, SpecialInfo.TopicsEntity topicsEntity2) {
                        return topicsEntity.getIndex() - topicsEntity2.getIndex();
                    }
                })
                //拆分
                .flatMap(new Func1<List<SpecialInfo.TopicsEntity>, Observable<SpecialInfo.TopicsEntity>>() {
                    @Override
                    public Observable<SpecialInfo.TopicsEntity> call(List<SpecialInfo.TopicsEntity> topicsEntities) {
                        return Observable.from(topicsEntities);
                    }
                })
                //类型转换
                .flatMap(new Func1<SpecialInfo.TopicsEntity, Observable<SpecialItem>>() {
                    @Override
                    public Observable<SpecialItem> call(SpecialInfo.TopicsEntity topicsEntity) {
                        return Observable.from(topicsEntity.getDocs())
                                .map(new Func1<NewsItemInfo, SpecialItem>() {
                                    @Override
                                    public SpecialItem call(NewsItemInfo newsItemInfo) {
                                        return new SpecialItem(newsItemInfo);
                                    }
                                })
                                .startWith(specialItems[topicsEntity.getIndex()-1]);
                    }
                });
    }

    @Override
    public void getMoreData() {

    }
}
