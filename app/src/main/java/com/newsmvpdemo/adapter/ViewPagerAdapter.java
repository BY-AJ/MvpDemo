package com.newsmvpdemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.newsmvpdemo.module.fragment.NewsListFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by yb on 2018/3/3.
 * ViewPager适配器
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    List<String> mTitles;//标题
    List<Fragment> fragments;//对应的fragment

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        mTitles = new ArrayList<String>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    //重写该方法，实现fragment顺序的变换
    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;//重新更新这个位置fragment
        //return POSITION_UNCHANGED;//那么该位置的fragment是不会被更新的
    }


    /**
     * 设置适配器条目
     * @param fragments
     * @param titles
     */
    public void setItems(List<Fragment> fragments, List<String> titles) {
        this.fragments = fragments;
        this.mTitles = titles;
        notifyDataSetChanged();
    }

    public void setItems(List<Fragment> fragments, String[] mTitles) {
        this.fragments = fragments;
        this.mTitles = Arrays.asList(mTitles);
        notifyDataSetChanged();
    }

    /**
     * 添加一个条目
     * @param fragment
     * @param name
     */
    public void addItem(NewsListFragment fragment, String name) {
        fragments.add(fragment);
        mTitles.add(name);
        notifyDataSetChanged();
    }

    /**
     * 删除一个条目
     * @param name
     * @return
     */
    public int deleteItem(String name) {
        int index = mTitles.indexOf(name);
        if(index != -1) {
            deleteItem(index);
        }
        return index;
    }

    /**
     * 删除一个条目
     * @param pos
     */
    public void deleteItem(int pos) {
        mTitles.remove(pos);
        fragments.remove(pos);
        notifyDataSetChanged();
    }

    /**
     * 互换条目
     * @param fromPos
     * @param toPos
     */
    public void swapItems(int fromPos, int toPos) {
        Collections.swap(mTitles,fromPos,toPos);
        Collections.swap(fragments,fromPos,toPos);
        notifyDataSetChanged();
    }

}
