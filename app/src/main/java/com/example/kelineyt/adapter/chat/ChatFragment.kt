package com.example.kelineyt.adapter.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.kelineyt.R
import com.example.kelineyt.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint

//@AndroidEntryPoint
//class ChatFragment: Fragment(R.layout.fragment_chat) {
//
//    lateinit var btnAddchatRoom: Button
//    lateinit var btnSignout: Button
//    lateinit var binding: FragmentChatBinding
//    lateinit var firebaseDatabase: DatabaseReference
//    lateinit var recycler_chatroom: RecyclerView
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentChatBinding.inflate(layoutInflater)
//        return binding.root
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        initializeView()
//        initializeListener()
//        setupRecycler()
//    }
//
//    fun initializeView() { //뷰 초기화
//        try {
//            firebaseDatabase = FirebaseDatabase.getInstance().getReference("ChatRoom")!!
//            btnAddchatRoom = binding.btnNewMessage
//            recycler_chatroom = binding.recyclerChatrooms
//        }catch (e:Exception)
//        {
//            e.printStackTrace()
//            Toast.makeText(this,"화면 초기화 중 오류가 발생하였습니다.",Toast.LENGTH_LONG).show()
//        }
//    }
//    fun initializeListener()  //버튼 클릭 시 리스너 초기화
//    {
//        btnSignout.setOnClickListener()
//        {
//            signOut()
//        }
//        btnAddchatRoom.setOnClickListener()  //새 메시지 화면으로 이동
//        {
//            startActivity(Intent(this@MainActivity, AddChatRoomActivity::class.java))
//            finish()
//        }
//    }
//
//    fun setupRecycler() {
//        recycler_chatroom.layoutManager = LinearLayoutManager(this)
//        recycler_chatroom.adapter = RecyclerChatRoomsAdapter(this)
//    }
//
//    override fun onBackPressed() {
//        signOut()
//    }
//}

import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelineyt.util.hideBottomNavigationView
import com.example.kelineyt.util.showBottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var binding: FragmentChatBinding

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val adapter by lazy {MessageAdapter()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messageEditText = view.findViewById(R.id.messageEditText)
        sendButton = view.findViewById(R.id.sendButton)

        database = FirebaseDatabase.getInstance().reference.child("messages").child("aa")
        auth = FirebaseAuth.getInstance()

        chatMessageRv()
        // 이전에 작성된 모든 메시지를 가져오기
        // 근데 이거는 조금 수정이 필요한게 가장 최근에 연락한 메세지 상위 50개? 그렇게 하면서 페이징으로 차근차근 올라가야
        // 하는 거 같음.. 너무 비효율적이다.
        //그리고 마지막의 메세지가 2번 올라간다

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableListOf<Message>()
                for (childSnapshot in snapshot.children) {
                    val message = childSnapshot.getValue(Message::class.java)
                    if (message != null) {
                        messages.add(message)
                    }
                }
                adapter.differ.submitList(messages)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })


        sendButton.setOnClickListener {
            sendMessage()
        }

        // 메시지가 추가될 때마다 호출되는 리스너
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                Log.e("message", message.toString())
                adapter.differ.submitList(adapter.differ.currentList + listOf(message!!))
//                adapter.differ.submitList(message!!)
                // RecyclerView를 맨 아래로 스크롤
                binding.messageListView.smoothScrollToPosition(adapter.itemCount)

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onResume() {
        super.onResume()
        hideBottomNavigationView()
    }

    override fun onPause() {
        // ChatFragment가 다른 Fragment로 대체되거나 화면에서 사라질 때
        showBottomNavigationView()
        super.onPause()
    }


    private fun chatMessageRv() {
        binding.messageListView.adapter = adapter
        binding.messageListView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false).apply {
            stackFromEnd = true
        }
    }


    private fun sendMessage() {
        val messageText = messageEditText.text.toString()
        if (messageText.isNotEmpty()) {
            val currentUser = auth.currentUser!!.uid
            val message = Message(currentUser, messageText)
            database.push().setValue(message)
            messageEditText.text.clear()
        }
    }
}