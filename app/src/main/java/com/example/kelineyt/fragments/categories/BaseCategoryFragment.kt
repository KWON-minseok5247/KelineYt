package com.example.kelineyt.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kelineyt.R
import com.example.kelineyt.adapter.BestProductsAdapter
import com.example.kelineyt.databinding.FragmentBaseCategoryBinding
import com.example.kelineyt.util.showBottomNavigationView

open class BaseCategoryFragment: Fragment(R.layout.fragment_base_category) {
    private lateinit var binding: FragmentBaseCategoryBinding
    protected val offerAdapter: BestProductsAdapter by lazy{BestProductsAdapter()} // 이렇게 하면 함수를 실행할 때
    // bestProductsAdapter = BestProductsAdapter()를 할 필요 없이 딱 offerAdapter가 실행되는 처음에 초기화할 수 있다.
    // 아마도 이렇게 하는 이유는 cupboard, furniture 등 여러 카테고리를 들어갈 때마다 초기화해주기보다는 처음에 딱 한번만 하는게 훨씬 성능에 도움이 되는듯?
    protected val bestProductsAdapter: BestProductsAdapter by lazy{BestProductsAdapter()} // 이렇게

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOfferRv()
        setupBestProductsRv()


        bestProductsAdapter.onClick = {
            // 저기서 setonclicklistener가 없어서 onclick으로 대체를 한 건가???????????
            // 아이템 뷰를 클릭할 때에 해당 product(b)를 들고 해당 프래그먼트로 이동한다.
            val b = Bundle().apply { putParcelable("product", it)  }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }

        offerAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it)  }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }


        binding.rvOffer.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollHorizontally(1) && dx != 0) {
                    onOfferPagingRequest()
                }
            }
        })

        binding.nestedScrollBaseCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
//            Log.e("asd","${v.getChildAt(0).bottom} ${v.height} ${scrollY}")
            // v.getChildAt(0)은 초기 고정값(최대높이 정도?)인 것 같고, v.height는 nestedscrollview의 높이, scrollY는 현재 Y의 정도로 아래로 스크롤
            // 할수록 점점 커진다.
            if (v.getChildAt(0).bottom <= v.height + scrollY) {
                Log.e("asd","${v.getChildAt(0).bottom} ${v.height} ${scrollY}")
                onBestProductsPagingRequest()
            }

        })




    }

    open fun onOfferPagingRequest() {

    }

    open fun onBestProductsPagingRequest() {

    }

    fun     showOfferLoading() {
        binding.offerProductsProgressBar.visibility = View.VISIBLE
    }
    fun hideOfferLoading() {
        binding.offerProductsProgressBar.visibility = View.GONE
    }
    fun showBestProductsLoading() {
        binding.BestProductsProgressBar.visibility = View.VISIBLE
    }
    fun hideBestProductsLoading() {
        binding.BestProductsProgressBar.visibility = View.GONE
    }


    private fun setupBestProductsRv() {
//        bestProductsAdapter = BestProductsAdapter()
        binding.apply {
            rvBestProducts.layoutManager = GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)
            rvBestProducts.adapter = bestProductsAdapter
        }
    }

    private fun setupOfferRv() {
//        offerAdapter = BestProductsAdapter()
        binding.rvOffer.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = offerAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }


}