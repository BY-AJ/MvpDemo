package com.newsmvpdemo.module.home;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.animation.SlideEnter.SlideBottomEnter;
import com.flyco.animation.SlideEnter.SlideLeftEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.flyco.animation.SlideExit.SlideTopExit;
import com.newsmvpdemo.R;
import com.newsmvpdemo.api.NewsUtils;
import com.newsmvpdemo.inject.components.DaggerNewsActicleComponent;
import com.newsmvpdemo.inject.modules.NewsArticleModule;
import com.newsmvpdemo.module.base.BaseSwipeBackActivity;
import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.bean.NewsDetailInfo;
import com.newsmvpdemo.module.view.INewsArticleView;
import com.newsmvpdemo.utils.AnimatorHelper;
import com.newsmvpdemo.utils.DialogHelper;
import com.newsmvpdemo.utils.ListUtils;
import com.newsmvpdemo.utils.PreferencesUtils;
import com.newsmvpdemo.widget.EmptyLayout;
import com.newsmvpdemo.widget.PullScrollView;
import com.orhanobut.logger.Logger;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.OnURLClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yb on 2018/3/11.
 * 正常新闻详情页
 */

public class NewsArticleActivity extends BaseSwipeBackActivity<IBasePresenter> implements INewsArticleView{

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.ll_pre_toolbar)
    LinearLayout mLlPreToolbar;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.tv_next_title)
    TextView mTvNextTitle;
    @BindView(R.id.ll_foot_view)
    LinearLayout mLlFootView;
    @BindView(R.id.scroll_view)
    PullScrollView mScrollView;
    @BindView(R.id.empty_layout)
    EmptyLayout mEmptyLayout;
    @BindView(R.id.iv_back_2)
    ImageView mIvBack2;
    @BindView(R.id.tv_title_2)
    TextView mTvTitle2;
    @BindView(R.id.ll_top_bar)
    LinearLayout mLlTopBar;

    private String mNewsId;//新闻详情Id
    private int mToolbarHeight;//Toolbar的高度
    private int mTopBarHeight;//顶部Toolbar的高度
    private int mMinScrollSlop;// 最小触摸滑动距离
    private Animator mTopBarAnimator;//顶部Toolbar的动画对象
    private int mLastScrollY = 0;
    private String mNextNewsId;//关联的下一个新闻详情Id

    private static final String SHOW_POPUP_DETAIL = "ShowPopupDetail";

    public static void launch(Context context, String newsId) {
        Intent intent = new Intent(context, NewsArticleActivity.class);
        intent.putExtra("newsId", newsId);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_right_entry, R.anim.hold);
    }

    private void launchInside(String newsId) {
        Intent intent = new Intent(this, NewsArticleActivity.class);
        intent.putExtra("newsId", newsId);
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.slide_bottom_entry, R.anim.hold);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_newdetail;
    }

    @Override
    protected void initInject() {
        mNewsId = getIntent().getStringExtra("newsId");
        DaggerNewsActicleComponent.builder()
                .applicationComponent(getAppComponent())
                .newsArticleModule(new NewsArticleModule(this,mNewsId))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        mToolbarHeight = getResources().getDimensionPixelSize(R.dimen.news_detail_toolbar_height);
        mTopBarHeight = getResources().getDimensionPixelSize(R.dimen.toolbar_height);
        //滑动的最小距离
        mMinScrollSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        Logger.d("滑动的最小距离:"+mMinScrollSlop);
        //隐藏顶部的Toolbar,setTranslationY改变view在Y轴的位置
        //偏移量为正数时，表示View从上向下平移。反之则从下向上平移
        ViewCompat.setTranslationY(mLlTopBar, -mTopBarHeight);
        //设置ScrollView的滚动监听，主要实现在滚动过程中mLlTopBar的显示与隐藏
        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Logger.d("scrollY："+scrollY);
                if(scrollY > mToolbarHeight) {
                    //如果动画正在运行，不操作
                    if(AnimatorHelper.isRunning(mTopBarAnimator)) {
                        return;
                    }
                    if(Math.abs(scrollY-mLastScrollY) > mMinScrollSlop) {
                        boolean isPullUp = scrollY > mLastScrollY;
                        mLastScrollY = scrollY;
                        Logger.d("isPullUp:"+isPullUp+".....顶部Toolbar:"+mLlTopBar.getTranslationY());
                        if(isPullUp && mLlTopBar.getTranslationY() != -mTopBarHeight) {
                            //隐藏顶部ToolBar
                            mTopBarAnimator = AnimatorHelper.doMoveVertical(mLlTopBar, (int) mLlTopBar.getTranslationY(),
                                    -mTopBarHeight,300);
                        }else if(!isPullUp && mLlTopBar.getTranslationY() != 0) {
                            //显示顶部Toolbar,通过动画实现
                            mTopBarAnimator = AnimatorHelper.doMoveVertical(mLlTopBar, (int) mLlTopBar.getTranslationY(),
                                    0,300);
                        }
                    }
                }else {
                    //上拉到顶部的时候，隐藏顶部ToolBar
                    //mLlTopBar.getTranslationY() != -mTopBarHeight
                    if(mTopBarAnimator != null) {
                        AnimatorHelper.stopAnimator(mTopBarAnimator);
                        ViewCompat.setTranslationY(mLlTopBar,-mTopBarHeight);
                        mLastScrollY = 0;
                    }
                }
            }
        });
        //给ScrollView添加脚布局
        mScrollView.setFootView(mLlFootView);
        //设置ScrollView的上拉监听
        mScrollView.setOnPullListener(new PullScrollView.OnPullListener() {
            boolean isShowPopup = PreferencesUtils.getBoolean(NewsArticleActivity.this, SHOW_POPUP_DETAIL, true);
            @Override
            public boolean isDoPull() {
                if(mEmptyLayout.getEmptyStatus() != EmptyLayout.STATUS_HIDE) {
                    return false;
                }
                if(isShowPopup) {
                    showPopup();
                    isShowPopup = false;
                }
                return true;
            }
            @Override
            public boolean handlePull() {
                //处理上拉操作
                if(TextUtils.isEmpty(mNextNewsId)) {
                    return false;
                }else {
                    //开启下一个新闻详情页
                    launchInside(mNextNewsId);
                    return true;
                }
            }
        });
    }

    /**
     * 第一次进入新闻详情页，上拉倒底部的时候，显示弹出提示
     */
    private void showPopup() {
        if (PreferencesUtils.getBoolean(this, SHOW_POPUP_DETAIL, true)) {
            DialogHelper.createPopup(this,R.layout.layout_popup)
                    .anchorView(mTvTitle2)//锚准那个控件
                    .gravity(Gravity.BOTTOM)//设置相对于锚准控件的位置
                    .showAnim(new SlideBottomEnter())//设置进来显示的动画
                    .dismissAnim(new SlideTopExit())//设置退出显示的动画
                    .autoDismiss(true)//设置自动消失
                    .autoDismissDelay(3500)//设置消失的延迟时间
                    .show();//显示
            DialogHelper.createPopup(this, R.layout.layout_popup_bottom)
                    .anchorView(mLlFootView)
                    .gravity(Gravity.TOP)
                    .showAnim(new SlideLeftEnter())
                    .dismissAnim(new SlideRightExit())
                    .autoDismiss(true)
                    .autoDismissDelay(3500)
                    .show();
            PreferencesUtils.putBoolean(this, SHOW_POPUP_DETAIL, false);
        }
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void loadData(NewsDetailInfo newsDetailBean) {
        mTvTitle.setText(newsDetailBean.getTitle());//设置新闻标题
        mTvTitle2.setText(newsDetailBean.getTitle());//设置新闻标题
        mTvTime.setText(newsDetailBean.getPtime());//设置新闻时间
        RichText.from(newsDetailBean.getBody()).into(mTvContent);//设置富文本加载到控件mTvContent中
        handleSpInfo(newsDetailBean.getSpinfo());//处理关联的内容
        handleRelatedNews(newsDetailBean);//处理关联新闻
    }

    /**
     * 处理关联新闻，设置标题
     */
    private void handleRelatedNews(NewsDetailInfo newsDetailBean) {
        if(ListUtils.isEmpty(newsDetailBean.getRelative_sys())) {
            mTvNextTitle.setText("没有相关的文章了");
        }else {
            mNextNewsId = newsDetailBean.getRelative_sys().get(0).getId();
            mTvNextTitle.setText(newsDetailBean.getRelative_sys().get(0).getTitle());
        }
    }

    /**
     * 处理关联的内容
     */
    private void handleSpInfo(List<NewsDetailInfo.SpinfoEntity> spinfo) {
        if(!ListUtils.isEmpty(spinfo)) {
            ViewStub stub = (ViewStub) findViewById(R.id.vs_related_content);
            assert stub != null;
            //显示布局
            stub.inflate();
            TextView tvType = (TextView) findViewById(R.id.tv_type);
            TextView tvRelatedContent = (TextView) findViewById(R.id.tv_related_content);
            tvType.setText(spinfo.get(0).getSptype());
            RichText.from(spinfo.get(0).getSpcontent())
                    .urlClick(new OnURLClickListener() {//处理超链接的点击回调
                        @Override
                        public boolean urlClicked(String url) {
                            String newsId = NewsUtils.clipNewsIdFromUrl(url);
                            if(newsId != null) {
                                launch(NewsArticleActivity.this,newsId);
                            }
                            return true;
                        }
                    })
                    .into(tvRelatedContent);
        }
    }

    @OnClick({R.id.iv_back,R.id.iv_back_2,R.id.tv_title_2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
            case R.id.iv_back_2:
                finish();
                break;
            case R.id.tv_title_2:
                //点击标题，回到顶部
                mScrollView.stopNestedScroll();
                mScrollView.smoothScrollTo(0,0);
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_right_exit);
    }
}
