package com.dicoding.testcodequest.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.testcodequest.data.response.AuthResponse
import com.dicoding.testcodequest.data.response.LoginResponse
import com.dicoding.testcodequest.data.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception

class UserRepository (
    private val apiService: ApiService
) {


    fun userLogin(
        nim: String,
        password: String
    ): LiveData<Result<LoginResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.loginUser(nim, password)
                emit(Result.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Failure(e.message.toString()))
            }
        }

    fun userRegist(
        name: String,
        nim: String,
        password: String
    ): LiveData<Result<AuthResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.registerUser(name, nim, password)
                emit(Result.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Failure(e.message.toString()))
            }
        }
}




