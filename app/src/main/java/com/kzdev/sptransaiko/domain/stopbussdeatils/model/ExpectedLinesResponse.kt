package com.kzdev.sptransaiko.domain.stopbussdeatils.model

data class ExpectedLinesResponse(
    val sl: Int,
    val cl: Int,
    val lt0: String,
    val lt1: String,
    val vs: List<ExpectedBussPositionResponse>,
)