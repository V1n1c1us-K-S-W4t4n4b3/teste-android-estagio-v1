package com.kzdev.sptransaiko.domain.monitoringlines.model

data class MonitoringBussResponse(
    val hr: String,
    val vs: List<MonitoringBussDataResponse>,
)
