package com.kzdev.sptransaiko.domain.stopbussdeatils.repository

import com.kzdev.sptransaiko.domain.stopbussdeatils.network.ExpectedStopBusLinesService

class ExpectedStopBussLinesRepository(private val service: ExpectedStopBusLinesService) {
    fun getExpectedStopBussLines(token: String, codigoParada: Int) =
        service.getExpectedStopBussLines(token, codigoParada)
}