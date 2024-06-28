package com.dicoding.testcodequest.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.testcodequest.data.Injection
import com.dicoding.testcodequest.data.UserRepository

class RegisterViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: RegisterViewModelFactory? = null
        fun getInstance(context: Context): RegisterViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: RegisterViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}