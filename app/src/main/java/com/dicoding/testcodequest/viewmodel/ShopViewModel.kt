package com.dicoding.testcodequest.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.testcodequest.data.response.Avatar
import com.dicoding.testcodequest.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.dicoding.testcodequest.data.response.AvatarResponse

class ShopViewModel() : ViewModel() {

    private val _error = MutableLiveData<Int>()
    val error: LiveData<Int> = _error

    private val _load = MutableLiveData<Boolean>()
    val load: LiveData<Boolean> = _load

    private val _list = MutableLiveData<List<Avatar>>()
    val list: LiveData<List<Avatar>> = _list

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setStories(obtainMethod: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAvatarbyMethod(obtainMethod)
        client.enqueue(object : Callback<List<Avatar>> {
            override fun onResponse(call: Call<List<Avatar>>, response: Response<List<Avatar>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        _list.value = it
                    }
                } else {
                    _error.value = response.code()
                }
            }

            override fun onFailure(call: Call<List<Avatar>>, t: Throwable) {
                _isLoading.value = false
                _error.value = -1
            }
        })
    }
    fun getAvatar(): LiveData<List<Avatar>> {
        return list
    }

}