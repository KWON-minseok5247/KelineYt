package com.example.kelineyt.fragments.settings

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelineyt.adapter.makeIt.BillingAdapters
import com.example.kelineyt.data.order.OrderStatus
import com.example.kelineyt.databinding.FragmentOrderDetailBinding
import com.shuhart.stepview.StepView


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
        val stepViewList = mutableListOf<String>(OrderStatus.Ordered.status,
        OrderStatus.Shipped.status, OrderStatus.Delivered.status,
        OrderStatus.Confirmed.status)


        var status = navArgs.orderStatus
        binding.stepView.setSteps(stepViewList)
        when (status) {
            "Ordered" -> {
                binding.stepView.go(0, true)
            }
            "Canceled" -> {
            }
            "Confirmed" -> {
                binding.stepView.go(3, true)

            }
            "Shipped" -> {
                binding.stepView.go(1, true)

            }
            "Delivered" -> {
                binding.stepView.go(2, true)

            }
            else -> Unit

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