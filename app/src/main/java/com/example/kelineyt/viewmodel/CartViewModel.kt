package com.example.kelineyt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kelineyt.data.CartProduct
import com.example.kelineyt.firebase.FireBaseCommon
import com.example.kelineyt.helper.getProductPrice
import com.example.kelineyt.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val fireBaseCommon: FireBaseCommon
):ViewModel() {
//    // stateFlow는 마지막으로 emit 된 값 또한 상태로 가지고 있다. stateflow는 일종의 화살표같은 느낌같음...
//    private val _cartProducts = MutableStateFlow<Resource<List<CartProduct>>>(Resource.unspecified())
//    val cartProducts = _cartProducts.asStateFlow()
//
//
//
////    val productsPrice = cartProducts.map {
////        when(it) {
////            is Resource.Success -> {
////                calculatePrice(it.data!!)
////            }
////            else -> null
////        }
////    }
//
//    private val _deleteDialog = MutableSharedFlow<CartProduct>()
//    val deleteDialog = _deleteDialog.asSharedFlow()
//    private var cartProductDocuments = emptyList<DocumentSnapshot>()
//
//
////    fun deleteCartProduct(cartProduct: CartProduct) {
////        val index = cartProducts.value.data?.indexOf(cartProduct)
////        if (index != null && index != -1) {    //알고리즘이 조금 느리게 적용이 될 수 있다. 그래서 사전에 차단하는 것
////
////        val documentId = cartProductDocuments[index].id
////        firestore.collection("user").document(auth.uid!!).collection("cart")
////            .document(documentId).delete()
////        }
////    }
////
////
////
////    private fun calculatePrice(data: List<CartProduct>): Float {
////        return data.sumOf { cartProduct ->
////            (cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price) * cartProduct.quantity).toDouble()
////        }.toFloat()
////    }
//
//
//
//    init {
//        getCartProducts()
//    }
////firestore의 collection 은 QuerySnapshot 이라는 걸 리턴
//    //즉 collection 스트림에서 QuerySnapshot 을 얻으면 컬렉션 정보가 다 들어있는데
//// 여기서 data 에는 컬렉션에 포함된 모든 문서 정보가 DocumentSnapshot 형태로 들어있습니다.
//
//    private fun getCartProducts() {
//        viewModelScope.launch {
//            _cartProducts.emit(Resource.Loading())
//            //addSnapshotListener는 콜백인데 cart 콜렉션이 변할 때 실행되는 것이다. 유저가 ui를 자주 변경하기에 이게 get보다 효율적.
//            firestore.collection("user").document(auth.uid!!).collection("cart")
//                .addSnapshotListener { value, error ->
//                    if (error != null || value == null) { // 문제가 발생했을 때
//                        viewModelScope.launch {
//                            _cartProducts.emit(Resource.Error(error?.message.toString()))
//                        }
//                    } else {
//                        cartProductDocuments = value.documents
//                        val cartProduct = value.toObjects(CartProduct::class.java)
//                        viewModelScope.launch { _cartProducts.emit(Resource.Success(cartProduct))  }
//                    }
//            }
//        }
//    }
//
////    fun changeQuantity(
////        cartProduct: CartProduct,
////        quantityChanging: FireBaseCommon.QuantityChanging // 올리는지 내리는지 상태를 확인하는 매개변수
////    ) {
////
////
////        //value까지는 Resource<List<CartProduct>>이다. 그러면 data는 자연스레 List<CartProduct>>가 된다.
////        val index = cartProducts.value.data?.indexOf(cartProduct)
////
////        if (index != null && index != -1) {    //알고리즘이 조금 느리게 적용이 될 수 있다. 그래서 사전에 차단하는 것
////            val documentId = cartProductDocuments[index].id
////            when (quantityChanging) {
////                FireBaseCommon.QuantityChanging.INCREASE -> {
////                    viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
////                    increaseQuantity(documentId)
////                }
////                FireBaseCommon.QuantityChanging.DECREASE -> {
////                    if (cartProduct.quantity == 1) {
////                        viewModelScope.launch { _deleteDialog.emit(cartProduct) }
////                        return
////                    }
////
////                    viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
////                    decreaseQuantity(documentId)
////                }
////
////            }
////
////        }
////
////        /**
////         * index could be equal to -1 if the function [getCartProducts] delays which will also delay the result we expect to be inside the [_cartProducts]
////         * and to prevent the app from crashing we make a check
////         */
////
////
////    }
//
//    private fun decreaseQuantity(documentId: String) {
//        fireBaseCommon.decreaseQuantity(documentId) { result, exception ->
//            if (exception != null)
//                viewModelScope.launch { _cartProducts.emit(Resource.Error(exception.message.toString())) }
//        }
//    }
//
//    private fun increaseQuantity(documentId: String) {
//        fireBaseCommon.increaseQuantity(documentId) { result, exception ->
//            if (exception != null)
//                viewModelScope.launch { _cartProducts.emit(Resource.Error(exception.message.toString())) }
//        }
//    }
//
    private val _cartProducts = MutableStateFlow<Resource<List<CartProduct>>>(Resource.unspecified())
    val cartProducts = _cartProducts.asStateFlow()

    /// 아래부터는 직접 작성함.
    init {
        fetchCartProducts()
    }
    private var cartProductList = emptyList<DocumentSnapshot>() ?: null

    private fun fetchCartProducts() { // 여기에 addsnapshot을 넣으니까 반복재생이 되지 않는다.
        firestore.collection("user").document(auth.uid!!).collection("cart")
            .addSnapshotListener { value, error ->
                val listCartProduct = value?.toObjects(CartProduct::class.java)
                cartProductList = value?.documents

                calculatePrice(listCartProduct!!)
                viewModelScope.launch { _cartProducts.emit(Resource.Success(listCartProduct!!)) }
            }
    }

    fun changingQuantity(cartProduct: CartProduct, quantityChanging: FireBaseCommon.QuantityChanging) {
        //여기서 documentId를 얻고
        val index = cartProducts.value.data?.indexOf(cartProduct)
        if (index != null && index != -1) {

            firestore.collection("user").document(auth.uid!!).collection("cart")
            .get().addOnSuccessListener {
                val document = it.documents[index!!]
                val documentId = document.id

                if (quantityChanging == FireBaseCommon.QuantityChanging.INCREASE) {
                    viewModelScope.launch {
                        _cartProducts.emit(Resource.Loading())
                        increaseQuantity(documentId)
                    }
                }

                if (quantityChanging == FireBaseCommon.QuantityChanging.DECREASE) {
                    viewModelScope.launch {
                        _cartProducts.emit(Resource.Loading())
                        decreaseQuantity(documentId)
                    }
                }

        }.addOnFailureListener {
                viewModelScope.launch {
                    _cartProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }
    }


//    fun calculatePrice(cartProducts: List<CartProduct>) : Float {
//        var a : Float = 0F
//        for (i in cartProducts.indices) {
//            a += cartProducts[i].product.offerPercentage.getProductPrice(cartProducts[i].product.price) * cartProducts[i].quantity
//        }
//            return a
//    }

    // 금액을 실시간으로 변경하는 것도 stateflow로 해당 데이터를 넣어주어야 한다.


        val productsPrice = cartProducts.map {
        when(it) {
            is Resource.Success -> {
                calculatePrice(it.data!!)
            }
            else -> null
        }
    }

    fun deleteProduct(cartProduct: CartProduct) {
        // 여기서는 quantity가 0이 될 때 발생하며 다이얼로그 등을 띄어 놓는다.
        val index = cartProducts.value.data?.indexOf(cartProduct)
        if (index != null && index != -1) {
            val documentId = cartProductList?.get(index!!)?.id
            firestore.collection("user").document(auth.uid!!).collection("cart")
                .document(documentId!!).delete()
        }

//        val index = cartProducts.value.data?.indexOf(cartProduct)
//        if (index != null && index != -1) {    //알고리즘이 조금 느리게 적용이 될 수 있다. 그래서 사전에 차단하는 것
//
//        val documentId = cartProductDocuments[index].id
//        firestore.collection("user").document(auth.uid!!).collection("cart")
//            .document(documentId).delete()
//        }

    }


    private fun calculatePrice(data: List<CartProduct>): Float {
        return data.sumOf { cartProduct ->
            (cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price) * cartProduct.quantity).toDouble()
        }.toFloat()
    }




    fun increaseQuantity(documentId: String) {
        fireBaseCommon.increaseQuantity(documentId) { documentId, e ->
            if (e != null)
                viewModelScope.launch { _cartProducts.emit(Resource.Error(e.message.toString())) }
        }
    }

    fun decreaseQuantity(documentId: String) {
//        firestore.collection("user").document(auth.uid!!).collection("cart").document(documentId)
//            .addSnapshotListener { value, error -
        // 위에 2줄을 클릭하니까 무한반복으로 증가했다.
        fireBaseCommon.decreaseQuantity(documentId) { documentId, e ->
            if (e != null)
                viewModelScope.launch { _cartProducts.emit(Resource.Error(e.message.toString())) }
        }

    }





}