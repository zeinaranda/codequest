package com.dicoding.testcodequest.utils

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

import com.bumptech.glide.Glide


fun ImageView.setImageUrl(url: String) =
    Glide.with(this)
        .load(url)
        .into(this)

fun ImageView.setImageDrawable(drawable: Int) =
    Glide.with(this)
        .load(drawable)
        .into(this)

// get edittext text toString
fun EditText.getTextTrim() =
    this.text.toString().trim()

// show toast
fun Fragment.quickShowToast(msg: String) =
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()

