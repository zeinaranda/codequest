package com.dicoding.testcodequest.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.ui.StageDua.StageDuaActivity
import com.dicoding.testcodequest.ui.StageEmpat.StageEmpatActivity
import com.dicoding.testcodequest.ui.StageEnam.StageEnamActivity
import com.dicoding.testcodequest.ui.StageLima.StageLimaActivity
import com.dicoding.testcodequest.ui.StageSatu.StageSatuActivity
import com.dicoding.testcodequest.ui.StageTiga.StageTigaActivity
import com.dicoding.testcodequest.ui.StageTujuh.StageTujuhActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ResultBossActivity : AppCompatActivity() {
    private var codeScene: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_boss)

        val btn_back : ImageView = findViewById(R.id.btn_back)
        btn_back.setOnClickListener {
            when {
                (shouldNavigateToStageActivity(codeScene)) -> {
                    navigateToStageActivity()
                }

                else -> Intent(this, StageSatuActivity::class.java) // Default fallback jika knowledgeId tidak sesuai
            }

        }

        // Get values from intent
        val points = intent.getIntExtra("points", 0)
        val coins = intent.getIntExtra("coins", 0)
        val avatarImage = intent.getStringExtra("avatarImage")
        val avatarName = intent.getStringExtra("avatarName")
        val correctAnswers = intent.getIntExtra("correctAnswers", 0)
        val wrongAnswers = intent.getIntExtra("wrongAnswers", 0)

        findViewById<TextView>(R.id.correctAnswers).text = "Correct Answers: $correctAnswers"
        findViewById<TextView>(R.id.wrongAnswers).text = "Wrong Answers: $wrongAnswers"
        codeScene = intent.getStringExtra("codeScene") ?: "default_scene"


        // Update UI with received values
        val resultText = findViewById<TextView>(R.id.resultText)
        resultText.text =
            when {
                points in 0..1119 -> "Yah nilaimu masih kurang. Tapi jangan menyerah, coba lagi ya"
                points in 1120..1360 -> "Selamat Anda mendapatkan medali perunggu di Tes ini."
                points in 1440..1680 -> "Selamat Anda mendapatkan medali perak di Tes ini"
                else -> "Selamat Anda mendapatkan medali emas di Tes ini"
            }

        val pointsText = findViewById<TextView>(R.id.pointsText)
        pointsText.text = "+ $points"

        val coinsText = findViewById<TextView>(R.id.coinsText)
        coinsText.text = "+ $coins"

        // Set avatarName
        val avatarNameText = findViewById<TextView>(R.id.avatarName)
        avatarNameText.text = avatarName

        // Set avatarImage using Glide
        val ivAvatarImage = findViewById<ImageView>(R.id.avatarImage)
        Glide.with(this)
            .load(avatarImage)
            .into(ivAvatarImage)

        // Find LottieAnimationView
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieAnimationView)

        // Set appropriate animation based on points
        when {
            points in 0..1119 -> {
                lottieAnimationView.setAnimation(R.raw.sad)
                // Hide avatarImage and avatarName
                ivAvatarImage.visibility = View.GONE
                avatarNameText.visibility = View.GONE
            }
            points in 1120..1360 -> lottieAnimationView.setAnimation(R.raw.perunggu_medal)
            points in 1440..1680 -> lottieAnimationView.setAnimation(R.raw.perak_medal)
            else -> lottieAnimationView.setAnimation(R.raw.gold_medal)
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

    private fun shouldNavigateToStageActivity(codeScene: String?): Boolean {
        return codeScene in listOf("b11", "b22", "b33", "b44", "b55", "b66", "b77")
    }

    private fun navigateToStageActivity() {
        val intent = when (codeScene) {
            "b11" -> Intent(this, StageSatuActivity::class.java)

            "b22" -> Intent(this, StageDuaActivity::class.java)

            "b33" -> Intent(this, StageTigaActivity::class.java)

            "b44" -> Intent(this, StageEmpatActivity::class.java)

            "b55" -> Intent(this, StageLimaActivity::class.java)

            "b66" -> Intent(this, StageEnamActivity::class.java)

            "b77" -> Intent(this, StageTujuhActivity::class.java)


            else -> Intent(this, StageSatuActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

}
