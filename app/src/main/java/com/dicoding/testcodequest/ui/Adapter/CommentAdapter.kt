package com.dicoding.testcodequest.ui.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.response.Comment

// CommentAdapter.kt
class CommentAdapter(var comments: List<Comment>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.bind(comment)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    fun setData(newList: List<Comment>) {
        comments = newList
        notifyDataSetChanged()
    }

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textContent: TextView = itemView.findViewById(R.id.textContent)
        private val textUserName: TextView = itemView.findViewById(R.id.textUserName)
        private val imageProfileAvatar: ImageView = itemView.findViewById(R.id.imageProfileAvatar)

        fun bind(comment: Comment) {
            textContent.text = comment.content
            textUserName.text = comment.nama ?: "Unknown User"

            Glide.with(itemView.context).load(comment.profileAvatar).into(imageProfileAvatar)
        }
    }
}
