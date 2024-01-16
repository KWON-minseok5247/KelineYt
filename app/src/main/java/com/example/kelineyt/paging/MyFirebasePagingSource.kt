package com.example.kelineyt.paging

import android.util.Log
import androidx.paging.*
import com.example.kelineyt.data.Product
import com.example.kelineyt.util.Constants.PAGE_SIZE
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class MyFirebasePagingSource(private val collectionReference: CollectionReference) :
    PagingSource<Int, Product>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        try {
            val currentPage = params.key ?: 0
            val pageSize = params.loadSize

            val dataList = fetchDataFromFirebase(currentPage, pageSize)
            Log.e("PagingSource", "Load data for page $currentPage: ${dataList.joinToString { it.toString() }}")

            return LoadResult.Page(
                data = dataList,
                prevKey = if (currentPage == 0) null else currentPage - 1,
                nextKey = if (dataList.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            Log.e("PagingSource", "Error loading data", e)
            return LoadResult.Error(e)
        }
//
//        return try {
//            // 현재 페이지 가져오기
//            val currentPage = params.key ?: 0
//
//            // 데이터 가져오기
//            val data = fetchDataFromFirebase(currentPage)
//
//            // 다음 페이지 계산 (여기서는 간단히 다음 페이지가 현재 페이지 + 1인 것으로 가정)
//            val nextPage = currentPage + 1
//
//            // LoadResult 반환
//            LoadResult.Page(
//                data = data,
//                prevKey = if (currentPage == 0) null else currentPage - 1,
//                nextKey = nextPage
//            )
//        } catch (e: Exception) {
//            // 에러 처리
//            LoadResult.Error(e)
//        }
    }

    private suspend fun fetchDataFromFirebase(page: Int, pageSize: Int): List<Product> {
        // Firebase에서 데이터 가져오기
        // 여기에서는 간단한 예시로 Firebase Realtime Database를 사용했습니다.
        // 실제로는 Firestore 또는 다른 데이터 소스를 사용할 수 있습니다.

        // 데이터 가져오는 로직 작성...

        // 예시: Firebase Realtime Database에서 데이터 가져오기
        val dataSnapshot = collectionReference.limit(pageSize.toLong()).get().await()

//        val query: Query = collectionReference
//            .orderBy("pageNumber")  // 예시: 페이지 번호를 가지고 정렬된 데이터를 사용
//            .startAt(page * pageSize)
//            .limit(pageSize.toLong())


        // 데이터 변환
        val dataList = mutableListOf<Product>()
        for (snapshot in dataSnapshot.documents) {
            // YourDataType에 따라 데이터 변환 로직 작성
            val data = snapshot.toObject(Product::class.java)
            data?.let { dataList.add(it) }
        }

        return dataList
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        // 페이지를 새로 고치기 위한 키 반환 (일반적으로 null 또는 가장 최근의 페이지 번호 반환)
        return null
    }
}