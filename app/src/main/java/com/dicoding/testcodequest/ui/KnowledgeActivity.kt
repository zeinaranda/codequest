package com.dicoding.testcodequest.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.preference.AuthPreference
import com.dicoding.testcodequest.data.response.KnowledgeDone
import com.dicoding.testcodequest.data.response.Question
import com.dicoding.testcodequest.data.response.User
import com.dicoding.testcodequest.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KnowledgeActivity : AppCompatActivity() {
    private lateinit var questionText: TextView
    private lateinit var timerText: TextView
    private lateinit var option1: Button
    private lateinit var option2: Button
    private lateinit var option3: Button
    private lateinit var option4: Button
    private lateinit var preference: AuthPreference
    private lateinit var answerFeedback: TextView
    private var userId: Int? = null
    private var currentPoints: Int = 0
    private var currentCoins: Int = 0

    private val handler = Handler(Looper.getMainLooper())
    private var call: Call<List<Question>>? = null

    private var questions = mutableListOf<Question>()
    private var currentQuestionIndex = 0
    private var stageTimer: CountDownTimer? = null

    private var points = 0
    private var coins = 0

    private var knowledgeId: Int? = null // Variable to store knowledgeId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_knowledge)

        preference = AuthPreference(this)
        userId = preference.getId()
        Log.d("QuestionActivity", "User ID from Preference: $userId") // Cek userId dari preference

        questionText = findViewById(R.id.questionText)
        timerText = findViewById(R.id.timerText)
        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)
        answerFeedback = findViewById(R.id.answerFeedback)

        option1.setOnClickListener { checkAnswer(option1) }
        option2.setOnClickListener { checkAnswer(option2) }
        option3.setOnClickListener { checkAnswer(option3) }
        option4.setOnClickListener { checkAnswer(option4) }

        // Load GIF 1 on start
        answerFeedback.text = "What's your Answer?"
        answerFeedback.visibility = TextView.VISIBLE
        // Get materi_id from intent
        knowledgeId = intent.getIntExtra("materi_id", 1) // Default to 1 if not found
        Log.d("QuestionActivity", "Materi ID from Intent: $knowledgeId") // Cek materi_id dari intent

        fetchQuestions()
        startStageTimer()
    }

    private fun fetchQuestions() {
        ApiConfig.getApiService().getQuestionsByKnowledge(knowledgeId ?: 0).enqueue(object :
            Callback<List<Question>> {
            override fun onResponse(call: Call<List<Question>>, response: Response<List<Question>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        questions.addAll(it)
                        showQuestion()
                    }
                } else {
                    Toast.makeText(this@KnowledgeActivity, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Question>>, t: Throwable) {
                Toast.makeText(this@KnowledgeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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
            answerFeedback.text = "What's your Answer?"
            answerFeedback.visibility = TextView.VISIBLE
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
            answerFeedback.text = "Good!"
            answerFeedback.visibility = TextView.VISIBLE
        } else {
            selectedOption.background = ContextCompat.getDrawable(this, R.drawable.option_button_wrong)
            answerFeedback.text = ". . ."
            answerFeedback.visibility = TextView.VISIBLE
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
                Toast.makeText(this@KnowledgeActivity, "Time's up!", Toast.LENGTH_SHORT).show()
                // Tindakan yang ingin dilakukan saat waktu habis
                finishQuiz()
            }
        }.start()
    }

    private fun finishQuiz() {
        updateUserPointsAndCoins()

        updateKnowledgeDone()
        Toast.makeText(this, "Quiz Finished!", Toast.LENGTH_SHORT).show()

        // Load GIF 3 if points >= 640
        if (points >= 640) {
            answerFeedback.text = "Awesome"
            answerFeedback.visibility = TextView.VISIBLE
        } else {
            answerFeedback.text = "It's ok.. you can try it again"
            answerFeedback.visibility = TextView.VISIBLE        }

        // Start ShopActivity after delay
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("points", points)
                putExtra("coins", coins)
            }
            startActivity(intent)
            finish()
        }, 1500) // Jeda 1,5 detik sebelum berpindah ke ShopActivity
    }

    private fun updateKnowledgeDone() {
        val userId = userId // Ganti dengan userId yang sesuai
        val point = points
        val koin = coins

        // Menentukan nilai quizDone berdasarkan nilai points
        val quizDone = points >= 640

        val knowledgeDone = userId?.let { KnowledgeDone(quizDone, point, koin, knowledgeId ?: 1, it) }

        if (knowledgeDone != null) {
            ApiConfig.getApiService().updateKnowledgeDone(knowledgeDone).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@KnowledgeActivity, "Data updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@KnowledgeActivity, "Failed to update data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@KnowledgeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun updateUserPointsAndCoins() {
        userId?.let { id ->
            // Mengambil poin dan koin yang ada di preference sebagai nilai saat ini
            currentPoints = preference.getPoints()
            currentCoins = preference.getCoins()
            Log.d("QuestionActivity", "User ID from Preference: $userId, $currentCoins, $currentPoints") // Cek userId dari preference

            // Menambahkan poin dan koin yang diperoleh selama kuis ke nilai saat ini
            val newPoints = currentPoints + points
            val newCoins = currentCoins + coins

            val updatedUser = User(
                userId = id,
                point = newPoints,
                koin = newCoins,
            )

            ApiConfig.getApiService().updateUserPoints(id, updatedUser).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@KnowledgeActivity, "Points and coins updated successfully", Toast.LENGTH_SHORT).show()
                        // Update preferences dengan nilai terbaru
                        preference.setPoints(newPoints)
                        preference.setCoins(newCoins)
                    } else {
                        Toast.makeText(this@KnowledgeActivity, "Failed to update points and coins", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@KnowledgeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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


}