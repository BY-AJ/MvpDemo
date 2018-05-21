package com.newsmvpdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.github.ybq.android.spinkit.SpinKitView;
import com.newsmvpdemo.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 加载、空视图
 * Created by yb on 2018/2/28.
 */

public class EmptyLayout extends FrameLayout{

    public static final int STATUS_HIDE = 1001;//隐藏
    public static final int STATUS_LOADING = 1;//正在加载
    public static final int STATUS_NO_NET = 2;//无网络
    public static final int STATUS_NO_DATA = 3;//无数据
    private Context mContext;
    private int mEmptyStatus = STATUS_LOADING;
    private int mBgcolor;//背景颜色

    @BindView(R.id.fl_empty_layout)
    FrameLayout mEmptyLayout;
    @BindView(R.id.fl_empty_container)
    FrameLayout mEmptyContainer;
    @BindView(R.id.empty_loading)
    SpinKitView mEmptyLoading;

    public EmptyLayout(Context context) {
        this(context,null);
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        //1.获取控件属性
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.EmptyLayout);
        try {
            mBgcolor = a.getColor(R.styleable.EmptyLayout_background_color, Color.WHITE);
        }finally {
            a.recycle();
        }
        //2.打气筒加载空布局
        View.inflate(mContext, R.layout.layout_empty_loading, this);
        //3.绑定控件
        ButterKnife.bind(this);
        //4.设置背景颜色
        mEmptyLayout.setBackgroundColor(mBgcolor);
        //5.显示对应的布局控件
        switchEmptyView();
    }

    private void switchEmptyView() {
        switch (mEmptyStatus) {
            case STATUS_LOADING://正在加载
                setVisibility(VISIBLE);
                mEmptyContainer.setVisibility(GONE);
                mEmptyLoading.setVisibility(VISIBLE);
                break;
            case STATUS_NO_NET:
            case STATUS_NO_DATA://无网络，无数据
                setVisibility(VISIBLE);
                mEmptyContainer.setVisibility(VISIBLE);
                mEmptyLoading.setVisibility(GONE);
                break;
            case STATUS_HIDE://隐藏
                setVisibility(GONE);
                break;
        }
    }

    /**
     * 隐藏视图
     */
    public void hide() {
        mEmptyStatus = STATUS_HIDE;
        switchEmptyView();
    }

    /**
     * 设置加载的状态
     */
    public void setEmptyStatus(@EmptyStatus int status) {
        mEmptyStatus = status;
        switchEmptyView();//重新刷新视图
    }

    /**
     * 获取加载的状态
     */
    public int getEmptyStatus(){
        return mEmptyStatus;
    }

    /**
     * 设置监听事件
     */
    private OnRetryListener listener;

    public void setOnRetryListener(OnRetryListener listener) {
        this.listener = listener;
    }

    public interface OnRetryListener {
        void onRetry();
    }

    /**
     * 点击重新加载
     */
    @OnClick(R.id.tv_net_error)
    public void onClick(){
        if(listener != null) {
            listener.onRetry();
        }
    }

    /**
     * 使用注解的方式来代替枚举类型
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUS_LOADING,STATUS_NO_NET,STATUS_NO_DATA})
    public @interface EmptyStatus{}
}
