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
import com.dicoding.testcodequest.ui.ViewPager.MyPostFragment

class MyPostAdapter(private var posts: List<Post>, private val listener: OnItemDeleteListener) : RecyclerView.Adapter<MyPostAdapter.MyPostViewHolder>() {

    interface OnItemDeleteListener {
        fun onItemDelete(post: Post, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mypost, parent, false)
        return MyPostViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyPostViewHolder, position: Int) {
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

    fun removePost(position: Int) {
        val updatedList = posts.toMutableList()
        updatedList.removeAt(position)
        posts = updatedList
        notifyItemRemoved(position)
    }

    inner class MyPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textContent: TextView = itemView.findViewById(R.id.textContent)
        private val textUserName: TextView = itemView.findViewById(R.id.textUserName)
        private val imageProfileAvatar: ImageView = itemView.findViewById(R.id.imageProfileAvatar)
        private val linearComments: LinearLayout = itemView.findViewById(R.id.linear_comments)
        private val imgDelete: ImageView = itemView.findViewById(R.id.imageView7)

        fun bind(post: Post) {
            textContent.text = post.content
            textUserName.text = post.nama
            Glide.with(itemView.context).load(post.profileAvatar).into(imageProfileAvatar)

            linearComments.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, CommentActivity::class.java)
                intent.putExtra("postId", post.postId)
                context.startActivity(intent)
            }

            imgDelete.setOnClickListener {
                listener.onItemDelete(post, adapterPosition)
            }
        }
    }
}
