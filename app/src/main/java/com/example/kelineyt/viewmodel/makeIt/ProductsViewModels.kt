package com.example.kelineyt.viewmodel.makeIt

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.paging.cachedIn
import com.example.kelineyt.data.Product
import com.example.kelineyt.helper.FirestorePagingSource
import com.google.firebase.firestore.Query
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ProductsViewModels @Inject constructor(
    private val queryProductsByName: Query
) : ViewModel() {
    val flow = Pager(
        PagingConfig( //// 예시: 한 번에 PAGE_SIZE 개의 아이템을 로드
            pageSize = PAGE_SIZE,
            enablePlaceholders = false  // 빈 자리 표시자 비활성화
        )
    ) {
        FirestorePagingSource(queryProductsByName)
    }.flow.cachedIn(viewModelScope)

//    val pagingDataFlow: Flow<PagingData<Product>> = Pager(
//            config = PagingConfig(pageSize = 10),
//            remoteMediator = FirestorePagingSource(queryProductsByName)
//        ) {
//        FirestorePagingSource(queryProductsByName)
//        }.flow.cachedIn(viewModelScope)



    //val flow = Pager(
    //  // 어떻게 데이터를 가져올건지 추가적인 속성을 사용하자.
    //  PagingConfig(pageSize = 20)
    //) {
    //  ExamplePagingSource(backend, query)
    //}.flow
    //  .cachedIn(viewModelScope)




    //// Paging Source만 사용하는 형태의 Pager생성
    //val pagingData: Flow<PagingData<TestPagingData>> = Pager(
    //        config = PagingConfig(pageSize = 10)
    //    ) {
    //        PagingSourceExample()
    //    }.flow.cachedIn(viewModelScope)
    //
    //// RemoteMediator를 사용하는 형태의 Pager생성
    //val pagingDataFlow: Flow<PagingData<TestPagingData>> = Pager(
    //        config = PagingConfig(pageSize = 10),
    //        remoteMediator = TestRemoteMediator()
    //    ) {
    //        PagingSourceExample()
    //    }.flow
}