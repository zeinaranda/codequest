package com.dicoding.testcodequest.ui

import AvatarAdapter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.testcodequest.data.preference.AuthPreference
import com.dicoding.testcodequest.data.preference.ShopPreference
import com.dicoding.testcodequest.data.response.Avatar
import com.dicoding.testcodequest.data.response.BuyResponse
import com.dicoding.testcodequest.data.response.ShopResponse
import com.dicoding.testcodequest.data.response.User
import com.dicoding.testcodequest.data.retrofit.ApiConfig
import com.dicoding.testcodequest.databinding.ActivityShopBinding
import com.dicoding.testcodequest.viewmodel.ShopViewModel
import com.dicoding.testcodequest.viewmodel.ShopViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopBinding
    private lateinit var rvShop: RecyclerView
    private lateinit var viewModel: ShopViewModel
    private lateinit var preferences: ShopPreference
    private lateinit var authPreference: AuthPreference
    private lateinit var adapter: AvatarAdapter
    private val list = ArrayList<Avatar>()
    private var obtainMethod: String? = "shop"
    private val requiredCoins = 800
    private var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvShop = binding.rvShop
        rvShop.setHasFixedSize(true)

        authPreference = AuthPreference(this)
        userId = authPreference.getId() // Ambil userId dari preferences

        preferences = ShopPreference(this)
        adapter = AvatarAdapter(list)
        adapter.notifyDataSetChanged()

        viewModel = ViewModelProvider(this, ShopViewModelFactory()).get(ShopViewModel::class.java)

        binding.rvShop.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@ShopActivity, 3)
            adapter = this@ShopActivity.adapter
        }

        viewModel.setStories(obtainMethod!!)
        viewModel.getAvatar().observe(this, { avatars ->
            avatars?.let {
                adapter.setList(it)
            }
        })

        adapter.setOnItemClickCallback(object : AvatarAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Avatar) {
                showPurchaseDialog(data)
            }
        })

        viewModel.isLoading.observe(this, { isLoading ->
            showLoading(isLoading)
        })
    }

    private fun showPurchaseDialog(avatar: Avatar) {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Pembelian")
            .setMessage("Apakah Anda ingin membeli avatar ini?")
            .setPositiveButton("Ya") { _, _ ->
                checkUserCoinsAndPurchase(avatar)
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun checkUserCoinsAndPurchase(avatar: Avatar) {
        userId?.let { userId ->
            val apiService = ApiConfig.getApiService()
            apiService.getUserById(userId).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        user?.let {
                            if (it.ownedAvatars?.contains(avatar.avatarId) == true) {
                                Toast.makeText(this@ShopActivity, "Avatar ini sudah Anda miliki", Toast.LENGTH_SHORT).show()
                            } else {
                                if (it.koin >= requiredCoins) {
                                    updateUserKoinAndAddAvatar(userId, it.koin, avatar.avatarId)
                                } else {
                                    Toast.makeText(this@ShopActivity, "Koin tidak cukup untuk membeli avatar", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } ?: run {
                            Toast.makeText(this@ShopActivity, "Gagal mengambil data user", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@ShopActivity, "Gagal mengambil data user", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@ShopActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


    private fun updateUserKoinAndAddAvatar(userId: Int, currentKoin: Int, avatarId: Int) {
        val newKoin = currentKoin - requiredCoins

        val apiService = ApiConfig.getApiService()

        // Retrieve the user's current owned avatars
        apiService.getUserById(userId).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    user?.let {
                        val updatedOwnedAvatars = it.ownedAvatars?.toMutableList()
                        if (updatedOwnedAvatars != null) {
                            updatedOwnedAvatars.add(avatarId)
                        }

                        val updatedUser = User(
                            userId = userId,
                            koin = newKoin,
                            ownedAvatars = updatedOwnedAvatars // Update the ownedAvatars list
                        )

                        // Update the user's koin and owned avatars
                        apiService.updateUserKoin(userId, updatedUser).enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(this@ShopActivity, "Avatar berhasil dibeli", Toast.LENGTH_SHORT).show()
                                    authPreference.setCoins(newKoin)
                                } else {
                                    Toast.makeText(this@ShopActivity, "Gagal memperbarui koin", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Toast.makeText(this@ShopActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    } ?: run {
                        Toast.makeText(this@ShopActivity, "Gagal mengambil data user", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ShopActivity, "Gagal mengambil data user", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@ShopActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
