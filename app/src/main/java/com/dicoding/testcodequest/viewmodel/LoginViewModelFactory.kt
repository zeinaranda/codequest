package com.dicoding.testcodequest.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.testcodequest.data.Injection
import com.dicoding.testcodequest.data.UserRepository

class LoginViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: LoginViewModelFactory? = null
        fun getInstance(context: Context): LoginViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: LoginViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}