package com.dicoding.testcodequest.data.retrofit
import com.dicoding.testcodequest.data.response.*
import retrofit2.Call
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

    @POST("/userstage/user/{userId}/stage/{stageId}/progress")
    fun updateUserStageProgress(
        @Path("userId") userId: Int,
        @Path("stageId") stageId: Int,
        @Body progressPoint: ProgressPoint
    ): Call<Void>

    @PUT("/user/{id}")
    fun updateUserPoints(@Path("id") id: Int, @Body user: User): Call<Void>

    @GET("question/{boss_id}")
    fun getQuestions(@Path("boss_id") bossId: Int): Call<List<Question>>

    @GET("question/{knowledge_id}")
    fun getQuestionsByKnowledge(@Path("knowledge_id") knowledgeId: Int): Call<List<Question>>

    @GET("storytelling/code/{codeScene}")
    fun getStorytelling(@Path("codeScene") codeScene: String): Call<List<Storytelling>>


    @POST("bossdone")
    fun updateBossDone(@Body bossDone: BossDone): Call<Void>

    @POST("knowledgedone")
    fun updateKnowledgeDone(@Body knowledgeDone: KnowledgeDone): Call<Void>

    @GET("user/top-users")
    fun getTopUsers(): Call<List<User>>

    @GET("user/{userId}")
    fun getOwnedAvatars(@Path("userId") userId: Int): Call<OwnedAvatarsResponse>

    @GET("avatar/avatars")
        fun getAvatar(): Call<List<Avatar>>

    @GET("user/{userId}")
    fun getUserById(
        @Path("userId") userId: Int
    ): Call<User>

//    @FormUrlEncoded
//    @PUT("user/{userId}")
//    fun updateUserKoin(
//        @Path("userId") userId: Int,
//        @Field("koin") koin: Int
//    ): Call<User>

    @PUT("/user/{id}")
    fun updateUserKoin(@Path("id") id: Int, @Body user: User): Call<Void>


    @FormUrlEncoded
    @POST("user/addOwnedAvatar/{userId}")
    fun addOwnedAvatar(
        @Path("userId") userId: Int,
        @Field("avatarId") avatarId: Int
    ): Call<BuyResponse>

//    @GET("users/{userId}")
//    fun getUser(@Path("userId") userId: Int): Call<User>


    @GET("avatar/method/{obtainMethod}")
    fun getAvatarbyMethod(@Path("obtainMethod") obtainMethod: String): Call<List<Avatar>>
}
