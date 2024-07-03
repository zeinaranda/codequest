package com.dicoding.testcodequest.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.preference.AuthPreference
import com.dicoding.testcodequest.ui.StageSatu.StageSatuActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class CourseActivity : AppCompatActivity() {
    private lateinit var preference: AuthPreference
    private var userPoint: Int? = null
    private var userKoin: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)

        preference = AuthPreference(this)

        userPoint = preference.getPoints()
        userKoin = preference.getCoins()

        val pointsText = findViewById<TextView>(R.id.userPoint)
        pointsText.text = userPoint.toString()

        val coinsText = findViewById<TextView>(R.id.userCoin)
        coinsText.text = userKoin.toString()

        val btn_back : ImageView= findViewById(R.id.btn_back)
        btn_back.setOnClickListener {
            startActivity(Intent(this, StageSatuActivity::class.java))
            finish()
        }

        val textViewMateri: TextView = findViewById(R.id.textViewMateri)
        val buttonKnowledgeCheck: Button = findViewById(R.id.buttonKnowledgeCheck)

        val materiId = intent.getIntExtra("materi_id", 0)

        val materiText = when (materiId) {
            1 -> getString(R.string.materi1)
            2 -> getString(R.string.materi2)
            3 -> getString(R.string.materi3)
            4 -> getString(R.string.materi4)
            else -> "Materi tidak ditemukan."
        }

        textViewMateri.text = materiText

        buttonKnowledgeCheck.setOnClickListener {
            val intent = Intent(this, KnowledgeActivity::class.java)
            intent.putExtra("materi_id", materiId)
            startActivity(intent)
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
    }
}