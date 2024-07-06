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
import com.dicoding.testcodequest.data.response.User
import com.dicoding.testcodequest.data.retrofit.ApiConfig
import com.dicoding.testcodequest.ui.Adapter.OwnedAdapter
import com.dicoding.testcodequest.ui.StageSatu.StageSatuActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity(), OwnedAdapter.OnItemClickListener {
    private var userPoint: Int? = null
    private var userKoin: Int? = null
    private lateinit var avatarAdapter: OwnedAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var preference: AuthPreference
    private lateinit var tvNama: TextView
    private lateinit var tvNim: TextView
    private lateinit var btn_logout: ImageView
    private lateinit var ivProfile: ImageView
    private var userId: Int? = null
    private var nama: String? = null
    private var nim: String? = null
    private var profileAvatar: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        btn_logout = findViewById(R.id.btn_logout)
        btn_logout.setOnClickListener {
            getLogout()
            this.finish()
        }

        ivProfile = findViewById(R.id.ivProfile)
        tvNama = findViewById(R.id.tv_nama)
        tvNim = findViewById(R.id.tv_nim)
        recyclerView = findViewById(R.id.rv_owned)
        recyclerView.layoutManager = GridLayoutManager(this, 4)

        preference = AuthPreference(this)

        userPoint = preference.getPoints()
        userKoin = preference.getCoins()

        val pointsText = findViewById<TextView>(R.id.userPoint)
        pointsText.text = userPoint.toString()

        val coinsText = findViewById<TextView>(R.id.userCoin)
        coinsText.text = userKoin.toString()

        userId = preference.getId()
        nama = preference.getNama()
        nim = preference.getNim()
        profileAvatar = preference.getProfileAvatarUrl()

        Log.d("ProfileActivity", "User ID from Preference: $userId, $nama, $nim, $profileAvatar")

        tvNama.text = nama
        tvNim.text = nim

        profileAvatar?.let {
            displayProfileAvatar(it)
        }

        userId?.let { fetchAvatars(it) }

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
                    val i = Intent(this, StageSatuActivity::class.java)
                    startActivity(i)
                    finish()
                    true
                }
                R.id.menu3 -> {
                    val i = Intent(this, ShopActivity::class.java)
                    startActivity(i)
                    finish()
                    true
                }
                R.id.menu4 -> {
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

        apiService.getOwnedAvatars(userId).enqueue(object : Callback<OwnedAvatarsResponse> {
            override fun onResponse(call: Call<OwnedAvatarsResponse>, response: Response<OwnedAvatarsResponse>) {
                if (response.isSuccessful) {
                    val ownedAvatars = response.body()?.ownedAvatars ?: emptyList()
                    apiService.getAvatar().enqueue(object : Callback<List<Avatar>> {
                        override fun onResponse(call: Call<List<Avatar>>, response: Response<List<Avatar>>) {
                            if (response.isSuccessful) {
                                val avatars = response.body() ?: emptyList()
                                avatarAdapter = OwnedAdapter(avatars, ownedAvatars, this@ProfileActivity)
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

    private fun getLogout() {
        preference.logout()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onItemClick(avatar: Avatar) {
        showConfirmationDialog(avatar)
    }

    private fun showConfirmationDialog(avatar: Avatar) {
        AlertDialog.Builder(this)
            .setTitle("Gunakan Avatar sebagai Profil?")
            .setMessage("Apakah Anda ingin menggunakan avatar ini sebagai gambar profil Anda?")
            .setPositiveButton("Ya") { _, _ ->
                // Update profile image dengan avatar yang dipilih
                userId?.let {
                    avatar.imageAvatar?.let { imageUri ->
                        sendUpdateProfileImageRequest(it, imageUri)
                    }
                }
                // Tampilkan pesan atau lakukan tindakan sesuai kebutuhan
                showToast("Profil gambar berhasil diperbarui")
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun sendUpdateProfileImageRequest(userId: Int, imageUri: String) {
        val apiService = ApiConfig.getApiService()

        val requestBody = mapOf("imageUri" to imageUri)

        apiService.updateUserProfileImage(userId, requestBody).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    showToast("Profil gambar berhasil diperbarui")

                    // Simpan URL gambar baru di preferensi
                    preference.saveProfileAvatarUrl(imageUri)

                    // Tampilkan gambar profil yang baru di ImageView menggunakan Glide
                    displayProfileAvatar(imageUri)
                } else {
                    showToast("Gagal memperbarui profil gambar")
                    Log.e("ProfileActivity", "Failed to update profile image: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Gagal memperbarui profil gambar")
                Log.e("ProfileActivity", "Failed to update profile image", t)
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}