package com.kzdev.sptransaiko.domain.stopbuss.network

import com.kzdev.sptransaiko.domain.stopbuss.models.DataStopBussResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface StopBussListService {

    @GET("Parada/Buscar")
    fun getStopBuss(
        @Query("token") token: String,
        @Query("termosBusca") termosBusca: String,
    ): Call<List<DataStopBussResponse>>
}