package com.example.kelineyt.fragments.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kelineyt.R
import com.example.kelineyt.adapter.BillingAdapter
import com.example.kelineyt.adapter.makeIt.BestProductsAdapters
import com.example.kelineyt.adapter.makeIt.BillingAdapters
import com.example.kelineyt.adapter.makeIt.CartProductAdapters
import com.example.kelineyt.data.order.OrderStatus
import com.example.kelineyt.data.order.getOrderStatus
import com.example.kelineyt.databinding.BillingProductsRvItemBinding
import com.example.kelineyt.databinding.FragmentOrderDetailBinding
import com.example.kelineyt.util.VerticalItemDecoration

class OrderDetailFragment: Fragment() {

    lateinit var binding: FragmentOrderDetailBinding
    private val navArgs by navArgs<OrderDetailFragmentArgs>()
    private val cartAdapter by lazy { BillingAdapters() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navArgs = navArgs.order
        val cartProducts = navArgs.products
        cartProductRv()
        cartAdapter.differ.submitList(cartProducts)

        putOrderData()

        var status = navArgs.orderStatus
        when (status) {
            "Ordered" -> {
                binding.stepView.
            }
            "Canceled" -> {
                binding.imageOrderState.setColorFilter(res.getColor(R.color.g_red))
            }
            "Confirmed" -> {
                binding.imageOrderState.setColorFilter(res.getColor(R.color.g_green))
            }
            "Shipped" -> {
                binding.imageOrderState.setColorFilter(res.getColor(R.color.purple_200))
            }
            "Delivered" -> {
                binding.imageOrderState.setColorFilter(res.getColor(R.color.g_blue_gray200))
            }
            else ->
                binding.imageOrderState.setColorFilter(res.getColor(R.color.g_light_red))


        }

    }

    private fun putOrderData() {
        val totalPrice = navArgs.order.totalPrice
        val orderId = navArgs.order.orderId
        val fullName = navArgs.order.address.fullName
        val addressTitle = navArgs.order.address.addressTitle
        val phone = navArgs.order.address.phone
        val city = navArgs.order.address.city
        val state = navArgs.order.address.state
        val street = navArgs.order.address.street

        binding.apply {
            tvTotalPrice.text = totalPrice.toString()
            tvOrderId.text = "#${orderId.toString()}"
            tvFullName.text = fullName
            tvAddress.text = "${city} ${state} ${street}"
            tvPhoneNumber.text = phone

        }


    }

    private fun cartProductRv() {
        binding.rvProducts.adapter = cartAdapter
        binding.rvProducts.layoutManager = LinearLayoutManager(
            requireContext(),LinearLayoutManager.VERTICAL, false)
    }


//    private lateinit var binding: FragmentOrderDetailBinding
//    private val billingAdapter by lazy { BillingAdapter() }
//    private val args by navArgs<OrderDetailFragmentArgs>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentOrderDetailBinding.inflate(inflater)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val order = args.order
//
//
//        setupOrderRv()
//
//        binding.apply {
//            tvOrderId.text = "Order #${order.orderId}"
//
//            // step을 4가지로 정렬
//            stepView.setSteps(
//                mutableListOf(
//                    OrderStatus.Ordered.status,
//                    OrderStatus.Confirmed.status,
//                    OrderStatus.Shipped.status,
//                    OrderStatus.Delivered.status,
//                )
//            )
//            // 현재 상태를 나타냄.
//            val currentOrderState = when(getOrderStatus(order.orderStatus)) {
//                is OrderStatus.Ordered -> 0
//                is OrderStatus.Confirmed -> 1
//                is OrderStatus.Shipped -> 2
//                is OrderStatus.Delivered -> 3
//                else -> 0
//            }
//            // 한칸씩 전진
//            stepView.go(currentOrderState,false)
//            if (currentOrderState == 3){
//                stepView.done(true)
//            }
//
//            tvFullName.text = order.address.fullName
//            tvAddress.text = "${order.address.street} ${order.address.city}"
//            tvPhoneNumber.text = order.address.phone
//            tvTotalPrice.text = "$ ${order.totalPrice}"
//
//        }
//        billingAdapter.differ.submitList(order.products)
//
//
//
//
//    }
//
//    private fun setupOrderRv() {
//        binding.rvProducts.apply {
//            adapter = billingAdapter
//            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
//            addItemDecoration(VerticalItemDecoration())
//        }
//    }
}