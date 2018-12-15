package com.shuyun.qapp.base;

import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求配置
 */
public class BasePresenter {
    private static final long CONNECT_TIMEOUT = 10L;
    private static final long READ_TIMEOUT = 15L;
    private static final long WRITE_TIMEOUT = 15L;
    private static volatile OkHttpClient mOkHttpClient;
    private static ApiService apiService;

    public static ApiService create(int port) {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConst.BASE_URL)
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    public static ApiService Builder() {
        return create(0);
    }

    /**
     * 获取OkHttpClient实例
     *
     * @return
     */
    private static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (BasePresenter.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                            .addInterceptor(mSignagureInteceptor)
                            .addInterceptor(mLoggingInterceptor)
                            .build();
                }
            }
        }
        return mOkHttpClient;
    }

    private static final Interceptor mSignagureInteceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            HttpUrl url = originalRequest.url();
            URI uri = url.uri();
            String urlPath = uri.getPath();
            /**
             * 登录前没有获取到Authorization和sycm请求头参数;
             * 在调用登录和获取验证码接口的时候,需要reture chain.proceed(originalRequest);
             */
            if (urlPath.contains("/rest/security/")) {
                return chain.proceed(originalRequest);
            }
            Request newRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", AppConst.TOKEN)
                    .addHeader("sycm", AppConst.sycm())
                    .build();
            return chain.proceed(newRequest);
        }
    };
    /**
     * 日志拦截器
     */
    private static final Interceptor mLoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);//请求输入的链接
            //错误拦截
            if (response.code() != 200) {
                return new Response.Builder()
                        .code(0)
                        .body(ResponseBody.create(response.body().contentType(), "服务器错误！"))
                        .request(request)
                        .protocol(Protocol.HTTP_1_1)
                        .build();
            }
            return response;
        }
    };
}
