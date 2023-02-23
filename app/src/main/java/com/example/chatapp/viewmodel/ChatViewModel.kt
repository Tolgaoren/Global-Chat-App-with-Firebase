package com.example.chatapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.Message
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {

    private val db = Firebase.firestore
    val chatMessagesList = MutableLiveData<List<Message>?>()


    fun getMessagesData() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = db.collection("chat").orderBy("messageDate")
            data.get().addOnSuccessListener {
                if (it != null) {
                    chatMessagesList.value = it.toObjects(Message::class.java)
                }
            }.addOnFailureListener{
                chatMessagesList.value = null
            }
                    }
                }

    fun getMessagesChanges() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = db.collection("chat").orderBy("messageDate")
            data.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    return@addSnapshotListener
                }
                querySnapshot?.let {
                    for (document in it) {
                        chatMessagesList.value = it.toObjects(Message::class.java)
                    }
                }
            }
        }
    }
    fun pushMessage(messages: Message) {
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("chat")
                .document().set(messages)
        }
    }
}