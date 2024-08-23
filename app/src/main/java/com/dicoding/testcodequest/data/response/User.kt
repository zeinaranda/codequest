package com.dicoding.testcodequest.data.response

import com.google.gson.annotations.SerializedName

data class User(

    val error: Int? = null,
    val message: String? = null,


    @field:SerializedName("nama")
    var nama: String? = null,

    @field:SerializedName("nim")
    var nim: String? = null,

    @field:SerializedName("userId")
    var userId: Int? = null,

    @field:SerializedName("token")
    var token: String? = null,

    @field:SerializedName("profileAvatar")
    var profileAvatar: String? = null,

    @field:SerializedName("point")
    var point: Int?=null,

    @field:SerializedName("koin")
    var koin: Int?=null,

    @field:SerializedName("ownedAvatars")
    val ownedAvatars: List<Int>? = null,

    val firstLogin: Boolean? = null,

    // Add fields for arrowRank and rankChange
    @field:SerializedName("rankArrow")
    var arrowRank: String? = null, // "up" or "down"

    @field:SerializedName("rankChange")
    var rankChange: String? = null
)
