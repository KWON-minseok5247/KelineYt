package com.example.kelineyt.viewmodel.makeIt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kelineyt.data.Address
import com.example.kelineyt.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModels
    @Inject constructor(
        private val auth: FirebaseAuth,
        private val firestore: FirebaseFirestore
    ) : ViewModel() {

    private val _address = MutableStateFlow<Resource<Address>>(Resource.unspecified())
    val address = _address.asStateFlow()

    fun saveAddress(address: Address) {
        firestore.collection("user").document(auth.uid!!).collection("address")
            .add(address).addOnSuccessListener {
                viewModelScope.launch {
                    _address.emit(Resource.Success(address))

                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _address.emit(Resource.Error(it.message.toString()))
                }
            }
    }






}