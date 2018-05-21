package com.newsmvpdemo.api;

import com.newsmvpdemo.local.table.VideoInfo;
import com.newsmvpdemo.module.bean.NewsDetailInfo;
import com.newsmvpdemo.module.bean.NewsInfo;
import com.newsmvpdemo.module.bean.PhotoInfo;
import com.newsmvpdemo.module.bean.PhotoSetInfo;
import com.newsmvpdemo.module.bean.SpecialInfo;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import rx.Observable;

import static com.newsmvpdemo.api.RetrofitHelper.AVOID_HTTP403_FORBIDDEN;
import static com.newsmvpdemo.api.RetrofitHelper.CACHE_CONTROL_NETWORK;

/**
 * Created by yb on 2018/3/7.
 * 新闻类型服务地址接口
 */

public interface INewsApi {

    /**
     * 获取新闻列表
     * eg: http://c.m.163.com/nc/article/headline/T1348647909107/60-20.html
     *      http://c.m.163.com/nc/article/list/T1348647909107/60-20.html
     * @param type 新闻类型
     * @param id 新闻ID
     * @param startPage 起始页码
     */
    @Headers(CACHE_CONTROL_NETWORK)
    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    Observable<Map<String,List<NewsInfo>>> getNewsList(@Path("type") String type,@Path("id") String id,
                                                       @Path("startPage") int startPage);

    /**
     * 获取正常新闻详情页
     * eg: http://c.3g.163.com/nc/article/BV56RVG600011229/full.html
     */
    @Headers(AVOID_HTTP403_FORBIDDEN)
    @GET("nc/article/{newsId}/full.html")
    Observable<Map<String, NewsDetailInfo>> getNewsDetail(@Path("newsId") String newsId);

    /**
     * 获取专题新闻详情页
     * eg: http://c.3g.163.com/nc/special/S1451880983492.html
     */
    @Headers(CACHE_CONTROL_NETWORK)
    @GET("nc/special/{specialId}.html")
    Observable<Map<String, SpecialInfo>> getSpecial(@Path("specialId") String specialIde);

    /**
     * 获取图集新闻详情页
     * eg: http://c.3g.163.com/photo/api/set/0006/2136404.json
     */
    @Headers(CACHE_CONTROL_NETWORK)
    @GET("photo/api/set/{photoId}.json")
    Observable<PhotoSetInfo> getPhotoSet(@Path("photoId") String photoId);

    /**
     * 获取视频列表
     * eg: http://c.3g.163.com/nc/video/list/V9LG4B3A0/n/10-10.html
     *
     * @param id  video ID
     * @param startPage 起始页码
     * @return
     */
    @Headers(AVOID_HTTP403_FORBIDDEN)
    @GET("nc/video/list/{id}/n/{startPage}-10.html")
    Observable<Map<String, List<VideoInfo>>> getVideoList(@Path("id") String id,
                                                          @Path("startPage") int startPage);

    /**
     * 获取图片列表
     * eg: http://c.3g.163.com/photo/api/list/0096/4GJ60096.json
     *
     * @return
     */
    @Headers(CACHE_CONTROL_NETWORK)
    @GET("photo/api/list/0096/4GJ60096.json")
    Observable<List<PhotoInfo>> getPhotoList();

    /**
     * 获取更多图片列表
     * eg: http://c.3g.163.com/photo/api/morelist/0096/4GJ60096/106571.json
     *
     * @return
     */
    @Headers(CACHE_CONTROL_NETWORK)
    @GET("photo/api/morelist/0096/4GJ60096/{setId}.json")
    Observable<List<PhotoInfo>> getPhotoMoreList(@Path("setId") String setId);
}
