package com.dicoding.testcodequest.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.testcodequest.R

class PasswordEditText : AppCompatEditText {

    private lateinit var passwordIconDrawable: Drawable
    private lateinit var eyeIconOpen: Drawable
    private lateinit var eyeIconClosed: Drawable
    private var isPasswordVisible = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        transformationMethod = if (isPasswordVisible) null else PasswordTransformationMethod.getInstance()
    }

    private fun init() {
        passwordIconDrawable = ContextCompat.getDrawable(context, R.drawable.icons8_lock_2) as Drawable
        eyeIconOpen = ContextCompat.getDrawable(context, R.drawable.ic_eye_open) as Drawable
        eyeIconClosed = ContextCompat.getDrawable(context, R.drawable.ic_eye_close) as Drawable

        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        compoundDrawablePadding = 16

        setHint(R.string.password)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setAutofillHints(View.AUTOFILL_HINT_PASSWORD)
        }
        setCompoundDrawablesRelativeWithIntrinsicBounds(passwordIconDrawable, null, eyeIconClosed, null)

        setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP &&
                event.rawX >= (right - compoundDrawables[2].bounds.width())
            ) {
                togglePasswordVisibility()
                true
            } else {
                false
            }
        }

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (!s.isNullOrEmpty() && s.length < 8) {
//                    error = context.getString(R.string.et_password_error_message)
//                } else {
//                    error = null
//                }
            }
        })
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        transformationMethod = if (isPasswordVisible) null else PasswordTransformationMethod.getInstance()
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            passwordIconDrawable,
            null,
            if (isPasswordVisible) eyeIconOpen else eyeIconClosed,
            null
        )
    }
}
