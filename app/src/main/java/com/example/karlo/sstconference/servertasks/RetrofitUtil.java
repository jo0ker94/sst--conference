package com.example.karlo.sstconference.servertasks;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Karlo on 25.3.2018..
 */

public class RetrofitUtil {

    private RetrofitUtil() { }

    private static Retrofit mRetrofit = null;

    public static Retrofit getRetrofit(String baseUrl) {
        return mRetrofit != null ? mRetrofit : createRetrofit(baseUrl);
    }

    private static HttpLoggingInterceptor getInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    private static OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(getInterceptor())
                .build();
    }

    private static Retrofit createRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient())
                .build();
    }
}
