package com.example.kelineyt.paging.viewholder.vh

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kelineyt.data.Product
import com.example.kelineyt.databinding.SpecialRvItemBinding
import com.example.kelineyt.paging.viewholder.data.SpecialProductsItems

class PagingSpecialProductsViewHolder(private val binding: SpecialRvItemBinding): RecyclerView.ViewHolder(binding.root) {


    init {

    }

    fun bind(product: SpecialProductsItems){
        product.specialProducts
        binding.apply {
            Glide.with(itemView).load(product.specialProducts.images[0]).into(ImageSpecialRvItem)
            tvSpecialProductName.text = product.specialProducts.name
            tvSpecialProductPrice.text = "$ ${product.specialProducts.price.toString()}"
        }
    }
}