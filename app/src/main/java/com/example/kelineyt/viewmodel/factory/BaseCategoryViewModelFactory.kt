package com.example.kelineyt.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.kelineyt.data.Category
import com.example.kelineyt.viewmodel.CategoryViewModel
import com.google.firebase.firestore.FirebaseFirestore
///https://philosopher-chan.tistory.com/1471
//https://minhyuuk.tistory.com/entry/AndroidKotlin-ViewModelFactory%EB%A5%BC-%EC%82%AC%EC%9A%A9%ED%95%98%EC%9E%90
// 뷰모델 객체를 만드는 것!
// 아마도 체어, 가구 등 여러 뷰모델이 필요하니까 category 종류에 맞는 viewModel을 끌어오기 위해 factory를 사용하는 것 같다.

// 만약 ViewModel에 생성자로 파라미터를 넘겨줄 때, ViewModel Factory를 사용하여 파라미터를 전달할 수 있습니다.
// 내 생각에는 카테고리마다 받아야 하는 데이터가 전부 다르다. 그래서 뷰모델 파라미터에 category를 넣어 해당
// 프래그먼트에서 바로 적용할 수 있도록 한다.
//
// 특정 프래그먼트에서 내가 원하는 파라미터를 넣을 수 있도록 한다. 그게 팩토리의 역할이다.
class BaseCategoryViewModelFactory(
    private val firestore: FirebaseFirestore,
    private val category: Category
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel(firestore,category) as T
        //CategoryViewModel의 파라미터 값을 넘겨준다.
    }


}