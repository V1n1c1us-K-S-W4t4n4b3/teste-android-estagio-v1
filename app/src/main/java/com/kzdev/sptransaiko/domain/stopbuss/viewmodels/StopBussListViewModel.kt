package com.kzdev.sptransaiko.domain.stopbuss.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kzdev.sptransaiko.common.network.RetrofitUtils
import com.kzdev.sptransaiko.domain.stopbuss.models.DataStopBussResponse
import com.kzdev.sptransaiko.domain.stopbuss.network.StopBussListService
import com.kzdev.sptransaiko.domain.stopbuss.repository.StopBussListRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class StopBussListViewModel(private val repository: StopBussListRepository) : ViewModel() {

    val stopBussList = MutableLiveData<List<DataStopBussResponse>>()
    val errorException = MutableLiveData<Boolean>()

    fun getStopBuss(token: String, termosBusca: String) {
        val request = this.repository.getStopBuss(token, termosBusca)
        request.enqueue(object : Callback<List<DataStopBussResponse>> {
            override fun onResponse(
                call: Call<List<DataStopBussResponse>>,
                response: Response<List<DataStopBussResponse>>,
            ) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    stopBussList.postValue(response.body())
                } else {
                    errorException.postValue(true)
                    Log.d("erro1", "${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<DataStopBussResponse>>, t: Throwable) {
                errorException.postValue(true)
            }
        })
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val service =
                    RetrofitUtils.retrofit.create(StopBussListService::class.java)
                val repository = StopBussListRepository(service)
                StopBussListViewModel(repository)
            }
        }
    }
}