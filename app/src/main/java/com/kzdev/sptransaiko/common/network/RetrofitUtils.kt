package com.kzdev.sptransaiko.common.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.util.concurrent.TimeUnit

interface RetrofitUtils {

    companion object {

        private val cookieJar = UvCookieJar()

        val retrofit: Retrofit by lazy {
            val client = OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(CookieInterceptor(cookieJar))
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()

            val gson = GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
                .create()

            Retrofit.Builder()
                .baseUrl("https://api.olhovivo.sptrans.com.br/v2.1/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
    }
}