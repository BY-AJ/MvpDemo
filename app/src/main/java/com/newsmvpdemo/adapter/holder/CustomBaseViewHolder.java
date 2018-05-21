package com.newsmvpdemo.adapter.holder;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by ictcxq on 2018/4/2.
 */

public class CustomBaseViewHolder extends BaseViewHolder{

    public CustomBaseViewHolder(View view) {
        super(view);
    }

    public boolean isVisible(int viewId) {
        View view = getView(viewId);
        return view.getVisibility() == View.VISIBLE;
    }

    public CustomBaseViewHolder setSelected(int viewId, boolean selected) {
        View view = getView(viewId);
        view.setSelected(selected);
        return this;
    }

    public boolean isSelected(int viewId) {
        View view = getView(viewId);
        return view.isSelected();
    }

}
