package com.example.kelineyt.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kelineyt.adapter.chat.Message
import com.example.kelineyt.data.Address
import com.example.kelineyt.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val database: DatabaseReference

) : ViewModel() {

    companion object {
        private val TAG = "ChatViewModel"
    }


    private val _messageList = MutableLiveData<List<Message>>()
    val messageList: LiveData<List<Message>> get() = _messageList

    init {
        // 초기에 메시지를 읽어옴
        readMessages()
    }

    // 메시지 추가하는 메서드
    fun addMessage(message: Message) {
        val currentList = _messageList.value.orEmpty().toMutableList()
        currentList.add(message)
        _messageList.value = currentList
        Log.e("_messageList", _messageList.toString())
    }

    // 네트워크로부터 메시지를 읽어오는 메서드
    private fun readMessages() {
            val chatDatabaseReference = database.child("messages").child(auth.uid!!)

            chatDatabaseReference.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue(Message::class.java)
                    Log.e("message",message.toString())
                    message?.let { addMessage(it) }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
        }

     fun sendMessage(message: String) {
        if (message.isNotEmpty()) {
            val currentUser = auth.currentUser!!.uid
            val userAndMessage = Message(currentUser, message)
            database.child("messages").child(auth.uid!!).push().setValue(userAndMessage)
//            messageEditText.text.clear()
        }
    }





}


