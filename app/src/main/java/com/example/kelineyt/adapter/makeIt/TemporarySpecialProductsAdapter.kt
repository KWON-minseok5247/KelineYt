package com.example.kelineyt.adapter.makeIt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kelineyt.data.Product
import com.example.kelineyt.databinding.SpecialRvItemBinding


class TemporarySpecialProductsAdapter: RecyclerView.Adapter<TemporarySpecialProductsAdapter.TemporarySpecialProductsViewHolder>() {
    inner class TemporarySpecialProductsViewHolder(val binding: SpecialRvItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            val image = product.images[0]
            Glide.with(itemView).load(image).into(binding.ImageSpecialRvItem)
            binding.apply {
                tvSpecialProductName.text = product.name
                tvSpecialProductPrice.text = product.price.toString()
            }
        }
    }
    // dummy가 나오는 이유는 viewholder를 제대로 작성하지 않아서 발생하는 문제인 듯 하다.

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TemporarySpecialProductsViewHolder {
        return TemporarySpecialProductsViewHolder(
            SpecialRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    private val diffCallback = object: DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: TemporarySpecialProductsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }
    var onClick : ((Product) -> Unit)? = null


}