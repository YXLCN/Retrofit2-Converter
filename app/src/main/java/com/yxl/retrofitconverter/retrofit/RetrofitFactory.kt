package com.yxl.retrofitconverter.retrofit

import android.content.Context
import android.util.Log
import com.yxl.retrofitconverter.converter.RespAdapterFactory
import com.yxl.retrofitconverter.converter.RespConvertFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

class RetrofitFactory {
    private val BASE_URL: String = "http://www.geelun.cn"
    private var mRetrofit: Retrofit? = null
    private lateinit var mAnyTrustManager: AnyTrustManager

    constructor(context: Context) {
        val appContext = context.applicationContext
        init(appContext, BASE_URL)
    }

    companion object {
        //通过@JvmStatic注解，使得在Java中调用instance直接是像调用静态函数一样，
        //类似KLazilyDCLSingleton.getInstance(),如果不加注解，在Java中必须这样调用: KLazilyDCLSingleton.Companion.getInstance().
        @JvmStatic
        //使用lazy属性代理，并指定LazyThreadSafetyMode为SYNCHRONIZED模式保证线程安全
        private var sInstance: RetrofitFactory? = null

        fun getInstance(context: Context): RetrofitFactory =
            sInstance ?: synchronized(this) {
                sInstance ?: RetrofitFactory(context).apply {
                    sInstance = this
                }
            }
    }

    private fun init(appContext: Context, baseUrl: String) {
        if (mRetrofit != null) {
            return
        }

        val dispatcher = Dispatcher(Executors.newFixedThreadPool(3))
        dispatcher.maxRequests = 3
        dispatcher.maxRequestsPerHost = 1

        val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            //打印retrofit日志
            Log.i("RetrofitLog", it)
        })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
        if (baseUrl.startsWith("https")) {
            builder.sslSocketFactory(createSSLSocketFactory()!!, mAnyTrustManager)
                .hostnameVerifier(TrustAllHostnameVerifier())
        }
        val okHttpClient = builder.apply {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(20, TimeUnit.SECONDS)
            // 添加日志拦截器
            addInterceptor(loggingInterceptor)
            // 添加解析拦截器
            addInterceptor(getInterceptor())
            connectionPool(ConnectionPool(3, 30, TimeUnit.SECONDS))
        }.build()

        mRetrofit = initRetrofit(okHttpClient, baseUrl)
    }

    private fun initRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(baseUrl)
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            addCallAdapterFactory(RespAdapterFactory<Any>())
            addConverterFactory(RespConvertFactory())
            addConverterFactory(GsonConverterFactory.create())
            client(okHttpClient)
        }.build()
    }

    // 自定义拦截器
    private fun getInterceptor(): Interceptor {
        return Interceptor {
            val originalRequest = it.request()
            // 可在请求前进行token/header等设置操作
            val response = it.proceed(originalRequest)
            response
        }
    }

    fun createSSLSocketFactory(): SSLSocketFactory? {
        var sslFactory: SSLSocketFactory? = null
        try {
            mAnyTrustManager = AnyTrustManager()
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, arrayOf(mAnyTrustManager), SecureRandom())
            sslFactory = sc.socketFactory
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sslFactory
    }

    public fun <T> createService(clazz: Class<T>): T {
        return mRetrofit!!.create(clazz)!!
    }


    //实现X509TrustManager接口
    class AnyTrustManager : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkClientTrusted(
            chain: Array<X509Certificate>,
            authType: String
        ) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(
            chain: Array<X509Certificate>,
            authType: String
        ) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate?> {
            return arrayOfNulls(0)
        }
    }

    //实现HostnameVerifier接口
    private class TrustAllHostnameVerifier : HostnameVerifier {
        override fun verify(
            hostname: String,
            session: SSLSession
        ): Boolean {
            return true
        }
    }

}