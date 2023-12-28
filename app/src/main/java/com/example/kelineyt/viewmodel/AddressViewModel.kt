package com.example.kelineyt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kelineyt.data.Address
import com.example.kelineyt.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {
    // mutablestateflow는 값이 변경되지 않으면 새로운 네트워크 호출이 이루어지지 않는다. 즉 값이 업데이트되어야 flow 데이터를 흘려보낸다.
    // 그래서 cartProdct 등의 Flow에서도 다양한 내용이 들어가지만 실시간으로 흘려보내주어 is Resource.Success(데이터)로 빠른 조치를 다 할 수 있다.
    // sharedFlow는 이벤트 핸들링에 적합하며 초기화 값이 필요없다.

    private val _addNewAddress = MutableStateFlow<Resource<Address>>(Resource.unspecified())
    val addNewAddress = _addNewAddress.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    fun addAddress(address: Address) {
        val validateInputs = validateInputs(address)
        if (validateInputs) {
            viewModelScope.launch { _addNewAddress.emit(Resource.Loading()) }
            firestore.collection("user").document(auth.uid!!).collection("address").document()
                .set(address).addOnSuccessListener {
                    viewModelScope.launch { _addNewAddress.emit(Resource.Success(address)) }

                }.addOnFailureListener {
                    viewModelScope.launch { _addNewAddress.emit(Resource.Error(it.message.toString())) }
                }
        } else {
            // 얘는 따로 값을 업데이트하지 않아도 된다.
            viewModelScope.launch { _error.emit("All fields are required.") }
        }

    }



    private fun validateInputs(address: Address): Boolean {
        return address.addressTitle.isNotEmpty() &&
                address.city.isNotEmpty() &&
                address.state.isNotEmpty() &&
                address.fullName.isNotEmpty() &&
                address.phone.isNotEmpty() &&
                address.street.isNotEmpty()
    }

    fun updateAddress(oldAddress: Address) {
        val validateInputs = validateInputs(oldAddress)
        if (validateInputs) {
//            firestore.runTransaction { transition ->
//                val address = firestore.collection("user").document(auth.uid!!)
//                    .collection("address").whereEqualTo()
//                Log.e("address", address.toString())
//            }
        }

    }
    /*private fun saveUserInformation(user: User, shouldRetrievedOldImage: Boolean) {
        firestore.runTransaction { transaction ->
            val documentRef = firestore.collection("user").document(auth.uid!!)
            val currentUser = transaction.get(documentRef).toObject(User::class.java)
            if (shouldRetrievedOldImage) {
                val newUser = user.copy(imagePath = currentUser?.imagePath ?: "")
                transaction.set(documentRef, newUser)
            } else { // 새로운 이미지 데이터 추가 용도로 사용함.
                transaction.set(documentRef, user)
            }
        }.addOnSuccessListener {
            viewModelScope.launch {
                _updateInfo.emit(Resource.Success(user))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _updateInfo.emit(Resource.Error(it.message.toString()))
            }
        }
    }*/


}