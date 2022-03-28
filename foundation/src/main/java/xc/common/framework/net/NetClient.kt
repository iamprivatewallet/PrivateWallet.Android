package xc.common.framework.net

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import xc.common.tool.utils.SWLog
import xc.common.tool.utils.checkNotEmpty
import java.util.concurrent.TimeUnit


class NetClient {


    companion object {

        private var apiUrl: String = "http://www.baidu.com"
        private var netClient: NetClient? = null
        private var interceptors: List<Interceptor>? = null
        private val headers: HashMap<String, String> = hashMapOf()

        fun initUrls(api: String) {
            apiUrl = api
        }

        fun addHeader(key: String, value: String) {
            headers.put(key, value)
        }

        fun initApiClient(interceptor1: List<Interceptor>) {
            interceptors = interceptor1
        }

        fun build() {
            netClient = NetClient()
        }

        fun <T> getApi(clazz: Class<T>): T {
            return netClient!!.retrofit.create(clazz)
        }

    }


    private val retrofit: Retrofit

    private constructor() {
        var client = OkHttpClient.Builder()
        client.writeTimeout(15 * 1000, TimeUnit.MILLISECONDS)
        client.readTimeout(15 * 1000, TimeUnit.MILLISECONDS)
        client.connectTimeout(15 * 1000, TimeUnit.MILLISECONDS)
        var log = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger() {
            SWLog.i("拦截器-->>>>>"+it)
        })
        log.level = HttpLoggingInterceptor.Level.BODY
        client.addNetworkInterceptor(log)

        if (interceptors.checkNotEmpty()) {
            for (interceptor in interceptors!!) {
                client.addInterceptor(interceptor)
            }
        }

        retrofit = Retrofit.Builder()
            .client(client.build())
            .baseUrl(apiUrl)
            .addConverterFactory(MyGsonConvertor.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }


}
