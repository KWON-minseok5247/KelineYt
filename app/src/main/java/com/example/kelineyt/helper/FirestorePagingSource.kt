package com.example.kelineyt.helper

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.kelineyt.data.Product
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

//https://velog.io/@hs0204/Paging-library%EB%A5%BC-%EC%95%8C%EC%95%84%EB%B3%B4%EC%9E%90
//  📍 PagingData
// 페이징된 데이터의 Container 역할을 한다. 데이터가 새로고침될 때마다 이에 상응하는 PagingData가 별도로 생성된다.
//
//  📍 PagingSource
// 로컬 데이터베이스 또는 네트워크로 데이터를 불러오는 것을 담당하는 추상 클래스이다.
//데이터 소스를 정의하고 데이터를 가져오는 방법을 정의한다.
//
//  📍 RemoteMediator
// 네트워크(Remote)에서 불러온 데이터를 로컬 데이터베이스에 캐시(Cache)하여 불러오는 것을 담당한다.
//오프라인 상태에서도 캐시된 데이터를 불러옴으로 유저 경험을 향상시켜줄 수 있다.
//(현재 Experimental 상태로 향후 변경될 수 있다.)
//

//📌 ViewModel Layer
//  📍 Pager
// Repository Layer에서 구현된 PagingSource과 함께 PagingData 인스턴스를 구성하는 반응형 스트림을 생성한다.
//PagingSource에서 데이터를 로드하는 방법, 옵션을 정의한 PagingConfig 클래스와 함께 사용된다.
//
//📌 UI Layer
//  📍 PagingDataAdapter
// PagingData를 RecyclerView에 바인딩하기 위해 사용된다.
//데이터를 어느 시점에서 더 받아올 것인가 등 UI와 관련된 대부분의 일을 책임진다.


//PagingSource란?
//특정 페이지 쿼리의 데이터 청크를 로드하는 기본 클래스. 데이터 레이어의 일부로,
// 일반적으로는 DataSource 클래스에서 확인할 수 있으며 이후에는 ViewModel에서 사용하기 위해
// Repository에서 사용된다.
// PagingSource 객체는 네트워크 소스 및 로컬 데이터베이스를 포함한 단일 소스에서 데이터를 로드할 수 있다.

//PagingConfig
//페이징 동작을 결정하는 매개변수를 정의하는 클래스. 페이지 크기, placeholder의 사용여부 등이 포함된다.

//Pager
//PagingData를 생성하는 클래스. PagingSource에 따라 다르게 실행되고, ViewModel에서 생성되어야 한다.

//PagingData
//페이지로 나눈 데이터의 스냅샷을 가지고 있는 컨테이너.
// 데이터를 새로고침할 때마다 자체 PagingSource로 지원되는 상응 PagingData가 별도로 생성된다.
// PagingSource 객체를 쿼리해 결과를 저장한다는 말이다.



class FirestorePagingSource (
    private val queryProductsByName: Query
) : PagingSource<QuerySnapshot, Product>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, Product>): QuerySnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Product> {
        return try {
            val currentPage = params.key ?: queryProductsByName.get().await()
            val lastVisibleProduct = currentPage.documents[currentPage.size() - 1]
            val nextPage = queryProductsByName.startAfter(lastVisibleProduct).get().await()


            // TODO 얘가 왜 스크롤할 때가 아니라 시작하자마자 전부 바로 실행이 되지? 해결해야 한다.
            LoadResult.Page(
                data = currentPage.toObjects(Product::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}