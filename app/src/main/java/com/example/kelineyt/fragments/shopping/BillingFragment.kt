package com.example.kelineyt.fragments.shopping

import android.app.AlertDialog
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
import com.example.kelineyt.R
import com.example.kelineyt.adapter.AddressAdapter
import com.example.kelineyt.adapter.BillingAdapter
import com.example.kelineyt.adapter.makeIt.AddressAdapters
import com.example.kelineyt.adapter.makeIt.BillingAdapters
import com.example.kelineyt.data.Address
import com.example.kelineyt.data.CartProduct
import com.example.kelineyt.data.order.Order
import com.example.kelineyt.data.order.OrderStatus
import com.example.kelineyt.databinding.FragmentBillingBinding
import com.example.kelineyt.util.HorizontalItemDecoration
import com.example.kelineyt.util.Resource
import com.example.kelineyt.viewmodel.BillingViewModel
import com.example.kelineyt.viewmodel.OrderViewModel
import com.example.kelineyt.viewmodel.makeIt.AddressViewModels
import com.example.kelineyt.viewmodel.makeIt.BillingViewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


//TODO 주소를 새로 추가했을 때 기존의 position과 새로 추가되면서 한 줄씩 밀려난다. 그래서 2칸이 선택되는 현상이 발생
// 클릭하기 전까지 사라지지 않는 문제가 발생 정확하게는 마지막 항목이 업데이트되지 않는다.
@AndroidEntryPoint
class BillingFragment : Fragment() {
    private val args : BillingFragmentArgs by navArgs()
    lateinit var binding: FragmentBillingBinding
    private val billingAdapters by lazy { BillingAdapters() }
    private val addressAdapters by lazy { AddressAdapters() }
    private val billingViewModels by viewModels<BillingViewModels>()
    lateinit var selectedAddress : Address

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBillingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val totalPrice = args.totalPrice
        val payment = args.payment

        binding.tvTotalPrice.text = String.format("%.2f", totalPrice)
        billingCartProductRv()

        addressRv()
        lifecycleScope.launchWhenStarted {
            billingViewModels.address.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        addressAdapters.differ.submitList(it.data)
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
        addressAdapters.onClick = {
            selectedAddress = it
            Log.e("selectedAddress",selectedAddress.toString())
        }

        billingAdapters.onClick = {
            val b = Bundle().apply {
                putParcelable("product", it.product)
            }
            findNavController().navigate(R.id.productDetailsFragment, b)
        }


        //선택을 했을 때 연파란색 나머지는 흰색


//        lifecycleScope.launchWhenStarted {
//            addressViewModel.address.collectLatest {
//                when (it) {
//                    is Resource.Loading -> {
//
//                    }
//                    is Resource.Success -> {
//                        addressAdapters.differ.submitList(it.data)
//                    }
//                    is Resource.Error -> {
//                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
//
//                    }
//                    else -> Unit
//                }
//            }
//        }

        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }


    }

    override fun onResume() {
        super.onResume()
        billingViewModels.getAddressRv()

    }

    private fun addressRv() {
        binding.rvAddress.adapter = addressAdapters
        binding.rvAddress.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
    }

    private fun billingCartProductRv() {
        binding.rvProducts.adapter = billingAdapters
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        billingAdapters.differ.submitList(args.products.toMutableList())
    }



//    private lateinit var binding: FragmentBillingBinding
//    private val addressAdapter by lazy { AddressAdapter() }
//    private val billingAdapter by lazy { BillingAdapter() }
//    private val BillingViewModel by viewModels<BillingViewModel>()
//    private val args by navArgs<BillingFragmentArgs>()
//    private var products = emptyList<CartProduct>()
//    private var totalPrice = 0f
//
//    private var selectedAddress: Address? = null
//    private val orderViewModel by viewModels<OrderViewModel>()
//
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        // 데이터를 들고온 것
//        products = args.products.toList()
//        totalPrice = args.totalPrice
//    }
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentBillingBinding.inflate(inflater)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        setupBillingProductsRv()
//        setupAddressRv()
//
//        if (!args.payment) {
//            binding.apply {
//                buttonPlaceOrder.visibility = View.INVISIBLE
//                totalBoxContainer.visibility = View.INVISIBLE
//                middleLine.visibility = View.INVISIBLE
//                bottomLine.visibility = View.INVISIBLE
//            }
//        }
//
//
//        binding.imageAddAddress.setOnClickListener {
//            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
//        }
//
//        lifecycleScope.launchWhenStarted {
//            BillingViewModel.address.collectLatest {
//                when (it) {
//                    is Resource.Loading -> {
//                        binding.progressbarAddress.visibility = View.VISIBLE
//                    }
//                    is Resource.Success -> {
//                        addressAdapter.differ.submitList(it.data)
//                        binding.progressbarAddress.visibility = View.GONE
//                    }
//                    is Resource.Error -> {
//                        binding.progressbarAddress.visibility = View.GONE
//                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
//                    }
//                    else -> Unit
//                }
//            }
//        }
//
//        lifecycleScope.launchWhenStarted {
//            orderViewModel.order.collectLatest {
//                when (it) {
//                    is Resource.Loading -> {
//                        binding.buttonPlaceOrder.startAnimation()
//                    }
//                    is Resource.Success -> {
//                        binding.buttonPlaceOrder.revertAnimation()
//                        findNavController().navigateUp()
//                        Snackbar.make(requireView(),"Your order was placed", Snackbar.LENGTH_LONG).show()
//                    }
//                    is Resource.Error -> {
//                        binding.buttonPlaceOrder.revertAnimation()
//                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
//                    }
//                    else -> Unit
//                }
//            }
//        }
//        // billingAdapter는 그냥 args로 들고와서 따로 flow로 만들어놓지 않아도 된다.
//        billingAdapter.differ.submitList(products)
//
//        binding.tvTotalPrice.text = "$ ${totalPrice}"
//
//        addressAdapter.onClick = {
//            selectedAddress = it
//            if (!args.payment) {
//                val b = Bundle().apply { putParcelable("address", selectedAddress) }
//                findNavController().navigate(R.id.action_billingFragment_to_addressFragment,b)
//            }
//        }
//
//        binding.buttonPlaceOrder.setOnClickListener {
//            if (selectedAddress == null) {
//                Toast.makeText(requireContext(),"Please select your address", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            showOrderConfirmationDialog()
//        }
//
//
//    }
//
//    private fun showOrderConfirmationDialog() {
//        val alertDialog = AlertDialog.Builder(requireContext()).apply {
//                    setTitle("Order items")
//                        .setMessage("Do you want to order your cart items?")
//                        .setNegativeButton("Cancel") {dialog , _ ->
//                            dialog.dismiss()
//                        }
//                        .setPositiveButton("Yes") { dialog, _ ->
//                            val order = Order(
//                                OrderStatus.Ordered.status,
//                                totalPrice,
//                                products,
//                                selectedAddress!!
//                            )
//                           orderViewModel.placeOrder(order)
//                          dialog.dismiss()
//                        }
//                }
//                alertDialog.create()
//                alertDialog.show()
//    }
//
//    private fun setupAddressRv() {
//        binding.rvAddress.apply {
//            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
//            adapter = addressAdapter
//            addItemDecoration(HorizontalItemDecoration())
//        }
//    }
//
//    private fun setupBillingProductsRv() {
//        binding.rvProducts.apply {
//            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
//            adapter = billingAdapter
//            addItemDecoration(HorizontalItemDecoration())
//
//        }
//    }


}