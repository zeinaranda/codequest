package com.dicoding.testcodequest.ui.ViewPager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.preference.AuthPreference
import com.dicoding.testcodequest.data.response.Post
import com.dicoding.testcodequest.data.retrofit.ApiConfig
import com.dicoding.testcodequest.data.retrofit.ApiService
import com.dicoding.testcodequest.databinding.FragmentPostBinding
import com.dicoding.testcodequest.ui.Adapter.PostAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PostFragment : Fragment() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var editTextContent: EditText
    private lateinit var buttonCreatePost: ImageView
    private lateinit var recyclerViewPosts: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var authPreference: AuthPreference
    private lateinit var apiInterface: ApiService
    private lateinit var progressBar: LottieAnimationView
    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post, container, false)
        authPreference = AuthPreference(requireContext())
        userId = authPreference.getId() ?: 0

        // Inisialisasi view
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        editTextContent = view.findViewById(R.id.editTextContent)
        buttonCreatePost = view.findViewById(R.id.buttonCreatePost)
        recyclerViewPosts = view.findViewById(R.id.recyclerViewPosts)

        // Inisialisasi progressBar
        progressBar = view.findViewById(R.id.progressBar)

        // Atur visibilitas progressBar menjadi visible sebelum memulai fetching data
        progressBar.visibility = View.VISIBLE


        // Inisialisasi RecyclerView dan Adapter
        postAdapter = PostAdapter(emptyList()) // Adapter awal dengan list kosong
        recyclerViewPosts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postAdapter
            setHasFixedSize(true)
        }

        // Setup Retrofit API interface
        apiInterface = ApiConfig.getApiService()
        getAllPosts()

        // Setup SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            // Aksi refresh yang dilakukan saat swipe
            getAllPosts()
        }

        // Setup listener untuk button create post
        buttonCreatePost.setOnClickListener {
            createNewPost()
        }

        return view
    }


    private fun getAllPosts() {
        apiInterface.getAllPosts().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    val posts = response.body()
                    posts?.let {
                        postAdapter.setData(it)
                    }
                }
                progressBar.visibility = View.GONE
                swipeRefreshLayout.isRefreshing = false
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                t.printStackTrace()
                progressBar.visibility = View.GONE
                swipeRefreshLayout.isRefreshing = false
                // Handle failure, e.g., show error message
            }
        })
    }

    private fun createNewPost() {
        val content = editTextContent.text.toString().trim()
        if (content.isNotEmpty()) {
            // Buat objek Post baru
            val post = Post(
                content = content,
                userId = userId, // Ganti dengan ID pengguna yang sesuai
                createdAt = Date(),
                updatedAt = Date(),
            )

            // Kirim post baru ke server
            apiInterface.createPost(post).enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (response.isSuccessful) {
                        // Jika berhasil, tambahkan post ke RecyclerView
                        val createdPost = response.body()
                        createdPost?.let {
                            postAdapter.addData(it)

                            getAllPosts()
                        }
                        // Bersihkan field input
                        editTextContent.text.clear()
                    }
                    progressBar.visibility = View.GONE

                    swipeRefreshLayout.isRefreshing = false
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    t.printStackTrace()
                    // Handle failure, e.g., show error message
                    progressBar.visibility = View.GONE
                    swipeRefreshLayout.isRefreshing = false
                }
            })
        }
    }
}