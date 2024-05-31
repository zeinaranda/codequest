package com.dicoding.testcodequest.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.R.string.et_name_error_message

class NameEditText : AppCompatEditText {

    private lateinit var nameIconDrawable: Drawable

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

    private fun init() {
        nameIconDrawable =
            ContextCompat.getDrawable(context, R.drawable.ant_design_user_outlinedname) as Drawable
        inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        compoundDrawablePadding = 16

        setHint(R.string.nama)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setAutofillHints(AUTOFILL_HINT_USERNAME)
        }
        setDrawable(nameIconDrawable)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty())
                    error = context.getString(et_name_error_message)
            }
        })
    }

    private fun setDrawable(
        start: Drawable? = null,
        top: Drawable? = null,
        end: Drawable? = null,
        bottom: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom)
    }
}