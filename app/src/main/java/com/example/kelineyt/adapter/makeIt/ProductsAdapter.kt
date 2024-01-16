package com.example.kelineyt.adapter.makeIt

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kelineyt.data.PagingProducts
import com.example.kelineyt.data.Product
import com.example.kelineyt.databinding.ProductRvItemBinding

class ProductsAdapter : PagingDataAdapter<Product, ProductsAdapter.ProductViewHolder>(Companion) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val dataBinding = ProductRvItemBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        return ProductViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position) ?: return
        holder.bindProduct(product)
    }

    companion object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    inner class ProductViewHolder(
        private val binding: ProductRvItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindProduct(product: Product) {
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
        }
    }
}
