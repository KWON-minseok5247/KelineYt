package com.example.kelineyt.fragments.shopping

import android.R.attr.bitmap
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.kelineyt.adapter.makeIt.ColorAdapters
import com.example.kelineyt.adapter.makeIt.SizeAdapters
import com.example.kelineyt.adapter.makeIt.ViewPagerImage
import com.example.kelineyt.data.CartProduct
import com.example.kelineyt.data.Product
import com.example.kelineyt.databinding.FragmentProductDetailsBinding
import com.example.kelineyt.util.Resource
import com.example.kelineyt.viewmodel.DetailsViewModel
import com.example.kelineyt.viewmodel.makeIt.CategoryViewModels
import com.example.kelineyt.viewmodel.makeIt.DetailsViewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.io.ByteArrayOutputStream
import java.io.InputStream


@AndroidEntryPoint
class ProductDetailsFragment: Fragment() {
    // 각종 아이템뷰에서 클릭을 했을 때 받은 Product는 navArgs로 등록할 수 있다!!
    //TODO 여기서는 이미지가 여러개라면 이동시켜서 볼 수 있는 뷰페이저, 사이즈, 색상을 선택할 수 있는 리사이클러뷰가 필요하다.
    //TODO 또한 클릭한 상품을 가져올 수 있어야 하며, 사이즈 등을 선택하고 카트에 넣을 수 있도록 한다.
//    private lateinit var binding: FragmentProductDetailsBinding


    private lateinit var binding: FragmentProductDetailsBinding
    private val sizesAdapter by lazy { SizeAdapters() }
    private val colorAdapters by lazy { ColorAdapters() }
    private val viewPagerImageAdapters by lazy { ViewPagerImage() }
    private val navArgs by navArgs<ProductDetailsFragmentArgs>()
    val viewModel by viewModels<DetailsViewModels>()

//    private var emptyList = emptyList<String>() as MutableList<String>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = navArgs.product
        getTv(product)
        var cartProduct = CartProduct(product,1,)

        getImageViewPager()
        viewPagerImageAdapters.differ.submitList(product.images)

        getSizeRv()
        sizesAdapter.differ.submitList(product.sizes)
        // 클릭한 데이터를 가지고 나중에 카트에 추가할 때 반영을 해야 한다. - complete
        sizesAdapter.onClick = {
            cartProduct = cartProduct.copy(selectedSize = it)
        }

        getColorRv()
        colorAdapters.differ.submitList(product.colors)
        colorAdapters.onClick = {
            cartProduct = cartProduct.copy(selectedColor = it)
        }

        binding.buttonAddToCart.setOnClickListener {
            viewModel.addToCartIt(cartProduct)
            Toast.makeText(requireContext(),"물건이 카트에 추가되었습니다.", Toast.LENGTH_SHORT).show()
        }
        binding.imageClose.setOnClickListener {
            findNavController().navigateUp()

        }

        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.buttonAddToCart.startAnimation()

                    }
                    is Resource.Success -> {
                        binding.buttonAddToCart.revertAnimation()

                    }
                    is Resource.Error -> {
                        binding.buttonAddToCart.revertAnimation()
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

    }

    private fun getTv(product: Product) {
        binding.apply {
            tvProductName.text = product.name
            tvProductPrice.text = product.price.toString()
            tvProductDescription.text = product.description
        }
    }

    private fun getImageViewPager() {
        binding.viewPagerProductImages.apply {
            adapter = viewPagerImageAdapters
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }

    }


    private fun getColorRv() {
        binding.rvColors.apply {
            adapter = colorAdapters
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }
    private fun getImagesList(): ArrayList<Int> {
        return arrayListOf()
    }

    private fun getSizeRv() {
        binding.rvSizes.apply {
            adapter = sizesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }




//
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//    }
//
//
//
//
//
//
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