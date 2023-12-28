package com.example.kelineyt.viewmodel.makeIt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kelineyt.data.Category
import com.google.firebase.firestore.FirebaseFirestore

class BaseCategoryViewModel1(
    private val firestore: FirebaseFirestore,
    private val category: Category
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModels(firestore, category) as T
    }

}