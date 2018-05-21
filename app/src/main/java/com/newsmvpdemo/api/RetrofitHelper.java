package com.newsmvpdemo.api;

import com.newsmvpdemo.AndroidApplication;
import com.newsmvpdemo.local.table.VideoInfo;
import com.newsmvpdemo.local.table.WelfareInfo;
import com.newsmvpdemo.module.bean.NewsDetailInfo;
import com.newsmvpdemo.module.bean.NewsInfo;
import com.newsmvpdemo.module.bean.PhotoInfo;
import com.newsmvpdemo.module.bean.PhotoSetInfo;
import com.newsmvpdemo.module.bean.SpecialInfo;
import com.newsmvpdemo.module.bean.WelfarePhotoList;
import com.newsmvpdemo.utils.NetUtil;
import com.newsmvpdemo.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yb on 2018/3/7.
 * 网络管理帮助类
 */

public final class RetrofitHelper {

    private static final String HEAD_LINE_NEWS = "T1348647909107";
    private static final int DEFAULT_TIMEOUT = 15;//请求超时时长，单位秒
    private static final String NEWS_HOST = "http://c.3g.163.com/";
    private static final String WELFARE_HOST = "http://gank.io/";
    private static INewsApi mNewsApiService;
    private static IWelfareApi mWelfareService;
    private static final int INCREASE_PAGE = 20;// 递增页码

    //(假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)
    static final String CACHE_CONTROL_NETWORK = "Cache-Control: public, max-age=3600";
    // 避免出现 HTTP 403 Forbidden，参考：http://stackoverflow.com/questions/13670692/403-forbidden-with-java-but-not-web-browser
    static final String AVOID_HTTP403_FORBIDDEN = "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";

    private RetrofitHelper() {
        throw new AssertionError();
    }

