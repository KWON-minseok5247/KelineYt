package com.example.kelineyt.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.*
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kelineyt.R
import com.example.kelineyt.adapter.*
import com.example.kelineyt.adapter.makeIt.BestProductsAdapters
import com.example.kelineyt.adapter.makeIt.ProductsAdapter
import com.example.kelineyt.adapter.makeIt.StringAdapters
import com.example.kelineyt.adapter.makeIt.TemporarySpecialProductsAdapter
import com.example.kelineyt.data.Product
import com.example.kelineyt.databinding.FragmentMainCategoryBinding
import com.example.kelineyt.databinding.TemperarymainBinding
import com.example.kelineyt.helper.StickyHeaderItemDecoration
import com.example.kelineyt.paging.MyFirebasePagingSource
import com.example.kelineyt.paging.MyPagingDataAdapter
import com.example.kelineyt.paging.MyPagingViewModel
import com.example.kelineyt.paging.SampleLoadStateAdapter
import com.example.kelineyt.paging.viewholder.*
import com.example.kelineyt.paging.viewholder.data.SpecialProductsItems
import com.example.kelineyt.paging.viewholder.data.TvBestDealsItems
import com.example.kelineyt.paging.viewholder.data.TvBestProductsItems
//import com.example.kelineyt.paging.CountryAdapter
//import com.example.kelineyt.paging.MainViewModel
import com.example.kelineyt.util.*
import com.example.kelineyt.viewmodel.MainCategoryViewModel
import com.example.kelineyt.viewmodel.makeIt.ProductsViewModels
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
private val TAG = "MainCategoryFragment"
@AndroidEntryPoint
class MainCategoryFragment: Fragment(R.layout.fragment_main_category) {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductsAdapter: TemporarySpecialProductsAdapter
    private lateinit var bestDealsAdapter: BestDealsAdapter
    private lateinit var stringAdapter: StringAdapters

    //    private lateinit var bestProductsAdapter: BestProductsAdapters
//    private lateinit var productsAdapters: ProductsAdapter
//    private val productsViewModel by viewModels<ProductsViewModels>()
    private var page = 1 // 현재 페이지

    private lateinit var myPagingDataAdapter: MyPagingDataAdapter
    private val myFirebaseViewModel by viewModels<MyPagingViewModel>()

    private val sampleLoadStateAdapter = SampleLoadStateAdapter()


    //    private val mainViewModel by viewModels<MainViewModel>()
//    private val countryAdapter = CountryAdapter()
//    private val collectionReference = FirebaseFirestore.getInstance().collection("Products")
    private val viewModel by viewModels<MainCategoryViewModel>()

