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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelineyt.R
import com.example.kelineyt.adapter.*
import com.example.kelineyt.adapter.makeIt.BestProductsAdapters
import com.example.kelineyt.adapter.makeIt.TemporarySpecialProductsAdapter
import com.example.kelineyt.databinding.FragmentMainCategoryBinding
import com.example.kelineyt.util.*
import com.example.kelineyt.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

private val TAG = "MainCategoryFragment"
@AndroidEntryPoint
class MainCategoryFragment: Fragment(R.layout.fragment_main_category) {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductsAdapter: TemporarySpecialProductsAdapter
    private lateinit var bestDealsAdapter: BestDealsAdapter
    private lateinit var bestProductsAdapter: BestProductsAdapters
    private var page = 1 // 현재 페이지

    private val viewModel by viewModels<MainCategoryViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO 여기서는 뷰모델로부터 총 3개를 받아야 한다. Special, Best Deals, Best Product
        //TODO 또한 각각 어댑터를 적용해야 한다.
        //TODO best Products같은 경우 10개마다 불러오는 등의 기술이 필요하다.
        temporarySpecialProductRv()

        lifecycleScope.launchWhenStarted {
            viewModel.specialProduct.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.mainCategoryProgressbar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.mainCategoryProgressbar.visibility = View.GONE
                        specialProductsAdapter.differ.submitList(it.data)
                    }
                    is Resource.Error -> {
                        binding.mainCategoryProgressbar.visibility = View.GONE

                    }
                    else -> Unit
                }
            }
        }
        bestProductsRv()
        lifecycleScope.launchWhenStarted {
            viewModel.bestProduct.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.bestProductsProgressbar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.bestProductsProgressbar.visibility = View.GONE
                        bestProductsAdapter.differ.submitList(it.data)

                    }
                    is Resource.Error -> {
                        binding.bestProductsProgressbar.visibility = View.GONE

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
                        binding.bestDealsProgressbar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.bestDealsProgressbar.visibility = View.GONE
                        bestDealsAdapter.differ.submitList(it.data)

                    }
                    is Resource.Error -> {
                        binding.bestDealsProgressbar.visibility = View.GONE

                    }
                    else -> Unit
                }
            }
        }


        binding.nestedScrollMainCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
//            Log.e("asd","${v.getChildAt(0).bottom} ${v.height} ${scrollY}")

            // v.getChildAt(0)은 초기 고정값(최대높이 정도?)인 것 같고, v.height는 nestedscrollview의 높이, scrollY는 현재 Y의 정도로 아래로 스크롤
            // 할수록 점점 커진다.
            if (v.getChildAt(0).bottom <= v.height + scrollY) {
                Log.e("asd", "${v.getChildAt(0).bottom} ${v.height} ${scrollY}")
                // 3411 1313 2098
                // 1번 스크롤시,
                // 5459 1313 4146
                viewModel.getBestProducts()

            }

        })

        //        bestProductsAdapter.onClick = {
