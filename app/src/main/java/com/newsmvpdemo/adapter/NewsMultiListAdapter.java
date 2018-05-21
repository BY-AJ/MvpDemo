package com.newsmvpdemo.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.labelview.LabelView;
import com.newsmvpdemo.R;
import com.newsmvpdemo.adapter.entity.NewsMultiItem;
import com.newsmvpdemo.api.NewsUtils;
import com.newsmvpdemo.module.bean.NewsInfo;
import com.newsmvpdemo.utils.DefIconFactory;
import com.newsmvpdemo.utils.ListUtils;
import com.newsmvpdemo.utils.StringUtils;

import java.util.List;

/**
 * Created by yb on 2018/3/6.
 * 复合新闻类型适配器
 */

public class NewsMultiListAdapter extends BaseMultiItemQuickAdapter<NewsMultiItem,BaseViewHolder>{
    public NewsMultiListAdapter(List<NewsMultiItem> data) {
        super(data);
        addItemType(NewsMultiItem.ITEM_TYPE_NORMAL, R.layout.adapter_news_list);
        addItemType(NewsMultiItem.ITEM_TYPE_PHOTO_SET,R.layout.adapter_news_photo_set);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsMultiItem item) {
        switch (helper.getItemViewType()) {
            case NewsMultiItem.ITEM_TYPE_NORMAL :
                handleNewsNormal(helper,item.getNewsBean());
                break;
            case NewsMultiItem.ITEM_TYPE_PHOTO_SET :
                handleNewsPhotoSet(helper,item.getNewsBean());
                break;
        }
    }

    //正常新闻展示
    private void handleNewsNormal(BaseViewHolder helper, NewsInfo newsBean) {
        helper.setText(R.id.tv_title,newsBean.getTitle())
                .setText(R.id.tv_source, StringUtils.clipNewsSource(newsBean.getSource()))
                .setText(R.id.tv_time,newsBean.getPtime());
        if(NewsUtils.isNewsSpecial(newsBean.getSkipType())) {
            LabelView labelView = helper.itemView.findViewById(R.id.label_view);
            labelView.setVisibility(View.VISIBLE);
            labelView.setText("专题");
        }else {
            helper.setVisible(R.id.label_view,false);
        }

        ImageView newsIcon = helper.itemView.findViewById(R.id.iv_icon);
        Glide.with(helper.itemView.getContext())
                .load(newsBean.getImgsrc())
                .dontAnimate()
                .placeholder(DefIconFactory.provideIcon())
                .into(newsIcon);
    }

    //图集新闻展示
    private void handleNewsPhotoSet(BaseViewHolder helper, NewsInfo item) {
        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_source, StringUtils.clipNewsSource(item.getSource()))
                .setText(R.id.tv_time, item.getPtime());

        ImageView[] newsPhoto = new ImageView[3];
        newsPhoto[0] = helper.getView(R.id.iv_icon_1);
        newsPhoto[1] = helper.getView(R.id.iv_icon_2);
        newsPhoto[2] = helper.getView(R.id.iv_icon_3);
        helper.setVisible(R.id.iv_icon_2, false).setVisible(R.id.iv_icon_3, false);
        Glide.with(helper.itemView.getContext())
                .load(item.getImgsrc())
                .dontAnimate()
                .placeholder(DefIconFactory.provideIcon())
                .into(newsPhoto[0]);
        if (!ListUtils.isEmpty(item.getImgextra())) {
            for (int i = 0; i < Math.min(2, item.getImgextra().size()); i++) {
                newsPhoto[i + 1].setVisibility(View.VISIBLE);
                Glide.with(helper.itemView.getContext())
                        .load(item.getImgextra().get(i).getImgsrc())
                        .dontAnimate()
                        .placeholder(DefIconFactory.provideIcon())
                        .into(newsPhoto[i+1]);
            }
        }
    }
}
