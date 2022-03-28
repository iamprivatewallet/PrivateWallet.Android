package com.fanrong.frwallet.tools

import okhttp3.Interceptor
import okhttp3.Response


const val HEADER_BASE_URL = "header-base-url"

class BaseUrlChangeInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newBuilder = request.newBuilder()
        if (request.header(HEADER_BASE_URL) != null) {
            newBuilder.url(request.header(HEADER_BASE_URL)!!)
            newBuilder.removeHeader(HEADER_BASE_URL)
        }

        val request1 = newBuilder.build()
        return chain.proceed(request1)

        // 自定义  404 response
//        try {
//        } catch (e: Exception) {
//            return Response.Builder().protocol(Protocol.HTTP_1_1).request(request1).code(200).body("{}".toResponseBody()).message("ok").build()
//        }

    }
}