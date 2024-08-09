package com.dicoding.testcodequest.data

import com.dicoding.testcodequest.data.response.Storytelling
import com.dicoding.testcodequest.data.retrofit.ApiConfig
import com.dicoding.testcodequest.data.retrofit.ApiService

class StorytellingRepository {

    fun getStorytelling(codeScene: String, onSuccess: (List<Storytelling>) -> Unit, onError: () -> Unit) {
        ApiConfig.getApiService().getStorytelling(codeScene).enqueue(object : retrofit2.Callback<List<Storytelling>> {
            override fun onResponse(call: retrofit2.Call<List<Storytelling>>, response: retrofit2.Response<List<Storytelling>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it)
                    } ?: onError()
                } else {
                    onError()
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Storytelling>>, t: Throwable) {
                onError()
            }
        })
    }
}