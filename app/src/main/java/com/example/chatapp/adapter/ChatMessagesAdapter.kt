package com.example.chatapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.MessagesBinding
import com.example.chatapp.model.Message
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatMessagesAdapter(
    private val chatMessagesList : ArrayList<Message>
) : RecyclerView.Adapter<ChatMessagesAdapter.MessageHolder>() {

    class MessageHolder(val binding : MessagesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val binding = MessagesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MessageHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val date = chatMessagesList[position].messageDate

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        val time = dateFormat.parse(date)

        val dateFormat2 = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        dateFormat2.timeZone = TimeZone.getDefault()
        dateFormat2.format(time)

        val time1 = time?.toString()

        holder.binding.userNameTextView.text = chatMessagesList[position].messageOwner
        holder.binding.userMessageTextView.text = chatMessagesList[position].messageText
        holder.binding.userMessageTime.text = time1
    }

    override fun getItemCount(): Int {
        return chatMessagesList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newMessagesList: ArrayList<Message>) {
        chatMessagesList.clear()
        chatMessagesList.addAll(newMessagesList)
        notifyDataSetChanged()
    }
}