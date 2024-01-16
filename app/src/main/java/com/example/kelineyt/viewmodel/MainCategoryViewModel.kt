package com.example.kelineyt.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kelineyt.data.Product
import com.example.kelineyt.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

//    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.unspecified())
//    val specialProducts : StateFlow<Resource<List<Product>>> = _specialProducts
//
//    private val _bestDeals = MutableStateFlow<Resource<List<Product>>>(Resource.unspecified())
//    val bestDeals : StateFlow<Resource<List<Product>>> = _bestDeals
//
//    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.unspecified())
//    val bestProducts : StateFlow<Resource<List<Product>>> = _bestProducts
//
//    private val pagingInfo = PagingInfo()
//    private val pagingBestDeals = PagingBestDeals()


    private val _specialProduct = MutableStateFlow<Resource<List<Product>>>(Resource.unspecified())
    val specialProduct = _specialProduct.asStateFlow()

    private val _bestProduct = MutableStateFlow<Resource<List<Product>>>(Resource.unspecified())
    val bestProduct = _bestProduct.asStateFlow()

    private val _bestDeals = MutableStateFlow<Resource<List<Product>>>(Resource.unspecified())
    val bestDeals: StateFlow<Resource<List<Product>>> = _bestDeals

    private var page = 1
    private var pagination = true
    private var emptyList = emptyList<Product>()


    init {
        getSpecialProduct()
//        getBestProducts()
        getBestDealsProducts()
    }

    private fun getBestDealsProducts() {
        viewModelScope.launch {
            _bestDeals.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("category", "Best Deals")
            .get().addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestDeals.emit(Resource.Success(products))
                }

            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestDeals.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun getBestProducts() {
        if (pagination) { // 페이지네이션이 가능할 때
            viewModelScope.launch {
                _bestProduct.emit(Resource.Loading())
            }
            firestore.collection("Products").limit((page * 10).toLong())
                .get().addOnSuccessListener {
                    val products = it.toObjects(Product::class.java)
                    viewModelScope.launch {
                        _bestProduct.emit(Resource.Success(products))
                        page++
                        if (emptyList != products) {
                            emptyList = products
                            pagination = true
                        } else {
                            pagination = false
                        }
                    }
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _bestProduct.emit(Resource.Error(it.message.toString()))
                    }
                }
        }

    }

    // 먼저 초기에 파이어베이스로부터 데이터를 받아야 한다. 여기 뷰모델에는 이게 제일 핵심임.
    // 그리고 해당 데이터를 어댑터와 연결을 시킨다. 프래그먼트에서는
    fun getSpecialProduct() {
        viewModelScope.launch {
            _specialProduct.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("category", "Special Products")
            .get().addOnSuccessListener {
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


//    init {
//        fetchSpecialProducts()
//        fetchBestDeals()
//        fetchBestProducts()
//    }
//
//    fun fetchBestProducts() {
////        viewModelScope.launch {
////            _bestProducts.emit(Resource.Loading())
////        } // 조건을 여러가지 추가할 때 파이어베이스에 인덱스를 미리 추가해둬야 값을 구할 수 있다.
//        //.whereEqualTo("category", "Chair").orderBy(
//        //                    "id",
//        //                    Query.Direction.ASCENDING
//        //                ) 이거 쓰면 쿼리형식으로 구해주는데 쿼리 없으면 파이어베이스에서 직접 추가를 해줘야 한다.
//
//        // BestProducts에는 whereEqualTo가 삭제됨. .whereEqualTo("category", "Best Products")
//        if (!pagingInfo.isPagingEnd) {
//            viewModelScope.launch {
//                _bestProducts.emit(Resource.Loading())
//                firestore.collection("Products")
//                    .limit(pagingInfo.bestProductsPage * 10).get()
//                    .addOnSuccessListener {result ->
//                        // 우리가 파이어베이스나 외부로부터 파일을 가져오기 위해서는 기본적으로 null이 될지도 모른다. 그래서 기본 생성자를 추가해야 한다. constructor
//                        val bestProducts = result.toObjects(Product::class.java)
//                        pagingInfo.isPagingEnd = bestProducts == pagingInfo.oldBestProducts
//                        pagingInfo.oldBestProducts = bestProducts
//
//                        viewModelScope.launch {
//                            _bestProducts.emit(Resource.Success(bestProducts))
//                        }
//                        pagingInfo.bestProductsPage ++
//                    }
//                    .addOnFailureListener {
//                        viewModelScope.launch {
//                            _bestProducts.emit(Resource.Error(it.message.toString()))
//                        }
//                    }
//            }
//
//        }
//
//    }
    internal data class PagingInfo(
        var bestProductsPage: Long = 1,
        var oldBestProducts: List<Product> = emptyList(),
        var isPagingEnd: Boolean = false
    )
//
//
//    fun fetchBestDeals() {
//        if (!pagingBestDeals.isBestDealsPagingEnd) {
//            viewModelScope.launch {
//                _bestDeals.emit(Resource.Loading())
//            }
//            // Products에서 category항목이 Special Products와 동일한 것을 찾는 것
//            firestore.collection("Products").whereEqualTo("category", "Best Deals").limit(pagingBestDeals.bestDealsPage * 3).get()
//                .addOnSuccessListener {result ->
//                    // 우리가 파이어베이스나 외부로부터 파일을 가져오기 위해서는 기본적으로 null이 될지도 모른다. 그래서 기본 생성자를 추가해야 한다. constructor
//                    val bestDealsList = result.toObjects(Product::class.java)
//                    if (bestDealsList != pagingBestDeals.oldBestDeals) {
//                        viewModelScope.launch {
//                            _bestDeals.emit(Resource.Success(bestDealsList))
//                        }
//                        pagingBestDeals.oldBestDeals = bestDealsList
//                        pagingBestDeals.bestDealsPage ++
//                    } else {
//                        pagingBestDeals.isBestDealsPagingEnd = true
//                    }
//
//                }
//                .addOnFailureListener {
//                    viewModelScope.launch {
//                        _bestDeals.emit(Resource.Error(it.message.toString()))
//                    }
//                }
//        }
//
//    }
//
    internal data class PagingBestDeals(
        var bestDealsPage: Long = 1,
        var oldBestDeals: List<Product> = emptyList(),
        var isBestDealsPagingEnd: Boolean = false
    )
//
////    fun fetchBestDeals() {
////        viewModelScope.launch {
////            _bestDeals.emit(Resource.Loading())
////        }
////        // Products에서 category항목이 Special Products와 동일한 것을 찾는 것
////        firestore.collection("Products").whereEqualTo("category", "Best Deals").get()
////            .addOnSuccessListener {result ->
////                // 우리가 파이어베이스나 외부로부터 파일을 가져오기 위해서는 기본적으로 null이 될지도 모른다. 그래서 기본 생성자를 추가해야 한다. constructor
////                val bestDealsList = result.toObjects(Product::class.java)
////                viewModelScope.launch {
////                    _bestDeals.emit(Resource.Success(bestDealsList))
////                }
////            }
////            .addOnFailureListener {
////                viewModelScope.launch {
////                    _bestDeals.emit(Resource.Error(it.message.toString()))
////                }
////            }
////    }
//
//    fun fetchSpecialProducts() {
//
//        viewModelScope.launch {
//            _specialProducts.emit(Resource.Loading())
//        }
//        // Products에서 category항목이 Special Products와 동일한 것을 찾는 것
//        firestore.collection("Products").whereEqualTo("category", "Special Products").get()
//            .addOnSuccessListener {result ->
//                // 우리가 파이어베이스나 외부로부터 파일을 가져오기 위해서는 기본적으로 null이 될지도 모른다. 그래서 기본 생성자를 추가해야 한다. constructor
//                val specialProductsList = result.toObjects(Product::class.java)
//                viewModelScope.launch {
//                    _specialProducts.emit(Resource.Success(specialProductsList))
//                }
//            }
//            .addOnFailureListener {
//                viewModelScope.launch {
//                    _specialProducts.emit(Resource.Error(it.message.toString()))
//                }
//            }
//    }
}