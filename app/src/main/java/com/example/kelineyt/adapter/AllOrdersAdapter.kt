package com.example.kelineyt.adapter

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.kelineyt.R
import com.example.kelineyt.data.Address
import com.example.kelineyt.data.order.Order
import com.example.kelineyt.data.order.OrderStatus
import com.example.kelineyt.data.order.getOrderStatus
import com.example.kelineyt.databinding.AddressRvItemBinding
import com.example.kelineyt.databinding.OrderItemBinding

class AllOrdersAdapter : Adapter<AllOrdersAdapter.AllOrdersViewHolder>() {


    inner class AllOrdersViewHolder(private val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.apply {
                tvOrderId.text = order.orderId.toString()
                tvOrderDate.text = order.date
                val resources = itemView.resources

                val colorDrawable = when (getOrderStatus(order.orderStatus)) {
                    is OrderStatus.Ordered -> {
                        ColorDrawable(resources.getColor(R.color.g_orange))
                    }
                    is OrderStatus.Confirmed -> {
                        ColorDrawable(resources.getColor(R.color.g_green))
                    }
                    is OrderStatus.Delivered -> {
                        ColorDrawable(resources.getColor(R.color.g_green))
                    }
                    is OrderStatus.Shipped -> {
                        ColorDrawable(resources.getColor(R.color.g_green))
                    }
                    is OrderStatus.Canceled -> {
                        ColorDrawable(resources.getColor(R.color.g_red))
                    }
                    is OrderStatus.Returned -> {
                        ColorDrawable(resources.getColor(R.color.g_red))
                    }
                }

                imageOrderState.setImageDrawable(colorDrawable)
            }
        }


    }

    private val diffCallback = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrdersViewHolder {
        //return AllOrdersViewHolder(OrderItemBinding.inflate(LayoutInflater.from(parent.context))) 이렇게 하면 matched해도 가로길이가 짧음.
        return AllOrdersViewHolder(OrderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }       // 이렇게 하니까 아이템뷰의 가로 전체 길이가 확장되었다.

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