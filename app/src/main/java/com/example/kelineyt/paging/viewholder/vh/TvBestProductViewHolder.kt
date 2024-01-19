package com.example.kelineyt.paging.viewholder.vh

import androidx.recyclerview.widget.RecyclerView
import com.example.kelineyt.databinding.TvBestProductsBinding
import com.example.kelineyt.paging.viewholder.data.TvBestProductsItems

class TvBestProductViewHolder(private val binding: TvBestProductsBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(product: TvBestProductsItems) {
// 아이템에 대한 바인딩 코드 추가
        binding.tvBestProducts.text = product.tvBestProduct
    }
}