package com.example.chatapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.R
import com.example.chatapp.adapter.ChatMessagesAdapter
import com.example.chatapp.databinding.ChatFragmentBinding
import com.example.chatapp.model.Message
import com.example.chatapp.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatFragment : Fragment() {

    private var _binding: ChatFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var chatMessagesList: ArrayList<Message>
    private lateinit var chatAdapter: ChatMessagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = ChatFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMessagesData()
        loadMessagesData()

        chatMessagesList = ArrayList()
        chatAdapter = ChatMessagesAdapter(chatMessagesList)

        binding.apply {

            chatMessagesRecyclerView.layoutManager = LinearLayoutManager(context)
            chatMessagesRecyclerView.adapter = chatAdapter
            chatMessagesRecyclerView.setHasFixedSize(true)
            chatMessagesRecyclerView.itemAnimator

            messagePushButton.setOnClickListener {

                if (messageInput.text.toString().isNotBlank()
                    && messageInput.text.toString().isNotEmpty()
                ) {
                    if (userInput.text.toString().isNotBlank()
                        && userInput.text.toString().isNotEmpty()) {
                        val messageInput = messageInput.text.toString()
                        val messageOwner = userInput.text.toString()
                        val date = getDate()

                        val messages = Message(
                            id = "$messageOwner.$date",
                            messageText = messageInput,
                            messageOwner = messageOwner,
                            messageDate = date
                        )

                        viewModel.pushMessage(messages)
                        binding.messageInput.text.clear()
                    } else {
                        Toast.makeText(requireContext(),"Enter a username", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            viewModel.getMessagesChanges()
        }

    }

    private fun loadMessagesData() {
        viewModel.chatMessagesList.observe(viewLifecycleOwner) {
            it?.let {
                chatAdapter.updateList(it as ArrayList<Message>)
                chatMessagesList.clear()
                chatMessagesList.addAll(it)

                val size = chatAdapter.itemCount
                binding.chatMessagesRecyclerView.scrollToPosition(size - 1)
            }
        }    }
    private fun getDate(): String {
        val now = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(now)
    }

}