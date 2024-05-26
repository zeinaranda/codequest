package com.dicoding.testcodequest.ui

import AvatarAdapter
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
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
import com.dicoding.testcodequest.data.preference.ShopPreference
import com.dicoding.testcodequest.databinding.ActivityShopBinding
import com.dicoding.testcodequest.viewmodel.ShopViewModel
import com.dicoding.testcodequest.viewmodel.ShopViewModelFactory


class ShopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopBinding
    private lateinit var rvShop: RecyclerView
    private lateinit var viewModel: ShopViewModel
    private lateinit var preferences: ShopPreference
    private lateinit var adapter: AvatarAdapter
    private val list = ArrayList<Avatar>()
    private var obtainMethod: String? = "shop"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvShop = findViewById(R.id.rv_shop) as RecyclerView
        rvShop.setHasFixedSize(true)

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



        viewModel.isLoading.observe(this, {
            showLoading(it)
        })

    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
