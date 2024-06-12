package com.dicoding.testcodequest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.testcodequest.data.UserRepository
import com.dicoding.testcodequest.data.response.Avatar
import com.dicoding.testcodequest.data.response.User

class UserViewModel(private val repository: UserRepository) : ViewModel() {

//    fun getUser(userId: Int): LiveData<User> {
//        return repository.getUser(userId)
//    }
//
//    fun getAvatars(): LiveData<List<Avatar>> {
//        return repository.getAvatars()
//    }
}