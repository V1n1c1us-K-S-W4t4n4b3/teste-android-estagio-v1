package com.kzdev.sptransaiko.domain.stopbussdeatils.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kzdev.sptransaiko.common.network.RetrofitUtils
import com.kzdev.sptransaiko.domain.stopbussdeatils.model.DataExpectedTimeStopBussResponse
import com.kzdev.sptransaiko.domain.stopbussdeatils.network.ExpectedStopBusLinesService
import com.kzdev.sptransaiko.domain.stopbussdeatils.repository.ExpectedStopBussLinesRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class ExpectedStopBussLinesViewModel(private val repository: ExpectedStopBussLinesRepository) :
    ViewModel() {

    val list = MutableLiveData<DataExpectedTimeStopBussResponse>()
    val errorException = MutableLiveData<Boolean>()

    fun getExpectedStopBussLines(token: String, codigoParada: Int) {
        val request = this.repository.getExpectedStopBussLines(token, codigoParada)
        request.enqueue(object : Callback<DataExpectedTimeStopBussResponse> {
            override fun onResponse(
                call: Call<DataExpectedTimeStopBussResponse>,
                response: Response<DataExpectedTimeStopBussResponse>,
            ) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    list.postValue(response.body())
                } else {
                    errorException.postValue(true)
                    Log.d("erro1", "${response.code()}")
                }
            }

            override fun onFailure(call: Call<DataExpectedTimeStopBussResponse>, t: Throwable) {
                errorException.postValue(true)
            }
        })
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val service =
                    RetrofitUtils.retrofit.create(ExpectedStopBusLinesService::class.java)
                val repository = ExpectedStopBussLinesRepository(service)
                ExpectedStopBussLinesViewModel(repository)
            }
        }
    }
}