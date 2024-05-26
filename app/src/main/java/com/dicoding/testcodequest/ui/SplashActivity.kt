package com.dicoding.testcodequest.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.common.Constant
import com.dicoding.testcodequest.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        moveToMainActivity()
    }

    private fun moveToMainActivity() {

                startActivity(Intent(this, MainActivity::class.java))
        Constant.SPLASH_TIME
    }
}