package com.dicoding.testcodequest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ShopViewModelFactory (): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShopViewModel::class.java)){
            return ShopViewModel() as T
        }
        throw IllegalArgumentException("Unknown VM Class: " + modelClass.name)
    }
}