package com.newsmvpdemo.module.home;

import android.content.Intent;
import android.widget.TextView;

import com.newsmvpdemo.R;
import com.newsmvpdemo.module.base.BaseActivity;
import com.newsmvpdemo.utils.RxHelper;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by yb on 2018/2/28.
 * Splash页
 */

public class SplashActivity extends BaseActivity{

    @BindView(R.id.tv_skip)
    TextView tvSkip;

    private boolean mIsSkip = false;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initViews() {}

    @Override
    protected void initInject() {
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        RxHelper.countdown(5)
                .compose(this.<Integer>bindToLife())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        toHome();
                    }
                    @Override
                    public void onError(Throwable e) {
                        Logger.d(e.getMessage());
                    }
                    @Override
                    public void onNext(Integer integer) {
                        tvSkip.setText("跳过 " + integer+"s");
                    }
                });
    }

    private void toHome() {
        if(!mIsSkip) {
            mIsSkip = true;
            finish();
            startActivity(new Intent(SplashActivity.this,HomeActivity.class));
            //第一个参数是进入时的动画，第二个参数是离开时的动画
            overridePendingTransition(R.anim.hold,R.anim.zoom_in_exit);
        }
    }

    @Override
    public void onBackPressed() {
        return;//不响应后退键
    }

    @OnClick(R.id.fl_ad)
    public void onClick() {
        toHome();
    }
}
