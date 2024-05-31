package com.dicoding.testcodequest.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, RegisterViewModelFactory.getInstance(this)).get(
            RegisterViewModel::class.java
        )

        initAction()

        binding.tvToLogin.setOnClickListener {
            val myIntent = Intent(this, LoginActivity::class.java)
            startActivity(myIntent)
        }
    }

    fun initAction() {
        binding.btnRegister.setOnClickListener {
            register()
        }
    }

    fun register() {

        viewModel.getRegist(
            name = binding.etNama.text.toString(),
            nim = binding.etNim.text.toString(),
            password = binding.etPass.text.toString()
        ).observe(this, { response ->
            if (response != null) {
                when (response) {
                    is com.dicoding.testcodequest.data.Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is com.dicoding.testcodequest.data.Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val myIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(myIntent)
                        Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show()
                    }
                    is com.dicoding.testcodequest.data.Result.Failure -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "Register Gagal", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}