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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kelineyt.R
import com.example.kelineyt.adapter.*
import com.example.kelineyt.adapter.makeIt.BestProductsAdapters
import com.example.kelineyt.adapter.makeIt.ProductsAdapter
import com.example.kelineyt.adapter.makeIt.TemporarySpecialProductsAdapter
import com.example.kelineyt.data.Product
import com.example.kelineyt.databinding.FragmentMainCategoryBinding
import com.example.kelineyt.helper.StickyHeaderItemDecoration
import com.example.kelineyt.paging.MyFirebasePagingSource
import com.example.kelineyt.paging.MyPagingDataAdapter
import com.example.kelineyt.paging.MyPagingViewModel
import com.example.kelineyt.paging.viewholder.CustomAdapter
import com.example.kelineyt.paging.viewholder.ListDataType
import com.example.kelineyt.paging.viewholder.ListDataWrapper
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
    private val myFirebaseViewModel by viewModels<MyPagingViewModel>()
    private val viewModel by viewModels<MainCategoryViewModel>()

    private lateinit var pagingProducts: PagingData<Product>
    private lateinit var specialProducts : List<Product>
//    private val bestDealsProducts: MutableList<Product> =
//        emptyList<Product>().toMutableList()

    private val mItems: ArrayList<ListDataWrapper> = arrayListOf()
    private lateinit var mAdapter: CustomAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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


//        lifecycleScope.launchWhenStarted {
//            myFirebaseViewModel.flow.collectLatest {
//                mItems.add(
//                    ListDataWrapper(
//                        ListDataType.TYPE_BEST_PRODUCTS,
//                        HeaderData("Header Type ViewHolder")
//                    )
//                )
//
//            }
//        }

        lifecycleScope.launchWhenStarted {
            viewModel.specialProduct.collectLatest {

                when (it) {
                    is Resource.Loading -> {
//                        binding.mainCategoryProgressbar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
//                        binding.mainCategoryProgressbar.visibility = View.GONE
//                        specialProductsAdapter.differ.submitList(it.data)
                         specialProducts = it.data!!
                        init()

                    }
                    is Resource.Error -> {
//                        binding.mainCategoryProgressbar.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }


//        lifecycleScope.launchWhenStarted {
//            viewModel.bestDeals.collectLatest {
//                when (it) {
//                    is Resource.Loading -> {
//                        binding.bestDealsProgressbar.visibility = View.VISIBLE
//                    }
//                    is Resource.Success -> {
//                        binding.bestDealsProgressbar.visibility = View.GONE
//                        bestDealsAdapter.differ.submitList(it.data)
//
//                    }
//                    is Resource.Error -> {
//                        binding.bestDealsProgressbar.visibility = View.GONE
//
//                    }
//                    else -> Unit
//                }
//            }
//        }




    }

    private fun init() {
        initSampleData()
        Log.e("melist", mItems.toString())

        mAdapter = CustomAdapter(mItems)
        binding.apply {
            rvMain.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvMain.itemAnimator = null
            rvMain.adapter = mAdapter
        }
    }

    private fun initSampleData() {
        mItems.add(
            ListDataWrapper(
                ListDataType.TYPE_TV_BEST_DEAL,
                TvBestDealsItems("처음")
            )
        )


        for (i in specialProducts) {
            Log.e("specialProducts", specialProducts.toString())

            mItems.add(
                ListDataWrapper(
                    ListDataType.TYPE_SPECIAL_PRODUCTS,
                    SpecialProductsItems(i)
                )
            )
        }

        mItems.add(
            ListDataWrapper(
                ListDataType.TYPE_TV_BEST_PRODUCTS,
                TvBestProductsItems("마지막")
            )
        )


//        val range = IntRange(0, 1)
//        for (idx in 0 until 100) {
//            if (idx.mod(20) == 0) {
//                mItems.add(
//                    ListDataWrapper(
//                        ListDataType.TYPE_AD_ITEM,
//                        AdItemData(
//                            "다코 라이프 일주일이면 내 취향 완벽 분석!\n" +
//                                    "맛집이 넘치는 세상, 다이닝코드의 빅데이터 기술로 내 취향에 맞는 맛집만을 쏙쏙 뽑아 보여드립니다.\n" +
//                                    "빅데이터 맛집 검색은 다이닝코드로"
//                        )
//                    )
//                )
//            }
//
//            when (range.random()) {
//                0 -> {
//                    mItems.add(
//                        ListDataWrapper(
//                            ListDataType.TYPE_SMALL_ITEM,
//                            SmallItemData("Small Type ViewHolder")
//                        )
//                    )
//                }
//                1 -> {
//                    mItems.add(
//                        ListDataWrapper(
//                            ListDataType.TYPE_LARGE_ITEM,
//                            LargeItemData("Large Type ViewHolder")
//                        )
//                    )
//                }
//            }
//        }


    }
}