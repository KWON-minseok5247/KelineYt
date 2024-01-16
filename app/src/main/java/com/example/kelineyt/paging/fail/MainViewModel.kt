package com.example.kelineyt.paging.fail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.kelineyt.helper.FirestorePagingSource
import com.google.firebase.firestore.FirebaseFirestore

//class MainViewModel : ViewModel() {
//
//    val flow = Pager(PagingConfig(20)) {
//        FirestorePagingSource1(FirebaseFirestore.getInstance())
//    }.flow.cachedIn(viewModelScope)
//
//}