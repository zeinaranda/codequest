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


    @PUT("user/updateFirstLoginStatus")
    fun updateFirstLoginStatus(
        @Body requestBody: UpdateFirstLoginRequest // Ganti dengan objek request yang sesuai
    ): Call<Void>


    @POST("/userstage/user/{userId}/stage/{stageId}/progress")
    fun updateUserStageProgress(
        @Path("userId") userId: Int,
        @Path("stageId") stageId: Int,
        @Body progressPoint: ProgressPoint
    ): Call<Void>

    @GET("/userstage/user/{userId}/stage/{stageId}/completion")
    fun checkStageCompletion(
        @Path("userId") userId: Int,
        @Path("stageId") stageId: Int
    ): Call<StageCompletionResponse>

    @PUT("/user/{id}")
    fun updateUserPoints(@Path("id") id: Int, @Body user: User): Call<Void>

    @PUT("user/{userId}/profile/image")
    fun updateUserProfileImage(
        @Path("userId") userId: Int,
        @Body requestBody: Map<String, String>
    ): Call<Void>

    @GET("question/{boss_id}")
    fun getQuestions(@Path("boss_id") bossId: Int): Call<List<Question>>

    @GET("question/knowledge/{knowledge_id}")
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

    @GET("friend/{userId}")
    fun getFriends(@Path("userId") userId: Int): Call<ArrayList<Friend>>

    @POST("/friend")
    fun addFriend(@Body friendRequest: FriendRequest): Call<Friend>

    @GET("forum/posts")
    fun getAllPosts(): Call<List<Post>>

    @GET("forum/posts/{userId}")
    fun getPostsByUserId(@Path("userId") userId: Int): Call<List<Post>>

    @DELETE("forum/posts/{postId}")
    fun deletePost(@Path("postId") postId: Int): Call<Void>

    @GET("forum/posts/{postId}/comments")
    fun getCommentsByPostId(@Path("postId") postId: Int): Call<List<Comment>>

    // Tambahkan fungsi untuk membuat posting baru
    @POST("forum/posts")
    fun createPost(@Body post: Post): Call<Post>

    // Tambahkan fungsi untuk membuat komentar baru
    @POST("forum/posts/{postId}/comments")
    fun createComment(@Path("postId") postId: Int, @Body comment: Comment): Call<Comment>

}
