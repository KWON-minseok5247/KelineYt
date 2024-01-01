package com.example.kelineyt.viewmodel.makeIt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kelineyt.data.User
import com.example.kelineyt.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserAccountViewModels @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {
    //TODO 여기서는 일단 접속을 함과 동시에 파이어베이스의 데이터를 불러오고 작성할 수 있어야 한다.
    // 그리고 데이터를 수정했으면 다시 파이어베이스에 업로드를 해야 함.
    // 마지막으로 사진을 업로드하고 새로 수정하는 역할도 있어야 한다.

    private val _user = MutableStateFlow<Resource<User>>(Resource.unspecified())
    val user = _user.asStateFlow()

    init {
        getUserData()
    }


    private fun getUserData() {

        viewModelScope.launch {
            _user.emit(Resource.Loading())
        }

        firestore.collection("user").document(auth.uid!!).get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                viewModelScope.launch {
                    _user.emit(Resource.Success(user!!))
                }

            }.addOnFailureListener {
                viewModelScope.launch {
                    _user.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun updateUserData(updateUser: User) {
        viewModelScope.launch {
            _user.emit(Resource.Loading())
        }
        firestore.collection("user").document(auth.uid!!).set(updateUser)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _user.emit(Resource.Success(updateUser))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _user.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}