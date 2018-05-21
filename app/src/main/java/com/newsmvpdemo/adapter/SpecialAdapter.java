package com.newsmvpdemo.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.labelview.LabelView;
import com.newsmvpdemo.R;
import com.newsmvpdemo.adapter.entity.SpecialItem;
import com.newsmvpdemo.api.NewsUtils;
import com.newsmvpdemo.utils.DefIconFactory;
import com.newsmvpdemo.utils.StringUtils;

import java.util.List;

/**
 * Created by yb on 2018/3/12.
 *专题列表适配器
 */

public class SpecialAdapter extends BaseSectionQuickAdapter<SpecialItem,BaseViewHolder>{
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public SpecialAdapter(int layoutResId, int sectionHeadResId, List<SpecialItem> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, SpecialItem item) {
        helper.setText(R.id.tv_head,item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, SpecialItem item) {
        helper.setText(R.id.tv_title,item.t.getTitle())
                .setText(R.id.tv_source, StringUtils.clipNewsSource(item.t.getSource()))
                .setText(R.id.tv_time,item.t.getPtime());
        if(NewsUtils.isNewsSpecial(item.t.getSkipType())) {
            LabelView labelView = helper.itemView.findViewById(R.id.label_view);
            labelView.setVisibility(View.VISIBLE);
            labelView.setText("专题");
        }else if(NewsUtils.isNewsPhotoSet(item.t.getSkipType())) {
            LabelView labelView = helper.itemView.findViewById(R.id.label_view);
            labelView.setVisibility(View.VISIBLE);
            labelView.setText("图集");
        }else {
            helper.setVisible(R.id.label_view,false);
        }

        ImageView newsIcon = helper.itemView.findViewById(R.id.iv_icon);
        Glide.with(helper.itemView.getContext())
                .load(item.t.getImgsrc())
                .dontAnimate()
                .placeholder(DefIconFactory.provideIcon())
                .into(newsIcon);
    }
}
