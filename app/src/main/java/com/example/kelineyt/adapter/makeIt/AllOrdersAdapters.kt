package com.example.kelineyt.adapter.makeIt

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.Resource
import com.example.kelineyt.R
import com.example.kelineyt.data.CartProduct
import com.example.kelineyt.data.order.Order
import com.example.kelineyt.data.order.OrderStatus
import com.example.kelineyt.databinding.BillingProductsRvItemBinding
import com.example.kelineyt.databinding.OrderItemBinding

class AllOrdersAdapters : RecyclerView.Adapter<AllOrdersAdapters.AllOrdersViewHolder>() {

    inner class AllOrdersViewHolder(val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.apply {
                tvOrderDate.text = order.date
                tvOrderId.text = order.orderId.toString()
                imageOrderState.background
            }
            val res = itemView.resources
            when (order.orderStatus) {
                "Ordered" -> {
                    binding.imageOrderState.setColorFilter(res.getColor(R.color.g_orange_yellow))
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

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrdersViewHolder {
        val binding =
            OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllOrdersViewHolder(binding)
    }


    val diffCallback = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffCallback)

    override fun onBindViewHolder(holder: AllOrdersViewHolder, position: Int) {
        val order = differ.currentList[position]
        holder.bind(order)

        holder.itemView.setOnClickListener {
            onClick?.invoke(order)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((Order) -> Unit)? = null
}