package com.dicoding.testcodequest.data.retrofit
import com.dicoding.testcodequest.data.response.Avatar
import com.dicoding.testcodequest.data.response.AvatarResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

interface ApiService {
    @GET("avatar")
        fun getAvatar(): Call<AvatarResponse>

    @GET("avatar/method/{obtainMethod}")
    fun getAvatarbyMethod(@Path("obtainMethod") obtainMethod: String): Call<List<Avatar>>
}
