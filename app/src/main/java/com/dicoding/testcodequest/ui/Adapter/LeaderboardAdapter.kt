package com.dicoding.testcodequest.ui.Adapter

import android.graphics.Color
import android.util.Log
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
        holder.bind(user, position + 1)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class LeaderboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val positionText: TextView = itemView.findViewById(R.id.positionText)
        private val avatarImageView: ImageView = itemView.findViewById(R.id.userAvatar)
        private val nameTextView: TextView = itemView.findViewById(R.id.userName)
        private val pointTextView: TextView = itemView.findViewById(R.id.userPoints)
        private val arrowImageView: ImageView = itemView.findViewById(R.id.arrowImage)
        private val rankChangeTextView: TextView = itemView.findViewById(R.id.rankChangeText)

        fun bind(user: User, position: Int) {
            positionText.text = position.toString()
            nameTextView.text = user.nama
            pointTextView.text = user.point.toString()
            Glide.with(itemView.context).load(user.profileAvatar).into(avatarImageView)

            Log.d("LeaderboardAdapter", "arrowRank in bind: ${user.arrowRank}")

            when (user.arrowRank) {
                "up" -> {
                    Log.d("LeaderboardAdapter", "arrowRank is up")
                    Glide.with(itemView.context)
                        .load(R.drawable.ic_baseline_arrow_upward_24)
                        .into(arrowImageView)
                    arrowImageView.visibility = View.VISIBLE
                    rankChangeTextView.text = user.rankChange ?: ""
                    rankChangeTextView.visibility = View.VISIBLE
                    rankChangeTextView.setTextColor(Color.parseColor("#03A503"))  // Set color for "up"
                }
                "down" -> {
                    Log.d("LeaderboardAdapter", "arrowRank is down")
                    Glide.with(itemView.context)
                        .load(R.drawable.ic_baseline_arrow_downward_24)
                        .into(arrowImageView)
                    arrowImageView.visibility = View.VISIBLE
                    rankChangeTextView.text = user.rankChange ?: ""
                    rankChangeTextView.visibility = View.VISIBLE
                    rankChangeTextView.setTextColor(Color.parseColor("#A1080C"))  // Set color for "down"
                }
                else -> {
                    Log.d("LeaderboardAdapter", "arrowRank is null or not recognized")
                    arrowImageView.visibility = View.GONE
                    rankChangeTextView.visibility = View.GONE
                }
            }
        }
    }
}