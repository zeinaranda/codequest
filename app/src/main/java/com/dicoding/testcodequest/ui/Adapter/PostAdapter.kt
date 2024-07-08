package com.dicoding.testcodequest.ui.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.response.Post
import com.dicoding.testcodequest.ui.ViewPager.CommentActivity


class PostAdapter(private var posts: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    fun setData(newList: List<Post>) {
        posts = newList
        notifyDataSetChanged()
    }

    fun addData(post: Post) {
        val updatedList = posts.toMutableList()
        updatedList.add(post)
        posts = updatedList
        notifyItemInserted(updatedList.size - 1)
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textContent: TextView = itemView.findViewById(R.id.textContent)
        private val textUserName: TextView = itemView.findViewById(R.id.textUserName)
        private val imageProfileAvatar: ImageView = itemView.findViewById(R.id.imageProfileAvatar)
        private val linearComments: LinearLayout = itemView.findViewById(R.id.linear_comments)

        fun bind(post: Post) {
            textContent.text = post.content
            textUserName.text = post.nama
            Glide.with(itemView.context).load(post.profileAvatar).into(imageProfileAvatar)

            linearComments.setOnClickListener {
                val context = itemView.context
                // Buat intent untuk CommentActivity
                val intent = Intent(context, CommentActivity::class.java)
                // Tambahkan data yang dibutuhkan ke intent, misalnya postId atau informasi lainnya
                intent.putExtra("postId", post.postId)
                intent.putExtra("userId", post.userId)
                // Start activity dengan intent
                context.startActivity(intent)
            }

        }
    }
}
