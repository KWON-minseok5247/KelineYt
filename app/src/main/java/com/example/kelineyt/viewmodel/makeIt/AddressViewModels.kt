package com.example.kelineyt.viewmodel.makeIt

import android.util.Log
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

    private val firebaseAddressCollection =
        firestore.collection("user").document(auth.uid!!).collection("address")

    private val _address = MutableStateFlow<Resource<Address>>(Resource.unspecified())
    val address = _address.asStateFlow()

    fun saveAddress(address: Address) {
        firebaseAddressCollection
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

    fun updateAddress(index: Int, newAddress: Address) { // 업데이트를 하려면 해당 document부터 찾아야 한다.
        firebaseAddressCollection.get().addOnSuccessListener {

            val wantedAddressId = it.documents[index].id
            firestore.runTransaction { transition ->
                val documentRef = firebaseAddressCollection.document(wantedAddressId)
                transition.set(documentRef, newAddress)
                viewModelScope.launch {
                    _address.emit(Resource.Success(newAddress))
                }
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _address.emit(Resource.Error(it.message.toString()))
            }
        }
    }

    fun deleteAddress(index: Int) { // 업데이트를 하려면 해당 document부터 찾아야 한다.
        firebaseAddressCollection.get().addOnSuccessListener {
            val wantedAddressId = it.documents[index].id
            firebaseAddressCollection.document(wantedAddressId).delete()
        }.addOnFailureListener {
            viewModelScope.launch {
                _address.emit(Resource.Error(it.message.toString()))
            }
        }
    }


}