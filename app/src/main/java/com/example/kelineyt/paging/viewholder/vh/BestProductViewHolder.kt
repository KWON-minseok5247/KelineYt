package com.example.kelineyt.paging.viewholder.vh

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kelineyt.data.Product
import com.example.kelineyt.databinding.ProductRvItemBinding

class BestProductViewHolder(private val binding: ProductRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(product: Product) {
// 아이템에 대한 바인딩 코드 추가
        Glide.with(itemView).load(product.images[0]).into(binding.bestProductsImgProduct)
        binding.apply {
            bestProductsTvName.text = product.name
            bestProductsTvPrice.text = product.price.toString()
            if (product.offerPercentage != null) {
                bestProductsTvNewPrice.text =
                    (product.price * (1 - product.offerPercentage)).toString()
            } else {
                bestProductsTvNewPrice.visibility = View.INVISIBLE
            }
        }
// 나머지 필요한 UI 요소들에 대한 바인딩 코드 추가
    }
}