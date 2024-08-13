package com.dicoding.testcodequest.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.response.Storytelling
import com.dicoding.testcodequest.data.retrofit.ApiConfig
import com.dicoding.testcodequest.data.retrofit.ApiService
import com.dicoding.testcodequest.ui.StageDua.BossDuaActivity
import com.dicoding.testcodequest.ui.StageDua.StageDuaActivity
import com.dicoding.testcodequest.ui.StageEmpat.BossEmpatActivity
import com.dicoding.testcodequest.ui.StageEmpat.StageEmpatActivity
import com.dicoding.testcodequest.ui.StageEnam.BossEnamActivity
import com.dicoding.testcodequest.ui.StageEnam.StageEnamActivity
import com.dicoding.testcodequest.ui.StageLima.BossLimaActivity
import com.dicoding.testcodequest.ui.StageLima.StageLimaActivity
import com.dicoding.testcodequest.ui.StageSatu.BossSatuActivity
import com.dicoding.testcodequest.ui.StageSatu.StageSatuActivity
import com.dicoding.testcodequest.ui.StageTiga.BossTigaActivity
import com.dicoding.testcodequest.ui.StageTiga.StageTigaActivity
import com.dicoding.testcodequest.ui.StageTujuh.BossTujuhActivity
import com.dicoding.testcodequest.ui.StageTujuh.StageTujuhActivity
import org.jetbrains.annotations.Nullable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StorytellingActivity : AppCompatActivity() {
    private lateinit var layoutStory: ConstraintLayout
    private lateinit var tvStoryText: TextView
    private lateinit var tvAvatarName: TextView
    private lateinit var ivAvatarImage: ImageView
    private lateinit var ivBg: ImageView
    private lateinit var tvSkip: TextView
    private var storytellingList: List<Storytelling>? = null
    private var currentStoryIndex: Int = 0
    private var materiId: Int = 0
    private var codeScene: String? = null
    private var avatarImage: String? = null
    private var avatarName: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storytelling)

        // Get values from intent
        val points = intent.getIntExtra("points", 0)
        val coins = intent.getIntExtra("coins", 0)
        val correctAnswers = intent.getIntExtra("correctAnswers", 0)
        val wrongAnswers = intent.getIntExtra("wrongAnswers", 0)


        val btnBack: ImageView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            when (codeScene) {
                "1", "b1", "b11", "prolog" -> {
                    startActivity(Intent(this, StageSatuActivity::class.java))
                }
                "2", "b2", "b22" -> {
                    startActivity(Intent(this, StageDuaActivity::class.java))
                }
                "3", "b3", "b33" -> {
                    startActivity(Intent(this, StageTigaActivity::class.java))
                }
                "4", "b4", "b44" -> {
                    startActivity(Intent(this, StageEmpatActivity::class.java))
                }
                "5", "b5", "b55" -> {
                    startActivity(Intent(this, StageLimaActivity::class.java))
                }
                "6", "b6", "b66" -> {
                    startActivity(Intent(this, StageEnamActivity::class.java))
                }
                "7", "b7", "b77", "epilog" -> {
                    startActivity(Intent(this, StageTujuhActivity::class.java))
                }
                else -> {
                    // Default case
                    startActivity(Intent(this, StageSatuActivity::class.java))
                }
            }
            finish()
        }

        layoutStory = findViewById(R.id.layoutStory)
        tvStoryText = findViewById(R.id.tvStoryText)
        tvAvatarName = findViewById(R.id.tvAvatarName)
        ivAvatarImage = findViewById(R.id.ivAvatarImage)
        ivBg = findViewById(R.id.ivBg)
        tvSkip = findViewById(R.id.tvSkip)

        // Get codeScene and materi_id from intent
        codeScene = intent.getStringExtra("codeScene") ?: "default_scene"
        materiId = intent.getIntExtra("materi_id", 0)
        val avatarImage = intent.getStringExtra("avatarImage") ?: "default_image"
        val avatarName = intent.getStringExtra("avatarName") ?: "default_name"



        // Fetch storytelling content
        fetchStorytellingContent(codeScene)

        layoutStory.setOnClickListener {
            storytellingList?.let {
                currentStoryIndex++
                if (currentStoryIndex < it.size) {
                    displayStory(currentStoryIndex)
                } else {
                    when {
                        (shouldNavigateToQuestionActivity(codeScene)) -> {
                            navigateToQuestionActivity()}

                        shouldNavigateToResultActivity(codeScene) -> {
                            navigateToResultActivity(points, coins, avatarImage, avatarName, correctAnswers, wrongAnswers)
                        }

                        shouldNavigateToStageSatuActivity(codeScene) -> {
                            navigateToStageSatuActivity()
                        }

                        shouldNavigateToEpilog(codeScene) -> {
                            navigateToEpilog(points, coins, avatarImage, avatarName, correctAnswers, wrongAnswers)
                        }
                        else -> {
                            navigateToCourseActivity()
                        }
                    }
                }
            }
        }

        tvSkip.setOnClickListener {
            when {
                (shouldNavigateToQuestionActivity(codeScene)) -> {
                    navigateToQuestionActivity()}

                shouldNavigateToResultActivity(codeScene) -> {
                    navigateToResultActivity(points, coins, avatarImage, avatarName, correctAnswers, wrongAnswers)
                }

                shouldNavigateToStageSatuActivity(codeScene) -> {
                    navigateToStageSatuActivity()
                }

                shouldNavigateToEpilog(codeScene) -> {
                    navigateToEpilog(points, coins, avatarImage, avatarName, correctAnswers, wrongAnswers)
                }
                else -> {
                    navigateToCourseActivity()
                }
            }
        }
    }

    private fun fetchStorytellingContent(codeScene: String?) {
        val apiService = ApiConfig.getApiService()
        val call = codeScene?.let { apiService.getStorytelling(it) }
        if (call != null) {
            call.enqueue(object : Callback<List<Storytelling>> {
                override fun onResponse(call: Call<List<Storytelling>>, response: Response<List<Storytelling>>) {
                    if (response.isSuccessful) {
                        storytellingList = response.body()
                        if (storytellingList != null && storytellingList!!.isNotEmpty()) {
                            displayStory(currentStoryIndex)
                        } else {
                            Toast.makeText(this@StorytellingActivity, "Storytelling not found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@StorytellingActivity, "Failed to fetch storytelling", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Storytelling>>, t: Throwable) {
                    Toast.makeText(this@StorytellingActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun displayStory(index: Int) {
        val storytelling = storytellingList?.get(index)
        tvStoryText.text = storytelling?.text

        if (storytelling != null) {
            Glide.with(this)
                .load(storytelling.bgImage)
                .apply(RequestOptions().dontTransform())
                .into(ivBg)
        }

        storytelling?.namaAvatar?.let { avatar ->
            tvAvatarName.text = storytelling.namaAvatar
            tvAvatarName.visibility = View.VISIBLE

            if (storytelling.imageAvatar != null && storytelling.imageAvatar.isNotEmpty()) {
                ivAvatarImage.visibility = View.VISIBLE
                Glide.with(this)
                    .load(storytelling.imageAvatar)
                    .apply(RequestOptions().dontTransform())
                    .into(ivAvatarImage)
            } else {
                ivAvatarImage.visibility = View.GONE
            }
        } ?: run {
            tvAvatarName.visibility = View.GONE
            ivAvatarImage.visibility = View.GONE
        }
    }

    private fun shouldNavigateToQuestionActivity(codeScene: String?): Boolean {
        return codeScene in listOf("b1", "b2", "b3", "b4", "b5", "b6", "b7")
    }

    private fun shouldNavigateToResultActivity(codeScene: String?): Boolean {
        return codeScene in listOf("b11", "b22", "b33", "b44", "b55", "b66", "epilog")
    }

    private fun shouldNavigateToStageSatuActivity(codeScene: String?): Boolean {
        return codeScene == "prolog"
    }

    private fun shouldNavigateToEpilog(codeScene: String?): Boolean {
        return codeScene == "b77"
    }

    private fun navigateToStageSatuActivity() {
        val intent = Intent(this, StageSatuActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToCourseActivity() {
        val intent = Intent(this, CourseActivity::class.java)
        intent.putExtra("materi_id", materiId)
        startActivity(intent)
        finish()
    }

    private fun navigateToQuestionActivity() {
        val intent = when (codeScene) {
            "b1" -> Intent(this, BossSatuActivity::class.java)

            "b2" -> Intent(this, BossDuaActivity::class.java)

            "b3" -> Intent(this, BossTigaActivity::class.java)

            "b4" -> Intent(this, BossEmpatActivity::class.java)

            "b5" -> Intent(this, BossLimaActivity::class.java)

            "b6" -> Intent(this, BossEnamActivity::class.java)

            "b7" -> Intent(this, BossTujuhActivity::class.java)


            else -> Intent(this, StageSatuActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

    private fun navigateToEpilog(points: Int, coins: Int, avatarImage: String, avatarName: String, correctAnswersCount: Int, wrongAnswersCount: Int) {
        val intent = Intent(this, StorytellingActivity::class.java)
        intent.putExtra("codeScene", "epilog")
        intent.putExtra("points", points)
        intent.putExtra("coins", coins)
        intent.putExtra("avatarImage", avatarImage)
        intent.putExtra("avatarName", avatarName)
        intent.putExtra("correctAnswers", correctAnswersCount)
        intent.putExtra("wrongAnswers", wrongAnswersCount)

        Log.d("ResultActivity", "User to be Intent:${codeScene}")
        startActivity(intent)
        finish()
    }

    private fun navigateToResultActivity(points: Int, coins: Int, avatarImage: String, avatarName: String, correctAnswersCount: Int, wrongAnswersCount: Int) {
        val intent = Intent(this, ResultBossActivity::class.java)
        intent.putExtra("points", points)
        intent.putExtra("coins", coins)
        intent.putExtra("codeScene", codeScene)
        intent.putExtra("avatarImage", avatarImage)
        intent.putExtra("avatarName", avatarName)
        intent.putExtra("correctAnswers", correctAnswersCount)
        intent.putExtra("wrongAnswers", wrongAnswersCount)
        Log.d("ResultActivity", "User to be Intent: ${avatarImage}, ${avatarName}, ${codeScene}")
        startActivity(intent)
        finish()
    }
}