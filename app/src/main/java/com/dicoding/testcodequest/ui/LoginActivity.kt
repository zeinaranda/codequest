package com.dicoding.testcodequest.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.preference.AuthPreference
import com.dicoding.testcodequest.ui.LoginViewModelFactory
import com.dicoding.testcodequest.databinding.ActivityLoginBinding
import androidx.lifecycle.ViewModelProvider
import com.dicoding.testcodequest.data.response.User
import com.dicoding.testcodequest.viewmodel.ShopViewModel
import com.dicoding.testcodequest.viewmodel.ShopViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var preferences: AuthPreference
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = AuthPreference(this)
        viewModel = ViewModelProvider(this, LoginViewModelFactory.getInstance(this)).get(LoginViewModel::class.java)

        initAction()

        binding.tvToRegister.setOnClickListener {
            val myIntent = Intent(this, RegisterActivity::class.java)
            startActivity(myIntent)
            finish()
        }
    }

    private fun initAction() {
        binding.btnLogin.setOnClickListener {
            handleLogin()
        }
    }

    private fun handleLogin() {
        val nim = binding.etNim.text.toString()
        val password = binding.etPass.text.toString()

        Log.d("LoginActivity", "Attempting login with NIM: $nim")

        viewModel.getLogin(nim, password).observe(this) { response ->
            when (response) {
                is com.dicoding.testcodequest.data.Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d("LoginActivity", "Loading login request...")
                }
                is com.dicoding.testcodequest.data.Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val loginResult = response.data
                    Log.d("LoginActivity", "Login response: ${loginResult}")

                    val user = User(
                        nama = loginResult.nama,
                        nim = loginResult.nim,
                        userId = loginResult.userId,
                        token = loginResult.token
                    )
                    preferences.setUser(user)
                    preferences.setStatusLogin(true)
                    Log.d("LoginActivity", "Login successful for User ID: ${user.userId}")
                    Toast.makeText(applicationContext, "Login Berhasil", Toast.LENGTH_SHORT).show()

                    // Navigate to QuestionActivity
                    val intent = Intent(this@LoginActivity, QuestionActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is com.dicoding.testcodequest.data.Result.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Log.e("LoginActivity", "Login failed: ${response.error}")
                    Toast.makeText(this, "Login Gagal", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}