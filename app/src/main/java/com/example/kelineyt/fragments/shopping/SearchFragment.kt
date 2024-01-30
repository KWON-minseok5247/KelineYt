package com.example.kelineyt.fragments.shopping

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kelineyt.R
import com.example.kelineyt.adapter.BestDealsAdapter
import com.example.kelineyt.adapter.makeIt.StringAdapters
import com.example.kelineyt.adapter.makeIt.TemporarySpecialProductsAdapter
import com.example.kelineyt.databinding.FragmentMainCategoryBinding
import com.example.kelineyt.paging.MyPagingDataAdapter
import com.example.kelineyt.paging.MyPagingViewModel
import com.example.kelineyt.paging.SampleLoadStateAdapter
import com.example.kelineyt.paging.viewholder.HorizontalWrapperAdapter
import com.example.kelineyt.paging.viewholder.HorizontalWrapperForSpecialAdapter
import com.example.kelineyt.util.Resource
import com.example.kelineyt.util.showBottomNavigationView
import com.example.kelineyt.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

//class SearchFragment: Fragment(R.layout.fragment_search) {
//}
private val TAG = "SearchFragment"

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_main_category) {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductsAdapter: TemporarySpecialProductsAdapter
    private lateinit var bestDealsAdapter: BestDealsAdapter

    private var page = 1 // 현재 페이지

    private lateinit var myPagingDataAdapter: MyPagingDataAdapter
    private val myFirebaseViewModel by viewModels<MyPagingViewModel>()

    private val sampleLoadStateAdapter = SampleLoadStateAdapter()


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

        lifecycleScope.launchWhenStarted {
            myPagingDataAdapter.loadStateFlow.collect {loadStates ->
                when {
                    loadStates.refresh is LoadState.Loading -> {
                        // 데이터 로딩 중일 때
                        binding.bestProductsProgressbar.visibility = View.VISIBLE
                    }
                    loadStates.append is LoadState.Loading || loadStates.prepend is LoadState.Loading -> {
                        // 더 데이터를 불러오고 있는 중일 때
                        // 필요에 따라 추가적인 처리를 할 수 있습니다.
                    }
                    else -> {
                        // 데이터 로딩이 완료되었을 때
                        binding.bestProductsProgressbar.visibility = View.GONE
                    }
                }

            }

        }


        myPagingDataAdapter.onClick = { product ->
            val b = Bundle().apply {
                putParcelable("product", product)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, b)
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

    private fun myFirebaseStorageRv() {

        myPagingDataAdapter = MyPagingDataAdapter()

        binding.rvBestProducts.adapter = myPagingDataAdapter.withLoadStateFooter(
            footer = SampleLoadStateAdapter()
        )

        val layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
//            return if (myPagingDataAdapter.getItemViewType(position) == R.layout.list_item_footer) {
                return if (position == myPagingDataAdapter.itemCount) {
                    2 // Use the full span when it's a loading state item
                } else {
                    1 // Normal span size for other items
                }
            }
        }

        binding.rvBestProducts.layoutManager = layoutManager

    }

}
