package com.newsmvpdemo.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.newsmvpdemo.R;
import com.newsmvpdemo.utils.AnimatorHelper;

/**
 * Created by yb on 2018/3/11.
 * 可滚动超出上拉的 ScrollView
 */

public class PullScrollView extends NestedScrollView{

    private OnPullListener listener;
    private int mPullCriticalDistance;
    private boolean mIsPullStatus = false;
    private float mLastY;
    private View mFootView;

    public PullScrollView(Context context) {
        this(context,null);
    }

    public PullScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public PullScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //上拉的最小距离
        mPullCriticalDistance = getResources().getDimensionPixelSize(R.dimen.pull_critical_distance);
    }

    /**
     * Scrollview在滑动过程就会回调该方法
     *  第一个参数为变化后的X轴位置
     *  第二个参数为变化后的Y轴的位置
     *  第三个参数为原先的X轴的位置
     *  第四个参数为原先的Y轴的位置
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //1.滑动到顶部判断为 getScrollY() == 0

        //2.滑动到底部判断：getScrollY()>=getChildAt(0).getMeasuredHeight()-getHeight()
        //getChildAt(0).getMeasuredHeight():表示得到子view的高度
        //getHeight():scrollView可见的高度即屏幕的高度
        //getScrollY():scrollView滑动的距离，达到最大时加上scrollView的高度就等于它内容的高度

        //如果滑动到最底部，实现监听回调
        if((t >=(getChildAt(0).getMeasuredHeight()-getHeight())) && listener!= null) {
            listener.isDoPull();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE :
                if(!mIsPullStatus) {
                    //滑动到最底部时，接着往上拉
                    if(getScrollY() >=(getChildAt(0).getMeasuredHeight()-getHeight()) || (getChildAt(0).getMeasuredHeight() < getHeight())) {
                        if(listener != null && listener.isDoPull()) {
                            mIsPullStatus = true;
                            mLastY = ev.getY();
                        }
                    }
                }else if(mLastY < ev.getY()) {
                    mIsPullStatus = false;
                    pullFootView(0);
                }else {
                    float offset = mLastY - ev.getY();
                    pullFootView(offset);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(mIsPullStatus) {
                    if(mFootView.getHeight() > mPullCriticalDistance && listener != null) {
                        if(!listener.handlePull()) {
                            AnimatorHelper.doClipViewHeight(mFootView,mFootView.getHeight(),0,200);
                        }
                    }else {
                        AnimatorHelper.doClipViewHeight(mFootView,mFootView.getHeight(),0,200);
                    }
                    mIsPullStatus = false;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void pullFootView(float offset) {
        if(mFootView != null) {
            ViewGroup.LayoutParams params = mFootView.getLayoutParams();
            params.height = (int) (offset*1/2);
            mFootView.setLayoutParams(params);
        }
    }

    public void setFootView(View view) {
        mFootView = view;
    }

    public void setOnPullListener(OnPullListener listener) {
        this.listener = listener;
    }

    public interface OnPullListener {
        boolean isDoPull();
        boolean handlePull();
    }
}
