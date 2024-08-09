package com.dicoding.testcodequest.ui.StageLima

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.airbnb.lottie.LottieAnimationView
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.preference.AuthPreference
import com.dicoding.testcodequest.data.response.BossDoneResponse
import com.dicoding.testcodequest.data.response.KnowledgeDoneResponse
import com.dicoding.testcodequest.data.response.StageCompletionResponse
import com.dicoding.testcodequest.data.retrofit.ApiConfig
import com.dicoding.testcodequest.ui.*
import com.dicoding.testcodequest.ui.StageEmpat.StageEmpatActivity
import com.dicoding.testcodequest.ui.StageEnam.StageEnamActivity
import com.dicoding.testcodequest.ui.StageSatu.StageSatuActivity
import com.dicoding.testcodequest.ui.StageTiga.StageTigaActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StageLimaActivity : AppCompatActivity() {
    private lateinit var preference: AuthPreference
    private lateinit var arrowButton: ImageView
    private lateinit var arrowButtonBack: ImageView
    private lateinit var courseSatu: ImageView
    private lateinit var courseDua: ImageView
    private lateinit var bossDua: ImageView
    private var userPoint: Int? = null
    private var userKoin: Int? = null
    private var isStageCompleted: Boolean = false
    private lateinit var progressBar: LottieAnimationView
    private var userId: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stage_lima)

        // Inisialisasi progressBar
        progressBar = findViewById(R.id.progressBar)

        // Atur visibilitas progressBar menjadi visible sebelum memulai fetching data
        progressBar.visibility = View.VISIBLE

        preference = AuthPreference(this)
        userId = preference.getId()
        userPoint = preference.getPoints()
        userKoin = preference.getCoins()

        val pointsText = findViewById<TextView>(R.id.userPoint)
        pointsText.text = userPoint.toString()

        val coinsText = findViewById<TextView>(R.id.userCoin)
        coinsText.text = userKoin.toString()

        arrowButton = findViewById(R.id.arrowButton)
        arrowButtonBack = findViewById(R.id.arrowButtonBack)

        courseSatu = findViewById(R.id.courseSatu)
        courseDua = findViewById(R.id.courseDua)
        bossDua = findViewById(R.id.bossDua)

        userId?.let { checkKnowledgeCompletion(it) }
        userId?.let { checkBossCompletion(it) }

        courseSatu.setOnClickListener {
            val intent = Intent(this, StorytellingActivity::class.java)
            intent.putExtra("codeScene", "5")
            intent.putExtra("materi_id", 9)
            startActivity(intent)
        }

        courseDua.setOnClickListener {
            val intent = Intent(this, CourseActivity::class.java)
            intent.putExtra("materi_id", 10)
            startActivity(intent)
        }

        bossDua.setOnClickListener {
            val intent = Intent(this, StorytellingActivity::class.java)
            intent.putExtra("codeScene", "b5")
            startActivity(intent)
        }

        checkStageCompletion()

        arrowButtonBack.setOnClickListener {
            val intent = Intent(this, StageEmpatActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        arrowButton.setOnClickListener {
            if (isStageCompleted) {
                val intent = Intent(this, StageEnamActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left) // Tambahkan animasi slide ke kiri
            } else {
                showInfoDialog()
            }
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.itemIconTintList = null

        bottomNavigationView.selectedItemId = R.id.menu3

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
                    supportActionBar?.title = getString(R.string.menu_home)
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
        val stageId = 5 // ID Stage 1

        if (userId != null) {
            ApiConfig.getApiService().checkStageCompletion(userId, stageId).enqueue(object :
                Callback<StageCompletionResponse> {
                override fun onResponse(call: Call<StageCompletionResponse>, response: Response<StageCompletionResponse>) {
                    if (response.isSuccessful) {
                        isStageCompleted = response.body()?.stageCompleted ?: false

                    } else {
                        Toast.makeText(this@StageLimaActivity, "Failed to check stage completion", Toast.LENGTH_SHORT).show()
                    }
                    progressBar.visibility = View.GONE
                }

                override fun onFailure(call: Call<StageCompletionResponse>, t: Throwable) {
                    Toast.makeText(this@StageLimaActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            })
        }
    }

    private fun showInfoDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_info, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<Button>(R.id.dialog_button).setOnClickListener {
            dialogBuilder.dismiss()
        }

        dialogBuilder.show()
    }

    private fun checkKnowledgeCompletion(userId: Int) {
        val api = ApiConfig.getApiService()
        val call = api.checkKnowledgeCompletion(userId)

        call.enqueue(object : Callback<KnowledgeDoneResponse> {
            override fun onResponse(call: Call<KnowledgeDoneResponse>, response: Response<KnowledgeDoneResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { knowledgeDoneResponse ->
                        if (knowledgeDoneResponse.knowledge9Completed) {
                            courseSatu.setImageResource(R.drawable.btn_stage_satu_upd)
                        }
                        if (knowledgeDoneResponse.knowledge10Completed) {
                            courseDua.setImageResource(R.drawable.btn_stage_dua_upd)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<KnowledgeDoneResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun checkBossCompletion(userId: Int) {
        val api = ApiConfig.getApiService()
        val call = api.checkBossCompletion(userId)

        call.enqueue(object : Callback<BossDoneResponse> {
            override fun onResponse(call: Call<BossDoneResponse>, response: Response<BossDoneResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { bossDoneResponse ->
                        if (bossDoneResponse.boss5Completed) {
                            bossDua.setImageResource(R.drawable.btn_stage_boss_upd)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<BossDoneResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }
}