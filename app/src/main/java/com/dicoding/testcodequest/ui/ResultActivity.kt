package com.dicoding.testcodequest.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.ui.StageSatu.StageSatuActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val btn_back : ImageView = findViewById(R.id.btn_back)
        btn_back.setOnClickListener {
            startActivity(Intent(this, StageSatuActivity::class.java))
            finish()
        }

        // Get values from intent
        val points = intent.getIntExtra("points", 0)
        val coins = intent.getIntExtra("coins", 0)

        // Update UI with received values
        val resultText = findViewById<TextView>(R.id.resultText)
//        resultText.text = if (points >= 640) "Selamat " else "Coba Lagi"
        resultText.text =
        when {
            points in 640..720 -> "Selamat Anda mendapatkan medali perunggu di Tes ini."
            points in 721..850 -> "Selamat Anda mendapatkan medali perak di Tes ini"
            points in 851..1000 -> "Selamat Anda mendapatkan medali emas di Tes ini"
            else -> "Jangan menyerah, coba lagi ya"
        }

        val pointsText = findViewById<TextView>(R.id.pointsText)
        pointsText.text = "Points: $points"

        val coinsText = findViewById<TextView>(R.id.coinsText)
        coinsText.text = "Coins: $coins"

        // Find LottieAnimationView
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieAnimationView)

        // Set appropriate animation based on points
        when {
            points in 640..720 -> lottieAnimationView.setAnimation(R.raw.perunggu_medal)
            points in 721..850 -> lottieAnimationView.setAnimation(R.raw.perak_medal)
            points in 851..1000 -> lottieAnimationView.setAnimation(R.raw.gold_medal)
            else -> lottieAnimationView.setAnimation(R.raw.applause)
        }

        lottieAnimationView.playAnimation()

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
                    // Stay in the current activity
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
}