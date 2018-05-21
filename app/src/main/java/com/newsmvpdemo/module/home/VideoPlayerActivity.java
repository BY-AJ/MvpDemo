package com.newsmvpdemo.module.home;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.dl7.downloaderlib.model.DownloadStatus;
import com.dl7.player.danmaku.OnDanmakuListener;
import com.dl7.player.media.IjkPlayerView;
import com.dl7.player.utils.SoftInputUtils;
import com.newsmvpdemo.R;
import com.newsmvpdemo.danmaku.DanmakuConverter;
import com.newsmvpdemo.danmaku.DanmakuLoader;
import com.newsmvpdemo.danmaku.DanmakuParser;
import com.newsmvpdemo.engine.DownloaderWrapper;
import com.newsmvpdemo.inject.components.DaggerVideoPlayerComponent;
import com.newsmvpdemo.inject.modules.VideoPlayerModule;
import com.newsmvpdemo.local.table.DanmakuInfo;
import com.newsmvpdemo.local.table.VideoInfo;
import com.newsmvpdemo.module.base.BaseActivity;
import com.newsmvpdemo.module.presenter.IVideoPlayerPresenter;
import com.newsmvpdemo.module.view.IVideoPlayerView;
import com.newsmvpdemo.utils.DialogHelper;
import com.newsmvpdemo.utils.ShareUtils;
import com.newsmvpdemo.utils.SnackbarUtils;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ictcxq on 2018/3/16.
 */

public class VideoPlayerActivity extends BaseActivity<IVideoPlayerPresenter> implements IVideoPlayerView{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.video_player)
    IjkPlayerView mPlayerView;
    @BindView(R.id.iv_video_share)
    ImageView mIvVideoShare;
    @BindView(R.id.iv_video_collect)
    ShineButton mIvVideoCollect;
    @BindView(R.id.iv_video_download)
    ImageView mIvVideoDownload;
    @BindView(R.id.ll_operate)
    LinearLayout mLlOperate;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.ll_edit_layout)
    LinearLayout mLlEditLayout;
    @BindView(R.id.btn_send)
    Button mBtnSend;

    public static final String VIDEO_DATA_KEY = "VideoDataKey";
    public static final String RESULT_KEY = "ResultKey";
    public static final int VIDEO_REQUEST_CODE = 10087;
    private VideoInfo mVideoInfo;

    public static void launch(Context context, VideoInfo data) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra(VIDEO_DATA_KEY, data);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_bottom_entry, R.anim.hold);
    }

    public static void launchForResult(Fragment fragment, VideoInfo data) {
        Intent intent = new Intent(fragment.getContext(), VideoPlayerActivity.class);
        intent.putExtra(VIDEO_DATA_KEY, data);
        fragment.startActivityForResult(intent,VIDEO_REQUEST_CODE);
        fragment.getActivity().overridePendingTransition(R.anim.slide_bottom_entry, R.anim.hold);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_video_player;
    }

    @Override
    protected void initInject() {
        mVideoInfo = getIntent().getParcelableExtra(VIDEO_DATA_KEY);
        DaggerVideoPlayerComponent.builder()
                .applicationComponent(getAppComponent())
                .videoPlayerModule(new VideoPlayerModule(this,mVideoInfo))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        initToolBar(mToolbar,true,mVideoInfo.getTitle());
        //加载播放界面图片
        Glide.with(this).load(mVideoInfo.getCover()).fitCenter().into(mPlayerView.mPlayerThumb);
        mPlayerView.init()
                .setTitle(mVideoInfo.getTitle())//设置标题
                .setVideoSource(null,mVideoInfo.getMp4_url(),mVideoInfo.getMp4Hd_url(),null,null)//设置视频来源
                .enableDanmaku()//使能弹幕库
                .setDanmakuCustomParser(new DanmakuParser(), DanmakuLoader.instance(), DanmakuConverter.instance())//自定义弹幕库解析器
                .setDanmakuListener(new OnDanmakuListener<DanmakuInfo>() {//设置弹幕监听
                    @Override
                    public boolean isValid() {
                        return true;
                    }
                    @Override
                    public void onDataObtain(DanmakuInfo danmakuInfo) {
                        danmakuInfo.setUserName("yb");
                        danmakuInfo.setVid(mVideoInfo.getVid());
                        mPresenter.addDanmaku(danmakuInfo);
                    }
                });
        mIvVideoCollect.init(this);
        mIvVideoCollect.setShapeResource(R.drawable.ic_video_collect);
        mIvVideoCollect.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                mVideoInfo.setIsCollect(checked);
                if(mVideoInfo.getIsCollect()) {
                    mPresenter.insert(mVideoInfo);//收藏,就添加到数据库
                }else {
                    mPresenter.delete(mVideoInfo);//不收藏，就从数据库删除
                }
            }
        });
        //设置Edittext焦点监听事件
        mEtContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    mPlayerView.editVideo();
                }
                mLlOperate.setVisibility(hasFocus ? View.GONE : View.VISIBLE);
            }
        });
    }

    @OnClick({R.id.iv_video_download,R.id.iv_video_share,R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_video_share :
                ShareUtils.showShare(this);//分享
                break;
            case R.id.iv_video_download :
                if(view.isSelected()) {
                    DialogHelper.checkDialog(this,mVideoInfo);
                }else {
                    DialogHelper.downloadDialog(this, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DownloaderWrapper.start(mVideoInfo);
                            mIvVideoDownload.setSelected(true);
                            SnackbarUtils.showDownloadSnackbar(VideoPlayerActivity.this,"任务正在下载",true);
                        }
                    });
                }
                break;
            case R.id.btn_send:
                mPlayerView.sendDanmaku(mEtContent.getText().toString(),false);
                mEtContent.setText("");
                closeSoftInput();
                break;
        }
    }

    private void closeSoftInput() {
        mEtContent.clearFocus();
        SoftInputUtils.closeSoftInput(this);
        mPlayerView.recoverFromEditVideo();
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPlayerView.configurationChanged(newConfig);
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

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void loadData(VideoInfo data) {
        mVideoInfo = data;
        mIvVideoCollect.setChecked(data.getIsCollect());
        mIvVideoDownload.setSelected(data.getDownloadStatus() != DownloadStatus.NORMAL);
    }

    @Override
    public void loadDanmakuData(InputStream inputStream) {
        mPlayerView.setDanmakuSource(inputStream);
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(RESULT_KEY, mVideoInfo.getIsCollect());
        setResult(RESULT_OK, intent);
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_bottom_exit);
    }
}
