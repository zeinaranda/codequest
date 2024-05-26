package com.dicoding.testcodequest.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat.generateViewId
import com.dicoding.testcodequest.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonContainer: ConstraintLayout = findViewById(R.id.buttonContainer)
        val stages = listOf("Stage 1", "Stage 2", "Stage 3", "Stage 4", "Stage 5")
        val buttonIds = mutableListOf<Int>()

        stages.forEachIndexed { index, stage ->
            val button = Button(this).apply {
                id = generateViewId()
                text = stage
                setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.purple_200))
                setTextColor(Color.WHITE)
                textSize = 14f
                setOnClickListener {
                    Toast.makeText(this@MainActivity, "$stage Selected", Toast.LENGTH_SHORT).show()
                }
                val size = 200
                layoutParams = ConstraintLayout.LayoutParams(size, size).apply {
                    setMargins(16, 16, 16, 16)
                }
                background = ContextCompat.getDrawable(this@MainActivity, R.drawable.round_button)
            }
            buttonIds.add(button.id)
            buttonContainer.addView(button)
        }

        val constraintSet = ConstraintSet()
        constraintSet.clone(buttonContainer)

        buttonIds.forEachIndexed { index, id ->
            val horizontalBias = if (index % 2 == 0) 0.3f else 0.7f
            val verticalBias = 0.1f + (index * 0.2f)
            constraintSet.constrainWidth(id, ConstraintSet.WRAP_CONTENT)
            constraintSet.constrainHeight(id, ConstraintSet.WRAP_CONTENT)
            constraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            constraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            constraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            constraintSet.setHorizontalBias(id, horizontalBias)
            constraintSet.setVerticalBias(id, verticalBias)
        }

        constraintSet.applyTo(buttonContainer)
    }
}