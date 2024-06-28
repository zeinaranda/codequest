package com.dicoding.testcodequest.data.response

import com.google.gson.annotations.SerializedName

data class Storytelling(
    @field:SerializedName("text")
    val text: String,

    @field:SerializedName("stageId")
    val stageId: Int? = null,

    @field:SerializedName("avatarId")
    val avatarId: Int?=null,

    @field:SerializedName("codeScene")
    val codeScene: String,

    @SerializedName("namaAvatar")
    val namaAvatar: String?,
    @SerializedName("imageAvatar")
    val imageAvatar: String?,
    @SerializedName("bgImage")
    val bgImage: String?
)
