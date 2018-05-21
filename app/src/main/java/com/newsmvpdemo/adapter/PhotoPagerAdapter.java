package com.newsmvpdemo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.ybq.android.spinkit.SpinKitView;
import com.newsmvpdemo.R;
import com.newsmvpdemo.local.table.WelfareInfo;

import java.util.Collections;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by ictcxq on 2018/3/27.
 * 图片浏览适配器
 */

public class PhotoPagerAdapter extends PagerAdapter{

    private List<WelfareInfo> mImgList;
    private Context mContext;

    // 限制 Adapter 在倒数第3个位置时启动加载更多回调
    private final static int LOAD_MORE_LIMIT = 3;
    private boolean mIsLoadMore = false;

    private OnLoadMoreListener mLoadMoreListener;

    public PhotoPagerAdapter(Context context) {
        this.mContext = context;
        this.mImgList = Collections.EMPTY_LIST;
    }

    public void updateData(List<WelfareInfo> imgList) {
        this.mImgList = imgList;
        notifyDataSetChanged();
    }

    public void addData(List<WelfareInfo> imgList) {
        mImgList.addAll(imgList);
        notifyDataSetChanged();
        mIsLoadMore = false;
    }

    @Override
    public int getCount() {
        return mImgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_photo_pager, null);
        final PhotoView iv_photo = view.findViewById(R.id.iv_photo);
        final SpinKitView loading_view = view.findViewById(R.id.loading_view);

        //实现加载更多
        if((position >= mImgList.size() -LOAD_MORE_LIMIT) && !mIsLoadMore) {
            if(mLoadMoreListener != null) {
                mIsLoadMore = true;
                mLoadMoreListener.onLoadMore();
            }
        }

        Glide.with(mContext)
                .load(mImgList.get(position%mImgList.size()).getUrl())
                .centerCrop()
                .dontAnimate()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        loading_view.setVisibility(View.GONE);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        loading_view.setVisibility(View.GONE);
                        iv_photo.setImageDrawable(resource);
                        return true;
                    }
                })
                .into(iv_photo);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * 是否收藏
     */
    public boolean isLoved(int position) {
        return mImgList.get(position).getIsLove();
    }

    /**
     * 是否点赞
     */
    public boolean isPraise(int position) {
        return mImgList.get(position).getIsPraise();
    }

    /**
     * 是否下载
     */
    public boolean isDownload(int position) {
        return mImgList.get(position).getIsDownload();
    }

    /**
     * 获取对应数据
     */
    public WelfareInfo getData(int position) {
        return mImgList.get(position);
    }

    public WelfareInfo getData(String url) {
        for (WelfareInfo bean:mImgList) {
            if(bean.getUrl().equals(url)) {
                return bean;
            }
        }
        return null;
    }

    /**************************************************监听事件****************************************/
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }
}
