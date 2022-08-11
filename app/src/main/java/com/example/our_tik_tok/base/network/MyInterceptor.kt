package com.example.our_tik_tok.base.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/15
 */
class MyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("token", "")
            .build()
        return chain.proceed(newRequest)
    }
}