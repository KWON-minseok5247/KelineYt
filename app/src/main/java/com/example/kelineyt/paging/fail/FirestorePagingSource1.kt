package com.example.kelineyt.paging.fail

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.kelineyt.data.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

//class FirestorePagingSource1(private val db: FirebaseFirestore) : PagingSource<QuerySnapshot, Product>() {
//
//    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Product> {
//        return try {
//            val currentPage = params.key ?: db.collection("Products").limit(10).get().await()
//
//            val lastDocumentSnapshot = currentPage.documents[currentPage.size() - 1]
//            val nextPage = db.collection("countries").limit(10).startAfter(lastDocumentSnapshot)
//                .get()
//                .await()
//
//            LoadResult.Page(
//                data = currentPage.toObjects(Product::class.java),
//                prevKey = null,
//                nextKey = nextPage
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<QuerySnapshot, Product>): QuerySnapshot? {
//        TODO("Not yet implemented")
//    }
//}