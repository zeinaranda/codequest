package com.dicoding.testcodequest.ui.StageSatu

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.preference.AuthPreference
import com.dicoding.testcodequest.data.response.StageCompletionResponse
import com.dicoding.testcodequest.data.response.UpdateFirstLoginRequest
import com.dicoding.testcodequest.data.retrofit.ApiConfig
import com.dicoding.testcodequest.ui.*
import com.dicoding.testcodequest.ui.StageDua.StageDuaActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StageSatuActivity : AppCompatActivity() {
    private lateinit var preference: AuthPreference
    private lateinit var arrowButton: ImageView
    private var userPoint: Int? = null
    private var userKoin: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stage_satu)

        preference = AuthPreference(this)
        userPoint = preference.getPoints()
        userKoin = preference.getCoins()

        val pointsText = findViewById<TextView>(R.id.userPoint)
        pointsText.text = userPoint.toString()

        val coinsText = findViewById<TextView>(R.id.userCoin)
        coinsText.text = userKoin.toString()

        arrowButton = findViewById(R.id.arrowButton)

        val courseSatu: ImageView = findViewById(R.id.courseSatu)
        val courseDua: ImageView = findViewById(R.id.courseDua)
        val bossSatu: ImageView = findViewById(R.id.bossSatu)

        courseSatu.setOnClickListener {
            val intent = Intent(this, StorytellingActivity::class.java)
            intent.putExtra("codeScene", "1")
            intent.putExtra("materi_id", 1)
            startActivity(intent)
        }

        courseDua.setOnClickListener {
            val intent = Intent(this, CourseActivity::class.java)
            intent.putExtra("materi_id", 2)
            startActivity(intent)
        }

        bossSatu.setOnClickListener {
            val intent = Intent(this, StorytellingActivity::class.java)
            intent.putExtra("codeScene", "b1")
            startActivity(intent)
        }

        checkStageCompletion()

        arrowButton.setOnClickListener {
            if (arrowButton.isEnabled) {
                val intent = Intent(this, StageDuaActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "You need to score 640 points in Stage 1 to proceed", Toast.LENGTH_SHORT).show()
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
    checkFirstLogin()
    }

    private fun checkFirstLogin() {
        val firstLogin = preference.isFirstLogin()
        if (firstLogin) {
            showFirstLoginDialog()
        }
    }

    private fun showFirstLoginDialog() {
        try {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_first_login)
            dialog.setCancelable(false)

            val btnOk: Button = dialog.findViewById(R.id.btn_ok)
            btnOk.setOnClickListener {
                dialog.dismiss()

                // Update firstLogin to false after clicking OK
                updateFirstLoginStatus()
            }

            dialog.show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error showing dialog: ${e.message}", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun updateFirstLoginStatus() {
        val userId = preference.getId()
        if (userId != null) {
            val request = UpdateFirstLoginRequest(userId)
            ApiConfig.getApiService().updateFirstLoginStatus(request).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // Update firstLogin in local preferences
                        preference.setFirstLogin(false)
                        Toast.makeText(this@StageSatuActivity, "First login status updated", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@StageSatuActivity, "Failed to update first login status", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@StageSatuActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun checkStageCompletion() {
        val userId = preference.getId() // Ambil userId dari preferensi
        val stageId = 1 // ID Stage 1

        if (userId != null) {
            ApiConfig.getApiService().checkStageCompletion(userId, stageId).enqueue(object :
                Callback<StageCompletionResponse> {
                override fun onResponse(call: Call<StageCompletionResponse>, response: Response<StageCompletionResponse>) {
                    if (response.isSuccessful) {
                        val isCompleted = response.body()?.stageCompleted ?: false
                        arrowButton.isEnabled = isCompleted
                    } else {
                        Toast.makeText(this@StageSatuActivity, "Failed to check stage completion", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<StageCompletionResponse>, t: Throwable) {
                    Toast.makeText(this@StageSatuActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}