package com.example.kelineyt.adapter.makeIt

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kelineyt.R
import com.example.kelineyt.data.order.Order
import com.example.kelineyt.data.order.OrderStatus
import com.example.kelineyt.data.order.getOrderStatus
import com.example.kelineyt.databinding.OrderItemBinding
import com.example.kelineyt.databinding.StringRvBinding

class StringAdapters(private val string: String) : RecyclerView.Adapter<StringAdapters.StringViewHolder>() {
    companion object {
        const val VIEW_TYPE = 8888
    }
    inner class StringViewHolder(private val binding: StringRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(string: String) {
            binding.apply {
                stringRv.text = string
            }

        }
    }

        private val diffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
            //return AllOrdersViewHolder(OrderItemBinding.inflate(LayoutInflater.from(parent.context))) 이렇게 하면 matched해도 가로길이가 짧음.
            return StringViewHolder(
                StringRvBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }       // 이렇게 하니까 아이템뷰의 가로 전체 길이가 확장되었다.

        override fun onBindViewHolder(holder: StringViewHolder, position: Int) {
            holder.bind(string)

        }

        override fun getItemCount(): Int {
            return 1
        }

    }















//class StringAdapters : RecyclerView.Adapter<StringAdapters.StringViewHolder>() {
//
//    inner class StringViewHolder(private val binding: StringRvBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(string: String) {
//            binding.apply {
//                stringRv.text = string
//            }
//
//        }
//    }
//
//    private val diffCallback = object : DiffUtil.ItemCallback<String>() {
//        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
//            return oldItem == newItem
//        }
//
//        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
//            return oldItem == newItem
//        }
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
//        //return AllOrdersViewHolder(OrderItemBinding.inflate(LayoutInflater.from(parent.context))) 이렇게 하면 matched해도 가로길이가 짧음.
//        return StringViewHolder(
//            StringRvBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        )
//    }       // 이렇게 하니까 아이템뷰의 가로 전체 길이가 확장되었다.
//
//    override fun onBindViewHolder(holder: StringViewHolder, position: Int) {
//        val item = getItem(position)
//        holder.bind(item)
//
//    }
//
//    override fun getItemCount(): Int {
//        return differ.currentList.size
//    }
//
//}
