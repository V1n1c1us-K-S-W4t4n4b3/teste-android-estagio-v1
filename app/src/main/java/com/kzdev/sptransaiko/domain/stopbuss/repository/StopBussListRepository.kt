package com.kzdev.sptransaiko.domain.stopbuss.repository

import com.kzdev.sptransaiko.domain.stopbuss.network.StopBussListService

class StopBussListRepository(private val buildingListService: StopBussListService) {
    fun getStopBuss(token: String, termosBusca: String) =
        buildingListService.getStopBuss(token, termosBusca)
}