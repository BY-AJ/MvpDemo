package com.newsmvpdemo.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.dl7.downloaderlib.entity.FileInfo;
import com.dl7.downloaderlib.model.DownloadStatus;
import com.newsmvpdemo.R;
import com.newsmvpdemo.adapter.holder.CustomBaseViewHolder;
import com.newsmvpdemo.engine.DownloaderWrapper;
import com.newsmvpdemo.local.table.VideoInfo;
import com.newsmvpdemo.module.home.VideoPlayerActivity;
import com.newsmvpdemo.utils.DefIconFactory;
import com.newsmvpdemo.utils.ImageLoader;
import com.newsmvpdemo.utils.RxBusHelper;
import com.newsmvpdemo.utils.StringUtils;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ictcxq on 2018/4/2.
 */

public class VideoCacheAdapter extends BaseVideoDLAdapter{

    private static Map<String,CustomBaseViewHolder> saveInfo = new HashMap<>();

    public VideoCacheAdapter(@Nullable List<VideoInfo> data, RxBusHelper rxbus) {
        super(data, rxbus, R.layout.adapter_video_cache);
    }

    @Override
    protected void convert(final CustomBaseViewHolder holder,final VideoInfo item) {
        saveInfo.put(item.getVideoUrl(),holder);//保存每个item对应的holder

        ImageView ivThumb = holder.getView(R.id.iv_thumb);
        ImageLoader.loadFitCenter(mContext, item.getCover(), ivThumb, DefIconFactory.provideIcon());
        holder.setText(R.id.tv_title, item.getTitle());
        switchViews(holder, item);

        //以下是点击事件的处理
        holder.itemView.findViewById(R.id.btn_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClick(holder, item);
            }
        });
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
                // 注意这里在切换为编辑状态时，获取holder.getAdapterPosition()=-1 的情况，要在_handleCheckedChanged()进行判断处理
                _handleCheckedChanged(holder.getAdapterPosition(), isChecked);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsEditMode) {
                    cbDelete.setChecked(!cbDelete.isChecked());
                } else {
                    VideoPlayerActivity.launch(mContext, item);
                }
            }
        });
    }

    /**
     * 点击事件处理
     */
    private void handleClick(CustomBaseViewHolder holder, VideoInfo item) {
        switch (item.getDownloadStatus()) {
            case DownloadStatus.NORMAL:
            case DownloadStatus.ERROR:
            case DownloadStatus.STOP:
                holder.setText(R.id.tv_speed, "处理中...")
                        .setTextColor(R.id.tv_speed, ContextCompat.getColor(mContext, R.color.download_normal));
                DownloaderWrapper.start(item);
                break;

            case DownloadStatus.DOWNLOADING:
                holder.setText(R.id.tv_speed, "即将暂停")
                        .setTextColor(R.id.tv_speed, ContextCompat.getColor(mContext, R.color.download_normal));
                DownloaderWrapper.stop(item);
                break;

            default:
                break;
        }
    }

    private void switchViews(CustomBaseViewHolder holder, VideoInfo item) {
        switch (item.getDownloadStatus()) {
            case DownloadStatus.DOWNLOADING:
                NumberProgressBar pbDownload = holder.getView(R.id.pb_download);
                if (!holder.isVisible(R.id.pb_download) || !holder.isSelected(R.id.btn_download)) {
                    holder.setVisible(R.id.pb_download, true)
                            .setText(R.id.tv_total_size, StringUtils.convertStorageNoB(item.getTotalSize()))
                            .setTextColor(R.id.tv_speed, ContextCompat.getColor(mContext, R.color.download_normal));
                    holder.setSelected(R.id.btn_download, true);
                    pbDownload.setMax((int) item.getTotalSize());
                }
                holder.setText(R.id.tv_load_size, StringUtils.convertStorageNoB(item.getLoadedSize()) + "/")
                        .setText(R.id.tv_speed, StringUtils.convertStorageNoB(item.getDownloadSpeed()) + "/s");
                pbDownload.setProgress((int) item.getLoadedSize());
                break;
            case DownloadStatus.COMPLETE:
                mRxBus.post(item);
            case DownloadStatus.CANCEL:
                if(mData.contains(item)) {
                    mData.remove(item);
                    notifyDataSetChanged();
                }
                Logger.d("取消:"+item.getDownloadStatus()+".......");
                break;
            case DownloadStatus.STOP:
                if (holder.isVisible(R.id.pb_download) || holder.isSelected(R.id.btn_download)) {
                    holder.setVisible(R.id.pb_download, false)
                            .setText(R.id.tv_speed, "下载暂停")
                            .setTextColor(R.id.tv_speed, ContextCompat.getColor(mContext, R.color.download_stop));
                    holder.setSelected(R.id.btn_download, false);
                }
                Logger.d("停止:"+item.getDownloadStatus()+".......");
                break;
            case DownloadStatus.ERROR:
                holder.setText(R.id.tv_speed, "异常出错，请重新下载")
                        .setVisible(R.id.pb_download, false)
                        .setTextColor(R.id.tv_speed, ContextCompat.getColor(mContext, R.color.download_error));
                holder.setSelected(R.id.btn_download, false);
                break;
        }
    }

    /*********************************************************************/

    /**
     * 更新下载状态
     *
     * @param info 应用信息
     */
    private void _updateDownload(VideoInfo info) {
        CustomBaseViewHolder holder = saveInfo.get(info.getVideoUrl());
        if (holder == null) {
            return;
        }
        switchViews(holder, info);
    }

    /**
     * 查找 VideoInfo
     * url:http://flv2.bn.netease.com/videolib3/1501/28/wlncJ2098/SD/wlncJ2098-mobile.mp4
     *
     * @param name wlncJ2098-mobile.mp4 -> 文件名
     * @return
     */
    private VideoInfo _find(String name) {
        for (VideoInfo info : mData) {
            if (name.equals(StringUtils.clipFileName(info.getVideoUrl()))) {
                return info;
            }
        }
        return null;
    }

    /**
     * 更新下载状态
     *
     * @param fileInfo 文件信息
     */
    public void updateDownload(FileInfo fileInfo) {
        VideoInfo info = _find(fileInfo.getName());
        if (info == null) {
            return;
        }
        info.setDownloadStatus(fileInfo.getStatus());
        info.setTotalSize(fileInfo.getTotalBytes());
        info.setLoadedSize(fileInfo.getLoadBytes());
        info.setDownloadSpeed(fileInfo.getSpeed());

        _updateDownload(info);
    }

}
