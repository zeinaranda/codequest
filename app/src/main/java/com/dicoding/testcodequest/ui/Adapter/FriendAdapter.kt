package com.dicoding.testcodequest.ui.Adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.preference.AuthPreference
import com.dicoding.testcodequest.data.response.Avatar
import com.dicoding.testcodequest.data.response.Friend
import com.dicoding.testcodequest.data.response.FriendRequest
import com.dicoding.testcodequest.data.response.User
import com.dicoding.testcodequest.ui.DetailUserActivity

class FriendAdapter(private val friends: ArrayList<Friend>) :
    RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {

    fun setList (friend: ArrayList<Friend>){
        friends.clear()
        friends.addAll(friend)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = friends[position]
        holder.bind(friend)
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profileAvatar: ImageView = itemView.findViewById(R.id.profileAvatar)
        private val nama: TextView = itemView.findViewById(R.id.nama)
        private val nim: TextView = itemView.findViewById(R.id.nim)

        fun bind(friend: Friend) {
            // Menggunakan library seperti Glide atau Picasso untuk memuat gambar profil
            Glide.with(itemView.context).load(friend.profileAvatar).into(profileAvatar)
            nama.text = friend.nama
            nim.text = friend.nim

            Log.d("FriendAdapter", "${friend.friendUserId},Nama: ${friend.nama}, Nim: ${friend.nim}, Owned Avatars: ${friend.ownedAvatars}, Koin: ${friend.koin}, Point: ${friend.point}, ${friend.profileAvatar} ")

            // Menambahkan click listener untuk membuka DetailUserActivity dengan semua data
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailUserActivity::class.java)
                intent.putExtra("friendUserId", friend.friendUserId)
                intent.putExtra("nama", friend.nama)
                intent.putExtra("nim", friend.nim)
                intent.putExtra("koin", friend.koin)
                intent.putExtra("point", friend.point)
                intent.putExtra("profileAvatar", friend.profileAvatar)
                intent.putIntegerArrayListExtra("ownedAvatars", friend.ownedAvatars as ArrayList<Int>?)
                itemView.context.startActivity(intent)
            }
        }


    }
}