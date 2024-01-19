package com.example.kelineyt.paging.viewholder.vh

import androidx.recyclerview.widget.RecyclerView
import com.example.kelineyt.databinding.TvBestDealsBinding
import com.example.kelineyt.paging.viewholder.data.TvBestDealsItems

class TvBestDealsViewHolder(private val binding: TvBestDealsBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(product: TvBestDealsItems) {
    // 아이템에 대한 바인딩 코드 추가
        binding.tvBestDeals.text = product.tvBestDeals
    }
}