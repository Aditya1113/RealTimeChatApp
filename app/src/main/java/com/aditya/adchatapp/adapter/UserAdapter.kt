package com.aditya.adchatapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aditya.adchatapp.ChatBoxActivity
import com.aditya.adchatapp.R
import com.aditya.adchatapp.models.User

class UserAdapter(private val userList: List<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val statusImageView: ImageView = itemView.findViewById(R.id.tvOnlineStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.nameTextView.text = user.username

        // Display online/offline status symbol
        if (user.isOnline) {
            holder.statusImageView.setImageResource(R.drawable.ic_online) // Replace with your online icon
        } else {
            holder.statusImageView.setImageDrawable(null) // Replace with your offline icon
        }

        holder.itemView.setOnClickListener {

            // Get the selected user
            val selectedUser = userList[position]

            // Open ChatboxActivity and pass the selected user's username as an extra
            val intent = Intent(holder.itemView.context, ChatBoxActivity::class.java)
            intent.putExtra("FriendName", selectedUser.username)

            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}
