package com.example.kelineyt.viewmodel.makeIt

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kelineyt.data.CartProduct
import com.example.kelineyt.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModels @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): ViewModel() {
    //TODO 여기서는 일단 개수 조정할 수 있도록 하고, 0개가 될 시에 삭제하는 기능이 있어야 한다.
    // 물론 다이얼로그도 같이 들어가 있어야 한다.
    // 금액을 계산해주는 기능도 필요함.
    // Check out을 통해 해당 리스트를 billingFragment로 이동해야 한다.
    private val _cartProducts = MutableStateFlow<Resource<MutableList<CartProduct>>>(Resource.unspecified())
    val cartProducts = _cartProducts.asStateFlow()
    private var cartProductDocuments = emptyList<DocumentSnapshot>()

    private val _calculateProducts = MutableStateFlow<Resource<Float>>(Resource.unspecified())
    val calculateProducts = _calculateProducts.asStateFlow()

    init {
        getCartProducts()
    }




//    private fun getCartProducts() {
//        val cartCollection = firestore.collection("user").document(auth.uid!!).collection("cart").get()
//        cartCollection.addOnSuccessListener {
//            val cartProducts = it.toObjects(CartProduct::class.java)
//
//            viewModelScope.launch {
//                _cartProducts.emit(Resource.Success(cartProducts))
//            }
//        }.addOnFailureListener {
//            viewModelScope.launch {
//                _cartProducts.emit(Resource.Error(it.message.toString()))
//            }
//        }
//    }

    private fun getCartProducts() {
        viewModelScope.launch {
            _cartProducts.emit(Resource.Loading())
        }
        //addSnapshotListener는 콜백인데 cart 콜렉션이 변할 때 실행되는 것이다. 유저가 ui를 자주 변경하기에 이게 get보다 효율적.
        firestore.collection("user").document(auth.uid!!).collection("cart")
            .addSnapshotListener { value, error ->
                if (value != null && error == null) {
                    val cartCollection = value?.toObjects(CartProduct::class.java)
                    if (cartCollection != null) {
                        viewModelScope.launch {
                            _calculateProducts.emit(Resource.Success(calculateCartProducts(cartCollection)))
                        }
                    }
                    viewModelScope.launch {
                        _cartProducts.emit(Resource.Success(cartCollection!!))
                    }
                } else {
                    viewModelScope.launch {
                        _cartProducts.emit(Resource.Error(error?.message.toString()))
                    }
                }
            }
    }



     fun increaseQuantity(cartProduct: CartProduct) { // 아마 documentId가 필요할 듯 싶다.
         val index = cartProducts.value.data?.indexOf(cartProduct)
         // cartProducts의 리스트라서 cartProduct의 인덱스 값을 구하면 후에 해당 document와 Id 값을 쉽게 구할 수 있다.
         if (index != null && index != -1) {

             firestore.collection("user").document(auth.uid!!).collection("cart")
                 .get().addOnSuccessListener {
                     // cartcollection에서 내가 원하는 document를 찾아낸다.
                     val document = it.documents[index!!]
                     val documentId = document.id
                     firestore.runTransaction { transaction ->
                         //                    firestore.collection("user").document(auth.uid!!)
                         //                     .collection("cart").get()
                         //                     .addOnSuccessListener {
                         val documentRefer = firestore.collection("user").document(auth.uid!!)
                             .collection("cart").document(documentId)
                         val selectedDocument = transaction.get(documentRefer)
                         val selectedCartProduct = selectedDocument.toObject(CartProduct::class.java)
                         val newCartProduct =
                             selectedCartProduct?.copy(quantity = selectedCartProduct.quantity + 1)
                         if (newCartProduct != null) {
                             transaction.set(documentRefer, newCartProduct)
                         }

                     }.addOnFailureListener {
                         viewModelScope.launch {
                             _cartProducts.emit(Resource.Error(it.message.toString()))
                         }
                     }


                 }
         }
     }
    fun decreaseQuantity(cartProduct: CartProduct) {
        val index = cartProducts.value.data?.indexOf(cartProduct)
        if (index != null && index != -1) {
            firestore.collection("user").document(auth.uid!!).collection("cart").get()
                .addOnSuccessListener {
                    val document = it.documents[index]
                    val documentId = document.id

                    // 여기서는 실제로 quantity의 개수를 한개 줄이는 단계
                    firestore.runTransaction { transaction ->
                        val documentRefer = firestore.collection("user").document(auth.uid!!).collection("cart")
                            .document(documentId)
                        val oldDocument = transaction.get(documentRefer)
                        val oldCartProduct = oldDocument.toObject(CartProduct::class.java)
                        val newQuantity = oldCartProduct?.quantity?.minus(1)
                        val newCartProduct = oldCartProduct?.copy(quantity = newQuantity!!)
                        if (newCartProduct != null) {
                            transaction.set(documentRefer,newCartProduct)
                        }
                    }

                }.addOnFailureListener {
                    viewModelScope.launch {
                        _cartProducts.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
    }
    fun deleteCartProduct(cartProduct: CartProduct) {
        val index = cartProducts.value.data?.indexOf(cartProduct)
        if (index != null && index != -1) {


            firestore.collection("user").document(auth.uid!!).collection("cart").get()
                .addOnSuccessListener {
                    val document = it.documents[index]
                    val documentId = document.id
                    firestore.collection("user").document(auth.uid!!).collection("cart")
                        .document(documentId).delete()
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _cartProducts.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
    }
    fun calculateCartProducts(cartProducts: List<CartProduct>) : Float {
        var emptyPrice = 0f
        for (cartProduct in cartProducts) {
            if (cartProduct.product.offerPercentage != null) {
                emptyPrice += (cartProduct.product.price * (1 - cartProduct.product.offerPercentage)) * cartProduct.quantity
            } else {
                emptyPrice += cartProduct.product.price * cartProduct.quantity
            }
        }
        return emptyPrice

    }




//     fun decreaseQuantity(cartProduct: CartProduct, documentId: String) { // 아마 documentId가 필요할 듯 싶다.
//        firestore.runTransaction { transaction ->
//            val documentRefer = firestore.collection("user").document(auth.uid!!)
//                .collection("cart").document(documentId)
//            val document = transaction.get(documentRefer)
//            val selectedCartProduct = document.toObject(CartProduct::class.java)
//            val newCartProduct = selectedCartProduct?.copy(quantity = selectedCartProduct.quantity - 1)
//            if (newCartProduct != null) {
//                transaction.set(documentRefer,newCartProduct)
//            }
//
//
//
//        }
//    }

}