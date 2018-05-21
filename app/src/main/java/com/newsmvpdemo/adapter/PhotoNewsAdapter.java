package com.newsmvpdemo.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.newsmvpdemo.R;
import com.newsmvpdemo.module.bean.PhotoInfo;
import com.newsmvpdemo.utils.DefIconFactory;
import com.newsmvpdemo.utils.ImageLoader;

import java.util.List;

/**
 * Created by ictcxq on 2018/3/27.
 * 图片-----生活适配器
 */

public class PhotoNewsAdapter extends BaseQuickAdapter<PhotoInfo,BaseViewHolder>{

    public PhotoNewsAdapter(@Nullable List<PhotoInfo> data) {
        super(R.layout.adapter_photo_list,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PhotoInfo item) {
        helper.setText(R.id.tv_title,item.getSetname());

        ImageView ivPhoto1 = helper.itemView.findViewById(R.id.iv_photo_1);
        ImageView ivPhoto2 = helper.itemView.findViewById(R.id.iv_photo_2);
        ImageView ivPhoto3 = helper.itemView.findViewById(R.id.iv_photo_3);

        ImageLoader.loadCenterCrop(mContext,item.getPics().get(0),ivPhoto1, DefIconFactory.provideIcon());
        ImageLoader.loadCenterCrop(mContext,item.getPics().get(1),ivPhoto2, DefIconFactory.provideIcon());
        ImageLoader.loadCenterCrop(mContext,item.getPics().get(2),ivPhoto3, DefIconFactory.provideIcon());
    }
}
