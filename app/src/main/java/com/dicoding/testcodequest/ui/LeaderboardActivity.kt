package com.dicoding.testcodequest.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.response.User
import com.dicoding.testcodequest.data.retrofit.ApiConfig
import com.dicoding.testcodequest.ui.Adapter.LeaderboardAdapter
import com.dicoding.testcodequest.ui.StageSatu.StageSatuActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var firstPlaceAvatar: ImageView
    private lateinit var firstPlaceName: TextView
    private lateinit var firstPlacePoints: TextView
    private lateinit var secondPlaceAvatar: ImageView
    private lateinit var secondPlaceName: TextView
    private lateinit var secondPlacePoints: TextView
    private lateinit var thirdPlaceAvatar: ImageView
    private lateinit var thirdPlaceName: TextView
    private lateinit var thirdPlacePoints: TextView

    private lateinit var recyclerViewOthers: RecyclerView
    private lateinit var othersAdapter: LeaderboardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        firstPlaceAvatar = findViewById(R.id.firstPlaceAvatar)
        firstPlaceName = findViewById(R.id.firstPlaceName)
        firstPlacePoints = findViewById(R.id.firstPlacePoints)
        secondPlaceAvatar = findViewById(R.id.secondPlaceAvatar)
        secondPlaceName = findViewById(R.id.secondPlaceName)
        secondPlacePoints = findViewById(R.id.secondPlacePoints)
        thirdPlaceAvatar = findViewById(R.id.thirdPlaceAvatar)
        thirdPlaceName = findViewById(R.id.thirdPlaceName)
        thirdPlacePoints = findViewById(R.id.thirdPlacePoints)

        recyclerViewOthers = findViewById(R.id.recyclerViewOthers)
        recyclerViewOthers.layoutManager = LinearLayoutManager(this)
        othersAdapter = LeaderboardAdapter()
        recyclerViewOthers.adapter = othersAdapter

        fetchTopUsers()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.itemIconTintList = null
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu1 -> {
                    val i = Intent(this, StageSatuActivity::class.java)
                    startActivity(i)
                    finish()
                    true
                }
                R.id.menu2 -> {
                    // Stay in the current activity
                    true
                }
                else -> false
            }
        }
    }



    private fun fetchTopUsers() {
        ApiConfig.getApiService().getTopUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val topUsers = response.body()!!

                    if (topUsers.size >= 1) {
                        val user1 = topUsers[0]
                        setTopUser(user1, firstPlaceAvatar, firstPlaceName, firstPlacePoints)
                    }

                    if (topUsers.size >= 2) {
                        val user2 = topUsers[1]
                        setTopUser(user2, secondPlaceAvatar, secondPlaceName, secondPlacePoints)
                    }

                    if (topUsers.size >= 3) {
                        val user3 = topUsers[2]
                        setTopUser(user3, thirdPlaceAvatar, thirdPlaceName, thirdPlacePoints)
                    }

                    if (topUsers.size > 3) {
                        val others = topUsers.subList(3, topUsers.size)
                        othersAdapter.setUsers(others)
                    }
                } else {
                    Toast.makeText(this@LeaderboardActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@LeaderboardActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setTopUser(user: User, avatar: ImageView, name: TextView, points: TextView) {
        name.text = user.nama
        points.text = user.point.toString()
        if (user.profileAvatar != null) {
            Glide.with(this)
                .load(user.profileAvatar)
                .placeholder(R.drawable.placeholder_image) // Gambar placeholder jika perlu
                .into(avatar)
        } else {
            avatar.setImageResource(R.drawable.placeholder_image) // Gambar default jika URL null
        }
    }
}