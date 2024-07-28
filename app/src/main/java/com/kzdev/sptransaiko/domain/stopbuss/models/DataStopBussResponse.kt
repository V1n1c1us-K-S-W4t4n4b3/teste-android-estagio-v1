package com.kzdev.sptransaiko.domain.stopbuss.models

data class DataStopBussResponse(
    val cp: Int,
    val np: String,
    val ed: String,
    val py: Double,
    val px: Double,
)