    private val horizontalWrapperAdapter: HorizontalWrapperAdapter by lazy {
        HorizontalWrapperAdapter(bestDealsAdapter)
    }
    private val horizontalWrapperForSpecialAdapter: HorizontalWrapperForSpecialAdapter by lazy {
        HorizontalWrapperForSpecialAdapter(specialProductsAdapter)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(TAG, "onCreateView")

        binding = FragmentMainCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 여기서는 뷰모델로부터 총 3개를 받아야 한다. Special, Best Deals, Best Product
        // 또한 각각 어댑터를 적용해야 한다.
        // best Products같은 경우 10개마다 불러오는 등의 기술이 필요하다.
        Log.e(TAG, "onViewCreated")

            myFirebaseStorageRv()
            // Paging 데이터 흐름 설정
//        productRv()
            lifecycleScope.launchWhenStarted {
                myFirebaseViewModel.flow.collectLatest {
                    myPagingDataAdapter.submitData(it)
                }
            }



//        myPagingDataAdapter.addLoadStateListener { loadState ->
//            if (loadState.refresh is LoadState.Loading) {
////                binding.mainCategoryProgressbar.visibility = View.VISIBLE
//            } else {
////                binding.mainCategoryProgressbar.visibility = View.GONE
//            }
//        }

            temporarySpecialProductRv()

            lifecycleScope.launchWhenStarted {
                viewModel.specialProduct.collectLatest {
                    when (it) {
                        is Resource.Loading -> {
                            binding.rvBestProducts.visibility = View.INVISIBLE
                            binding.bestProductsProgressbar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            specialProductsAdapter.differ.submitList(it.data)
                        }
                        is Resource.Error -> {
//                        binding.mainCategoryProgressbar.visibility = View.GONE
                        }
                        else -> Unit
                    }
                }
            }

            bestDealsRv()
            lifecycleScope.launchWhenStarted {
                viewModel.bestDeals.collectLatest {
                    when (it) {
                        is Resource.Loading -> {
//                        binding.bestDealsProgressbar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding.rvBestProducts.visibility = View.VISIBLE
                            binding.bestProductsProgressbar.visibility = View.GONE
                            bestDealsAdapter.differ.submitList(it.data)

                        }
                        is Resource.Error -> {
//                        binding.bestDealsProgressbar.visibility = View.GONE

                        }
                        else -> Unit
                    }
                }
            }
            val firstStringAdapter = StringAdapters("Best Deals")
            firstStringAdapter.apply {

            }
            val viewPool = RecyclerView.RecycledViewPool()
            val concatAdapter = ConcatAdapter(
                ConcatAdapter.Config.Builder().apply {
                    setIsolateViewTypes(false)

                }.build(),
                horizontalWrapperForSpecialAdapter,
                StringAdapters("Best Deals"),
                horizontalWrapperAdapter,
                StringAdapters("Best Products"),
                myPagingDataAdapter
            )

            binding.rvBestProducts.setRecycledViewPool(viewPool)
            binding.rvBestProducts.adapter = concatAdapter
            binding.rvBestProducts.layoutManager = GridLayoutManager(requireContext(), 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (concatAdapter.getItemViewType(position)) {
                            R.layout.product_rv_item -> 1
                            else -> 2
//                        HorizontalWrapperForSpecialAdapter.VIEW_TYPE -> 2
//                        StringAdapters.VIEW_TYPE -> 2
//                        HorizontalWrapperAdapter.VIEW_TYPE -> 2
//                        else -> 1

                        }
                    }
                }
            }
        bestDealsAdapter.onClick = {product ->
            val b = Bundle().apply {
                putParcelable("product",product)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }

        specialProductsAdapter.onClick = {product ->
            val b = Bundle().apply {
                putParcelable("product",product)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }

        myPagingDataAdapter.onClick = {product ->
            val b = Bundle().apply {
                putParcelable("product",product)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }


    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        horizontalWrapperAdapter.onSaveState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.e(TAG, "onViewStateRestored")

        val lastVisiblePosition = savedInstanceState?.getInt("lastVisiblePosition", 0) ?: 0
        binding.rvBestProducts.scrollToPosition(lastVisiblePosition)

    }

//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//    }
override fun onStart() {
    super.onStart()
    Log.e(TAG, "onStart")

}
    override fun onResume() {
        super.onResume()
    Log.e(TAG, "onResume")
        showBottomNavigationView()
    }

    override fun onPause() {

        super.onPause()
        Log.e(TAG, "onPause")

    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(TAG, "onDestroyView")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy")

    }




    private fun bestDealsRv() {
        bestDealsAdapter = BestDealsAdapter()

//        binding.rvBestDealsProducts.adapter = bestDealsAdapter
//        binding.rvBestDealsProducts.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }
    private fun temporarySpecialProductRv() {
        specialProductsAdapter = TemporarySpecialProductsAdapter()
//        binding.rvSpecialProducts.adapter = specialProductsAdapter
//        binding.rvSpecialProducts.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }
    private fun myFirebaseStorageRv() {
        myPagingDataAdapter = MyPagingDataAdapter()

//        binding.rvBestProducts.adapter = myPagingDataAdapter
//        binding.rvBestProducts.layoutManager =
//            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
    }

}
