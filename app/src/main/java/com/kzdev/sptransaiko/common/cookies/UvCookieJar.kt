package com.kzdev.sptransaiko.common.cookies

import android.util.Log
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

internal class UvCookieJar : CookieJar {

    private val cookies = mutableListOf<Cookie>()

    override fun saveFromResponse(url: HttpUrl, cookieList: List<Cookie>) {
        cookies.clear()
        cookies.addAll(cookieList)
        Log.d("CookieJar", "Loading cookies for $url: $cookies")

    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> = cookies
}