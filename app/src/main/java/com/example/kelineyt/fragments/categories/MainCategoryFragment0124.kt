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

@AndroidEntryPoint
class MainCategoryFragment0124: Fragment(R.layout.fragment_main_category) {
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
        binding = FragmentMainCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 여기서는 뷰모델로부터 총 3개를 받아야 한다. Special, Best Deals, Best Product
        // 또한 각각 어댑터를 적용해야 한다.
        // best Products같은 경우 10개마다 불러오는 등의 기술이 필요하다.


        Log.e("view가 초기화?", "ㅇㅇㅇ")


        myFirebaseStorageRv()
        // Paging 데이터 흐름 설정
//        productRv()
        lifecycleScope.launchWhenStarted {
            myFirebaseViewModel.flow.collectLatest {
                when (it) {
                }
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

        val concatAdapter = ConcatAdapter(
            ConcatAdapter.Config.Builder().apply {
                setIsolateViewTypes(false)
            }.build(),
            horizontalWrapperForSpecialAdapter,
            StringAdapters("Best Deals")
            ,horizontalWrapperAdapter,StringAdapters("Best Products"),myPagingDataAdapter
        )

        binding.rvBestProducts.adapter = concatAdapter
        binding.rvBestProducts.layoutManager = GridLayoutManager(requireContext(), 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when(concatAdapter.getItemViewType(position)) {
                         R.layout.product_rv_item -> 1
                        else -> 2
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
        outState.putInt("lastVisiblePosition", (binding.rvBestProducts.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition() ?: 0)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val lastVisiblePosition = savedInstanceState?.getInt("lastVisiblePosition", 0) ?: 0
        binding.rvBestProducts.scrollToPosition(lastVisiblePosition)

    }

//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//    }
    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
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



//        bestProductsRv()
//        lifecycleScope.launchWhenStarted {
//            viewModel.bestProduct.collectLatest {
//                when (it) {
//                    is Resource.Loading -> {
//                        binding.bestProductsProgressbar.visibility = View.VISIBLE
//                    }
//                    is Resource.Success -> {
//                        binding.bestProductsProgressbar.visibility = View.GONE
//                        bestProductsAdapter.differ.submitList(it.data)
//
//                    }
//                    is Resource.Error -> {
//                        binding.bestProductsProgressbar.visibility = View.GONE
//
//                    }
//                    else -> Unit
//                }
//            }
//        }
// Activity에서는 lifecycleScope를 직접적으로 사용할 수 있지만
// Fragment는 viewLifecycleOwner.lifecycleScope를 사용해야 한다.

//        binding.nestedScrollMainCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
////            Log.e("asd","${v.getChildAt(0).bottom} ${v.height} ${scrollY}")
//
//            // v.getChildAt(0)은 초기 고정값(최대높이 정도?)인 것 같고, v.height는 nestedscrollview의 높이, scrollY는 현재 Y의 정도로 아래로 스크롤
//            // 할수록 점점 커진다.
//            if (v.getChildAt(0).bottom <= v.height + scrollY) {
//                Log.e("asd", "${v.getChildAt(0).bottom} ${v.height} ${scrollY}")
//                // 3411 1313 2098
//                // 1번 스크롤시,
//                // 5459 1313 4146
//                viewModel.getBestProducts()
//
//            }
//
//        })

//        bestProductsAdapter.onClick = { product ->
//            val b = Bundle().apply {
//                putParcelable("product",product)
//            }
//            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
//        }


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



//    private fun getSectionCallback(): StickyHeaderItemDecoration.SectionCallback {
//        return object : StickyHeaderItemDecoration.SectionCallback {
//            override fun isHeader(position: Int): Boolean {
//                return binding.rvBestProducts.isHeader(position)
//            }
//
//            override fun getHeaderLayoutView(list: RecyclerView, position: Int): View? {
//                return binding.rvBestProducts.getHeaderView(list, position)
//            }
//        }
//    }



//    private fun bestProductsRv() {
//        bestProductsAdapter = BestProductsAdapters()
//        binding.rvBestProducts.adapter = bestProductsAdapter
//        binding.rvBestProducts.layoutManager =
//            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
//    }


//    private fun productRv() {
//        productsAdapters = ProductsAdapter()
//        binding.rvBestProducts.adapter = productsAdapters
//        binding.rvBestProducts.layoutManager =
//            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
//    }

}
//private val TAG = "MainCategoryFragment"
//@AndroidEntryPoint
//class MainCategoryFragment: Fragment(R.layout.fragment_main_category) {
//    private lateinit var binding: FragmentMainCategoryBinding
//    private val myFirebaseViewModel by viewModels<MyPagingViewModel>()
//    private val viewModel by viewModels<MainCategoryViewModel>()
//
//    private lateinit var pagingProducts: PagingData<Product>
//    private lateinit var specialProducts : List<Product>
//    private lateinit var bestDeals : List<Product>
//
//
////    private val bestDealsProducts: MutableList<Product> =
////        emptyList<Product>().toMutableList()
//
//    private val mItems: ArrayList<ListDataWrapper> = arrayListOf()
//    private lateinit var mAdapter: CustomAdapter
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentMainCategoryBinding.inflate(layoutInflater)
//
//        return binding.root
//
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//
//        lifecycleScope.launchWhenStarted {
//            myFirebaseViewModel.flow.collectLatest {
//                pagingProducts = it
//
//            }
//        }
//
//        lifecycleScope.launchWhenStarted {
//            viewModel.specialProduct.collectLatest {
//
//                when (it) {
//                    is Resource.Loading -> {
////                        binding.mainCategoryProgressbar.visibility = View.VISIBLE
//                    }
//                    is Resource.Success -> {
////                        binding.mainCategoryProgressbar.visibility = View.GONE
////                        specialProductsAdapter.differ.submitList(it.data)
//                         specialProducts = it.data!!
////                        init()
//
//                    }
//                    is Resource.Error -> {
////                        binding.mainCategoryProgressbar.visibility = View.GONE
//                    }
//                    else -> Unit
//                }
//            }
//        }
//
//        lifecycleScope.launchWhenStarted {
//            viewModel.bestDeals.collectLatest {
//
//                when (it) {
//                    is Resource.Loading -> {
////                        binding.mainCategoryProgressbar.visibility = View.VISIBLE
//                    }
//                    is Resource.Success -> {
////                        binding.mainCategoryProgressbar.visibility = View.GONE
////                        specialProductsAdapter.differ.submitList(it.data)
//                        bestDeals = it.data!!
//
//                    }
//                    is Resource.Error -> {
////                        binding.mainCategoryProgressbar.visibility = View.GONE
//                    }
//                    else -> Unit
//                }
//            }
//        }
//
//
//
////        lifecycleScope.launchWhenStarted {
////            viewModel.bestDeals.collectLatest {
////                when (it) {
////                    is Resource.Loading -> {
////                        binding.bestDealsProgressbar.visibility = View.VISIBLE
////                    }
////                    is Resource.Success -> {
////                        binding.bestDealsProgressbar.visibility = View.GONE
////                        bestDealsAdapter.differ.submitList(it.data)
////
////                    }
////                    is Resource.Error -> {
////                        binding.bestDealsProgressbar.visibility = View.GONE
////
////                    }
////                    else -> Unit
////                }
////            }
////        }
//
//
//
//
//    }
//
//    private fun init() {
//        initSampleData()
//        Log.e("melist", mItems.toString())
//
//        mAdapter = CustomAdapter(mItems)
//        binding.apply {
//            rvMain.layoutManager =
//                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//            rvMain.itemAnimator = null
//            rvMain.adapter = mAdapter
//        }
//    }
//
//    private fun initSampleData() {
//
////        for (i in specialProducts) {
////            Log.e("specialProducts", specialProducts.toString())
////
////            mItems.add(
////                ListDataWrapper(
////                    ListDataType.TYPE_SPECIAL_PRODUCTS,
////                    SpecialProductsItems(i)
////                )
////            )
////        }
//
//        mItems.add(
//            ListDataWrapper(
//                ListDataType.TYPE_TV_BEST_DEAL,
//                TvBestDealsItems("처음")
//            )
//        )
//
//
//
//        mItems.add(
//            ListDataWrapper(
//                ListDataType.TYPE_TV_BEST_PRODUCTS,
//                TvBestProductsItems("마지막")
//            )
//        )
//
////        mItems.add(
////            ListDataWrapper(
////                ListDataType.TYPE_BEST_PRODUCTS,
////                TvBestProductsItems(pagingProducts)
////            )
////        )
//
//
//
////        val range = IntRange(0, 1)
////        for (idx in 0 until 100) {
////            if (idx.mod(20) == 0) {
////                mItems.add(
////                    ListDataWrapper(
////                        ListDataType.TYPE_AD_ITEM,
////                        AdItemData(
////                            "다코 라이프 일주일이면 내 취향 완벽 분석!\n" +
////                                    "맛집이 넘치는 세상, 다이닝코드의 빅데이터 기술로 내 취향에 맞는 맛집만을 쏙쏙 뽑아 보여드립니다.\n" +
////                                    "빅데이터 맛집 검색은 다이닝코드로"
////                        )
////                    )
////                )
////            }
////
////            when (range.random()) {
////                0 -> {
////                    mItems.add(
////                        ListDataWrapper(
////                            ListDataType.TYPE_SMALL_ITEM,
////                            SmallItemData("Small Type ViewHolder")
////                        )
////                    )
////                }
////                1 -> {
////                    mItems.add(
////                        ListDataWrapper(
////                            ListDataType.TYPE_LARGE_ITEM,
////                            LargeItemData("Large Type ViewHolder")
////                        )
////                    )
////                }
////            }
////        }
//
//
//    }
//}