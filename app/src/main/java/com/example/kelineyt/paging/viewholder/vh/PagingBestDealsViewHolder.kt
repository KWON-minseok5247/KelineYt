package com.example.kelineyt.paging.viewholder.vh

import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kelineyt.data.Product
import com.example.kelineyt.databinding.BestDealsRvItemBinding
import com.example.kelineyt.paging.viewholder.data.BestDealsItems

class PagingBestDealsViewHolder(private val binding: BestDealsRvItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(producted: BestDealsItems){
        val product = producted.bestDeals
        binding.apply {
            Glide.with(itemView).load(product.images[0]).into(imgBestDeal)
            tvDealProductName.text = product.name
            tvOldPrice.text = "$ ${product.price.toString()}"
            if (product.offerPercentage != null) {
                val newPrice = (product.price * product.offerPercentage)
                tvNewPrice.text = "$ ${newPrice.toString()}"
                // "$ ${String.format("%.2f", newPrice}"를 해도 된다. 숫자에 오류가 발생하지 않을 가능성 높아짐.
                tvOldPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }


        }
    }
}