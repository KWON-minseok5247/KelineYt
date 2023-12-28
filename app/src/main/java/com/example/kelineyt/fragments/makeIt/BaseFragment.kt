package com.example.kelineyt.fragments.makeIt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelineyt.R
import com.example.kelineyt.adapter.makeIt.BestProductsAdapters
import com.example.kelineyt.databinding.FragmentBaseCategoryBinding

//lateinit : 값 변경 가능 (var 사용)
//by lazy : 값 변경 불가능 (val 사용)
//용법 구분
//lateinit : 초기화 이후에 계속하여 값이 바뀔 수 있을 때
//by lazy : 초기화 이후에 읽기 전용 값으로 사용할 때
open class BaseFragment: Fragment(R.layout.fragment_base_category) {
    //TODO 여기서는 리사이클러뷰 2개를 항상 작동시킬 수 있어야 한다. rvBestProducts와 rvOffer
    // 그러기 위해서 필요한 것 카테고리별 데이터, Best Products 그리고 각종 어댑터 필요함必
    private lateinit var binding: FragmentBaseCategoryBinding
    protected val offerAdapter: BestProductsAdapters by lazy { BestProductsAdapters() }
    protected val bestProductsAdapter: BestProductsAdapters by lazy { BestProductsAdapters() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //TODO 여기서 어댑터 구현, 클릭하는 것도 여기서 하는 게 좋겠다.
        getSpecialProductRv()
        getBestProductRv()
        // 어댑터에 onClick이 있었던 이유가 바로 이거네,기존 방식으로는 setonClick을 할 때 한계점이 명확함.
        // 근데 onclick을 했을 때 product를 깔끔하게 호출할 수 있네.
//        binding.rvOffer.setOnClickListener {
//            val b = Bundle().putParcelable("product",binding.rvOffer.findViewById<it>())
//        } 그래서 이 방식으로는 못한다.
        offerAdapter.onClick = {
            val b = Bundle().apply {
                putParcelable("product", it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }
        bestProductsAdapter.onClick = {
            val b = Bundle().apply {
                putParcelable("product", it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }






    }
    fun showOfferProgressBar() {
        binding.offerProductsProgressBar.visibility = View.VISIBLE
    }
    fun hideOfferProgressBar() {
        binding.offerProductsProgressBar.visibility = View.GONE
    }
    fun showBestProductsProgressBar() {
        binding.BestProductsProgressBar.visibility = View.VISIBLE
    }
    fun hideBestProductsProgressBar() {
        binding.BestProductsProgressBar.visibility = View.GONE
    }

    private fun getBestProductRv() {
        binding.rvBestProducts.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
            adapter = bestProductsAdapter
        }
    }

    private fun getSpecialProductRv() {
        binding.rvOffer.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = offerAdapter
        }
    }


}