//            val b = Bundle().apply { putParcelable("product", it)  }
//            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
//        }

        bestProductsAdapter.onClick = {product ->
            val b = Bundle().apply {
                putParcelable("product",product)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
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



//
//        setupSpecialProductRv()
//        lifecycleScope.launchWhenStarted {
//            viewModel.specialProducts.collectLatest {
//                when (it) {
//                    is Resource.Loading -> {
//                        showLoading()
//                    }
//                    is Resource.Success -> {
//                        // submitList(List<T> newList) : 리스트 데이터를 교체할 때 사용하면 된다.
//                        specialProductsAdapter.differ.submitList(it.data)
//                        hideLoading()
//                    }
//                    is Resource.Error -> {
//                        hideLoading()
//                        Log.e(TAG,it.message.toString())
//                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
//                    }
//                    else -> Unit
//                }
//            }
//        }
//
//        setupBestDealsRv()
//        lifecycleScope.launchWhenStarted {
//            viewModel.bestDeals.collectLatest {
//                when (it) {
//                    is Resource.Loading -> {
//                        showLoading()
//                    }
//                    is Resource.Success -> {
//                        // submitList(List<T> newList) : 리스트 데이터를 교체할 때 사용하면 된다.
//                        bestDealsAdapter.differ.submitList(it.data)
//                        hideLoading()
//                    }
//                    is Resource.Error -> {
//                        hideLoading()
//                        Log.e(TAG,it.message.toString())
//                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
//                    }
//                    else -> Unit
//                }
//            }
//        }
//
//        setupBestProductsRv()
//        lifecycleScope.launchWhenStarted {
//            viewModel.bestProducts.collectLatest {
//                when (it) {
//                    is Resource.Loading -> {
//                        binding.bestProductsProgressbar.visibility = View.VISIBLE
//
//                    }
//                    is Resource.Success -> {
//                        // submitList(List<T> newList) : 리스트 데이터를 교체할 때 사용하면 된다.
//                        bestProductsAdapter.differ.submitList(it.data)
//                        binding.bestProductsProgressbar.visibility = View.GONE
//
//                    }
//                    is Resource.Error -> {
//                        Log.e(TAG,it.message.toString())
//                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
//                        binding.bestProductsProgressbar.visibility = View.GONE
//
//                    }
//                    else -> Unit
//                }
//            }
//        }
//
//
//        specialProductsAdapter.onClick = {
//            val b = Bundle().apply { putParcelable("product", it)  }
//            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
//        }
//
//        bestProductsAdapter.onClick = {
//            val b = Bundle().apply { putParcelable("product", it)  }
//            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
//        }
//
//        bestDealsAdapter.onClick = {
//            val b = Bundle().apply { putParcelable("product", it)  }
//            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
//        }
//        //setOnScrollChangeListener을 사용하면 위로 스크롤을 했는지 또는 아래로 스크롤을 했는지 또는 지금 현재 가장 위인지 아래인지 판단할수 있습니다
//        binding.nestedScrollMainCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
////            Log.e("asd","${v.getChildAt(0).bottom} ${v.height} ${scrollY}")
//
//            // v.getChildAt(0)은 초기 고정값(최대높이 정도?)인 것 같고, v.height는 nestedscrollview의 높이, scrollY는 현재 Y의 정도로 아래로 스크롤
//            // 할수록 점점 커진다.
//            if (v.getChildAt(0).bottom <= v.height + scrollY) {
//                Log.e("asd","${v.getChildAt(0).bottom} ${v.height} ${scrollY}")
//                // 3411 1313 2098
//                // 1번 스크롤시,
//                // 5459 1313 4146
//                viewModel.fetchBestProducts()
//
//            }
//
//        })
//
//
//        binding.rvBestDealsProducts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                // 스크롤이 끝에 도달했는지 확인
//                if (!recyclerView.canScrollHorizontally(1)) {
//                    viewModel.fetchBestDeals()
//                }
//            }
//        })
//
//    }
//
//    private fun setupBestProductsRv() {
//        bestProductsAdapter = BestProductsAdapter()
//        binding.apply {
////            rvBestProducts.layoutManager = GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)
//            rvBestProducts.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
//
//
//            rvBestProducts.adapter = bestProductsAdapter
//            rvBestProducts.addItemDecoration(HorizontalItemDecorationForBestProducts())
//        }
//
//    }
//
//    private fun setupBestDealsRv() {
//        bestDealsAdapter = BestDealsAdapter()
//        binding.apply {
//            rvBestDealsProducts.adapter = bestDealsAdapter
//            rvBestDealsProducts.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//
//        }
//    }
//
//    private fun hideLoading() {
//        binding.mainCategoryProgressbar.visibility = View.GONE
//    }
//
//    private fun showLoading() {
//        binding.mainCategoryProgressbar.visibility = View.INVISIBLE
//    }
//
//    private fun setupSpecialProductRv() {
//        specialProductsAdapter = SpecialProductsAdapter()
//        binding.rvSpecialProducts.apply {
//            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
//            adapter = specialProductsAdapter
//
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//        showBottomNavigationView()
//    }


    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()

    }

    private fun bestDealsRv() {
        bestDealsAdapter = BestDealsAdapter()
        binding.rvBestDealsProducts.adapter = bestDealsAdapter
        binding.rvBestDealsProducts.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun bestProductsRv() {
        bestProductsAdapter = BestProductsAdapters()
        binding.rvBestProducts.adapter = bestProductsAdapter
        binding.rvBestProducts.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
    }

    private fun temporarySpecialProductRv() {
        specialProductsAdapter = TemporarySpecialProductsAdapter()
        binding.rvSpecialProducts.adapter = specialProductsAdapter
        binding.rvSpecialProducts.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


    }
}