package com.example.kelineyt.firebase

import com.example.kelineyt.data.CartProduct
import com.example.kelineyt.data.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FireBaseCommon(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    private val cartCollection = firestore.collection("user").document(auth.uid!!).collection("cart")

    fun addProductToCart(cartProduct: CartProduct, onResult: (CartProduct?, Exception?) -> Unit) {
        cartCollection.document().set(cartProduct)
            .addOnSuccessListener {
            onResult(cartProduct, null)
        }.addOnFailureListener {
                onResult(null, it)
            }
    }
// runBatch()는 read만 가능 runTransaction은 write도 가능
    // Transaction: 하나 이상의 문서에 대한 read, write 작업 집합
    // Transaction클래스의 delete, get, set, update 메서드를 사용한다.
    fun increaseQuantity(documentId: String, onResult: (String?, Exception?) -> Unit) {
        firestore.runTransaction { transition ->
            val documentRef = cartCollection.document(documentId) // 카트 컬렉션에서 해당 document를 찾는것
            val document = transition.get(documentRef) // 그거를 transition으로 만들어 변경하거나 쓸 수 있도록 하는 객체 생성
            val productObject = document.toObject(CartProduct::class.java) // 그 객체를 CartProduct화 한다.
            productObject?.let { cartProduct ->
                val newQuantity = cartProduct.quantity + 1
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transition.set(documentRef,newProductObject)
            }
        }.addOnSuccessListener {
            onResult(documentId, null)
        }.addOnFailureListener {
            onResult(null, it)

        }
    }

    fun decreaseQuantity(documentId: String, onResult: (String?, Exception?) -> Unit) {
        firestore.runTransaction { transition ->
            val documentRef = cartCollection.document(documentId) // 카트 컬렉션에서 해당 document를 찾는것
            val document = transition.get(documentRef) // 그거를 transition으로 만들어 변경하거나 쓸 수 있도록 하는 객체 생성
            val productObject = document.toObject(CartProduct::class.java) // 그 객체를 CartProduct화 한다.
            productObject?.let { cartProduct ->
                val newQuantity = cartProduct.quantity - 1
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transition.set(documentRef,newProductObject)
            }
        }.addOnSuccessListener {
            onResult(documentId, null)
        }.addOnFailureListener {
            onResult(null, it)

        }
    }

    enum class QuantityChanging{
        INCREASE, DECREASE
    }



}