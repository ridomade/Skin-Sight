package com.dicoding.skinsight.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.skinsight.networking.api.ApiConfig
import com.dicoding.skinsight.networking.response.LoginDataAccount
import com.dicoding.skinsight.networking.response.RegisterDataAccount
import com.dicoding.skinsight.networking.response.ResponseLogin
import com.dicoding.skinsight.networking.response.ResponseRegister
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

class MainViewModel : ViewModel() {
    private val _isLoadingLogin = MutableLiveData<Boolean>()
    val isLoadingLogin: LiveData<Boolean> = _isLoadingLogin
    var isErrorLogin: Boolean = false

    private val _statusLogin = MutableLiveData<String>()
    val statusLogin: LiveData<String> = _statusLogin

    private val _userLogin = MutableLiveData<ResponseLogin>()
    val userLogin: LiveData<ResponseLogin> = _userLogin


    var isErrorRegist: Boolean = false
    private val _isLoadingRegister = MutableLiveData<Boolean>()
    val isLoadingRegister: LiveData<Boolean> = _isLoadingRegister
    private val _statusRegister = MutableLiveData<String>()
    val statusRegister: LiveData<String> = _statusRegister
    private val _messageRegister = MutableLiveData<String>()
    val messageRegister: LiveData<String> = _messageRegister

    fun getResponseLogin(loginDataAccount: LoginDataAccount) {
        _isLoadingLogin.value = true
        val api = ApiConfig.getApiService().loginUser(loginDataAccount)
        api.enqueue(object : retrofit2.Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                _isLoadingLogin.value = false
                val responseBody = response.body()
                if (response.isSuccessful) {
                    isErrorLogin = false
                    _userLogin.value = responseBody!!
                    _statusLogin.value = _userLogin.value!!.status
                } else {
                    isErrorLogin = true
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                isErrorLogin = true
                _isLoadingLogin.value = false
                _statusLogin.value = "Error message: " + t.message.toString()
            }
        })
    }

    fun getResponseRegister(registDataUser: RegisterDataAccount) {
        _isLoadingRegister.value = true
        val api = ApiConfig.getApiService().registerUser(registDataUser)
        api.enqueue(object : retrofit2.Callback<ResponseRegister> {
            override fun onResponse(call: Call<ResponseRegister>, response: Response<ResponseRegister>) {
                _isLoadingRegister.value = false
                if (response.isSuccessful) {
                    isErrorRegist = false
                    _statusRegister.value = response.body()?.status.toString()
                } else {
                    isErrorRegist = true
                }
            }

            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                isErrorRegist = true
                _isLoadingRegister.value = false
                _statusRegister.value = "Error message: " + t.message.toString()
            }
        })
    }
}