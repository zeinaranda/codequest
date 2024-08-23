package com.dicoding.testcodequest.data.preference

import android.content.Context
import com.dicoding.testcodequest.data.response.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AuthPreference(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: User) {
        val editor = preferences.edit()
        editor.putString(NAMA, value.nama)
        editor.putString(NIM, value.nim)
        editor.putInt(ID, value.userId ?: 0)
        editor.putString(TOKEN, value.token)
        value.point?.let { editor.putInt(POINT, it) }
        value.koin?.let { editor.putInt(KOIN, it) }
        editor.putString(PROFILE_AVATAR_URL, value.profileAvatar)
        value.firstLogin?.let { editor.putBoolean(FIRST_LOGIN, it) }
        editor.apply()
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

    // Menambahkan metode untuk menyimpan dan mengambil URL gambar profil
    fun saveProfileAvatarUrl(url: String) {
        val editor = preferences.edit()
        editor.putString(PROFILE_AVATAR_URL, url)
        editor.apply()
    }

    fun getProfileAvatarUrl(): String? {
        return preferences.getString(PROFILE_AVATAR_URL, "")
    }

    fun getNim(): String? {
        return preferences.getString(NIM, "")
    }

    // Metode untuk menyimpan dan mengambil status firstLogin
    fun setFirstLogin(firstLogin: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(FIRST_LOGIN, firstLogin)
        editor.apply()
    }

    fun isFirstLogin(): Boolean {
        return preferences.getBoolean(FIRST_LOGIN, true)
    }

    fun logout() {
        val editor = preferences.edit()
        editor.clear().apply()
    }

    fun setOwnedAvatars(avatars: List<Int>) {
        val editor = preferences.edit()
        val json = Gson().toJson(avatars)
        editor.putString("owned_avatars", json)
        editor.apply()
    }

    fun getOwnedAvatars(): List<Int> {
        val json = preferences.getString("owned_avatars", "[]")
        val type = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(json, type)
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
        private const val PROFILE_AVATAR_URL = "profile_avatar_url"
        private const val FIRST_LOGIN = "first_login"
        private const val KEY_STAGE_ONE_COMPLETED = "stage1_completed"
        private const val KEY_STAGE_TWO_COMPLETED = "stage2_completed"
    }
}