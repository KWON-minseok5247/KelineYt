package com.example.kelineyt.paging

import android.nfc.tech.MifareUltralight
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.kelineyt.data.Product
import com.example.kelineyt.helper.FirestorePagingSource
import com.example.kelineyt.util.Constants.PAGE_SIZE
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MyPagingViewModel @Inject constructor(
    private val collectionReference: CollectionReference
) : ViewModel() {
//    val flow = Pager(
//        PagingConfig( //// 예시: 한 번에 PAGE_SIZE 개의 아이템을 로드
//            pageSize = MifareUltralight.PAGE_SIZE,
//            enablePlaceholders = false  // 빈 자리 표시자 비활성화
//        )
//    ) {
//        MyFirebasePagingSource(collectionReference)
//    }.flow.cachedIn(viewModelScope)
//    val pagingDataFlow: Flow<PagingData<Product>> = Pager(
//        config = PagingConfig(pageSize = 20),
//        pagingSourceFactory = { MyFirebasePagingSource(collectionReference) }
//    ).flow
// PagingData가 이미 페이지별로 구분된 형태를 제공해서 List<Product>로 만들 필요는 없다.
//    val pagingDataFlow: Flow<PagingData<Product>> = Pager(
//        config = PagingConfig(
//            pageSize = 4,
//        ),
//        pagingSourceFactory = {
//            val source = MyFirebasePagingSource(collectionReference)
//            Log.d("PagingSourceFactory", "Created PagingSource: $source")
//            source
//        }
//    ).flow
    val flow = Pager(
        PagingConfig(
            pageSize = PAGE_SIZE
        )
    ) {
        MyFirebasePagingSource(collectionReference)
    }.flow.cachedIn(viewModelScope)




}
