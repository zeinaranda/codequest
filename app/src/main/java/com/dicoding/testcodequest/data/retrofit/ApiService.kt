package com.dicoding.testcodequest.data.retrofit
import com.dicoding.testcodequest.data.response.*
import okhttp3.Response
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

    @PUT("/user/{id}")
    fun updateUserPoints(@Path("id") id: Int, @Body user: User): Call<Void>

    @GET("question/{boss_id}")
    fun getQuestions(@Path("boss_id") bossId: Int): Call<List<Question>>

    @POST("bossdone")
    fun updateBossDone(@Body bossDone: BossDone): Call<Void>

    @GET("user/{userId}")
    fun getOwnedAvatars(@Path("userId") userId: Int): Call<OwnedAvatarsResponse>

    @GET("avatar/avatars")
        fun getAvatar(): Call<List<Avatar>>

    @FormUrlEncoded
    @POST("user/addOwnedAvatar")
    fun addOwnedAvatar(
        @Field("userId") userId: Int,
        @Field("avatarId") avatarId: Int
    ): Call<ShopResponse>

//    @GET("users/{userId}")
//    fun getUser(@Path("userId") userId: Int): Call<User>


    @GET("avatar/method/{obtainMethod}")
    fun getAvatarbyMethod(@Path("obtainMethod") obtainMethod: String): Call<List<Avatar>>
}
