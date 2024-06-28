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

    private var storytellingList: List<Storytelling>? = null
    private var currentStoryIndex: Int = 0
    private var materiId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storytelling)

        layoutStory = findViewById(R.id.layoutStory)
        tvStoryText = findViewById(R.id.tvStoryText)
        tvAvatarName = findViewById(R.id.tvAvatarName)
        ivAvatarImage = findViewById(R.id.ivAvatarImage)
        ivBg = findViewById(R.id.ivBg)

        // Get codeScene and materi_id from intent
        val codeScene = intent.getStringExtra("codeScene") ?: "default_scene"
        materiId = intent.getIntExtra("materi_id", 0)

        // Calling the Retrofit endpoint to get storytelling with the specified code scene
        val apiService = ApiConfig.getApiService()
        val call = apiService.getStorytelling(codeScene)
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

        layoutStory.setOnClickListener {
            storytellingList?.let {
                currentStoryIndex++
                if (currentStoryIndex < it.size) {
                    displayStory(currentStoryIndex)
                } else {
                    // All dialogs displayed, go to CourseActivity
                    val intent = Intent(this, CourseActivity::class.java)
                    intent.putExtra("materi_id", materiId)
                    startActivity(intent)
                    finish()  // Optional: Call finish() to close the current activity
                }
            }
        }
    }

    private fun displayStory(index: Int) {
        val storytelling = storytellingList?.get(index)
        tvStoryText.text = storytelling?.text

        if (storytelling != null) {
            Glide.with(this)
                .load(storytelling.bgImage)
                .apply(RequestOptions().dontTransform())  // Ensure no transformation that might affect transparency
                .into(ivBg)
        }

        // Check if avatar is not null before trying to access its properties
        storytelling?.namaAvatar?.let { avatar ->
            tvAvatarName.text = storytelling.namaAvatar
            tvAvatarName.visibility = View.VISIBLE

            // Using Glide to display the avatar image
            if (storytelling.imageAvatar != null && storytelling.imageAvatar.isNotEmpty()) {
                ivAvatarImage.visibility = View.VISIBLE
                Glide.with(this)
                    .load(storytelling.imageAvatar)
                    .apply(RequestOptions().dontTransform())  // Ensure no transformation that might affect transparency
                    .into(ivAvatarImage)
            } else {
                ivAvatarImage.visibility = View.GONE
            }
        } ?: run {
            tvAvatarName.visibility = View.GONE
            ivAvatarImage.visibility = View.GONE
        }
    }
}