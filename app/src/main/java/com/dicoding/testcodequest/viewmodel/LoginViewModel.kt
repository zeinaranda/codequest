package com.dicoding.testcodequest.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.testcodequest.data.UserRepository
import com.dicoding.testcodequest.data.response.User

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getLogin(nim: String, password: String) = userRepository.userLogin(nim, password)
}