    //初始化配置
    public static void init() {
        //配置OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)//失败重试
                .addInterceptor(getHttpLoggingInterceptor())//统一输出请求日志
                .addInterceptor(getCacheInterceptor())//应用拦截器,它只会在response被调用一次
                .addNetworkInterceptor(getCacheInterceptor())//网络拦截器,它会在request和response时分别被调用一次
                .cache(getCache())//设置缓存
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)//设置请求超时
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NEWS_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        mNewsApiService = retrofit.create(INewsApi.class);

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(WELFARE_HOST)
                .build();
        mWelfareService = retrofit.create(IWelfareApi.class);
    }

    //设置统一Log日志插值器
    private static HttpLoggingInterceptor getHttpLoggingInterceptor(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    //设置通用header
    private static Interceptor getRequestHeader() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder builder = originalRequest.newBuilder();
                //比如头参数如下
                builder.header("appid","1") ;
                builder.header("timestamp", System.currentTimeMillis() + "");
                builder.header("appkey", "zRc9bBpQvZYmpqkwOo");
                builder.header("signature", "dsljdljflajsnxdsd");
                Request.Builder requestBuilder = builder.method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
    }

    //提供缓存插值器
    private static Interceptor getCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //对request的设置是用来指定有网/无网下所走的方式
                Request request = chain.request();
                if (!NetUtil.isNetworkAvailable(AndroidApplication.getContext())) {
                    //无网络下强制使用缓存，无论缓存是否过期,此时该请求实际上不会被发送出去。
                    request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                }
                //对response的设置是用来指定有网/无网下的缓存时长
                Response response = chain.proceed(request);
                if (NetUtil.isNetworkAvailable(AndroidApplication.getContext())) {
                    //有网络情况下，超过1分钟，则重新请求，否则直接使用缓存数据
                    int maxAge = 60; //缓存一分钟
                    String cacheControl = "public,max-age=" + maxAge;
                    //当然如果你想在有网络的情况下都直接走网络，那么只需要
                    //将其超时时间这是为0即可:String cacheControl="Cache-Control:public,max-age=0"
                    return response.newBuilder().header("Cache-Control", cacheControl).removeHeader("Pragma").build();
                } else {
                    //无网络时直接取缓存数据，该缓存数据保存1周
                    int maxStale = 60 * 60 * 24 * 7 * 1;  //1周
                    return response.newBuilder().header("Cache-Control", "public,only-if-cached,max-stale=" + maxStale).removeHeader("Pragma").build();
                }

            }
        };
    }

    //配置缓存
    private static Cache getCache() {
        Cache cache = new Cache(new File(AndroidApplication.getContext().getCacheDir(), "HttpCache"), 1024 * 1024 * 50);
        return cache;
    }

    /********************************************** API *******************************************/
    /**
     * 获取新闻列表
     */
    public static Observable<NewsInfo> getNewsList(String newsId,int page) {
        String type = "";
        if(newsId.equals(HEAD_LINE_NEWS)) {
            type = "headline";
        }else {
            type = "list";
        }
        return mNewsApiService.getNewsList(type,newsId,page * INCREASE_PAGE)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(_flatMapNews(newsId));
    }

    /**
     * 获取新闻详情
     */
    public static Observable<NewsDetailInfo> getNewsDetail(final String newsId) {
        return mNewsApiService.getNewsDetail(newsId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Map<String, NewsDetailInfo>, Observable<NewsDetailInfo>>() {
                    @Override
                    public Observable<NewsDetailInfo> call(Map<String, NewsDetailInfo> newsDetailInfoMap) {
                        return Observable.just(newsDetailInfoMap.get(newsId));
                    }
                });
    }

    /**
     * 获取专题
     */
    public static Observable<SpecialInfo> getSpecial(String newsId) {
        return mNewsApiService.getSpecial(newsId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(_flatMapSpecial(newsId));
    }

    /**
     * 获取图集
     */
    public static Observable<PhotoSetInfo> getPhotoSet(String newsId) {
        return mNewsApiService.getPhotoSet(StringUtils.clipPhotoSetId(newsId))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取视频
     */
    public static Observable<List<VideoInfo>> getVideoList(String videoId, int page) {
        return mNewsApiService.getVideoList(videoId,page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(_flatMapVideo(videoId));
    }

    /**
     * 获取福利
     */
    public static Observable<WelfareInfo> getWelfarePhoto(int page) {
        return mWelfareService.getWelfarePhoto(page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(_flatMapWelfarePhotos());

    }

    /**
     * 获取图片---生活
     */
    public static Observable<List<PhotoInfo>> getPhotoList() {
        return mNewsApiService.getPhotoList()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取图片---生活加载更多
     */
    public static  Observable<List<PhotoInfo>> getPhotoMoreList(String setId) {
        return mNewsApiService.getPhotoMoreList(setId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 类型转换
     * @return 福利
     */
    private static Func1<WelfarePhotoList,Observable<WelfareInfo>> _flatMapWelfarePhotos() {
        return new Func1<WelfarePhotoList, Observable<WelfareInfo>>() {
            @Override
            public Observable<WelfareInfo> call(WelfarePhotoList welfarePhotoList) {
                if(welfarePhotoList.getResults().size() == 0) {
                    return Observable.empty();
                }
                return Observable.from(welfarePhotoList.getResults());
            }
        };
    }


    /**
     * 类型转换
     * @param   newsId 新闻类型id
     * @return
     */
    private static Func1<Map<String, List<NewsInfo>>,Observable<NewsInfo>> _flatMapNews(final String newsId) {
        return new Func1<Map<String, List<NewsInfo>>, Observable<NewsInfo>>() {
            @Override
            public Observable<NewsInfo> call(Map<String, List<NewsInfo>> stringListMap) {
                return Observable.from(stringListMap.get(newsId));
            }
        };

    }

    /**
     * 类型转换
     * @param newsId 专题id
     * @return
     */
    private static Func1<Map<String,SpecialInfo>,Observable<SpecialInfo>> _flatMapSpecial(final String newsId) {
        return new Func1<Map<String,SpecialInfo>, Observable<SpecialInfo>>() {
            @Override
            public Observable<SpecialInfo> call(Map<String,SpecialInfo> stringListMap) {
                return Observable.just(stringListMap.get(newsId));
            }
        };
    }

    /**
     * 视频类型转换
     * @param videoId
     * @return
     */
    private static Func1<Map<String, List<VideoInfo>>,Observable<List<VideoInfo>>> _flatMapVideo(final String videoId) {
        return new Func1<Map<String, List<VideoInfo>>, Observable<List<VideoInfo>>>() {
            @Override
            public Observable<List<VideoInfo>> call(Map<String, List<VideoInfo>> stringListMap) {
                return Observable.just(stringListMap.get(videoId));
            }
        };
    }

}
