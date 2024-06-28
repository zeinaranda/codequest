package com.dicoding.testcodequest.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.dicoding.testcodequest.R

class CourseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)

        val textViewMateri: TextView = findViewById(R.id.textViewMateri)
        val buttonKnowledgeCheck: Button = findViewById(R.id.buttonKnowledgeCheck)

        val materiId = intent.getIntExtra("materi_id", 0)

        val materiText = when (materiId) {
            1 -> getString(R.string.materi1)
            2 -> getString(R.string.materi2)
            3 -> getString(R.string.materi3)
            else -> "Materi tidak ditemukan."
        }

        textViewMateri.text = materiText

        buttonKnowledgeCheck.setOnClickListener {
            val intent = Intent(this, KnowledgeActivity::class.java)
            intent.putExtra("materi_id", materiId)
            startActivity(intent)
        }
    }
}