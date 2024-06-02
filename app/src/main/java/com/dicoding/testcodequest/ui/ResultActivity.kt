package com.dicoding.testcodequest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.dicoding.testcodequest.R

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Get values from intent
        val points = intent.getIntExtra("points", 0)
        val coins = intent.getIntExtra("coins", 0)

        // Update UI with received values
        val resultText = findViewById<TextView>(R.id.resultText)
        resultText.text = if (points >= 640) "Selamat" else "Coba Lagi"

        val pointsText = findViewById<TextView>(R.id.pointsText)
        pointsText.text = "Points: $points"

        val coinsText = findViewById<TextView>(R.id.coinsText)
        coinsText.text = "Coins: $coins"


    }
}