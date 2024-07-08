package com.dicoding.testcodequest.ui.ViewPager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.dicoding.testcodequest.R

class AddFriendFragment : Fragment() {

    private lateinit var editTextNIM: EditText
    private lateinit var buttonSearch: Button
    private lateinit var listViewSearchResults: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_friend, container, false)

        editTextNIM = view.findViewById(R.id.editTextNIM)
        buttonSearch = view.findViewById(R.id.buttonSearch)
        listViewSearchResults = view.findViewById(R.id.listViewSearchResults)

        // Implementasi logika pencarian teman
        buttonSearch.setOnClickListener {
            val nim = editTextNIM.text.toString()
            // Lakukan pencarian teman berdasarkan NIM
            // Implementasi logika pencarian dapat dilakukan di sini
        }

        return view
    }
}