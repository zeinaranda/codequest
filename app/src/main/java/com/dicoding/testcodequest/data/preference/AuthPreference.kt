package com.dicoding.testcodequest.data.preference

import android.content.Context
import com.dicoding.testcodequest.data.response.User

class AuthPreference(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: User) {
        val editor = preferences.edit()
        editor.putString(NAMA, value.nama)
        editor.putString(NIM, value.nim)
        editor.putInt(ID, value.userId ?: 0)
        editor.putString(TOKEN, value.token)
        editor.apply()
    }

    fun getUser(): User {
        return User(
            userId = preferences.getInt(ID, 0),
            nama = preferences.getString(NAMA, "") ?: "",
            nim = preferences.getString(NIM, "") ?: "",
            token = preferences.getString(TOKEN, "") ?: ""
        )
    }

    fun setStatusLogin(value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(LOGIN, value)
        editor.apply()
    }

    fun getStatusLogin(): Boolean {
        return preferences.getBoolean(LOGIN, false)
    }

    fun getToken(): String? {
        return preferences.getString(TOKEN, "")
    }

    fun getId(): Int? {
        return preferences.getInt(ID, 0)
    }

    fun logout() {
        val editor = preferences.edit()
        editor.clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "prefs_name"
        private const val ID = "id"
        private const val NAMA = "nama"
        private const val NIM = "nim"
        private const val TOKEN = "token"
        private const val LOGIN = "login"
    }
}
