package com.dicoding.testcodequest.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.ui.StageDua.StageDuaActivity
import com.dicoding.testcodequest.ui.StageEmpat.StageEmpatActivity
import com.dicoding.testcodequest.ui.StageEnam.StageEnamActivity
import com.dicoding.testcodequest.ui.StageLima.StageLimaActivity
import com.dicoding.testcodequest.ui.StageSatu.StageSatuActivity
import com.dicoding.testcodequest.ui.StageTiga.StageTigaActivity
import com.dicoding.testcodequest.ui.StageTujuh.StageTujuhActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ResultKnowledgeActivity : AppCompatActivity() {
    private var knowledgeId: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_knowledge)


        val btn_back : ImageView = findViewById(R.id.btn_back)
        btn_back.setOnClickListener {
            when {

                shouldNavigateToStageActivityByKnowledge(knowledgeId) -> {
                    navigateToStageActivityByKnowledge()
                }

                else -> Intent(this, StageSatuActivity::class.java) // Default fallback jika knowledgeId tidak sesuai
            }

        }


        // Get values from intent
        val points = intent.getIntExtra("points", 0)
        val coins = intent.getIntExtra("coins", 0)
        knowledgeId = intent.getIntExtra("knowledgeId", 1) // Default to 1 if not found


        // Update UI with received values
        val resultText = findViewById<TextView>(R.id.resultText)
        resultText.text =
            when {
                points in 0..39 -> "Yah jawabanmu kurang tepat. Tapi jangan menyerah, coba lagi ya"
                points in 40..160 -> "Waw kamu luar biasa!."
                else -> "Waw kamu luar biasa!."
            }

        val pointsText = findViewById<TextView>(R.id.pointsText)
        pointsText.text = "+ $points"

        val coinsText = findViewById<TextView>(R.id.coinsText)
        coinsText.text = "+ $coins"

        // Find LottieAnimationView
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieAnimationView)

        // Set appropriate animation based on points
        when {
            points in 0..39 -> lottieAnimationView.setAnimation(R.raw.sad)
            points in 40..160 -> lottieAnimationView.setAnimation(R.raw.applause)
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


    private fun shouldNavigateToStageActivityByKnowledge(knowledgeId: Int?): Boolean {
        return knowledgeId in listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)
    }

    private fun navigateToStageActivityByKnowledge() {
        val intent = when (knowledgeId) {
            1,2 -> Intent(this, StageSatuActivity::class.java)

            3,4 -> Intent(this, StageDuaActivity::class.java)

            5,6 -> Intent(this, StageTigaActivity::class.java)

            7,8 -> Intent(this, StageEmpatActivity::class.java)

            9,10 -> Intent(this, StageLimaActivity::class.java)

            11,12 -> Intent(this, StageEnamActivity::class.java)

            13,14 -> Intent(this, StageTujuhActivity::class.java)


            else -> Intent(this, StageSatuActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}