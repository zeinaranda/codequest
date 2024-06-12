package com.dicoding.testcodequest.data.response

data class LoginResponse(
    val token: String,
    val nim: String,
    val nama: String,
    val userId: Int,
    var point: Int,
    var koin: Int,
    val ownedAvatars: List<Int>

)