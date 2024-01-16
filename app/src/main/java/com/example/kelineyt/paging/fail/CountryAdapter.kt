package com.example.kelineyt.paging.fail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kelineyt.data.Product
import com.example.kelineyt.databinding.ProductRvItemBinding

//class CountryAdapter : PagingDataAdapter<Product, CountryViewHolder>(Companion) {
//    companion object : DiffUtil.ItemCallback<Product>() {
//        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
//            return oldItem == newItem
//        }
//
//        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
//            return oldItem == newItem
//        }
//    }
//
//    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
//        val binding = holder.binding as ProductRvItemBinding
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
//        val layoutInflater = LayoutInflater.from(parent.context)
//        val binding = ProductRvItemBinding.inflate(layoutInflater, parent, false)
//        return CountryViewHolder(binding)
//    }
//}
//
//class CountryViewHolder(val binding: ProductRvItemBinding) : RecyclerView.ViewHolder(binding.root)