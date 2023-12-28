package com.example.kelineyt.fragments.makeIt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.kelineyt.data.Category
import com.example.kelineyt.util.Resource
import com.example.kelineyt.viewmodel.makeIt.CategoryViewModels
import com.example.kelineyt.viewmodel.makeIt.BaseCategoryViewModel1
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class ChairsFragment: BaseFragment() {
    @Inject
    // @Inject : 의존성 주입(생성자 또는 메서드 등을 통해 외부로부터 생성된 객체를 전달받는 것)을 받겠다.
    lateinit var firestore: FirebaseFirestore
    val viewModel by viewModels<CategoryViewModels> {
        BaseCategoryViewModel1(firestore, Category.Chair)
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            viewModel.bestProduct.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showBestProductsProgressBar()
                    }
                    is Resource.Success -> {
                        bestProductsAdapter.differ.submitList(it.data)
                        hideBestProductsProgressBar()

                    }
                    is Resource.Error -> {
                        hideBestProductsProgressBar()
                        Snackbar.make(requireView(),it.message.toString(),Snackbar.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.specialProduct.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showOfferProgressBar()

                    }
                    is Resource.Success -> {
                        offerAdapter.differ.submitList(it.data)
                        hideOfferProgressBar()
                    }
                    is Resource.Error -> {
                        hideOfferProgressBar()
                        Snackbar.make(requireView(),it.message.toString(),Snackbar.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }


    }

}