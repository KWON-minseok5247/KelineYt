package com.example.kelineyt.paging

import android.util.Log
import androidx.paging.*
import com.example.kelineyt.data.Product
import com.example.kelineyt.util.Constants.PAGE_SIZE
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class MyFirebasePagingSource(private val queryProductsByName: Query) :
    PagingSource<QuerySnapshot, Product>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, Product>): QuerySnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Product> {
        return try {
            Log.e("log가 실행되었다", "이론상 5번 정도")
            val currentPage = params.key ?: queryProductsByName.get().await()
            val lastVisibleProduct = currentPage.documents.lastOrNull()

            if (lastVisibleProduct != null) {
                val nextPage = queryProductsByName.startAfter(lastVisibleProduct).get().await()

                return LoadResult.Page(
                    data = currentPage.toObjects(Product::class.java),
                    prevKey = null,
                    nextKey = nextPage
                )
            } else {
                // 리스트가 비어있을 때
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }
        } catch (e: Exception) {
            Log.e("FirestorePagingSource", "Error in load", e)
            return LoadResult.Error(e)
        }









//
//            val nextPage = queryProductsByName.startAfter(lastVisibleProduct).get().await()
//
//            Log.d("FirestorePagingSource", "Load called. CurrentPage size: ${currentPage.size()}, NextPage size: ${nextPage.size()}")
//
//            // TODO 얘가 왜 스크롤할 때가 아니라 시작하자마자 전부 바로 실행이 되지? 해결해야 한다.
//            LoadResult.Page(
//                data = currentPage.toObjects(Product::class.java),
//                prevKey = null,
//                nextKey = nextPage
//            )
//        } catch (e: Exception) {
//            Log.e("FirestorePagingSource", "Error in load", e)
//
//            LoadResult.Error(e)
//        }
//
//        try {
//            val currentPage = params.key ?: queryProductsByName.get().await()
//            val nextPage = queryProductsByName.startAfter(currentPage).get().await()
//
//            val productList = currentPage.toObjects(Product::class.java)
//            Log.e("productlist", "${currentPage.toString()} ${nextPage.documents.toString()}, ")
//
//            return LoadResult.Page(
//                data = productList,
//                prevKey = null, // 이전 페이지는 없음
//                nextKey = nextPage // 다음 페이지는 다음 쿼리 결과
//            )
//        } catch (e: Exception) {
//            return LoadResult.Error(e)
//        }
    }


}