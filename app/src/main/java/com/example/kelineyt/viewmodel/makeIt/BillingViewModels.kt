package com.example.kelineyt.viewmodel.makeIt

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kelineyt.data.Address
import com.example.kelineyt.data.order.Order
import com.example.kelineyt.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
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

    private val _order = MutableStateFlow<Resource<Order>>(Resource.unspecified())
    val order = _order.asStateFlow()

    init {
        getAddressRv()
    }

    fun getAddressRv() {
        viewModelScope.launch {
            _address.emit(Resource.Loading())
        }
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

    fun placeOrder(order: Order) {
        viewModelScope.launch {
            _order.emit(Resource.Loading())
        }

        val newOrder = order.copy(userId = auth.uid!!)

        firestore.collection("user").document(auth.uid!!).collection("orders").add(newOrder)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _order.emit(Resource.Success(newOrder))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _order.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun adminOrder(order: Order) {

        val newOrder = order.copy(userId = auth.uid!!)

        firestore.collection("orders").add(newOrder)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _order.emit(Resource.Success(newOrder))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _order.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun deleteCartProducts() {
        firestore.collection("user").document(auth.uid!!).collection("cart").get()
            .addOnSuccessListener {
                it.documents.forEach {
                    it.reference.delete()
                }

            }.addOnFailureListener {
                viewModelScope.launch {
                    _order.emit(Resource.Error(it.message.toString()))
                }
            }

    }


}