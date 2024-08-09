package com.dicoding.testcodequest.data.response

import com.google.gson.annotations.SerializedName

data class Question(
    @field:SerializedName("content")
    val content: String,

    @field:SerializedName("choicesOne")
    val choicesOne: String,

    @field:SerializedName("choicesTwo")
    val choicesTwo: String,

    @field:SerializedName("choicesThree")
    val choicesThree: String,

    @field:SerializedName("choicesFour")
    val choicesFour: String,

    @field:SerializedName("correctAnswer")
    val correctAnswer: String,

    @field:SerializedName("knowledgeId")
    val knowledgeId: Int,

    @field:SerializedName("bossId")
    val bossId: Int
)
