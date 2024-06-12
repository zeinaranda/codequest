package com.dicoding.testcodequest.ui.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.GrayscaleTransformation
import com.dicoding.testcodequest.data.response.Avatar


class OwnedAdapter(private val avatars: List<Avatar>, private val ownedAvatars: List<Int>) :
    RecyclerView.Adapter<OwnedAdapter.AvatarViewHolder>() {

    class AvatarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatarImageView: ImageView = itemView.findViewById(R.id.imageAvatar)
        val avatarNameTextView: TextView = itemView.findViewById(R.id.tv_namaAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.owned_row, parent, false)
        return AvatarViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        val avatar = avatars[position]

        // Set avatar name
        holder.avatarNameTextView.text = avatar.namaAvatar

        // Set avatar image with different coloring based on ownership
        val imageUrl = avatar.imageAvatar
        if (ownedAvatars.contains(avatar.avatarId)) {
            // Load normal image
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .into(holder.avatarImageView)
        } else {
            // Load monochrome image
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .apply(RequestOptions().transform(jp.wasabeef.glide.transformations.GrayscaleTransformation()))
                .into(holder.avatarImageView)
        }
    }

    override fun getItemCount(): Int {
        return avatars.size
    }
}