package com.example.kelineyt.viewmodel.makeIt

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kelineyt.KelineApplication
import com.example.kelineyt.data.User
import com.example.kelineyt.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModels @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : ViewModel() {

    private val _userImage = MutableStateFlow<Resource<User>>(Resource.unspecified())
    val userImage = _userImage.asStateFlow()


    init {
        getUserData()
    }

    // 얘는 자꾸 변할 수 있으니까 addsnap으로 한다.
    private fun getUserData() {

        viewModelScope.launch {
            _userImage.emit(Resource.Loading())
        }

        firestore.collection("user").document(auth.uid!!).addSnapshotListener { value, error ->
            if (value != null && error == null) {
                val user = value.toObject(User::class.java)
                viewModelScope.launch {
                    _userImage.emit(Resource.Success(user!!))
                }

            } else {
                viewModelScope.launch {
                    if (error != null) {
                        _userImage.emit(Resource.Error(error.message.toString()))
                    }
                }
            }

            }
    }


}
