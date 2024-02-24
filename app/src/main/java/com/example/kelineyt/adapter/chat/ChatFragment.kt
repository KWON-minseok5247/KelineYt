package com.example.kelineyt.adapter.chat

import android.content.DialogInterface
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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment.STYLE_NO_FRAME
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelineyt.util.hideBottomNavigationView
import com.example.kelineyt.util.showBottomNavigationView
import com.example.kelineyt.viewmodel.ChatViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

private val TAG = "ChatFragment"
@AndroidEntryPoint
class ChatFragment : Fragment() {
init {
}
//    private lateinit var messageEditText: EditText
//    private lateinit var sendButton: Button
    private lateinit var binding: FragmentChatBinding
    private val viewModel: ChatViewModel by viewModels()

//    private lateinit var database: DatabaseReference
//    private lateinit var auth: FirebaseAuth
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
//        messageEditText = view.findViewById(R.id.messageEditText)
//        sendButton = view.findViewById(R.id.sendButton)
//
//        database = FirebaseDatabase.getInstance().reference.child("messages").child("aa")
//        auth = FirebaseAuth.getInstance()

        showMyDialog()

        chatMessageRv()
        // 이전에 작성된 모든 메시지를 가져오기
        // 근데 이거는 조금 수정이 필요한게 가장 최근에 연락한 메세지 상위 50개? 그렇게 하면서 페이징으로 차근차근 올라가야
        // 하는 거 같음.. 너무 비효율적이다.
        //그리고 마지막의 메세지가 2번 올라간다

        viewModel.messageList.observe(viewLifecycleOwner, Observer { messages ->
            adapter.differ.submitList(messages)
            // RecyclerView를 맨 아래로 스크롤
            binding.messageListView.smoothScrollToPosition(messages.size - 1)
        })

        binding.imageClose.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("채팅방을 나갑니다.")
                .setMessage("정말 삭제하시겠습니까?")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        viewModel.deleteMessages()
                        findNavController().navigateUp()
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            builder.show()
        }

        binding.sendButton.setOnClickListener {

            val message = binding.messageEditText.text.toString()
            if (!message.isNullOrEmpty()) {
                viewModel.sendMessage(message)
                binding.messageEditText.text.clear()
            } else {
//                Snackbar.make(requireView(),"메시지를 입력하지 않았습니다.", Snackbar.LENGTH_SHORT).show()
                binding.messageEditText.error = "메시지를 입력하지 않았습니다."
            }


        }

//        // 메시지가 추가될 때마다 호출되는 리스너
//        database.addChildEventListener(object : ChildEventListener {
//            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                val message = snapshot.getValue(Message::class.java)
//                Log.e("message", message.toString())
//                adapter.differ.submitList(adapter.differ.currentList + listOf(message!!))
////                adapter.differ.submitList(message!!)
//                // RecyclerView를 맨 아래로 스크롤
//                binding.messageListView.smoothScrollToPosition(adapter.itemCount)
//
//            }
//
//            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
//
//            override fun onChildRemoved(snapshot: DataSnapshot) {}
//
//            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
//
//            override fun onCancelled(error: DatabaseError) {}
//        })
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


//    private fun sendMessage() {
//        val messageText = binding.messageEditText.text.toString()
//        if (messageText.isNotEmpty()) {
//            val currentUser = auth.currentUser!!.uid
//            val message = Message(currentUser, messageText)
//            database.push().setValue(message)
//            messageEditText.text.clear()
//        }
//    }
    private fun showMyDialog() {
        // 다이얼로그를 생성하고 투명한 배경 설정
        val dialog = MyDialogFragment()
        dialog.setStyle(STYLE_NO_FRAME, R.style.MyDialogStyle)

        // 다이얼로그를 보여줍니다.
        dialog.show(childFragmentManager, "MyDialogFragment")

        // 뒷 배경을 흐릿하게 만듭니다.
//        val params = dialog.dialog?.window?.attributes
//        params?.dimAmount = 0.7f
//        dialog.dialog?.window?.attributes = params
    }
}
