package com.dicoding.testcodequest.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.preference.AuthPreference
import com.dicoding.testcodequest.data.response.Avatar
import com.dicoding.testcodequest.data.response.OwnedAvatarsResponse
import com.dicoding.testcodequest.data.retrofit.ApiConfig
import com.dicoding.testcodequest.ui.Adapter.DetailAdapter
import com.dicoding.testcodequest.ui.Adapter.FriendAdapter
import com.dicoding.testcodequest.ui.Adapter.OwnedAdapter
import com.dicoding.testcodequest.ui.StageSatu.StageSatuActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserActivity : AppCompatActivity(){

    private lateinit var avatarAdapter: DetailAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvNama: TextView
    private lateinit var tvNim: TextView
    private lateinit var userPoint: TextView
    private lateinit var userCoin: TextView
    private lateinit var btn_logout: ImageView
    private lateinit var ivProfile: ImageView
    private var friendUserId: Int? = null
    private var nama: String? = null
    private var nim: String? = null
    private var koin: Int? = null
    private var point: Int? = null
    private var profileAvatar: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        // Mengambil data dari Intent
        friendUserId = intent.getIntExtra("friendUserId", 0)
        koin = intent.getIntExtra("koin", 0)
        point = intent.getIntExtra("point", 0)
        nama = intent.getStringExtra("nama")
        nim = intent.getStringExtra("nim")
        profileAvatar = intent.getStringExtra("profileAvatar")
        Log.d("CEK" ,"${friendUserId}")


        ivProfile = findViewById(R.id.ivProfile)
        tvNama = findViewById(R.id.tv_nama)
        tvNim = findViewById(R.id.tv_nim)
        userPoint = findViewById(R.id.userPoint)
        userCoin = findViewById(R.id.userCoin)
        recyclerView = findViewById(R.id.rv_owned)
        recyclerView.layoutManager = GridLayoutManager(this, 4)

        tvNama.text = nama
        tvNim.text = nim
        userPoint.text = point.toString()
        userCoin.text = koin.toString()

        profileAvatar?.let {
            displayProfileAvatar(it)
        }

        fetchAvatars(friendUserId ?: 0)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.itemIconTintList = null
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu1 -> {
                    val i = Intent(this, LeaderboardActivity::class.java)
                    startActivity(i)
                    finish()
                    true
                }
                R.id.menu2 -> {
                    val i = Intent(this, FriendActivity::class.java)
                    startActivity(i)
                    finish()
                    true
                }
                R.id.menu3 -> {
                    val i = Intent(this, StageSatuActivity::class.java)
                    startActivity(i)
                    finish()
                    true
                }
                R.id.menu4 -> {
                    val i = Intent(this, ShopActivity::class.java)
                    startActivity(i)
                    finish()
                    true
                }
                R.id.menu5 -> {
                    val i = Intent(this, ProfileActivity::class.java)
                    startActivity(i)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun displayProfileAvatar(url: String) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.placeholder_image) // Placeholder image while loading
            .error(R.drawable.placeholder_image) // Placeholder image on error
            .apply(RequestOptions.circleCropTransform()) // Optional: Use circle crop transformation
            .into(ivProfile)
    }

    private fun fetchAvatars(userId: Int) {
        val apiService = ApiConfig.getApiService()

        apiService.getOwnedAvatars(friendUserId ?: 0).enqueue(object : Callback<OwnedAvatarsResponse> {
            override fun onResponse(call: Call<OwnedAvatarsResponse>, response: Response<OwnedAvatarsResponse>) {
                if (response.isSuccessful) {
                    val ownedAvatars = response.body()?.ownedAvatars ?: emptyList()
                    apiService.getAvatar().enqueue(object : Callback<List<Avatar>> {
                        override fun onResponse(call: Call<List<Avatar>>, response: Response<List<Avatar>>) {
                            if (response.isSuccessful) {
                                val avatars = response.body() ?: emptyList()
                                avatarAdapter = DetailAdapter(avatars, ownedAvatars)
                                recyclerView.adapter = avatarAdapter
                            } else {
                                Log.e("DetailUserActivity", "Failed to get avatars")
                            }
                        }

                        override fun onFailure(call: Call<List<Avatar>>, t: Throwable) {
                            Log.e("DetailUserActivity", "Failed to get avatars", t)
                        }
                    })
                } else {
                    Log.e("DetailUserActivity", "Failed to get owned avatars")
                }
            }

            override fun onFailure(call: Call<OwnedAvatarsResponse>, t: Throwable) {
                Log.e("DetailUserActivity", "Failed to get owned avatars", t)
            }
        })
    }

}
