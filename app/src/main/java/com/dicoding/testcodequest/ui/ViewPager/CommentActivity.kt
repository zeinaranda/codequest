package com.dicoding.testcodequest.ui.ViewPager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.preference.AuthPreference
import com.dicoding.testcodequest.data.response.Comment
import com.dicoding.testcodequest.data.retrofit.ApiConfig
import com.dicoding.testcodequest.data.retrofit.ApiService
import com.dicoding.testcodequest.ui.Adapter.CommentAdapter
import com.dicoding.testcodequest.ui.FriendActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentActivity : AppCompatActivity() {
    private lateinit var rvComments: RecyclerView
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var etComment: EditText
    private lateinit var ivSend: ImageView
    private lateinit var authPreference: AuthPreference
    private var userId: Int = 0
    private lateinit var btn_back: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        rvComments = findViewById(R.id.rv_comments)
        etComment = findViewById(R.id.et_comment)
        ivSend = findViewById(R.id.iv_send)
        authPreference = AuthPreference(this)
        userId = authPreference.getId() ?: 0

        commentAdapter = CommentAdapter(listOf())
        rvComments.layoutManager = LinearLayoutManager(this)
        rvComments.adapter = commentAdapter

        val postId = intent.getIntExtra("postId", -1)
        if (postId != -1) {
            loadComments(postId)
        }

        btn_back = findViewById(R.id.btn_back)
        btn_back.setOnClickListener {
            startActivity(Intent(this, FriendActivity::class.java))
            finish()
        }

        ivSend.setOnClickListener {
            val content = etComment.text.toString()
//            val userId = authPreference.getId() // Assuming userId is stored as an Int

            if (content.isNotEmpty() && userId != -1) {
                if (userId != null) {
                    createComment(postId, content, userId)
                }
            }
        }
    }

    private fun loadComments(postId: Int) {
        val apiService = ApiConfig.getApiService()
        apiService.getCommentsByPostId(postId).enqueue(object : Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        commentAdapter.setData(it)
                    }
                }
            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                // Handle error
            }
        })
    }

    private fun createComment(postId: Int, content: String, userId: Int) {
        val apiService = ApiConfig.getApiService()
        val comment = Comment(
            content = content,
            userId = userId,
            postId = postId
        )

        apiService.createComment(postId, comment).enqueue(object : Callback<Comment> {
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        // Clear the input field
                        etComment.text.clear()
                        // Refresh comments
                        loadComments(postId)
                    }
                }
            }

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                // Handle error
            }
        })
    }
}