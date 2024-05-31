package com.dicoding.testcodequest.data.response

import com.google.gson.annotations.SerializedName

data class BossDone(
    @field:SerializedName("quizDone")
    val quizDone: Boolean,

    @field:SerializedName("point")
    val point: Int,

    @field:SerializedName("koin")
    val koin: Int,

    @field:SerializedName("bossId")
    val bossId: Int,

    @field:SerializedName("userId")
    val userId: Int


)
