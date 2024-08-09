package com.dicoding.testcodequest.data.response

import com.google.gson.annotations.SerializedName

data class Avatar(

    @field:SerializedName("avatarId")
    val avatarId: Int,

    @field:SerializedName("namaAvatar")
    val namaAvatar: String? = null,

    @field:SerializedName("priceAvatar")
    val priceAvatar: Int? = null,

    @field:SerializedName("imageAvatar")
    val imageAvatar: String? = null,

    @field:SerializedName("obtainMethod")
    val obtainMethod: String? = null
)