package com.example.kelineyt.fragments.makeIt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelineyt.adapter.SizesAdapter
import com.example.kelineyt.adapter.makeIt.ColorAdapters
import com.example.kelineyt.adapter.makeIt.SizeAdapters
import com.example.kelineyt.databinding.FragmentProductDetailsBinding
import com.example.kelineyt.fragments.shopping.ProductDetailsFragmentArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragments : Fragment() {
    // 각종 아이템뷰에서 클릭을 했을 때 받은 Product는 navArgs로 등록할 수 있다!!
    //  이건 안쓴다.
    //TODO 여기서는 이미지가 여러개라면 이동시켜서 볼 수 있는 뷰페이저, 사이즈, 색상을 선택할 수 있는 리사이클러뷰가 필요하다.
    //TODO 또한 클릭한 상품을 가져올 수 있어야 하며, 사이즈 등을 선택하고 카트에 넣을 수 있도록 한다.
    private lateinit var binding: FragmentProductDetailsBinding
    private val sizesAdapter by lazy { SizeAdapters() }
    private val colorAdapters by lazy { ColorAdapters() }
    private val navArgs by navArgs<ProductDetailsFragmentArgs>()

//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentProductDetailsBinding.inflate(inflater)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.tvProductName.text = navArgs.product.name
//        Log.e("navarg의 product는 ", "${navArgs.product}")
//
//        getSizeRv()
//        getColorRv()
//
//    }
//
//    private fun getColorRv() {
//        binding.rvColors.apply {
//            adapter = colorAdapters
//            layoutManager =
//                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        }
//    }
//
//    private fun getSizeRv() {
//        binding.rvSizes.apply {
//            adapter = sizesAdapter
//            layoutManager =
//                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        }
//    }


//    private val args by navArgs<ProductDetailsFragmentArgs>()
//    private lateinit var binding: FragmentProductDetailsBinding
//    private val viewPagerAdapter by lazy { ViewPager2Images()}
//    private val sizesAdapter by lazy { SizesAdapter() }
//    private val colorsAdapter by lazy { ColorsAdapter() }
//    private var selectedColor: Int? = null
//    private var selectedSize: String? = null
//    private val viewModel by viewModels<DetailsViewModel>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        hideBottomNavigationView()
//
//        binding = FragmentProductDetailsBinding.inflate(inflater)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val product = args.product
//
//        setupSizesRv()
//        setupColorsRv()
//        setupViewpager()
//
//        binding.imageClose.setOnClickListener {
//            findNavController().navigateUp()
//        }
//
//        sizesAdapter.onItemClick = {
//            selectedSize = it
//        }
//
//        colorsAdapter.onItemClick = {
//            selectedColor = it
//        }
//
//        binding.buttonAddToCart.setOnClickListener{
//            viewModel.addUpdateProductInCart(CartProduct(product,1,selectedColor, selectedSize))
//        }
//
//        lifecycleScope.launchWhenCreated {
//            viewModel.addToCart.collectLatest {
//                when(it){
//                    is Resource.Loading -> {
//                        binding.buttonAddToCart.startAnimation()
//                    }
//                    is Resource.Success -> {
//                        binding.buttonAddToCart.revertAnimation()
//                        binding.buttonAddToCart.setBackgroundColor(resources.getColor(R.color.black))
//                        Toast.makeText(requireContext(), "Product was added", Toast.LENGTH_SHORT).show()
//
//                    }
//                    is Resource.Error -> {
//                        binding.buttonAddToCart.stopAnimation()
//                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
//                    }
//                    else -> Unit
//                }
//            }
//        }
//
//
//
//
//
//
//        binding.apply {
//            tvProductName.text = product.name
//            tvProductPrice.text = "$ ${product.price}"
//            tvProductDescription.text = product.description
//
//            if (product.colors.isNullOrEmpty()) {
//                tvProductColors.visibility = View.INVISIBLE
//            }
//            if (product.sizes.isNullOrEmpty()) {
//                tvProductSizes.visibility = View.INVISIBLE
//            }
//        }
//
//        viewPagerAdapter.differ.submitList(product.images)
//        // colorsAdapter.differ.submitList(product.colors) 이렇게 하지 않는 이유는 모두가 color를 가지고 있지 않기 때문에
//        product.colors?.let { colorsAdapter.differ.submitList(it)}
//        product.sizes?.let { sizesAdapter.differ.submitList(it)}
//
//
//
//    }
//
//    private fun setupViewpager() {
//        binding.apply {
//            viewPagerProductImages.adapter = viewPagerAdapter
//        }
//    }
//
//    private fun setupColorsRv() {
//        binding.apply {
//            rvColors.adapter = colorsAdapter
//            rvColors.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
//        }
//    }
//
//    private fun setupSizesRv() {
//        binding.apply {
//            rvSizes.adapter = sizesAdapter
//            rvSizes.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
//        }
//    }


}