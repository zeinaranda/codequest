package com.dicoding.testcodequest.ui

import AvatarAdapter
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.response.Avatar
import com.dicoding.testcodequest.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.dicoding.testcodequest.data.common.Resource
import com.dicoding.testcodequest.data.preference.AuthPreference
import com.dicoding.testcodequest.data.preference.ShopPreference
import com.dicoding.testcodequest.data.response.ShopResponse
import com.dicoding.testcodequest.databinding.ActivityShopBinding
import com.dicoding.testcodequest.viewmodel.ShopViewModel
import com.dicoding.testcodequest.viewmodel.ShopViewModelFactory


class ShopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopBinding
    private lateinit var rvShop: RecyclerView
    private lateinit var viewModel: ShopViewModel
    private lateinit var preferences: ShopPreference
    private lateinit var authPreference: AuthPreference
    private lateinit var adapter: AvatarAdapter
    private val list = ArrayList<Avatar>()
    private var obtainMethod: String? = "shop"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvShop = findViewById(R.id.rv_shop) as RecyclerView
        rvShop.setHasFixedSize(true)

        authPreference = AuthPreference(this)

        preferences = ShopPreference(this)
        adapter = AvatarAdapter(list)
        adapter.notifyDataSetChanged()

        viewModel = ViewModelProvider(this, ShopViewModelFactory()).get(
            ShopViewModel::class.java
        )

        binding.apply {
            rvShop.setHasFixedSize(true)
            rvShop.layoutManager = GridLayoutManager(this@ShopActivity, 3)
            rvShop.adapter = adapter
        }

        viewModel.setStories(obtainMethod!!)
        viewModel.getAvatar().observe(this, {
            if (it != null) {
                adapter.setList(it)
            }
        })

        adapter.setOnItemClickCallback(object : AvatarAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Avatar) {
                showPurchaseDialog(data)
            }
        })

        viewModel.isLoading.observe(this, {
            showLoading(it)
        })

    }

    private fun showPurchaseDialog(avatar: Avatar) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi Pembelian")
        builder.setMessage("Apakah Anda ingin membeli avatar ini?")
        builder.setPositiveButton("Ya") { _, _ ->
            // Tambahkan avatarId ke ownedAvatars
            addAvatarToOwned(avatar)
        }
        builder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun addAvatarToOwned(avatar: Avatar) {
        // Implementasi penambahan avatarId ke ownedAvatars, misalnya melalui API
        val userId = authPreference.getId() // Ambil userId dari preferences
        val avatarId = avatar.avatarId

        val apiService = ApiConfig.getApiService()
        if (userId != null) {
            apiService.addOwnedAvatar(userId, avatarId).enqueue(object : Callback<ShopResponse> {
                override fun onResponse(call: Call<ShopResponse>, response: Response<ShopResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ShopActivity, "Avatar berhasil dibeli", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@ShopActivity, "Gagal membeli avatar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ShopResponse>, t: Throwable) {
                    Toast.makeText(this@ShopActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
