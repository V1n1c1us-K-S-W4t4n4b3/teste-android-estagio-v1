package com.kzdev.sptransaiko.domain.monitoringlines.repository

import com.kzdev.sptransaiko.domain.monitoringlines.network.MonitoringBussService

class MonitoringBussRepository(private val service: MonitoringBussService) {
    fun getMonitoringBuss(token: String, codigoLinha: Int) =
        service.getMonitoringBuss(token, codigoLinha)
}