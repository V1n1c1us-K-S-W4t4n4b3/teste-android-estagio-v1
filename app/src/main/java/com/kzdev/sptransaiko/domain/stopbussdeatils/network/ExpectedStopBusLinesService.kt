package com.kzdev.sptransaiko.domain.stopbussdeatils.network

import com.kzdev.sptransaiko.domain.stopbussdeatils.model.DataExpectedTimeStopBussResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ExpectedStopBusLinesService {

    @GET("Previsao/Parada")
    fun getExpectedStopBussLines(
        @Query("token") token: String,
        @Query("codigoParada") codigoParada: Int,
    ): Call<DataExpectedTimeStopBussResponse>
}