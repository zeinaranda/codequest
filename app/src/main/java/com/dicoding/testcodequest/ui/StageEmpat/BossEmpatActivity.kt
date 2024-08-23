package com.dicoding.testcodequest.ui.StageEmpat

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
import com.dicoding.testcodequest.data.response.ProgressPoint
import com.dicoding.testcodequest.data.response.Question
import com.dicoding.testcodequest.data.response.User
import com.dicoding.testcodequest.data.retrofit.ApiConfig
import com.dicoding.testcodequest.ui.ResultActivity
import com.dicoding.testcodequest.ui.StageSatu.StageSatuActivity
import com.dicoding.testcodequest.ui.StorytellingActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BossEmpatActivity : AppCompatActivity() {
    // Deklarasi variabel-variabel UI dan data yang akan digunakan dalam activity
    private lateinit var questionText: TextView
    private lateinit var timerText: TextView
    private lateinit var questionCounter: TextView
    private lateinit var option1: Button
    private lateinit var option2: Button
    private lateinit var option3: Button
    private lateinit var option4: Button
    private lateinit var preference: AuthPreference
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boss_empat)

        // Mengatur tombol kembali untuk menuju ke StageActivity
        val btn_back : ImageView = findViewById(R.id.btn_back)
        btn_back.setOnClickListener {
            startActivity(Intent(this, StageEmpatActivity::class.java))
            finish()
        }

        // Mengambil userId dari preference
        preference = AuthPreference(this)
        userId = preference.getId()
        Log.d("QuestionActivity", "User ID from Preference: $userId") // Cek userId dari preference

        // Menginisialisasi komponen UI
        questionText = findViewById(R.id.questionText)
        timerText = findViewById(R.id.timerText)
        questionCounter = findViewById(R.id.questionCounter)
        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)

        // Mengatur listener untuk tombol-tombol jawaban
        option1.setOnClickListener { handleOptionClick(option1) }
        option2.setOnClickListener { handleOptionClick(option2) }
        option3.setOnClickListener { handleOptionClick(option3) }
        option4.setOnClickListener { handleOptionClick(option4) }

        // Memuat GIF awal
        loadGif("url_gif_1")

        // Memulai proses mengambil pertanyaan dan timer stage
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
            if (currentQuestionIndex < 24) {
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

    // Mengambil daftar pertanyaan dari API
    private fun fetchQuestions() {
        ApiConfig.getApiService().getQuestions(4).enqueue(object : Callback<List<Question>> {
            override fun onResponse(call: Call<List<Question>>, response: Response<List<Question>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        questions.addAll(it)
                        showQuestion()
                    }
                } else {
                    Toast.makeText(this@BossEmpatActivity, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Question>>, t: Throwable) {
                Toast.makeText(this@BossEmpatActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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

            // Mengupdate counter pertanyaan
            questionCounter.text = "Soal ke- ${currentQuestionIndex + 1}/25"

            // Memuat GIF untuk setiap pertanyaan baru
            loadGif("https://mr4vffpk-3000.asse.devtunnels.ms/images/image_nagi stand.gif")
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
            points += 80
            coins += 100
            correctAnswersCount++

            // Memuat GIF untuk jawaban benar
            loadGif("https://mr4vffpk-3000.asse.devtunnels.ms/images/image_nagi goal.gif")
        } else {
            selectedOption.background = ContextCompat.getDrawable(this, R.drawable.option_button_wrong)
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

        stageTimer = object : CountDownTimer(3600000, 1000) { // 60 menit untuk stage
            override fun onTick(millisUntilFinished: Long) {
                // Mengupdate tampilan timer setiap detik
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                timerText.text = String.format("Time Left: %02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                // Timer selesai, tampilkan pesan dan arahkan pengguna ke ResultActivity atau akhiri kuis
                Toast.makeText(this@BossEmpatActivity, "Time's up!", Toast.LENGTH_SHORT).show()
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
        updateBossDone()
        Toast.makeText(this, "Quiz Finished!", Toast.LENGTH_SHORT).show()

        var avatarImage = "https://mr4vffpk-3000.asse.devtunnels.ms/images/default_avatar.png"
        var avatarName = "Default Avatar"
        // Load GIF 3
        if (points >= 1120) {
            loadGif("https://mr4vffpk-3000.asse.devtunnels.ms/images/image_nagi end.gif")

            avatarImage = "https://mr4vffpk-3000.asse.devtunnels.ms/images/image_nagi.png"
            avatarName = "Avatar Nagi"

            // Berpindah ke StorytellingActivity dengan jeda 2 detik jika poin mencukupi
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, StorytellingActivity::class.java).apply {
                    putExtra("points", points)
                    putExtra("coins", coins)
                    putExtra("codeScene", "b44")
                    putExtra("avatarImage", avatarImage)  // replace with actual URL or value
                    putExtra("avatarName", avatarName)
                    putExtra("correctAnswers", correctAnswersCount)
                    putExtra("wrongAnswers", wrongAnswersCount)

                }
                Log.d("BossEmpatActivity", "Intent to be sent: avatarImage=$avatarImage, avatarName=$avatarName, codeScene=b44")
                startActivity(intent)
                finish()
            }, 2000)
        } else {
            // Jika poin tidak mencukupi, menuju ResultActivity
            loadGif("https://mr4vffpk-3000.asse.devtunnels.ms/images/image_nagi stand.gif")
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, ResultActivity::class.java).apply {
                    putExtra("points", points)
                    putExtra("coins", coins)
                    putExtra("correctAnswers", correctAnswersCount)
                    putExtra("wrongAnswers", wrongAnswersCount)
                }
                startActivity(intent)
                finish()
            }, 1500)
        }

    }


    private fun updateBossDone() {
        // Memperbarui status BossDone di backend
        val userId = userId
        val bossId = 4
        val point = points
        val koin = coins

        // Menentukan nilai quizDone berdasarkan nilai points
        val quizDone = if (points >= 1120) {
            true
        } else {
            false
        }

        val bossDone = userId?.let { BossDone(quizDone, point, koin, bossId, userId) }

        if (bossDone != null) {
            ApiConfig.getApiService().updateBossDone(bossDone).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        updateProgress(userId, bossId, point)
                        Toast.makeText(this@BossEmpatActivity, "Data updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@BossEmpatActivity, "Failed to update data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@BossEmpatActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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

                            val updatedAvatars = user.ownedAvatars?.toMutableList()?.apply {
                                if (points >= 1120 && !contains(21)) {
                                    add(21)
                                }
                            }

                            val updatedUser = User(
                                userId = id,
                                point = newPoints,
                                koin = newCoins,
                                ownedAvatars = updatedAvatars
                            )

                            // Update the user data on the server
                            ApiConfig.getApiService().updateUserPoints(id, updatedUser).enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(this@BossEmpatActivity, "Points and coins updated successfully", Toast.LENGTH_SHORT).show()
                                        // Update preferences with new values
                                        if (newPoints != null) {
                                            preference.setPoints(newPoints)
                                        }
                                        if (newCoins != null) {
                                            preference.setCoins(newCoins)
                                        }
                                    } else {
                                        Toast.makeText(this@BossEmpatActivity, "Failed to update points and coins", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    Toast.makeText(this@BossEmpatActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    } else {
                        Toast.makeText(this@BossEmpatActivity, "Failed to retrieve user data", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@BossEmpatActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun updateProgress(userId: Int, stageId: Int, progressPoint: Int) {
        //post userId, stageId, dan progress point ke userstage
        val progress = ProgressPoint(progressPoint, "Boss Fight")
        val call = ApiConfig.getApiService().updateUserStageProgress(userId, stageId, progress)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("QuestionActivity", "Progress updated successfully")
                } else {
                    Log.e("QuestionActivity", "Failed to update progress")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("QuestionActivity", "Error: ${t.message}", t)
            }
        })
    }

    private fun navigateToResultActivity() {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("points", points)
            putExtra("coins", coins)
            putExtra("codeScene", "b4")
            putExtra("correctAnswers", correctAnswersCount)
            putExtra("wrongAnswers", wrongAnswersCount)
        }
        startActivity(intent)
        finish()
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