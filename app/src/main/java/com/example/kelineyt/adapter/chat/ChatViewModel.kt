package com.example.kelineyt.adapter.chat

//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.kelineyt.firebase.FireBaseCommon
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//import com.google.firebase.firestore.FirebaseFirestore
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.async
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class ChatViewModel @Inject constructor(
//    private val firestore: FirebaseFirestore,
//    private val auth: FirebaseAuth,
//    private val fireBaseCommon: FireBaseCommon,
//    private val firebaseDatabase: DatabaseReference
//) : ViewModel() {
//
//
//
//    private val TAG = "ChatViewModel"
//
//    var chatMessages = MutableStateFlow<List<MessageData>>(emptyList())
//    var chatData = MutableStateFlow<ChatData?>(null)
//    var userId = "29 (임시)"
//
//    // 채팅방 들어가자마자 조회 - 한번도 채팅하지 않은 경우(채팅방이 생성되어있지 않은 경우) 조회불가
//    fun enterChatRoom(chatId: String){
//        // 한번도 채팅하지 않은경우는 조회 불가
//        Log.d("채팅방 id", chatId)
//
//        firebaseDatabase.child("chat").child(chatId).get()
//            .addOnSuccessListener {
//                Log.d("채팅방 정보", it.value.toString())
//                // 데이터는 hashMap 형태로 오기때문에 객체 형태로 변환해줘야함
//                it.value?.let { value ->
//                    val result = value as HashMap<String, Any>?
//                    val writer = result?.get("writer") as HashMap<String, Any>?
//                    val contact = result?.get("contact") as HashMap<String, Any>?
//                    val _chatData = ChatData(
//                        result?.get("id") as String,
//                        UserData((writer?.get("id") as Long).toInt(), writer["nickname"] as String, (writer["level"] as Long).toInt()),
//                        UserData((contact?.get("id") as Long).toInt(), contact["nickname"] as String, (contact["level"] as Long).toInt()),
//                        result["messages"] as List<MessageData>
//                    )
//                    chatData.value = _chatData
//                }
//            }
//            .addOnFailureListener{
//                Log.d("채팅룸 정보 가져오기 실패", it.toString())
//            }
//
//        val chatListener = object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val _chatMessage = arrayListOf<MessageData>()
//                val messageData = snapshot.value as ArrayList<HashMap<String, Any>>?
//
//                // snapshot은 hashMap 형태로 오기때문에 객체 형태로 변환해줘야함
//                messageData?.forEach {
//                    _chatMessage.add(
//                        MessageData(
//                            it["content"] as String,
//                            it["createdAt"] as Long,
//                            (it["from"] as Long).toInt()
//                        )
//                    )
//                }
//                chatMessages.value = arrayListOf()
//                chatMessages.value = _chatMessage.toList()
//                Log.d("변화 리스너2", chatMessages.value.toString())
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.d(TAG, "loadMessage:onCancelled", error.toException())
//            }
//        }
//        firebaseDatabase.child("chat").child(chatId).child("messages").addValueEventListener(chatListener)
//    }
//
//    // 메세지 보내기
//    fun newMessage(chatId: String, messageData: MessageData){
//        if(chatMessages.value.isEmpty()) {
//            chatMessages.value = listOf(messageData)
//            // 첫 메세지일때 채팅방 생성 - 채팅룸이 생성되는 시점
//            newChatRoom(chatId, userId, chatMessages.value)
//        }else{
//            chatMessages.value += messageData
//            firebaseDatabase.child("chat").child(chatId).child("messages").setValue(chatMessages.value)
//                .addOnSuccessListener {
//                    Log.d("newChatRoomSuccess", "메세지 보내기 성공")
//                }
//                .addOnFailureListener{
//                    Log.d("메세지 보내기 실패", it.toString())
//                }
//        }
//    }
//
//    // 채팅룸 생성
//    private fun newChatRoom(chatId: String, postId: Int, writerId: Int, message :List<MessageData>){
//        val userId = UserSharedPreference(App.context()).getUserPrefs("id").toString()
//
//        var usersChatList: List<String> = emptyList()
//
//        viewModelScope.launch {
//            viewModelScope.async {
//                firebaseDatabase.child("user").child(userId).get()
//                    .addOnSuccessListener {
//                        it.value?.let { it ->
//                            usersChatList = it as List<String>
//                        }
//                        Log.d("유저 채팅 목록 가져옴", usersChatList.toString())
//
//                    }
//                    .addOnFailureListener {
//                        isHave = false
//                    }
//            }.await()
//            // db에서 user정보 가져옴
//            val writerData: ArrayList<UserData> = dbAccessModule.getUserInfoById(writerId)
//            val writer = UserData(writerData[0].id, writerData[0].nickname)
//
//            // sharedPreference에 저장된 본인 user정보
//            val contactData = UserSharedPreference(App.context()).getUserPrefs()
//            val contact = RdbUserData(contactData.id, contactData.nickname
//
//                val chatData = ChatData(chatId, writer, contact, message)
//
//            firebaseDatabase.child("chat").child(chatId).setValue(chatData)
//                .addOnSuccessListener {
//                    Log.d("newChatRoomSuccess", "채팅룸 생성 완료")
//                    // 생성시 채팅 listener 재호출
//                    enterChatRoom(chatId)
//                }
//                .addOnFailureListener {
//                    Log.d("채팅룸 생성 실패", it.toString())
//                }
//
//            if (usersChatList.isEmpty()){
//                usersChatList = listOf(chatId)
//            }else {
//                usersChatList.plus(chatId)
//            }
//            Log.d("유저 채팅 리스트", usersChatList.toString())
//
//            firebaseDatabase.child("user").child(userId).setValue(usersChatList)
//                .addOnSuccessListener {
//                    Log.d("newChatRoomSuccess", "유저 정보에 추가 완료")
//                    enterChatRoom(chatId)
//                }
//                .addOnFailureListener {
//                    Log.d("유저 정보 추가 실패", it.toString())
//                }
//        }
//}
//}