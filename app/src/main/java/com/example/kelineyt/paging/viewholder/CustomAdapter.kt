package com.example.kelineyt.paging.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kelineyt.R
import com.example.kelineyt.data.Product
import com.example.kelineyt.databinding.*
import com.example.kelineyt.paging.viewholder.data.*
import com.example.kelineyt.paging.viewholder.vh.*

class CustomAdapter(
    private val items: ArrayList<ListDataWrapper>
) : PagingDataAdapter<Product, RecyclerView.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.tv_best_deals -> {
                TvBestDealsViewHolder(
                    TvBestDealsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            R.layout.tv_best_products -> {
                TvBestProductViewHolder(
                    TvBestProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            R.layout.best_deals_rv_item -> {
                PagingBestDealsViewHolder(
                    BestDealsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            R.layout.product_rv_item -> {
                BestProductViewHolder(
                    ProductRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            R.layout.special_rv_item -> {
                PagingSpecialProductsViewHolder(
                    SpecialRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            else -> throw IllegalArgumentException("Invalid viewType")
        }

        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            R.layout.tv_best_deals -> {
                val data = items[position].data as TvBestDealsItems
                (holder as TvBestDealsViewHolder).bind(data)
            }
            R.layout.tv_best_products -> {
                val data = items[position].data as TvBestProductsItems
                (holder as TvBestProductViewHolder).bind(data)
            }
            R.layout.best_deals_rv_item -> {
                val data = items[position].data as BestDealsItems
//                (holder as PagingBestDealsViewHolder).bind(data)
            }
            R.layout.product_rv_item -> {
                val data = items[position].data as BestProductItems
//                (holder as BestProductViewHolder).bind(data)
            }
            R.layout.special_rv_item -> {
                // 여기서 data가
                val data = items[position].data as SpecialProductsItems
                (holder as PagingSpecialProductsViewHolder).bind(data)
            }
        }
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
    val differ = AsyncListDiffer(this, diffCallback)

    override fun getItemCount(): Int {
//        return super.getItemCount()
        return items.size
    }



    override fun getItemViewType(position: Int) = when (items[position].type) {
        ListDataType.TYPE_BEST_DEAL -> R.layout.best_deals_rv_item
        ListDataType.TYPE_BEST_PRODUCTS -> R.layout.product_rv_item
        ListDataType.TYPE_SPECIAL_PRODUCTS -> R.layout.special_rv_item
        ListDataType.TYPE_TV_BEST_DEAL -> R.layout.tv_best_deals
        ListDataType.TYPE_TV_BEST_PRODUCTS -> R.layout.tv_best_products
    }




}