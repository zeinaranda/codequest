package com.dicoding.testcodequest.data.response

import com.google.gson.annotations.SerializedName

data class Avatar(

    @field:SerializedName("avatarId")
    val avatarId: Int,

    @field:SerializedName("namaAvatar")
    val namaAvatar: String,

    @field:SerializedName("priceAvatar")
    val priceAvatar: Int,

    @field:SerializedName("imageAvatar")
    val imageAvatar: String,

    @field:SerializedName("obtainMethod")
    val obtainMethod: String
)