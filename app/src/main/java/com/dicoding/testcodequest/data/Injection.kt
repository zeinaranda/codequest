package com.dicoding.testcodequest.data

import android.content.Context
import com.dicoding.testcodequest.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository(apiService)
    }
}