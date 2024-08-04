package com.kzdev.sptransaiko.domain.authentication.network

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthenticationService {
    @POST("Login/Autenticar")
    fun postAuthentication(
        @Query("token") token: String,
    ): Call<Boolean>
}