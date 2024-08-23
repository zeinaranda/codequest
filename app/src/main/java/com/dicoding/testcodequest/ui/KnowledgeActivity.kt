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
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.preference.AuthPreference
import com.dicoding.testcodequest.data.response.KnowledgeDone
import com.dicoding.testcodequest.data.response.ProgressPoint
import com.dicoding.testcodequest.data.response.Question
import com.dicoding.testcodequest.data.response.User
import com.dicoding.testcodequest.data.retrofit.ApiConfig
import com.dicoding.testcodequest.ui.StageDua.StageDuaActivity
import com.dicoding.testcodequest.ui.StageEmpat.StageEmpatActivity
import com.dicoding.testcodequest.ui.StageEnam.StageEnamActivity
import com.dicoding.testcodequest.ui.StageLima.StageLimaActivity
import com.dicoding.testcodequest.ui.StageSatu.StageSatuActivity
import com.dicoding.testcodequest.ui.StageTiga.StageTigaActivity
import com.dicoding.testcodequest.ui.StageTujuh.StageTujuhActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KnowledgeActivity : AppCompatActivity() {
    // Deklarasi variabel-variabel UI dan data yang akan digunakan dalam activity
    private var userPoint: Int? = null
    private var userKoin: Int? = null
    private lateinit var questionText: TextView
    private lateinit var timerText: TextView
    private lateinit var option1: Button
    private lateinit var option2: Button
    private lateinit var option3: Button
    private lateinit var option4: Button
    private lateinit var btn_back: ImageView
    private lateinit var preference: AuthPreference
    private lateinit var answerFeedback: TextView
    private var userId: Int? = null
    private var currentPoints: Int = 0
    private var currentCoins: Int = 0

    // Handler untuk mengelola operasi asynchronous
    private val handler = Handler(Looper.getMainLooper())
    private var call: Call<List<Question>>? = null

    // Daftar pertanyaan dan indeks pertanyaan saat ini
    private var questions = mutableListOf<Question>()
    private var currentQuestionIndex = 0
    private var stageTimer: CountDownTimer? = null

    // Variabel untuk menyimpan poin, koin, dan jumlah jawaban benar atau salah
    private var points = 0
    private var coins = 0
    private var correctAnswersCount = 0
    private var wrongAnswersCount = 0

    private var knowledgeId: Int? = null // Variable untuk menyimpan knowledgeId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_knowledge)

        val btn_back : ImageView= findViewById(R.id.btn_back)
        btn_back.setOnClickListener {
            val intent = when (knowledgeId) {
                1, 2 -> Intent(this, StageSatuActivity::class.java)
                3, 4 -> Intent(this, StageDuaActivity::class.java)
                5, 6 -> Intent(this, StageTigaActivity::class.java)
                7, 8 -> Intent(this, StageEmpatActivity::class.java)
                9, 10 -> Intent(this, StageLimaActivity::class.java)
                11, 12 -> Intent(this, StageEnamActivity::class.java)
                13, 14 -> Intent(this, StageTujuhActivity::class.java)
                else -> Intent(this, StageSatuActivity::class.java)
            }
            startActivity(intent)
            finish()
        }

        // Mengambil data dari preference
        preference = AuthPreference(this)
        userPoint = preference.getPoints()
        userKoin = preference.getCoins()
        userId = preference.getId()
        Log.d("QuestionActivity", "User ID from Preference: $userId") // Cek userId dari preference

        // Menginisialisasi komponen UI
        questionText = findViewById(R.id.questionText)
        timerText = findViewById(R.id.timerText)
        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)
        answerFeedback = findViewById(R.id.answerFeedback)

        // Mengatur listener untuk tombol-tombol jawaban
        option1.setOnClickListener { handleOptionClick(option1) }
        option2.setOnClickListener { handleOptionClick(option2) }
        option3.setOnClickListener { handleOptionClick(option3) }
        option4.setOnClickListener { handleOptionClick(option4) }

        // Load GIF 1 on start
        answerFeedback.text = "What's your Answer?"
        answerFeedback.visibility = TextView.VISIBLE
        // Get materi_id from intent
        knowledgeId = intent.getIntExtra("materi_id", 1) // Default to 1 if not found
        Log.d("QuestionActivity", "Materi ID from Intent: $knowledgeId") // Cek materi_id dari intent

        fetchQuestions()
        startStageTimer()
    }

    private fun handleOptionClick(selectedOption: Button) {
        // Menonaktifkan tombol untuk mencegah klik ganda
        disableButtons()

        // Memeriksa jawaban yang dipilih
        checkAnswer(selectedOption)

        // Mengaktifkan kembali tombol dan menampilkan pertanyaan berikutnya setelah umpan balik ditampilkan
        Handler(Looper.getMainLooper()).postDelayed({
            resetButtonStyles()
            enableButtons()

            // Lanjut ke pertanyaan berikutnya jika masih tersedia
            if (currentQuestionIndex < 3) {
                currentQuestionIndex++
                showQuestion()
            } else {
                finishQuiz()
                disableButtons()
            }
        }, 1300) // Jeda 1,3 detik untuk umpan balik
    }

    // Menonaktifkan semua tombol jawaban
    private fun disableButtons() {
        option1.isEnabled = false
        option2.isEnabled = false
        option3.isEnabled = false
        option4.isEnabled = false
    }

    // Mengaktifkan kembali semua tombol jawaban
    private fun enableButtons() {
        option1.isEnabled = true
        option2.isEnabled = true
        option3.isEnabled = true
        option4.isEnabled = true
    }

    private fun fetchQuestions() {
        // Mengambil daftar pertanyaan dari API
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
        // Menampilkan pertanyaan berdasarkan indeks saat ini
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
            finishQuiz() // Mengakhiri kuis jika tidak ada lagi pertanyaan
        }
    }

    private fun checkAnswer(selectedOption: Button) {
        // Memeriksa apakah jawaban yang dipilih benar atau salah
        val correctAnswer = questions[currentQuestionIndex].correctAnswer
        if (selectedOption.text == correctAnswer) {
            selectedOption.background = ContextCompat.getDrawable(this, R.drawable.option_button_correct)
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
            points += 40
            coins += 80
            correctAnswersCount++

            // Load GIF 2 for correct answer
            answerFeedback.text = "Good!"
            answerFeedback.visibility = TextView.VISIBLE
        } else {
            selectedOption.background = ContextCompat.getDrawable(this, R.drawable.option_button_wrong)
            answerFeedback.text = ". . ."
            answerFeedback.visibility = TextView.VISIBLE
            Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show()
            wrongAnswersCount++
        }

    }

    private fun resetButtonStyles() {
        // Mengembalikan gaya tombol ke keadaan semula
        option1.background = ContextCompat.getDrawable(this, R.drawable.option_button_normal)
        option2.background = ContextCompat.getDrawable(this, R.drawable.option_button_normal)
        option3.background = ContextCompat.getDrawable(this, R.drawable.option_button_normal)
        option4.background = ContextCompat.getDrawable(this, R.drawable.option_button_normal)
    }

    private fun startStageTimer() {
        stageTimer?.cancel() // Batalkan timer sebelumnya jika ada

        stageTimer = object : CountDownTimer(300000, 1000) { // 10 menit untuk stage
            override fun onTick(millisUntilFinished: Long) {
                // Mengupdate tampilan timer setiap detik
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                timerText.text = String.format("Time Left: %02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                Toast.makeText(this@KnowledgeActivity, "Time's up!", Toast.LENGTH_SHORT).show()
                if (points == 0) {
                    navigateToResultActivity() // Langsung ke ResultActivity jika poin adalah 0
                } else {
                    finishQuiz() // Proses normal jika poin lebih dari 0
                }
            }

        }.start()
    }

    private fun finishQuiz() {
        // Mengupdate poin dan koin pengguna, serta menyelesaikan kuis
        updateUserPointsAndCoins()

        updateKnowledgeDone()
        Toast.makeText(this, "Quiz Finished!", Toast.LENGTH_SHORT).show()


        if (points >= 160) {
            answerFeedback.text = "Awesome"
            answerFeedback.visibility = TextView.VISIBLE
        } else {
            answerFeedback.text = "It's ok.. you can try it again"
            answerFeedback.visibility = TextView.VISIBLE        }

        // Berpindah ke Resulr Knowledge Activity setelah delay
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ResultKnowledgeActivity::class.java).apply {
                putExtra("points", points)
                putExtra("coins", coins)
                putExtra("knowledgeId", knowledgeId)
                putExtra("correctAnswers", correctAnswersCount)
                putExtra("wrongAnswers", wrongAnswersCount)
            }
            startActivity(intent)
            finish()
        }, 1500) // Jeda 1,5 detik sebelum berpindah ke ShopActivity
    }

    private fun updateKnowledgeDone() {
        // Memperbarui status KnowledgeDone di backend
        val userId = userId
        val point = points
        val koin = coins

        // Menentukan nilai quizDone berdasarkan nilai points
        val quizDone = points >= 40

        val knowledgeDone = userId?.let { KnowledgeDone(quizDone, point, koin, knowledgeId ?: 1, it) }

        if (knowledgeDone != null) {
            ApiConfig.getApiService().updateKnowledgeDone(knowledgeDone).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        knowledgeId?.let { updateProgress(userId, it, point) }

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
        // Memperbarui poin dan koin pengguna ke API
        userId?.let { id ->
            // Fetch the current user data
            ApiConfig.getApiService().getUserById(id).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        if (user != null) {
                            // Add points and coins to the current values
                            val newPoints = user.point?.plus(points)
                            val newCoins = user.koin?.plus(coins)

                            val updatedUser = User(
                                userId = id,
                                point = newPoints,
                                koin = newCoins,
                                ownedAvatars = user.ownedAvatars // Preserve ownedAvatars
                            )

                            // Update the user data on the server
                            ApiConfig.getApiService().updateUserPoints(id, updatedUser).enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(this@KnowledgeActivity, "Points and coins updated successfully", Toast.LENGTH_SHORT).show()
                                        // Update preferences with new values
                                        if (newPoints != null) {
                                            preference.setPoints(newPoints)
                                        }
                                        if (newCoins != null) {
                                            preference.setCoins(newCoins)
                                        }
                                    } else {
                                        Toast.makeText(this@KnowledgeActivity, "Failed to update points and coins", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    Toast.makeText(this@KnowledgeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    } else {
                        Toast.makeText(this@KnowledgeActivity, "Failed to retrieve user data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@KnowledgeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun updateProgress(userId: Int, knowledgeId: Int, progressPoint: Int) {
        //post userId, knowledgeId, dan progress point ke userstage
        val stageId = when (knowledgeId) {
            1, 2 -> 1
            3, 4 -> 2
            5, 6 -> 3
            7, 8 -> 4
            9, 10 -> 5
            11, 12 -> 6
            13, 14 -> 7
            else -> 1 // Default ke stage 1 jika knowledgeId tidak sesuai
        }
        // Menentukan course based on knowledgeId
        val course = if (knowledgeId % 2 == 0) "Course 2" else "Course 1"
        Log.d("KnowledgeActivity", "Course: $course") // Log the course

        val progress = ProgressPoint(progressPoint, course)
        val call = ApiConfig.getApiService().updateUserStageProgress(userId, stageId, progress)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("KnowledgeActivity", "Progress updated successfully")
                } else {
                    Log.e("KnowledgeActivity", "Failed to update progress")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("KnowledgeActivity", "Error: ${t.message}", t)
            }
        })
    }



    override fun onDestroy() {
        super.onDestroy()
        stageTimer?.cancel() // Pastikan timer dibatalkan saat Activity dihancurkan
        handler.removeCallbacksAndMessages(null) // Batalkan semua callback dan pesan tertunda
        call?.cancel() // Batalkan Retrofit call jika belum selesai
    }

    private fun navigateToResultActivity() {
        val intent = Intent(this, ResultKnowledgeActivity::class.java).apply {
            putExtra("points", points)
            putExtra("coins", coins)
            putExtra("knowledgeId", knowledgeId)
            putExtra("correctAnswers", correctAnswersCount)
            putExtra("wrongAnswers", wrongAnswersCount)
        }
        startActivity(intent)
        finish()
    }
}