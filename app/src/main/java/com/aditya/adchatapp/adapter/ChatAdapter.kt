package com.aditya.adchatapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aditya.adchatapp.R
import com.aditya.adchatapp.models.Message

class ChatAdapter(messageList : List<Message>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    lateinit var msgList : List<Message>
    init {
        msgList = messageList
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var senderTV = itemView.findViewById<TextView>(R.id.chatItemSndr)
        var msgTV = itemView.findViewById<TextView>(R.id.chatItemMsg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val chatItemView = LayoutInflater.from(parent.context).inflate(R.layout.chatitem,parent,false)
        return ViewHolder(chatItemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val msg = msgList.get(position)
        holder.msgTV.text = msg.senderMessage
        holder.senderTV.text = msg.senderName
    }

    override fun getItemCount(): Int {
        return msgList.size
    }
}