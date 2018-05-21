package com.newsmvpdemo.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.dl7.downloaderlib.entity.FileInfo;
import com.newsmvpdemo.R;
import com.newsmvpdemo.adapter.holder.CustomBaseViewHolder;
import com.newsmvpdemo.local.table.VideoInfo;
import com.newsmvpdemo.module.home.VideoPlayerActivity;
import com.newsmvpdemo.utils.DefIconFactory;
import com.newsmvpdemo.utils.ImageLoader;
import com.newsmvpdemo.utils.RxBusHelper;
import com.newsmvpdemo.utils.StringUtils;

import java.util.List;

/**
 * Created by long on 2016/12/16.
 * video 缓存完成适配器
 */
public class VideoCompleteAdapter extends BaseVideoDLAdapter {


    public VideoCompleteAdapter(@Nullable List<VideoInfo> data, RxBusHelper rxbus) {
        super(data, rxbus, R.layout.adapter_video_complete);
    }

    @Override
    protected void convert(final CustomBaseViewHolder holder,final VideoInfo item) {
        ImageView ivThumb = holder.getView(R.id.iv_thumb);
        ImageLoader.loadFitCenter(mContext, item.getCover(), ivThumb, DefIconFactory.provideIcon());
        holder.setText(R.id.tv_size, StringUtils.convertStorageNoB(item.getTotalSize()))
                .setText(R.id.tv_title, item.getTitle());
        // 根据是否为编辑模式来进行处理
        final CheckBox cbDelete = holder.getView(R.id.cb_delete);
        if (mIsEditMode) {
            cbDelete.setVisibility(View.VISIBLE);
            cbDelete.setChecked(mSparseItemChecked.get(holder.getAdapterPosition()));
        } else {
            cbDelete.setVisibility(View.GONE);
            cbDelete.setChecked(false);
        }
        cbDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                _handleCheckedChanged(holder.getAdapterPosition(), isChecked);
            }
        });
        holder.getView(R.id.tv_show_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsEditMode) {
                    cbDelete.setChecked(!cbDelete.isChecked());
                } else {
                    VideoPlayerActivity.launch(mContext, item);
                }
            }
        });
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mIsEditMode) {
//                    cbDelete.setChecked(!cbDelete.isChecked());
//                } else {
//                    FullscreenActivity.launch(mContext,item);
//                }
//            }
//        });
    }

    @Override
    public void updateDownload(FileInfo fileInfo) {

    }
}
