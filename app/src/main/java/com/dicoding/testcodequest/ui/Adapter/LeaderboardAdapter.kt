package com.dicoding.testcodequest.ui.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.response.User

class LeaderboardAdapter : RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {

    private var users: List<User> = listOf()

    fun setUsers(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.leaderboard_row, parent, false)
        return LeaderboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user, position + 4)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class LeaderboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val positionText: TextView = itemView.findViewById(R.id.positionText)
        private val avatarImageView: ImageView = itemView.findViewById(R.id.userAvatar)
        private val nameTextView: TextView = itemView.findViewById(R.id.userName)
        private val pointTextView: TextView = itemView.findViewById(R.id.userPoints)

        fun bind(user: User, position: Int) {
            positionText.text = position.toString()
            nameTextView.text = user.nama
            pointTextView.text = user.point.toString()
            Glide.with(itemView.context).load(user.profileAvatar).into(avatarImageView)
        }
    }
}