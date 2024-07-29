package com.kzdev.sptransaiko.domain.monitoringlines.network

import com.kzdev.sptransaiko.domain.monitoringlines.model.MonitoringBussResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MonitoringBussService {

    @GET("Posicao/Linha")
    fun getMonitoringBuss(
        @Query("token") token: String,
        @Query("codigoLinha") codigoLinha: Int,
    ): Call<MonitoringBussResponse>

}