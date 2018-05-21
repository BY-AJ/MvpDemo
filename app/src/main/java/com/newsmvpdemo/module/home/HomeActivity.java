package com.newsmvpdemo.module.home;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.newsmvpdemo.R;
import com.newsmvpdemo.module.base.BaseActivity;
import com.newsmvpdemo.module.fragment.NewsFragment;
import com.newsmvpdemo.module.fragment.PhotosFragment;
import com.newsmvpdemo.module.fragment.VideosFragment;
import com.orhanobut.logger.Logger;

import butterknife.BindView;

/**
 * 主页
 */
public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    private int mItemId = -1;
    private long mExitTime = 0;

    //采用稀疏数组来代替HashMap<Integer,E>这种结构，目的是为了节约内存空间
    private SparseArray<String> sparseArray = new SparseArray<>();

    //处理Nav菜单栏点击后的事件处理
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case R.id.nav_news:
                    replaceFragment(R.id.fl_container,new NewsFragment(),sparseArray.get(R.id.nav_news));
                    break;
                case R.id.nav_photos:
                    replaceFragment(R.id.fl_container,new PhotosFragment(),sparseArray.get(R.id.nav_photos));
                    break;
                case R.id.nav_videos:
                    replaceFragment(R.id.fl_container,new VideosFragment(),sparseArray.get(R.id.nav_videos));
                    break;
                case R.id.nav_setting:
                    startActivity(new Intent(HomeActivity.this,SettingActivity.class));
                    break;
            }
            mItemId = -1;
        }
    };

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_home;
    }

    @Override
    protected void initViews() {
        initDrawerLayout();
        sparseArray.put(R.id.nav_news,"News");
        sparseArray.put(R.id.nav_photos,"Photos");
        sparseArray.put(R.id.nav_videos,"Videos");
    }

    @Override
    protected void initInject() {
    }

    private void initDrawerLayout() {
        //设置沉淀状态栏
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | params.flags);
            //将侧边栏顶部延伸至status bar
            mDrawerLayout.setFitsSystemWindows(true);
            //将主页面顶部延伸至status bar
            mDrawerLayout.setClipToPadding(false);
        }
        //对抽屉的打开与隐藏设置监听--只处理关闭抽屉的事件(抽屉关闭时，回调该方法)
        mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                mHandler.sendEmptyMessage(mItemId);
            }
        });
        //设置NavigationView菜单栏点击事件
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mNavigationView.setCheckedItem(R.id.nav_news);
        addFragment(R.id.fl_container,new NewsFragment(),"News");
    }

    //Nav菜单监听回调
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);//关闭抽屉
        if(item.isChecked()) {
            return true;
        }
        mItemId = item.getItemId();
        return true;
    }

    //菜单栏监听回调
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //回退按键几种情况的处理
    @Override
    public void onBackPressed() {
        //获取堆栈里总共有几个Fragment
        int stackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        Logger.d("堆栈中fragment个数："+stackEntryCount);
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else if(stackEntryCount == 1) {
            // 如果剩一个说明在主页，提示按两次退出app
            _exit();
        }else {
            //获取堆栈中前一个fragment的标记
            String tagName = getSupportFragmentManager().getBackStackEntryAt(stackEntryCount - 2).getName();
            //找到对应的下标
            int index = sparseArray.indexOfValue(tagName);
            //设置Nav菜单的点击
            mNavigationView.setCheckedItem(sparseArray.keyAt(index));
            super.onBackPressed();
        }

    }

    private void _exit() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
