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

class MyPagingDataAdapter : PagingDataAdapter<Product, MyPagingDataAdapter.MyPagingDataViewHolder>(YourDataComparator) {

    inner class MyPagingDataViewHolder(
        private val binding: ProductRvItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
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
    }    // RecyclerView에서 ViewHolder 생성
    override fun onBindViewHolder(holder: MyPagingDataViewHolder, position: Int) {

        val product = getItem(position)
//        val item = getItem(position)
//        if (item != null) {
            holder.bind(product!!)
//        }
    }

    // ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPagingDataAdapter.MyPagingDataViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val dataBinding = ProductRvItemBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        return MyPagingDataViewHolder(dataBinding)
    }
}

object YourDataComparator : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        // 아이템이 같은지 여부 반환
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        // 아이템 내용이 같은지 여부 반환
        return oldItem == newItem
    }
}
