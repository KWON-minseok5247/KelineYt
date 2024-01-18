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
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class MyPagingViewModel @Inject constructor(
    private val queryProductsByName: Query
) : ViewModel() {
    val flow = Pager(
        PagingConfig(
            pageSize = PAGE_SIZE,
//            prefetchDistance = 1,
            enablePlaceholders = false,
//            maxSize = PAGE_SIZE * 4 // Pager 가 메모리에 최대로 가지고 있을 수 있는 항목의 개수

        )
    ) {
        MyFirebasePagingSource(queryProductsByName)
    }.flow.cachedIn(viewModelScope)


}
