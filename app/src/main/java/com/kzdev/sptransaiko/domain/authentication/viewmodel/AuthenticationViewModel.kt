package com.kzdev.sptransaiko.domain.authentication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kzdev.sptransaiko.common.network.RetrofitUtils
import com.kzdev.sptransaiko.domain.authentication.network.AuthenticationService
import com.kzdev.sptransaiko.domain.authentication.repository.AuthenticationRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class AuthenticationViewModel(private val repository: AuthenticationRepository) : ViewModel() {

    val authentication = MutableLiveData<Boolean>()
    val errorException = MutableLiveData<Boolean>()

    fun postAuthentication(token: String) {
        val request = this.repository.postAuthentication(token)
        request.enqueue(object : Callback<Boolean> {
            override fun onResponse(
                call: Call<Boolean>, response: Response<Boolean>,
            ) {
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() == true) {
                    authentication.postValue(true)
                    Log.d("codigo ok", "${response.code()}")

                } else {
                    errorException.postValue(true)
                    Log.d("erro2", "${response.code()}")
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                errorException.postValue(true)
                Log.d("erro2", "n esperado")
            }
        })
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val service = RetrofitUtils.retrofit.create(AuthenticationService::class.java)
                val repository = AuthenticationRepository(service)
                AuthenticationViewModel(repository)
            }
        }
    }
}