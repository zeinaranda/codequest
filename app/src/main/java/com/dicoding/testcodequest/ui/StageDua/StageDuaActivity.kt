package com.dicoding.testcodequest.ui.StageDua

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.preference.AuthPreference
import com.dicoding.testcodequest.data.response.StageCompletionResponse
import com.dicoding.testcodequest.data.retrofit.ApiConfig
import com.dicoding.testcodequest.ui.*
import com.dicoding.testcodequest.ui.StageSatu.StageSatuActivity
import com.dicoding.testcodequest.ui.StageTiga.StageTigaActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StageDuaActivity : AppCompatActivity() {
    private lateinit var preference: AuthPreference
    private lateinit var arrowButton: ImageView
    private lateinit var arrowButtonBack: ImageView
    private var userPoint: Int? = null
    private var userKoin: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stage_dua)

        preference = AuthPreference(this)

        userPoint = preference.getPoints()
        userKoin = preference.getCoins()

        val pointsText = findViewById<TextView>(R.id.userPoint)
        pointsText.text = userPoint.toString()

        val coinsText = findViewById<TextView>(R.id.userCoin)
        coinsText.text = userKoin.toString()

        arrowButton = findViewById(R.id.arrowButton)
        arrowButtonBack = findViewById(R.id.arrowButtonBack)

        val courseSatu: ImageView = findViewById(R.id.courseSatu)
        val courseDua: ImageView = findViewById(R.id.courseDua)
        val bossDua: ImageView = findViewById(R.id.bossDua)

        courseSatu.setOnClickListener {
            val intent = Intent(this, StorytellingActivity::class.java)
            intent.putExtra("codeScene", "2")
            intent.putExtra("materi_id", 3)
            startActivity(intent)
        }

        courseDua.setOnClickListener {
            val intent = Intent(this, CourseActivity::class.java)
            intent.putExtra("materi_id", 4)
            startActivity(intent)
        }

        bossDua.setOnClickListener {
            val intent = Intent(this, StorytellingActivity::class.java)
            intent.putExtra("codeScene", "b2")
            startActivity(intent)
        }

        checkStageCompletion()

        arrowButtonBack.setOnClickListener {
            val intent = Intent(this, StageSatuActivity::class.java)
            startActivity(intent)
        }

        arrowButton.setOnClickListener {
            if (arrowButton.isEnabled) {
                val intent = Intent(this, StageTigaActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "You need to score 640 points in Stage 2 to proceed", Toast.LENGTH_SHORT).show()
            }
        }

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

    private fun checkStageCompletion() {
        val userId = preference.getId() // Ambil userId dari preferensi
        val stageId = 2 // ID Stage 1

        if (userId != null) {
            ApiConfig.getApiService().checkStageCompletion(userId, stageId).enqueue(object :
                Callback<StageCompletionResponse> {
                override fun onResponse(call: Call<StageCompletionResponse>, response: Response<StageCompletionResponse>) {
                    if (response.isSuccessful) {
                        val isCompleted = response.body()?.stageCompleted ?: false
                        arrowButton.isEnabled = isCompleted
                    } else {
                        Toast.makeText(this@StageDuaActivity, "Failed to check stage completion", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<StageCompletionResponse>, t: Throwable) {
                    Toast.makeText(this@StageDuaActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}