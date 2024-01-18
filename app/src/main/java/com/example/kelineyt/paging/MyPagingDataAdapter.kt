package com.example.kelineyt.paging
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingDataAdapter
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kelineyt.data.Product
import com.example.kelineyt.databinding.ProductRvItemBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MyPagingDataAdapter : PagingDataAdapter<Product, MyPagingDataAdapter.ProductViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ProductViewHolder(private val binding: ProductRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
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


}