package com.dicoding.testcodequest.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
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
import com.dicoding.testcodequest.ui.StageSatu.QuestionActivity
import com.dicoding.testcodequest.ui.StageSatu.StageSatuActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storytelling)

        val btnBack: ImageView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            startActivity(Intent(this, StageSatuActivity::class.java))
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

        // Fetch storytelling content
        fetchStorytellingContent(codeScene)

        layoutStory.setOnClickListener {
            storytellingList?.let {
                currentStoryIndex++
                if (currentStoryIndex < it.size) {
                    displayStory(currentStoryIndex)
                } else {
                    if (shouldNavigateToQuestionActivity(codeScene)) {
                        navigateToQuestionActivity()
                    } else {
                        navigateToCourseActivity()
                    }
                }
            }
        }

        tvSkip.setOnClickListener {
            if (shouldNavigateToQuestionActivity(codeScene)) {
                navigateToQuestionActivity()
            } else {
                navigateToCourseActivity()
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

    private fun navigateToCourseActivity() {
        val intent = Intent(this, CourseActivity::class.java)
        intent.putExtra("materi_id", materiId)
        startActivity(intent)
        finish()
    }

    private fun navigateToQuestionActivity() {
        val intent = Intent(this, QuestionActivity::class.java)
        startActivity(intent)
        finish()
    }
}