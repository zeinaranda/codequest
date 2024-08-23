package com.dicoding.testcodequest.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.preference.AuthPreference
import com.dicoding.testcodequest.data.response.User
import com.dicoding.testcodequest.data.retrofit.ApiConfig
import com.dicoding.testcodequest.ui.Adapter.LeaderboardAdapter
import com.dicoding.testcodequest.ui.StageSatu.StageSatuActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var preference: AuthPreference
    private var userPoint: Int? = null
    private var userKoin: Int? = null

    private lateinit var firstPlaceAvatar: ImageView
    private lateinit var firstPlaceName: TextView
    private lateinit var firstPlacePoints: TextView
    private lateinit var firstPlaceArrowImage: ImageView
    private lateinit var firstPlaceRankChange: TextView
    private lateinit var secondPlaceAvatar: ImageView
    private lateinit var secondPlaceName: TextView
    private lateinit var secondPlacePoints: TextView
    private lateinit var secondPlaceArrowImage: ImageView
    private lateinit var secondPlaceRankChange: TextView
    private lateinit var thirdPlaceAvatar: ImageView
    private lateinit var thirdPlaceName: TextView
    private lateinit var thirdPlacePoints: TextView
    private lateinit var thirdPlaceArrowImage: ImageView
    private lateinit var thirdPlaceRankChange: TextView
    private lateinit var progressBar: LottieAnimationView
    private lateinit var recyclerViewOthers: RecyclerView
    private lateinit var othersAdapter: LeaderboardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        // Initialize preferences
        preference = AuthPreference(this)

        // Initialize UI elements
        progressBar = findViewById(R.id.progressBar)
        recyclerViewOthers = findViewById(R.id.recyclerViewOthers)
        recyclerViewOthers.layoutManager = LinearLayoutManager(this)
        othersAdapter = LeaderboardAdapter()
        recyclerViewOthers.adapter = othersAdapter

        firstPlaceAvatar = findViewById(R.id.firstPlaceAvatar)
        firstPlaceName = findViewById(R.id.firstPlaceName)
        firstPlacePoints = findViewById(R.id.firstPlacePoints)
        firstPlaceArrowImage = findViewById(R.id.firstPlaceArrowImage)
        firstPlaceRankChange = findViewById(R.id.firstPlaceRankChange)

        secondPlaceAvatar = findViewById(R.id.secondPlaceAvatar)
        secondPlaceName = findViewById(R.id.secondPlaceName)
        secondPlacePoints = findViewById(R.id.secondPlacePoints)
        secondPlaceArrowImage = findViewById(R.id.secondPlaceArrowImage)
        secondPlaceRankChange = findViewById(R.id.secondPlaceRankChange)

        thirdPlaceAvatar = findViewById(R.id.thirdPlaceAvatar)
        thirdPlaceName = findViewById(R.id.thirdPlaceName)
        thirdPlacePoints = findViewById(R.id.thirdPlacePoints)
        thirdPlaceArrowImage = findViewById(R.id.thirdPlaceArrowImage)
        thirdPlaceRankChange = findViewById(R.id.thirdPlaceRankChange)

        // Get points and coins from preferences
        userPoint = preference.getPoints()
        userKoin = preference.getCoins()
        findViewById<TextView>(R.id.userPoint).text = userPoint.toString()
        findViewById<TextView>(R.id.userCoin).text = userKoin.toString()

        // Fetch top users
        fetchTopUsers()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.itemIconTintList = null

        bottomNavigationView.selectedItemId = R.id.menu1

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu1 -> {
                    startActivity(Intent(this, LeaderboardActivity::class.java))
                    finish()
                    true
                }
                R.id.menu2 -> {
                    startActivity(Intent(this, FriendActivity::class.java))
                    finish()
                    true
                }
                R.id.menu3 -> {
                    supportActionBar?.title = getString(R.string.menu_home)
                    startActivity(Intent(this, StageSatuActivity::class.java))
                    finish()
                    true
                }
                R.id.menu4 -> {
                    startActivity(Intent(this, ShopActivity::class.java))
                    finish()
                    true
                }
                R.id.menu5 -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
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

                    // Set data for top 3 users
                    if (topUsers.size >= 1) {
                        val user1 = topUsers[0]
                        setTopUser(user1, firstPlaceAvatar, firstPlaceName, firstPlacePoints, firstPlaceArrowImage, firstPlaceRankChange)
                    }

                    if (topUsers.size >= 2) {
                        val user2 = topUsers[1]
                        setTopUser(user2, secondPlaceAvatar, secondPlaceName, secondPlacePoints, secondPlaceArrowImage, secondPlaceRankChange)
                    }

                    if (topUsers.size >= 3) {
                        val user3 = topUsers[2]
                        setTopUser(user3, thirdPlaceAvatar, thirdPlaceName, thirdPlacePoints, thirdPlaceArrowImage, thirdPlaceRankChange)
                    }

                    // Set other users in RecyclerView
                    val otherUsers = topUsers.drop(3)
                    othersAdapter.setUsers(otherUsers)
                } else {
                    Log.e("LeaderboardActivity", "Error: ${response.message()}")
                }
                progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("LeaderboardActivity", "Failure: ${t.message}")
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun setTopUser(user: User, avatarView: ImageView, nameView: TextView, pointsView: TextView, arrowImageView: ImageView, rankChangeView: TextView) {
        Glide.with(this).load(user.profileAvatar).into(avatarView)
        nameView.text = user.nama
        pointsView.text = user.point.toString()

        when (user.rankChange) {
            "up" -> {
                Glide.with(this).load(R.drawable.ic_baseline_arrow_upward_24).into(arrowImageView)
                arrowImageView.visibility = View.VISIBLE
                rankChangeView.text = "Up"
            }
            "down" -> {
                Glide.with(this).load(R.drawable.ic_baseline_arrow_downward_24).into(arrowImageView)
                arrowImageView.visibility = View.VISIBLE
                rankChangeView.text = "Down"
            }
            else -> {
                arrowImageView.visibility = View.GONE
                rankChangeView.visibility = View.GONE
            }
        }
    }
}