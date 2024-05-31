package com.dicoding.testcodequest.data.response

import com.google.gson.annotations.SerializedName

data class User(

    @field:SerializedName("nama")
    var nama: String? = null,

    @field:SerializedName("nim")
    var nim: String? = null,

    @field:SerializedName("userId")
    var userId: Int? = null,

    @field:SerializedName("token")
    var token: String? = null,

)
