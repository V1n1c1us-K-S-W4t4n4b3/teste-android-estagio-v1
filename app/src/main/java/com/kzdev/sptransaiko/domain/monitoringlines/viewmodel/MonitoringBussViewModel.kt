package com.kzdev.sptransaiko.domain.monitoringlines.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kzdev.sptransaiko.common.network.RetrofitUtils
import com.kzdev.sptransaiko.domain.monitoringlines.model.MonitoringBussResponse
import com.kzdev.sptransaiko.domain.monitoringlines.network.MonitoringBussService
import com.kzdev.sptransaiko.domain.monitoringlines.repository.MonitoringBussRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class MonitoringBussViewModel(private val repository: MonitoringBussRepository) : ViewModel() {

    val list = MutableLiveData<MonitoringBussResponse>()
    val errorException = MutableLiveData<Boolean>()

    fun getMonitoringBuss(token: String, codigoLinha: Int) {
        val request = this.repository.getMonitoringBuss(token, codigoLinha)
        request.enqueue(object : Callback<MonitoringBussResponse> {
            override fun onResponse(
                call: Call<MonitoringBussResponse>,
                response: Response<MonitoringBussResponse>,
            ) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    list.postValue(response.body())
                } else {
                    errorException.postValue(true)
                    Log.d("erro1", "${response.code()}")
                }
            }

            override fun onFailure(call: Call<MonitoringBussResponse>, t: Throwable) {
                errorException.postValue(true)
            }
        })
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val service =
                    RetrofitUtils.retrofit.create(MonitoringBussService::class.java)
                val repository = MonitoringBussRepository(service)
                MonitoringBussViewModel(repository)
            }
        }
    }
}