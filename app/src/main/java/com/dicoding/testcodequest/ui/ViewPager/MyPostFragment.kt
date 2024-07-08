package com.dicoding.testcodequest.ui.ViewPager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.preference.AuthPreference
import com.dicoding.testcodequest.data.response.Post
import com.dicoding.testcodequest.data.retrofit.ApiConfig
import com.dicoding.testcodequest.data.retrofit.ApiService
import com.dicoding.testcodequest.ui.Adapter.MyPostAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPostFragment : Fragment(), MyPostAdapter.OnItemDeleteListener {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyPostAdapter
    private lateinit var authPreference: AuthPreference
    private lateinit var apiService: ApiService
    private lateinit var progressBar: LottieAnimationView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_post, container, false)

        // Inisialisasi progressBar
        progressBar = view.findViewById(R.id.progressBar)

        // Atur visibilitas progressBar menjadi visible sebelum memulai fetching data
        progressBar.visibility = View.VISIBLE

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        recyclerView = view.findViewById(R.id.recyclerViewMyPosts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        authPreference = AuthPreference(requireContext())
        apiService = ApiConfig.getApiService() // Adjust this to your ApiService creation method

        adapter = MyPostAdapter(arrayListOf(), this)
        recyclerView.adapter = adapter

        val userId = authPreference.getId() ?: 0
        fetchPosts(userId)
        // Setup SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            // Aksi refresh yang dilakukan saat swipe
            fetchPosts(userId)
        }
        return view
    }

    private fun fetchPosts(userId: Int) {
        apiService.getPostsByUserId(userId).enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        adapter.setData(it)
                    }
                    swipeRefreshLayout.isRefreshing = false

                }
                else {
                    Toast.makeText(requireContext(), "Failed to fetch posts", Toast.LENGTH_SHORT).show()
                }
                progressBar.visibility = View.GONE

            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("MyPostFragment", "onFailure: ${t.message}")
                progressBar.visibility = View.GONE
                swipeRefreshLayout.isRefreshing = false

            }
        })
    }

    override fun onItemDelete(post: Post, position: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Post")
            .setMessage("Are you sure you want to delete this post?")
            .setPositiveButton("Yes") { _, _ -> deletePost(post) }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deletePost(post: Post) {
        apiService.deletePost(post.postId!!).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Post deleted", Toast.LENGTH_SHORT).show()
                    val userId = authPreference.getId() ?: 0
                    fetchPosts(userId)  // Refresh the posts
                    swipeRefreshLayout.isRefreshing = false

                } else {
                    Toast.makeText(requireContext(), "Failed to delete post", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("MyPostFragment", "onFailure: ${t.message}")
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }
}