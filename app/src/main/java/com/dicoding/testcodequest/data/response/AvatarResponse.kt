package com.dicoding.testcodequest.data.response


import com.google.gson.annotations.SerializedName

class AvatarResponse(

    @field:SerializedName("data")
    val item: List<Avatar>,

    @field:SerializedName("error")
    val error: Int,

    @field:SerializedName("message")
    val message: String
)


