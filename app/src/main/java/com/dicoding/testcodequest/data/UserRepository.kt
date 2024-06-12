package com.dicoding.testcodequest.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.dicoding.testcodequest.data.response.AuthResponse
import com.dicoding.testcodequest.data.response.Avatar
import com.dicoding.testcodequest.data.response.LoginResponse
import com.dicoding.testcodequest.data.response.User
import com.dicoding.testcodequest.data.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

//    fun getUser(userId: Int): LiveData<User> {
//        val userData = MutableLiveData<User>()
//        apiService.getUser(userId).enqueue(object : Callback<User> {
//            override fun onResponse(call: Call<User>, response: Response<User>) {
//                if (response.isSuccessful) {
//                    userData.value = response.body()
//                }
//            }
//
//            override fun onFailure(call: Call<User>, t: Throwable) {
//                // Handle failure
//            }
//        })
//        return userData
//    }

//    fun getAvatars(): LiveData<List<Avatar>> {
//        val avatarsData = MutableLiveData<List<Avatar>>()
//        apiService.getAvatar().enqueue(object : Callback<List<Avatar>> {
//            override fun onResponse(call: Call<List<Avatar>>, response: Response<List<Avatar>>) {
//                if (response.isSuccessful) {
//                    avatarsData.value = response.body()
//                }
//            }
//
//            override fun onFailure(call: Call<List<Avatar>>, t: Throwable) {
//                // Handle failure
//            }
//        })
//        return avatarsData
//    }
}





