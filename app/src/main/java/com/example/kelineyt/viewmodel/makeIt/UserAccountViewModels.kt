package com.example.kelineyt.viewmodel.makeIt

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kelineyt.KelineApplication
import com.example.kelineyt.data.User
import com.example.kelineyt.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject


@HiltViewModel
class UserAccountViewModels @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: StorageReference,
    application: Application
) : AndroidViewModel(application) {
    //TODO 여기서는 일단 접속을 함과 동시에 파이어베이스의 데이터를 불러오고 작성할 수 있어야 한다.
    // 그리고 데이터를 수정했으면 다시 파이어베이스에 업로드를 해야 함.
    // 마지막으로 사진을 업로드하고 새로 수정하는 역할도 있어야 한다.

    private val _user = MutableStateFlow<Resource<User>>(Resource.unspecified())
    val user = _user.asStateFlow()

    private val _updateUser = MutableStateFlow<Resource<User>>(Resource.unspecified())
    val updateUser = _updateUser.asStateFlow()

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

    fun saveUser(updateUser: User) { // 사진말고 닉네임만 변경시 작동

        viewModelScope.launch {
            _updateUser.emit(Resource.Loading())
        }

        firestore.collection("user").document(auth.uid!!).set(updateUser)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _updateUser.emit(Resource.Success(updateUser))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _updateUser.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun saveUserWithImage(updateUser: User, uri: Uri) { // 사진만 혹은 사진과 같이 변경할 때 사용
// 근데 속도가 너무 느린데?????????
        viewModelScope.launch {
            _updateUser.emit(Resource.Loading())
        }
        // 따라하니까 속도 2배는 빨라짐
        viewModelScope.launch {
            try {
                val imageBitmap = MediaStore.Images.Media.getBitmap(getApplication<KelineApplication>()
                    .contentResolver,uri)
                val byteArrayOutputStream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 96, byteArrayOutputStream)
                val imageByteArray = byteArrayOutputStream.toByteArray()
                // 어디에 저장할지 주소를 만들어두고
                val imageDirectory = storage.child("profileImages/${auth.uid}/${UUID.randomUUID()}")
                // 해당 주소에 데이터를 입력한다.
                val result = imageDirectory.putBytes(imageByteArray).await()
                val imageUrl = result.storage.downloadUrl.await().toString()
                saveUser(updateUser.copy(imagePath = imageUrl))
            } catch (e: Exception) {
                viewModelScope.launch {
                    _updateUser.emit(Resource.Error(e.message.toString()))
                }
            }
        }
//
//        viewModelScope.launch {
//            _updateUser.emit(Resource.Loading())
//        }
//        val fileName = "${System.currentTimeMillis()}.png"
//
//        storage.child("profileImages").child(auth.uid!!).child(fileName)
//            .putFile(uri)
//            .addOnCompleteListener {
//                if (it.isSuccessful) {
//                    // 파일 업로드에 성공했기 때문에 파일을 다시 받아 오도록 해야함
//                    viewModelScope.launch {
//                        val imageUrl = storage.child("profileImages").child(auth.uid!!)
//                            .child(fileName).downloadUrl.await().toString()
//
//                        Log.e("imageurl", imageUrl)
//                        val newUser = updateUser
//                        saveUser(newUser.copy(imagePath = imageUrl))
//                    }
//
//
//                } else {
//                    viewModelScope.launch {
//                        _updateUser.emit(Resource.Error("사진 업로드 실패"))
//                    }
//                }
//            }

//        storage.child("profileImages").child(auth.uid!!).child(fileName)
//            .putFile(uri)
//            .addOnCompleteListener {
//                if (it.isSuccessful) {
//                    // 파일 업로드에 성공했기 때문에 파일을 다시 받아 오도록 해야함
//                    storage.child("profileImages").child(auth.uid!!)
//                        .child(fileName).downloadUrl
//                        .addOnSuccessListener { uri -> // 여기가 문제야
//
//                            viewModelScope.launch {
//                                val a = storage.child("profileImages").child(auth.uid!!)
//                                    .child(fileName).downloadUrl.await().path
//
//                            }
//
//                            val newUser = updateUser
//
//                            newUser.copy(imagePath = uri.path.toString())
//                            saveUser(newUser)
//                        }.addOnFailureListener {
//                            viewModelScope.launch {
//                                _updateUser.emit(Resource.Error(it.message.toString()))
//                            }
//                        }
//                } else {
//                    viewModelScope.launch {
//                        _updateUser.emit(Resource.Error("사진 업로드 실패"))
//                    }
//                }
//            }

    }
}