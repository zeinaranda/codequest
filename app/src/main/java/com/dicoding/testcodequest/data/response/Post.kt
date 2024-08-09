package com.dicoding.testcodequest.data.response

import java.util.*

data class Post(
    val postId: Int? = null,
    val content: String,
    val userId: Int,
    val createdAt: Date,
    val updatedAt: Date,
    val nama: String? = null, // Tambahan atribut untuk menampilkan nama
    val profileAvatar: String? = null // Tambahan atribut untuk menampilkan profileAvatar
)

data class Comment(
    val commentId: Int? = null,
    val content: String? = null,
    val userId: Int? =null,
    val postId: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val nama: String? = null, // Tambahan atribut untuk menampilkan nama
    val profileAvatar: String? = null // Tambahan atribut untuk menampilkan profileAvatar
)
