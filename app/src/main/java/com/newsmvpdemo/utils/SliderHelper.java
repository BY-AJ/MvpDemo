package com.newsmvpdemo.utils;

import android.content.Context;
import android.os.Bundle;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.newsmvpdemo.api.NewsUtils;
import com.newsmvpdemo.module.bean.NewsInfo;
import com.newsmvpdemo.module.home.NewsArticleActivity;
import com.newsmvpdemo.module.home.PhotoSetActivity;
import com.newsmvpdemo.module.home.SpecialActivity;

/**
 * Created by yb on 2018/3/6.
 * 广告滑动器帮助类
 * https://github.com/daimajia/AndroidImageSlider/blob/master/demo%2Fsrc%2Fmain%2Fjava%2Fcom%2Fdaimajia%2Fslider%2Fdemo%2FMainActivity.java
 */

public class SliderHelper {
    private static final String SLIDER_KEY = "SliderKey";

    private SliderHelper() {
        throw new RuntimeException("SliderHelper cannot be initialized!");
    }

    /**
     * 初始化设置Banner
     * @param context 上下文
     * @param sliderLayout Banner第三方控件
     * @param newsBean 数据源
     */
    public static void initAdSlider(final Context context, SliderLayout sliderLayout, final NewsInfo newsBean) {
        //循环遍历广告栏数据
        for (NewsInfo.AdData adData: newsBean.getAds()) {
            //创建一个滑块视图
            TextSliderView textSliderView = new TextSliderView(context);
            textSliderView.description(adData.getTitle())//设置描述
                    .image(adData.getImgsrc())//设置背景图片
                    .empty(DefIconFactory.provideIcon())//设置为空展示的图片
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)//设置图片展示的样式
                    //处理广告栏点击事件
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            if(slider != null) {
                                //获取之前保存的信息
                                NewsInfo.AdData adData = slider.getBundle().getParcelable(SLIDER_KEY);
                                if(adData != null) {
                                    if(NewsUtils.isNewsPhotoSet(adData.getTag())) {
                                        PhotoSetActivity.launch(context,adData.getUrl());
                                    }else if(NewsUtils.isNewsSpecial(adData.getTag())) {
                                        SpecialActivity.launch(context,adData.getUrl());
                                    }else {
                                        NewsArticleActivity.launch(context,adData.getUrl());
                                    }
                                }
                            }
                        }
                    });
            //将额外的信息保存起来
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putParcelable(SLIDER_KEY,adData);
            //将滑动视图添加到SliderLayout布局中
            sliderLayout.addSlider(textSliderView);
        }
        //选择SliderLayout的指示器样式，在右下角
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        //选择SliderLayout的视图转换效果
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        //设置自定义动画效果
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        //设置延迟时间
        sliderLayout.setDuration(4000);
    }
}
