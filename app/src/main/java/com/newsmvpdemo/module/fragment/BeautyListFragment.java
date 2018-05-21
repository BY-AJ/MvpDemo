package com.newsmvpdemo.module.fragment;

import com.newsmvpdemo.R;
import com.newsmvpdemo.local.table.BeautyPhotoInfo;
import com.newsmvpdemo.module.base.BaseFragment;
import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.view.IBeautyListView;

import java.util.List;

/**
 * Created by yb on 2018/3/25.
 * 美女
 */

public class BeautyListFragment extends BaseFragment<IBasePresenter> implements IBeautyListView<List<BeautyPhotoInfo>>{

    @Override
    protected void initInject() {

    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_photo_list;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }

    @Override
    public void loadData(List<BeautyPhotoInfo> data) {

    }

    @Override
    public void loadMoreData(List<BeautyPhotoInfo> data) {

    }

    @Override
    public void loadNoData() {

    }
}
