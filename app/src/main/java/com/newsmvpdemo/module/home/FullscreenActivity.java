package com.newsmvpdemo.module.home;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import com.dl7.downloaderlib.FileDownloader;
import com.dl7.player.media.IjkPlayerView;
import com.newsmvpdemo.R;
import com.newsmvpdemo.local.table.VideoInfo;
import com.newsmvpdemo.module.base.BaseActivity;

import static com.newsmvpdemo.utils.CommonConstant.VIDEO_DATA_KEY;

/**
 * Created by ictcxq on 2018/4/3.
 */

public class FullscreenActivity extends BaseActivity{

    private IjkPlayerView mPlayerView;

    private VideoInfo mVideoData;

    public static void launch(Context context, VideoInfo data) {
        Intent intent = new Intent(context, FullscreenActivity.class);
        intent.putExtra(VIDEO_DATA_KEY, data);
        context.startActivity(intent);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_full;
    }

    @Override
    protected void initInject() {
        mPlayerView = (IjkPlayerView) findViewById(R.id.video_player);
    }

    @Override
    protected void initViews() {
        mVideoData = getIntent().getParcelableExtra(VIDEO_DATA_KEY);
        mPlayerView.init()
                .alwaysFullScreen()
                .enableOrientation()
                .setVideoPath(FileDownloader.getFilePathByUrl(mVideoData.getVideoUrl()))
                .setTitle(mVideoData.getTitle())
                .start();
    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPlayerView.handleVolumeKey(keyCode)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mPlayerView.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}
