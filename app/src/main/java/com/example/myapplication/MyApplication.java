package com.example.myapplication;

import android.app.Application;
import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpHeaders;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class MyApplication  extends Application {
    private static Context sContext;


    public String  mToken=null;

    public List<BaseActivity> activities=new ArrayList<>();

    public String getmToken() {
        return mToken;
    }

    public void setmToken(String mToken) {
        this.mToken = mToken;
    }
    @Override
    public void onCreate() {

        super.onCreate();

        sContext = getApplicationContext();

        initOkGo();

    }

    public static Context getContext(){

        return sContext;

    }

    private void initOkGo() {
         OkHttpClient.Builder builder = new OkHttpClient.Builder();
         builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
         builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
         builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
         OkGo.getInstance().init(this)                           //必须调用初始化
         .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
         .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
         .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
         .setRetryCount(3);

    }

}
