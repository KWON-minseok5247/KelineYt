package com.example.kelineyt.viewmodel.makeIt

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kelineyt.data.CartProduct
import com.example.kelineyt.data.Product
import com.example.kelineyt.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.ViewModelLifecycle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailsViewModels @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {
    //  여기서는 사이즈, 색상, 이미지 등을 가져와 MutableFlow에 연결한다. 핵심은 파이어베이스에서 가져오는 게
    //  아니라 navarg를 통해서 가져와야 한다. - Clear

    // TODO addtocart를 해야 하는데 2가지를 적용해야 한다. buttonAddToCart버튼을 클릭해야 한다.
    // TODO 파이어베이스에 등록하는 건 CartProduct 형식으로 적용시키기
    // TODO 1. 새로 추가된 아이템이라면 파이어베이스에 새로 적용하기
    // TODO 2. 이미 적용된 아이템이라면 갯수만 추가하기.

    private val _addToCart = MutableStateFlow<Resource<CartProduct>>(Resource.unspecified())
    val addToCart = _addToCart.asStateFlow()

    private val cartCollection =
        firestore.collection("user").document(auth.uid!!).collection("cart")

    fun addToCartIt(cartProduct: CartProduct) {
        viewModelScope.launch {
            _addToCart.emit(Resource.Loading())
        }
        Log.e("cartProduct", cartProduct.toString())

        // 지금 문제점이 처음 누를 때 파이어베이스에 등록이 되고, 2번째 이후부터 아이템이 따로 등록이 된다. 즉 처음과 두번째 이후의 아이템이 다르게 인식한다는 것.
        cartCollection // 여기서도 사이즈랑 색깔도 일치시켜야 하나??

            .whereEqualTo("product.id", cartProduct.product.id)
            // 아래 조건을 제대로 입력하지 않았고, 조건에 대한 결과값도 제대로 입력하지 않아 문제가 발생했다.
            .whereEqualTo("selectedColor", cartProduct.selectedColor)
            .whereEqualTo("selectedSize", cartProduct.selectedSize)
            // whereEqualTo 조건을 새로 추가하면 처음 실행되는 것으로 취급된다.
            .get()
            .addOnSuccessListener {
                if (it.documents.isEmpty()) { // 처음 실행되었을 때
                    addNewCartProduct(cartProduct)
                    Log.e("d", "NewCartProduct가 실행되었다.")
                } else { // 이미 데이터가 있는 경우
                    val selectedCartProduct = it.toObjects(CartProduct::class.java)[0]
                    val documentId = it.documents.first().id
                    addCartProductQuantity(documentId, selectedCartProduct)
                    Log.e("d", "addCartProductQuantity 실행되었다.")

                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _addToCart.emit(Resource.Error(it.message.toString()))
                }
            }


    }

    // 파이어베이스의 데이터를 documentId를 통해 뽑아내고 해당 quantity를 1 증가시킨다.
    // 과정 1 -> 해당 카트 컬렉션에서 id를 통해 해당 문서를 추출한다.
    // 과정 2 -> 문서를 읽고 수정할 수 있는 객체를 get을 통해 생성한다.
    // 과정 3 -> 그 객체를 우리가 쉽게 볼 수 있도록 cartProduct 형태로 오브젝트화한다.
    // 과정 4 -> 그 오브젝트 내에서 quantity를 1 증가시킨 후 set을 통해 저장한다.

    private fun addCartProductQuantity(documentId: String, cartProduct: CartProduct) {
        //        firestore.runTransaction { transition ->
        //            val documentRef = cartCollection.document(documentId) // 카트 컬렉션에서 해당 document를 찾는것
        //            val document = transition.get(documentRef) // 그거를 transition으로 만들어 변경하거나 쓸 수 있도록 하는 객체 생성
        //            val productObject = document.toObject(CartProduct::class.java) // 그 객체를 CartProduct화 한다.
        //            productObject?.let { cartProduct ->
        //                val newQuantity = cartProduct.quantity + 1
        //                val newProductObject = cartProduct.copy(quantity = newQuantity)
        //                transition.set(documentRef,newProductObject)
        //            }

        firestore.runTransaction { transaction ->
            val documentRefer = cartCollection.document(documentId)
            val document = transaction.get(documentRefer)
            val cartProducts = document.toObject(CartProduct::class.java)
            cartProducts?.let { cartProduct ->
                val newQuantity = cartProduct.quantity + 1
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transaction.set(documentRefer, newProductObject)
            }
//            val newCartProduct = cartProducts?.copy(quantity = cartProduct.quantity + 1)
        }
            .addOnSuccessListener {
                viewModelScope.launch {
                    _addToCart.emit(Resource.Success(cartProduct))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _addToCart.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun addNewCartProduct(cartProduct: CartProduct) {
        firestore.collection("user").document(auth.uid!!).collection("cart").add(cartProduct)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _addToCart.emit(Resource.Success(cartProduct))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _addToCart.emit(Resource.Error(it.message.toString()))
                }
            }
    }


}