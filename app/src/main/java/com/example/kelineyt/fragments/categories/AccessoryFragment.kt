package com.example.kelineyt.fragments.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.kelineyt.data.Category
import com.example.kelineyt.util.Resource
import com.example.kelineyt.viewmodel.CategoryViewModel
import com.example.kelineyt.viewmodel.factory.BaseCategoryViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class AccessoryFragment: BaseCategoryFragment() {

    @Inject
    // @Inject : 의존성 주입(생성자 또는 메서드 등을 통해 외부로부터 생성된 객체를 전달받는 것)을 받겠다.
    lateinit var firestore: FirebaseFirestore
//    val view by viewModels<CategoryViewModel>(firestore,Category.Accessory) {}
//  위의 방식은 안통하니까 아래와같이 만들고 팩토리를 추가해야 한다.
    val viewModel by viewModels<CategoryViewModel> {
        // 내가 뷰 모델을 쓰긴 할 건데 여기 파라미터에 firestore와 Category.Accessory를 넣어라. 이 말임.
        BaseCategoryViewModelFactory(firestore, Category.Accessory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.offerProducts.collectLatest {
                when (it){
                    is Resource.Loading -> {
                        showOfferLoading()
                    }
                    is Resource.Success -> {
                        offerAdapter.differ.submitList(it.data)
                        hideOfferLoading()
                    }
                    is Resource.Error -> {
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG).show()
                        hideOfferLoading()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {
                when (it){
                    is Resource.Loading -> {
                        showBestProductsLoading()
                    }
                    is Resource.Success -> {
                        bestProductsAdapter.differ.submitList(it.data)
                        hideBestProductsLoading()
                    }
                    is Resource.Error -> {
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG).show()
                        hideBestProductsLoading()
                    }
                    else -> Unit
                }
            }
        }


        // 여기서 해야 할 것은 baseCategory에서 이미 다 완성했기 때문에 리사이클러뷰에 Products를 넣어주면 된다.



    }
}