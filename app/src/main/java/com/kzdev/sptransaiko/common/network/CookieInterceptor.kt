package com.kzdev.sptransaiko.common.network

import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.Response

class CookieInterceptor(private val cookieJar: CookieJar) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val cookies = cookieJar.loadForRequest(originalRequest.url)
        val newRequestBuilder = originalRequest.newBuilder()
        if (cookies.isNotEmpty()) {
            val cookieHeader = cookies.joinToString("; ") { "${it.name}=${it.value}" }
            newRequestBuilder.addHeader("Cookie", cookieHeader)
        }

        val newRequest = newRequestBuilder.build()
        return chain.proceed(newRequest)
    }
}

