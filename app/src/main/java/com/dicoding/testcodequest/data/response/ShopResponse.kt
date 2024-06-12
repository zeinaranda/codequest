package com.dicoding.testcodequest.data.response

data class ShopResponse(
    val error: Int,
    val message: String,
    val ownedAvatars: List<Int>? = null
)

