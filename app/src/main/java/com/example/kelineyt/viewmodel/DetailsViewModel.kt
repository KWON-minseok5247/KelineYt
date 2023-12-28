package com.example.kelineyt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kelineyt.data.CartProduct
import com.example.kelineyt.firebase.FireBaseCommon
import com.example.kelineyt.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 새로운 sub collection을 만들고 값 추가하기 위해서 uid나 authentication등이 필요하다.
// 여기 뷰모델의 핵심은 프래그먼트나 액티비티 등의 ui관리하는 일과 별개로 직접 firebase에서 저장하거나 가져오는 역할을 위주로 수행한다.

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val fireBaseCommon: FireBaseCommon
) : ViewModel() {


    private val _addToCart = MutableStateFlow<Resource<CartProduct>>(Resource.unspecified())
    val addToCart = _addToCart.asStateFlow()

    // 유저가 장바구니에 추가를 했는데 한 개를 더 추가하는 경우와 아무것도 없는데 추가하는 과정은 다르게 접근하는게 맞다.
    // collection에서도 user인지 User인지 잘 살펴봐야한다.(파이어베이스에 들어가서 비교해봐라.)
    // 또한 우리는 user 내에서도 누구 유저인지 확인하기 위해서 uid를 확인해야 하는데 k49BV5yzkObKOZOARQLMvA4Jy5E2 이런 식으로 되어있다.
    // 따라서 document(auth.uid)로 하면 된다. 이미 로그인하고 장바구니에 물건 넣는 시점에서 uid는 null이 나올 수 없기에 !!를 한다.
    // whereEqualTo()는 이미 존재하는지 그런거 비교하는것
    fun addUpdateProductInCart(cartProduct: CartProduct) {
        viewModelScope.launch {
            _addToCart.emit(Resource.Loading())
        }
        // sub collection이라고 해도 collection으로 하는 구나., 또 해당 순서대로 해야 제대로 된 값을 얻을 수 있는 듯?
        // whereEqualto에서 product.id는 파이어베이스의 id다.
        // firestore의 collection 은 QuerySnapshot 이라는 걸 리턴
        //즉 collection 스트림에서 QuerySnapshot 을 얻으면 컬렉션 정보가 다 들어있는데
        // 여기서 data 에는 컬렉션에 포함된 모든 문서 정보가 DocumentSnapshot 형태로 들어있습니다.
        firestore.collection("user").document(auth.uid!!).collection("cart")
            // whereEqualTo는 해당 제품이 cart에 담겨져 있는지에 대한 것, 그리고 product.id 형식으로 입력을 할 수도 있나보네.
            .whereEqualTo("product.id",cartProduct.product.id).get() // cartProduct.product.id는 chair 6 등의 고유 id(46ddc94d-0f5f-4083-8184-7addd7d235b7)값을 의미한다.
            .addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()) { // Add new product
                        addNewProducts(cartProduct)
                    } else {
                        // 다큐먼트에 해당 cartProduct가 있다면 파이어베이스로부터 객체로 불러온다.
                        val product = it.first().toObject(CartProduct::class.java)
                        if (product == cartProduct) { // increase the quantity
                            val documentId = it.first().id
                            // documentId는 cart 내 문서의 제목을 일컫는다. HHqBJDnIiG5gqyJ5j7vN 이런 식으로

                            increaseQuantities(documentId, cartProduct)
                        } else { // Add new product
                            addNewProducts(cartProduct)
                        }
                    }
                }
            }.addOnFailureListener {
                viewModelScope.launch { _addToCart.emit(Resource.Error(it.message.toString())) }

            }
    }

    private fun addNewProduct(cartProduct: CartProduct) {
        // onResult: (CartProduct?, Exception?) -> Unit) 이것 때문에 addedProduct랑 e를 이용할 수 있다.
        val cartCollection = firestore.collection("user").document(auth.uid!!).collection("cart")
        cartCollection.document().set(cartProduct).addOnSuccessListener {
            viewModelScope.launch {
                _addToCart.emit(Resource.Success(cartProduct))
            }

        }.addOnFailureListener {
            viewModelScope.launch {
                _addToCart.emit(Resource.Error(it.message.toString()))
            }
        }

        fireBaseCommon.addProductToCart(cartProduct){ addedProduct, e ->
            // 여기부터가 Unit이네....
            viewModelScope.launch {
                if (e == null) // exception이 없다면 성공한 것으로 간주
                    _addToCart.emit(Resource.Success(addedProduct!!))
                else
                    _addToCart.emit(Resource.Error(e.message.toString()))
            }
        }
    }

    private fun addNewProducts(cartProduct: CartProduct) {
        // onResult: (CartProduct?, Exception?) -> Unit) 이것 때문에 addedProduct랑 e를 이용할 수 있다.
        val cartCollection = firestore.collection("user").document(auth.uid!!).collection("cart")
        cartCollection.document().set(cartProduct).addOnSuccessListener {
            viewModelScope.launch {
                _addToCart.emit(Resource.Success(cartProduct))
            }

        }.addOnFailureListener {
            viewModelScope.launch {
                _addToCart.emit(Resource.Error(it.message.toString()))
            }
        }

//        fireBaseCommon.addProductToCart(cartProduct){ addedProduct, e ->
//            // 여기부터가 Unit이네....
//            viewModelScope.launch {
//                if (e == null) // exception이 없다면 성공한 것으로 간주
//                    _addToCart.emit(Resource.Success(addedProduct!!))
//                else
//                    _addToCart.emit(Resource.Error(e.message.toString()))
//            }
//        }
    }

    // 여기 밑에는 내가 직접 한 모양이네
    private fun increaseQuantity(documentId: String, cartProduct: CartProduct) {
        // 파이어베이스의 데이터를 documentId를 통해 뽑아내고 해당 quantity를 1 증가시킨다.
        // 과정 1 -> 해당 카트 컬렉션에서 id를 통해 해당 문서를 추출한다.
        // 과정 2 -> 문서를 읽고 수정할 수 있는 객체를 get을 통해 생성한다.
        // 과정 3 -> 그 객체를 우리가 쉽게 볼 수 있도록 cartProduct 형태로 오브젝트화한다.
        // 과정 4 -> 그 오브젝트 내에서 quantity를 1 증가시킨 후 set을 통해 저장한다.
        val cartCollection = firestore.collection("user").document(auth.uid!!).collection("cart")

        firestore.runTransaction { transition ->
            //DocumentReference는 Firestore 데이터베이스에서 문서 위치를 가리킴.
            val documentRef = cartCollection.document(documentId) // 카트 컬렉션에서 해당 document를 찾는것
            val document = transition.get(documentRef) // 그거를 transition으로 만들어 변경하거나 쓸 수 있도록 하는 객체 생성
            val productObject = document.toObject(CartProduct::class.java) // 그 객체를 CartProduct화 한다.
            productObject?.let { cartProduct ->
                val newQuantity = cartProduct.quantity + 1
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transition.set(documentRef,newProductObject)
            }
        }.addOnSuccessListener {
            viewModelScope.launch {
                _addToCart.emit(Resource.Success(cartProduct))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _addToCart.emit(Resource.Error(it.message.toString()))
            }
        }
//        firestore.runTransaction {transition ->
//            firestore.collection("user").document(auth.uid!!)
//                .collection("cart").whereEqualTo("product.id",documentId).get()
//                .addOnSuccessListener {
//                    val cartObject = it.first().toObject(CartProduct::class.java) // object화가 안된다면 first()를 고려해보기???
//                    val newQuantity = cartObject.quantity + 1
//                    cartObject.copy(quantity = newQuantity)
//                    transition.set(firestore.document(documentId),cartObject)
//
//                }.addOnFailureListener {
//
//                }
//        }



        fireBaseCommon.increaseQuantity(documentId) {_, e ->
            viewModelScope.launch {
                if (e == null)
                    _addToCart.emit(Resource.Success(cartProduct))
                else
                    _addToCart.emit(Resource.Error(e.message.toString()))
            }
        }
    }

    // 파이어베이스의 데이터를 documentId를 통해 뽑아내고 해당 quantity를 1 증가시킨다.
    // 과정 1 -> 해당 카트 컬렉션에서 id를 통해 해당 문서를 추출한다.
    // 과정 2 -> 문서를 읽고 수정할 수 있는 객체를 get을 통해 생성한다.
    // 과정 3 -> 그 객체를 우리가 쉽게 볼 수 있도록 cartProduct 형태로 오브젝트화한다.
    // 과정 4 -> 그 오브젝트 내에서 quantity를 1 증가시킨 후 set을 통해 저장한다.
    private fun increaseQuantities(documentId: String, cartProduct: CartProduct) {
        val cartCollection = firestore.collection("user").document(auth.uid!!)
            .collection("cart")
        firestore.runTransaction {transition ->
            val documentRefer = cartCollection.document(documentId)
            val document = transition.get(documentRefer)
            val temporaryCartProduct = document.toObject(CartProduct::class.java)

            temporaryCartProduct.let {
                val newQuantity = it!!.quantity + 1
                val newCartProduct = it.copy(quantity = newQuantity)
                transition.set(documentRefer,newCartProduct)
            }

        }.addOnSuccessListener {
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