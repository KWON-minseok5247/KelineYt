package com.example.kelineyt.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kelineyt.data.Category
import com.example.kelineyt.viewmodel.CategoryViewModel
import com.google.firebase.firestore.FirebaseFirestore
//https://minhyuuk.tistory.com/entry/AndroidKotlin-ViewModelFactory%EB%A5%BC-%EC%82%AC%EC%9A%A9%ED%95%98%EC%9E%90
// 뷰모델 객체를 만드는 것!
// 아마도 체어, 가구 등 여러 뷰모델이 필요하니까 category 종류에 맞는 viewModel을 끌어오기 위해 factory를 사용하는 것 같다.
class BaseCategoryViewModelFactory(
    private val firestore: FirebaseFirestore,
    private val category: Category
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel(firestore,category) as T
    }

}