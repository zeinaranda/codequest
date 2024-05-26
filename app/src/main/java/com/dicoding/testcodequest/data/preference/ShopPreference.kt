package com.dicoding.testcodequest.data.preference

import android.content.Context

class ShopPreference (context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getMethod(): String? {
        return preferences.getString(OBTAINMETHOD, "shop")
    }
    companion object {
        private const val PREFS_NAME = "prefs_name"
        private const val OBTAINMETHOD = "obtainMethod"

    }
}