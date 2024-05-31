package com.dicoding.testcodequest.ui

import androidx.lifecycle.ViewModel
import com.dicoding.testcodequest.data.UserRepository

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getRegist(name: String, nim: String, password: String) =
        userRepository.userRegist(name, nim, password)
}