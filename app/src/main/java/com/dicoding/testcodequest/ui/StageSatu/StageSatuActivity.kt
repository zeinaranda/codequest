package com.dicoding.testcodequest.ui.StageSatu

import android.app.Dialog
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
import com.dicoding.testcodequest.data.response.UpdateFirstLoginRequest
import com.dicoding.testcodequest.data.retrofit.ApiConfig
import com.dicoding.testcodequest.ui.*
import com.dicoding.testcodequest.ui.StageDua.StageDuaActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StageSatuActivity : AppCompatActivity() {
    private lateinit var preference: AuthPreference
    private lateinit var arrowButton: ImageView
    private var userPoint: Int? = null
    private var userKoin: Int? = null
    private var isStageCompleted: Boolean = false
    private lateinit var progressBar: LottieAnimationView
    private lateinit var courseSatu: ImageView
    private lateinit var courseDua: ImageView
    private lateinit var bossSatu: ImageView
    private var userId: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stage_satu)

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

        courseSatu = findViewById(R.id.courseSatu)
        courseDua = findViewById(R.id.courseDua)
        bossSatu = findViewById(R.id.bossSatu)

        userId?.let { checkKnowledgeCompletion(it) }
        userId?.let { checkBossCompletion(it) }

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
            if (isStageCompleted) {
                val intent = Intent(this, StageDuaActivity::class.java)
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

                // Navigate to StorytellingActivity with putExtra("codeScene", "prolog")
                val intent = Intent(this, StorytellingActivity::class.java).apply {
                    putExtra("codeScene", "prolog")
                }
                startActivity(intent)
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
                        isStageCompleted = response.body()?.stageCompleted ?: false

                    } else {
                        Toast.makeText(this@StageSatuActivity, "Failed to check stage completion", Toast.LENGTH_SHORT).show()
                    }
                    progressBar.visibility = View.GONE
                }

                override fun onFailure(call: Call<StageCompletionResponse>, t: Throwable) {
                    Toast.makeText(this@StageSatuActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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
                        if (knowledgeDoneResponse.knowledge1Completed) {
                            courseSatu.setImageResource(R.drawable.btn_stage_satu_upd)
                        }
                        if (knowledgeDoneResponse.knowledge2Completed) {
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
                        if (bossDoneResponse.boss1Completed) {
                            bossSatu.setImageResource(R.drawable.btn_stage_boss_upd)
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
