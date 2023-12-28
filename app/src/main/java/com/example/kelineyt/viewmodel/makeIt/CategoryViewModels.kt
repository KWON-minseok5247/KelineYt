package com.example.kelineyt.viewmodel.makeIt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kelineyt.data.Category
import com.example.kelineyt.data.Product
import com.example.kelineyt.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CategoryViewModels @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val category: Category
) : ViewModel() {

    // 여기서는 파이어베이스에서 해당 카테고리에 맞는 데이터를 얻어와야 한다. 총 2개의 어뎁터가 필요하기 때문에 2번 적용해야 한다. -완료-
    private val _bestProduct = MutableStateFlow<Resource<List<Product>>>(Resource.unspecified())
    val bestProduct = _bestProduct.asStateFlow()

    private val _specialProduct = MutableStateFlow<Resource<List<Product>>>(Resource.unspecified())
    val specialProduct = _specialProduct.asStateFlow()

    init {
        getSpecialProduct()
        getBestProduct()
    }

    private fun getBestProduct() {
        viewModelScope.launch {
            _bestProduct.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("category", category.category).get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestProduct.emit(Resource.Success(products))
                }

            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestProduct.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun getSpecialProduct() {
        viewModelScope.launch {
            _bestProduct.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("category", category.category).get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _specialProduct.emit(Resource.Success(products))
                }

            }.addOnFailureListener {
                viewModelScope.launch {
                    _specialProduct.emit(Resource.Error(it.message.toString()))
                }
            }
    }


}