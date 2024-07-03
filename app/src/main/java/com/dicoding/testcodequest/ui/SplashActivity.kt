package com.dicoding.testcodequest.ui

import android.content.Intent
import android.media.audiofx.DynamicsProcessing.Stage
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.common.Constant
import com.dicoding.testcodequest.data.preference.AuthPreference
import com.dicoding.testcodequest.databinding.ActivitySplashBinding
import com.dicoding.testcodequest.ui.StageSatu.QuestionActivity
import com.dicoding.testcodequest.ui.StageSatu.StageSatuActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var pref: AuthPreference
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        moveToMainActivity()
    }

    private fun moveToMainActivity() {
        pref = AuthPreference(this)
        Handler(Looper.getMainLooper()).postDelayed({
            if (pref.getStatusLogin()) {
                startActivity(Intent(this, StageSatuActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, Constant.SPLASH_TIME)
    }
}
