package com.dicoding.testcodequest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.testcodequest.R
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class MainFragment : Fragment() {

    private lateinit var textStageTitle: TextView
    private lateinit var imageStage: ImageView
    private lateinit var btnNextStage: Button

    private var currentStageIndex = 0 // Indeks awal
    private val stages = ArrayList<Stage>() // Menyimpan daftar stage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        // Inisialisasi tampilan
        textStageTitle = view.findViewById(R.id.textStageTitle)
        imageStage = view.findViewById(R.id.imageStage)
        btnNextStage = view.findViewById(R.id.btnNextStage)

        // Set listener untuk tombol next
        btnNextStage.setOnClickListener {
            // Pindah ke stage selanjutnya
            if (currentStageIndex < stages.size - 1) {
                currentStageIndex++
                showCurrentStage()
            }
        }

        // Dummy data stage (judul dan URL gambar), ganti dengan data yang sesungguhnya
        stages.add(Stage("Stage 1", "https://example.com/image1.jpg"))
        stages.add(Stage("Stage 2", "https://example.com/image2.jpg"))
        stages.add(Stage("Stage 3", "https://example.com/image3.jpg"))

        // Tampilkan judul dan gambar stage pertama
        showCurrentStage()

        return view
    }

    private fun showCurrentStage() {
        val currentStage = stages[currentStageIndex]
        textStageTitle.text = currentStage.title
        // Menggunakan Glide atau library lainnya untuk memuat gambar dari URL
        // Ganti implementasi ini sesuai dengan preferensi Anda
        Glide.with(this)
            .load(currentStage.imageUrl)
            .into(imageStage)
    }

    data class Stage(val title: String, val imageUrl: String)
}
