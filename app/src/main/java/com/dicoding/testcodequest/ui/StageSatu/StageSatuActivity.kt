package com.dicoding.testcodequest.ui.StageSatu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.ui.CourseActivity
import com.dicoding.testcodequest.ui.StorytellingActivity

class StageSatuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stage_satu)

        val courseSatu: ImageView = findViewById(R.id.courseSatu)
        val courseDua: ImageView = findViewById(R.id.courseDua)
        val bossSatu: ImageView = findViewById(R.id.bossSatu)

        courseSatu.setOnClickListener {
            val intent = Intent(this, StorytellingActivity::class.java)
            intent.putExtra("codeScene", "1")
            intent.putExtra("materi_id", 1)
            startActivity(intent)
        }

        courseDua.setOnClickListener {
            val intent = Intent(this, CourseActivity::class.java)
            intent.putExtra("materi_id", 2)
            startActivity(intent)
        }

        bossSatu.setOnClickListener {
            val intent = Intent(this, StorytellingActivity::class.java)
            intent.putExtra("codeScene", "b1")
            startActivity(intent)
        }
    }
}