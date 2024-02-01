package com.hassan.currencyconverter.di

import okhttp3.Interceptor
import okhttp3.Response

class HttpInterceptor (private val appId: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Token $appId")
            .build()

        return chain.proceed(newRequest)
    }
}