package com.newsmvpdemo.adapter;

import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dl7.downloaderlib.entity.FileInfo;
import com.newsmvpdemo.R;
import com.newsmvpdemo.adapter.holder.CustomBaseViewHolder;
import com.newsmvpdemo.engine.DownloaderWrapper;
import com.newsmvpdemo.local.table.VideoInfo;
import com.newsmvpdemo.module.event.VideosEvent;
import com.newsmvpdemo.utils.RxBusHelper;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by long on 2016/12/19.
 * video下载适配器基类
 */
public abstract class BaseVideoDLAdapter extends BaseQuickAdapter<VideoInfo,CustomBaseViewHolder> {

    private static final int INVALID_POS = -1;

    protected boolean mIsEditMode = false;
    protected SparseBooleanArray mSparseItemChecked = new SparseBooleanArray();
    protected final RxBusHelper mRxBus;

    public BaseVideoDLAdapter(@Nullable List<VideoInfo> data,RxBusHelper rxbus,int id) {
        super(id,data);
        mRxBus = rxbus;
    }

    /**
     * 处理选中事件
     * @param position
     * @param isChecked
     */
    protected void _handleCheckedChanged(int position, boolean isChecked) {
        if (position == INVALID_POS) {
            Logger.i(position + "" + isChecked);
            return;
        }
        mSparseItemChecked.put(position, isChecked);
        int checkedCount = 0;
        int checkedStatus;
        for (int i = 0; i < getItemCount(); i++) {
            if (mSparseItemChecked.get(i, false)) {
                checkedCount++;
            }
        }
        if (checkedCount == 0) {
            checkedStatus = VideosEvent.CHECK_NONE;
        } else if (checkedCount == getItemCount()) {
            checkedStatus = VideosEvent.CHECK_ALL;
        } else {
            checkedStatus = VideosEvent.CHECK_SOME;
        }
        // 通知 DownloadActivity 更新界面
        mRxBus.post(new VideosEvent(checkedStatus));
    }


    public boolean isEditMode() {
        return mIsEditMode;
    }

    public void setEditMode(boolean editMode) {
        mIsEditMode = editMode;
        if (!mIsEditMode) {
            mSparseItemChecked.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 切换 Item 的选中状态
     * @param position
     */
    public void toggleItemChecked(int position, BaseViewHolder holder) {
        boolean isChecked = mSparseItemChecked.get(position);
        Logger.d(position+""+!isChecked);
        holder.setChecked(R.id.cb_delete, !isChecked);
        _handleCheckedChanged(position, !isChecked);
    }

    public void deleteItemChecked() {
        for (int i = mSparseItemChecked.size() - 1; i >= 0; i--) {
            if (mSparseItemChecked.valueAt(i)) {
                DownloaderWrapper.delete(getItem(mSparseItemChecked.keyAt(i)));
                remove(mSparseItemChecked.keyAt(i));
                mSparseItemChecked.delete(mSparseItemChecked.keyAt(i));
            }
        }
    }

    public void checkAllOrNone(boolean isChecked) {
        for (int i = 0; i < getItemCount(); i++) {
            mSparseItemChecked.put(i, isChecked);
        }
        notifyDataSetChanged();
    }

    public abstract void updateDownload(FileInfo fileInfo);

}
