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
        value.point?.let { editor.putInt(POINT, it) }
        editor.putInt(KOIN, value.koin)
        editor.apply()
    }

//    fun getUser(): User {
//        return User(
//            userId = preferences.getInt(ID, 0),
//            nama = preferences.getString(NAMA, "") ?: "",
//            nim = preferences.getString(NIM, "") ?: "",
//            token = preferences.getString(TOKEN, "") ?: "",
//            point = preferences.getInt(POINT, 0),
//            koin = preferences.getInt(KOIN, 0),
//
//        )
//    }

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

    fun setPoints(points: Int) {
        val editor = preferences.edit()
        editor.putInt(POINT, points)
        editor.apply()
    }

    fun getPoints(): Int {
        return preferences.getInt(POINT, 0)
    }

    fun setCoins(coins: Int) {
        val editor = preferences.edit()
        editor.putInt(KOIN, coins)
        editor.apply()
    }

    fun getCoins(): Int {
        return preferences.getInt(KOIN, 0)
    }
    fun getNama(): String? {
        return preferences.getString(NAMA, "")
    }
    fun getNim(): String? {
        return preferences.getString(NIM, "")
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
        private const val POINT = "point"
        private const val KOIN = "koin"
    }
}