package com.example.kelineyt.fragments.shopping

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelineyt.R
import com.example.kelineyt.adapter.makeIt.CartProductAdapters
import com.example.kelineyt.data.CartProduct
import com.example.kelineyt.databinding.FragmentCartBinding
import com.example.kelineyt.firebase.FireBaseCommon
import com.example.kelineyt.util.Resource
import com.example.kelineyt.util.VerticalItemDecoration
import com.example.kelineyt.viewmodel.CartViewModel
import com.example.kelineyt.viewmodel.makeIt.CartViewModels
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CartFragment : Fragment(R.layout.fragment_cart) {
    private lateinit var binding: FragmentCartBinding
    private val cartAdapter by lazy { CartProductAdapters() }
    private val viewModel by activityViewModels<CartViewModels>()
    var emptyArray = emptyArray<CartProduct>()

    //     private val viewModel by viewModels<CartViewModel>()으로 안하는 이유가 shoppingActivity에 이미 있고 activity와 fragment
    // 둘 다 사용하기 때문에 이렇게 쓰는듯

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartProductRv()
        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        if (it.data?.size != 0) {
                            hideEmptyBox()
                            binding.rvCart.visibility = View.VISIBLE
                            cartAdapter.differ.submitList(it.data)
                        } else {
                            binding.rvCart.visibility = View.GONE
                            showEmptyBox()
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.calculateProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        binding.tvTotalPrice.text = String.format("%.2f", it.data)
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        cartAdapter.onClick = {
            val b = Bundle().apply {
                putParcelable("product",it.product)
            }
            findNavController().navigate(R.id.action_cartFragment_to_productDetailsFragment,b)
        }

        cartAdapter.onIncreaseClick = {
            viewModel.increaseQuantity(it)

        }

        cartAdapter.onDecreaseClick = {
            if (it.quantity == 1) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("카트에 담긴 상품이 삭제됩니다.")
                    .setMessage("정말로 삭제하시겠습니까?")
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener { dialog, id ->
                            viewModel.deleteCartProduct(it)
                        })
                    .setNegativeButton("취소",
                        DialogInterface.OnClickListener { dialog, id ->
                        })
                builder.show()


            } else {
                viewModel.decreaseQuantity(it)
            }
        }
        binding.buttonCheckout.setOnClickListener {
            // 여기서 전달해야 하는 건 리스트 + 토탈 금액
            val action = CartFragmentDirections.actionCartFragmentToBillingFragment(
                totalPrice = viewModel.calculateCartProducts(cartAdapter.differ.currentList),
                cartAdapter.differ.currentList.toTypedArray(), true)
            findNavController().navigate(action)
//            findNavController().navigate(R.id.action_cartFragment_to_billingFragment, action)
        }

    }

    private fun hideEmptyBox() {
        binding.apply {
            layoutCartEmpty.visibility = View.GONE
            imageEmptyBox.visibility = View.GONE
            imageEmptyBoxTexture.visibility = View.GONE
            textView2.visibility = View.GONE
            textView.visibility = View.VISIBLE
            totalBoxContainer.visibility = View.VISIBLE
            tvTotalPrice.visibility = View.VISIBLE
            buttonCheckout.visibility = View.VISIBLE

        }
    }

    private fun showEmptyBox() {
        binding.apply {
            layoutCartEmpty.visibility = View.VISIBLE
            imageEmptyBox.visibility = View.VISIBLE
            imageEmptyBoxTexture.visibility = View.VISIBLE
            textView2.visibility = View.VISIBLE
            textView.visibility = View.GONE
            totalBoxContainer.visibility = View.GONE
            tvTotalPrice.visibility = View.GONE
            buttonCheckout.visibility = View.GONE
        }
    }

    private fun cartProductRv() {
        binding.apply {
            rvCart.adapter = cartAdapter
            rvCart.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }



















//    // 2023 - 11 - 19
////    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
////        super.onViewCreated(view, savedInstanceState)
////
////        setupCartRv()
////
////        lifecycleScope.launchWhenCreated {
////            viewModel.productsPrice.collectLatest { price ->
////                price?.let {
////                    binding.tvTotalPrice.text = "$ $price"
////                }
////            }
////        }
////
////        cartAdapter.onProductClick = {
////            val b = Bundle().apply { putParcelable("product",it.product) }
////            findNavController().navigate(R.id.action_cartFragment_to_productDetailsFragment,b)
////        }
////
////        cartAdapter.onPlusClick = {
////            viewModel.changeQuantity(it,FireBaseCommon.QuantityChanging.INCREASE)
////        }
////
////        cartAdapter.onMinusClick = {
////            viewModel.changeQuantity(it,FireBaseCommon.QuantityChanging.DECREASE)
////        }
////
////        lifecycleScope.launchWhenCreated {
////            viewModel.deleteDialog.collectLatest {
////                val alertDialog = AlertDialog.Builder(requireContext()).apply {
////                    setTitle("Delete item from cart")
////                        .setMessage("Do you want to delete this item from your cart?")
////                        .setNegativeButton("Cancel") {dialog , _ ->
////                            dialog.dismiss()
////                        }
////                        .setPositiveButton("Yes") { dialog, _ ->
////                           viewModel.deleteCartProduct(it)
////                          dialog.dismiss()
////                        }
////                }
////                alertDialog.create()
////                alertDialog.show()
////            }
////        }
////
////
////        lifecycleScope.launchWhenCreated {
////            viewModel.cartProducts.collectLatest {
////                when(it) {
////                    is Resource.Loading -> {
////                        binding.progressbarCart.visibility = View.VISIBLE
////                    }
////
////                    is Resource.Success -> {
////                        binding.progressbarCart.visibility = View.INVISIBLE
////                        if (it.data!!.isEmpty()) {
////                            showEmptyCart()
////                            hideOtherViews()
////                        } else {
////                            hideEmptyCart()
////                            showOtherView()
////                            cartAdapter.differ.submitList(it.data)
////                        }
////
////                    }
////
////                    is Resource.Error -> {
////                        binding.progressbarCart.visibility = View.INVISIBLE
////                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
////                    }
////                    else -> Unit
////                }
////            }
////        }
////
////    }
////
////    private fun showOtherView() {
////        binding.apply {
////            rvCart.visibility = View.VISIBLE
////            totalBoxContainer.visibility = View.VISIBLE
////            buttonCheckout.visibility = View.VISIBLE
////        }
////    }
////
////    private fun hideOtherViews() {
////        binding.apply {
////            rvCart.visibility = View.GONE
////            totalBoxContainer.visibility = View.GONE
////            buttonCheckout.visibility = View.GONE
////        }    }
////
////    private fun hideEmptyCart() {
////        binding.apply {
////            layoutCartEmpty.visibility = View.GONE
////        }
////    }
////
////    private fun showEmptyCart() {
////        binding.apply {
////            layoutCartEmpty.visibility = View.VISIBLE
////        }
////    }
////
////    private fun setupCartRv() {
////        binding.rvCart.apply {
////            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL, false)
////            adapter = cartAdapter
////            addItemDecoration(VerticalItemDecooration())
////        }
////    }
//    //    /// 아래부터는 직접 작성함.
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        setupCartRv()
//        lifecycleScope.launchWhenCreated {
//            viewModel.cartProducts.collectLatest {
//                when (it) {
//                    is Resource.Loading -> {
//
//                    }
//                    is Resource.Success -> {
//                        if (it.data!!.isEmpty()) {
//                            showEmptyBox()
//                            hideRv()
//                        } else {
//                            cartAdapter.differ.submitList(it.data)
//                            hideEmptyBox()
//                            showRv()
//                        }
//
//
//                    }
//                    is Resource.Error -> {
//
//                    }
//                    else -> Unit
//                }
//            }
//        }
//
//        // 그리고 그 데이터는 여기서 입력한다.
//        var totalPrice = 0f
//
//        lifecycleScope.launchWhenCreated {
//            viewModel.productsPrice.collectLatest { price ->
//                if (price != null) {
//                    totalPrice = price
//                    binding.tvTotalPrice.text =  "$ $price"
//                }
//            }
//        }
//        // 여기에 아이템뷰 클릭시 해당 프래그먼트로 이동해야 하는 기술이 필요하다.
//
//
//        cartAdapter.onProductClick =  {
//            val b = Bundle().apply { putParcelable("product",it.product) }
//            findNavController().navigate(R.id.action_cartFragment_to_productDetailsFragment,b)
//        }
//
//        cartAdapter.onPlusClick = {
//            viewModel.changingQuantity(it,FireBaseCommon.QuantityChanging.INCREASE)
//        }
//
//        cartAdapter.onMinusClick = {
//            if (it.quantity == 1) {
//                viewModel.deleteProduct(it)
//
//            } else {
//                viewModel.changingQuantity(it,FireBaseCommon.QuantityChanging.DECREASE)
//            }
//        }
//        binding.buttonCheckout.setOnClickListener {
//            val action = CartFragmentDirections.actionCartFragmentToBillingFragment(totalPrice,cartAdapter.differ.currentList.toTypedArray(),true)
//            findNavController().navigate(action)
//        }
//
//
//
//
//
//
//    }
//
//    private fun hideRv() {
//        binding.rvCart.visibility = View.INVISIBLE
//    }
//
//    private fun showRv() {
//        binding.rvCart.visibility = View.VISIBLE
//    }
//
//    private fun showEmptyBox() {
//        binding.apply {
//            layoutCartEmpty.visibility = View.VISIBLE
//        }
//    }
//
//    private fun hideEmptyBox() {
//        binding.apply {
//            layoutCartEmpty.visibility = View.INVISIBLE
//        }
//    }
//
//
//    private fun setupCartRv() {
//        binding.rvCart.apply {
//            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
//            adapter = cartAdapter
//            addItemDecoration(VerticalItemDecoration())
//        }
//    }


}