package com.dicoding.testcodequest.data.retrofit
import com.dicoding.testcodequest.data.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("user/login")
    suspend fun loginUser(
        @Field("nim") nim: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("user/register")
    suspend fun registerUser(
        @Field("nama") name: String,
        @Field("nim") email: String,
        @Field("password") password: String
    ): AuthResponse

    @GET("question/{boss_id}")
    fun getQuestions(@Path("boss_id") bossId: Int): Call<List<Question>>

    @POST("bossdone")
    fun updateBossDone(@Body bossDone: BossDone): Call<Void>


    @GET("avatar")
        fun getAvatar(): Call<AvatarResponse>

    @GET("avatar/method/{obtainMethod}")
    fun getAvatarbyMethod(@Path("obtainMethod") obtainMethod: String): Call<List<Avatar>>
}
