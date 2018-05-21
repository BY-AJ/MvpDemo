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

import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by yb on 2018/3/13.
 * 图集适配器
 */

public class PhotoSetAdapter extends PagerAdapter{

    private Context mContext;
    private List<String> mImgUrl;

    public PhotoSetAdapter(Context context, List<String> data) {
        mContext = context;
        mImgUrl = data;
    }

    @Override
    public int getCount() {
        return mImgUrl.size();
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

        Glide.with(mContext)
                .load(mImgUrl.get(position%mImgUrl.size()))
                .fitCenter()
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
}
