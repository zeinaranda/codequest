package com.dicoding.testcodequest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.UserRepository
import com.dicoding.testcodequest.data.preference.AuthPreference
import com.dicoding.testcodequest.data.response.Avatar
import com.dicoding.testcodequest.data.response.OwnedAvatarsResponse
import com.dicoding.testcodequest.data.retrofit.ApiConfig
import com.dicoding.testcodequest.data.retrofit.ApiService
import com.dicoding.testcodequest.databinding.ActivityProfileBinding
import com.dicoding.testcodequest.ui.Adapter.OwnedAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var avatarAdapter: OwnedAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var preference: AuthPreference
    private lateinit var tvNama: TextView
    private lateinit var tvNim: TextView
    private var userId: Int? = null
    private var nama: String? = null
    private var nim: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tvNama = findViewById(R.id.tv_nama)
        tvNim = findViewById(R.id.tv_nim)
        recyclerView = findViewById(R.id.rv_owned)
        recyclerView.layoutManager = GridLayoutManager(this, 4)

        preference = AuthPreference(this)
        userId = preference.getId()
        nama = preference.getNama()
        nim = preference.getNim()
        Log.d("QuestionActivity", "User ID from Preference: $userId, $nama, $nim") // Cek userId dari preference

        tvNama.text = nama
        tvNim.text = nim
//        val userId = 1 // Ganti dengan ID pengguna yang sesuai
        userId?.let { fetchAvatars(it) }
    }

    private fun fetchAvatars(userId: Int) {
        val apiService = ApiConfig.getApiService()

        apiService.getOwnedAvatars(userId).enqueue(object : Callback<OwnedAvatarsResponse> {
            override fun onResponse(call: Call<OwnedAvatarsResponse>, response: Response<OwnedAvatarsResponse>) {
                if (response.isSuccessful) {
                    val ownedAvatars = response.body()?.ownedAvatars ?: emptyList()
                    apiService.getAvatar().enqueue(object : Callback<List<Avatar>> {
                        override fun onResponse(call: Call<List<Avatar>>, response: Response<List<Avatar>>) {
                            if (response.isSuccessful) {
                                val avatars = response.body() ?: emptyList()
                                avatarAdapter = OwnedAdapter(avatars, ownedAvatars)
                                recyclerView.adapter = avatarAdapter
                            } else {
                                Log.e("ProfileActivity", "Failed to get avatars")
                            }
                        }

                        override fun onFailure(call: Call<List<Avatar>>, t: Throwable) {
                            Log.e("ProfileActivity", "Failed to get avatars", t)
                        }
                    })
                } else {
                    Log.e("ProfileActivity", "Failed to get owned avatars")
                }
            }

            override fun onFailure(call: Call<OwnedAvatarsResponse>, t: Throwable) {
                Log.e("ProfileActivity", "Failed to get owned avatars", t)
            }
        })
    }
}