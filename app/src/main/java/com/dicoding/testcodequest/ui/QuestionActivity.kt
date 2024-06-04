package com.dicoding.testcodequest.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.preference.AuthPreference
import com.dicoding.testcodequest.data.response.BossDone
import com.dicoding.testcodequest.data.response.Question
import com.dicoding.testcodequest.data.retrofit.ApiConfig
import com.dicoding.testcodequest.data.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionActivity : AppCompatActivity() {
    private lateinit var questionText: TextView
    private lateinit var timerText: TextView
    private lateinit var option1: Button
    private lateinit var option2: Button
    private lateinit var option3: Button
    private lateinit var option4: Button
    private lateinit var preference: AuthPreference
    private var userId: Int? = null

    private val handler = Handler(Looper.getMainLooper())
    private var call: Call<List<Question>>? = null

    private var questions = mutableListOf<Question>()
    private var currentQuestionIndex = 0
    private var stageTimer: CountDownTimer? = null

    private var points = 0
    private var coins = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        preference = AuthPreference(this)
        userId = preference.getId()
        Log.d("QuestionActivity", "User ID from Preference: $userId") // Cek userId dari preference

        questionText = findViewById(R.id.questionText)
        timerText = findViewById(R.id.timerText)
        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)

        option1.setOnClickListener { checkAnswer(option1) }
        option2.setOnClickListener { checkAnswer(option2) }
        option3.setOnClickListener { checkAnswer(option3) }
        option4.setOnClickListener { checkAnswer(option4) }

        // Load GIF 1 on start
        loadGif("url_gif_1")

        fetchQuestions()
        startStageTimer()
    }

    private fun fetchQuestions() {
        ApiConfig.getApiService().getQuestions(1).enqueue(object : Callback<List<Question>> {
            override fun onResponse(call: Call<List<Question>>, response: Response<List<Question>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        questions.addAll(it)
                        showQuestion()
                    }
                } else {
                    Toast.makeText(this@QuestionActivity, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Question>>, t: Throwable) {
                Toast.makeText(this@QuestionActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showQuestion() {
        if (currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]
            questionText.text = question.content
            option1.text = question.choicesOne
            option2.text = question.choicesTwo
            option3.text = question.choicesThree
            option4.text = question.choicesFour
            resetButtonStyles()

            // Load GIF 1 for new question
            loadGif("https://mr4vffpk-3000.asse.devtunnels.ms/images/image_makima_waifu2x_photo_noise3_scale.gif")
        } else {
            Toast.makeText(this, "Quiz completed!", Toast.LENGTH_SHORT).show()
            finishQuiz()
        }
    }

    private fun checkAnswer(selectedOption: Button) {
        val correctAnswer = questions[currentQuestionIndex].correctAnswer
        if (selectedOption.text == correctAnswer) {
            selectedOption.background = ContextCompat.getDrawable(this, R.drawable.option_button_correct)
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
            points += 1000
            coins += 80

            // Load GIF 2 for correct answer
            loadGif("https://mr4vffpk-3000.asse.devtunnels.ms/images/image_makima bom.gif")
        } else {
            selectedOption.background = ContextCompat.getDrawable(this, R.drawable.option_button_wrong)
            Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            currentQuestionIndex++
            showQuestion()
        }, 2000) // Jeda 2 detik untuk umpan balik
    }

    private fun resetButtonStyles() {
        option1.background = ContextCompat.getDrawable(this, R.drawable.option_button_normal)
        option2.background = ContextCompat.getDrawable(this, R.drawable.option_button_normal)
        option3.background = ContextCompat.getDrawable(this, R.drawable.option_button_normal)
        option4.background = ContextCompat.getDrawable(this, R.drawable.option_button_normal)
    }

    private fun startStageTimer() {
        stageTimer?.cancel() // Batalkan timer sebelumnya jika ada

        stageTimer = object : CountDownTimer(600000, 1000) { // 10 menit untuk stage
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                timerText.text = String.format("Time Left: %02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                Toast.makeText(this@QuestionActivity, "Time's up!", Toast.LENGTH_SHORT).show()
                // Tindakan yang ingin dilakukan saat waktu habis
                finishQuiz()
            }
        }.start()
    }

    private fun finishQuiz() {
        updateBossDone()
        Toast.makeText(this, "Quiz Finished!", Toast.LENGTH_SHORT).show()

        // Load GIF 3 if points >= 640
        if (points >= 640) {
            loadGif("https://mr4vffpk-3000.asse.devtunnels.ms/images/image_makima die.gif")
        } else {
            loadGif("https://mr4vffpk-3000.asse.devtunnels.ms/images/image_makima_waifu2x_photo_noise3_scale.gif")
        }


        // Start ShopActivity after delay
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("points", points)
                putExtra("coins", coins)
            }
            startActivity(intent)
            finish()
        }, 1500)// Jeda 1,5 detik sebelum berpindah ke ShopActivity
    }

    private fun updateBossDone() {
        val userId = userId // Ganti dengan userId yang sesuai
        val bossId = 1 // Ganti dengan bossId yang sesuai
        val point = points
        val koin = coins

        // Menentukan nilai quizDone berdasarkan nilai points
        val quizDone = if (points >= 640) {
            true
        } else {
            false
        }

        val bossDone = userId?.let { BossDone(quizDone, point, koin, bossId, userId) }

        if (bossDone != null) {
            ApiConfig.getApiService().updateBossDone(bossDone).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@QuestionActivity, "Data updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@QuestionActivity, "Failed to update data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@QuestionActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stageTimer?.cancel() // Pastikan timer dibatalkan saat Activity dihancurkan
        handler.removeCallbacksAndMessages(null) // Batalkan semua callback dan pesan tertunda
        call?.cancel() // Batalkan Retrofit call jika belum selesai
    }

    private fun loadGif(url: String) {
        val gifView: ImageView = findViewById(R.id.gifView)
        Glide.with(this)
            .asGif()
            .load(url)
            .into(gifView)
    }
}
