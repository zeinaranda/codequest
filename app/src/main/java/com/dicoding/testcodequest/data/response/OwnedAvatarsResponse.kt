package com.dicoding.testcodequest.data.response

import com.google.gson.annotations.SerializedName

// OwnedAvatarsResponse.kt
data class OwnedAvatarsResponse(
    @field:SerializedName("ownedAvatars")
    val ownedAvatars: List<Int>
)
