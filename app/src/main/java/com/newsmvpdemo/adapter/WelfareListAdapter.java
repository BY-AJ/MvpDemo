package com.newsmvpdemo.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.newsmvpdemo.R;
import com.newsmvpdemo.local.table.WelfareInfo;
import com.newsmvpdemo.module.home.BigPhotoActivity;
import com.newsmvpdemo.utils.DefIconFactory;
import com.newsmvpdemo.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yb on 2018/3/27.
 * 福利适配器
 */

public class WelfareListAdapter extends BaseQuickAdapter<WelfareInfo,BaseViewHolder>{

    // 图片的宽度
    private int mPhotoWidth;
    private Fragment mFragment;

    public WelfareListAdapter(Fragment fragment) {
        this(null,fragment.getContext());
        mFragment = fragment;
    }

    public WelfareListAdapter(@Nullable List<WelfareInfo> data, Context context) {
        super(R.layout.adapter_welfare_photo,data);
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        int marginPixels = context.getResources().getDimensionPixelOffset(R.dimen.photo_margin_width);
        mPhotoWidth = widthPixels/2 - marginPixels;
    }

    @Override
    protected void convert(final BaseViewHolder helper, WelfareInfo item) {
        helper.setText(R.id.tv_title, item.getCreatedAt());

        ImageView ivPhoto = helper.itemView.findViewById(R.id.iv_photo);

        if(item.getPixel() != null) {
            //计算压缩后的照片的高度
            int photoHeight = StringUtils.calcPhotoHeight(item.getPixel(), mPhotoWidth);
            // 返回的数据有像素分辨率，根据这个来缩放图片大小
            final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivPhoto.getLayoutParams();
            params.width = mPhotoWidth;
            params.height = photoHeight;
            ivPhoto.setLayoutParams(params);
        }


        Glide.with(helper.itemView.getContext())
                .load(item.getUrl())
                .dontAnimate()
                .fitCenter()
                .placeholder(DefIconFactory.provideIcon())
                .into(ivPhoto);

        //条目点击事件处理
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFragment != null) {
                    BigPhotoActivity.launchForResult(mFragment, (ArrayList<WelfareInfo>) getData(),helper.getAdapterPosition());
                }else {
                    BigPhotoActivity.launch(mContext, (ArrayList<WelfareInfo>) getData(),helper.getAdapterPosition());
                }
            }
        });
    }
}
