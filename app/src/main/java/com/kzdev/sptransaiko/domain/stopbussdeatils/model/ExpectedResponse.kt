package com.kzdev.sptransaiko.domain.stopbussdeatils.model

data class ExpectedResponse(
    val cp: Int,
    val l: List<ExpectedLinesResponse>,
)