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
class BillingViewModels
@Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _address = MutableStateFlow<Resource<MutableList<Address>>>(Resource.unspecified())
    val address = _address.asStateFlow()

    init {
        getAddressRv()
    }


    fun getAddressRv() {
        firestore.collection("user").document(auth.uid!!).collection("address")
            .get().addOnSuccessListener {
                val addressList = it.toObjects(Address::class.java)
                viewModelScope.launch {
                    _address.emit(Resource.Success(addressList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _address.emit(Resource.Error(it.message.toString()))
                }
            }
    }






}