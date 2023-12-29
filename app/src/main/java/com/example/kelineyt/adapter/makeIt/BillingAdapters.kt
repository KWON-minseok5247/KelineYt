package com.example.kelineyt.adapter.makeIt

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kelineyt.data.Address
import com.example.kelineyt.data.CartProduct
import com.example.kelineyt.data.Product
import com.example.kelineyt.databinding.AddressRvItemBinding
import com.example.kelineyt.databinding.BillingProductsRvItemBinding

class BillingAdapters : RecyclerView.Adapter<BillingAdapters.BillingViewHolder>() {

    inner class BillingViewHolder(val binding: BillingProductsRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartProduct: CartProduct) {
//            binding.apply {
////                imageCartProduct.setImageDrawable(Drawable.createFromPath(cartProduct!!.product.images[0]))
//                tvProductCartName.text = cartProduct.product.name
//                tvBillingProductQuantity.text = cartProduct.quantity.toString()
//                tvCartProductSize.text = cartProduct.selectedSize
//                tvProductCartPrice.text = cartProduct.product.price.toString() // 얘도 할인전 가격일듯?
//            }
            Glide.with(itemView).load(cartProduct.product.images[0]).into(binding.imageCartProduct)
            binding.apply {
                tvProductCartName.text = cartProduct.product.name
                tvBillingProductQuantity.text = cartProduct.quantity.toString()
                if (cartProduct.product.offerPercentage != null) {
                    tvProductCartPrice.text =
//                        (cartProduct.product.price * (1 - cartProduct.product.offerPercentage)).toString()
                        String.format("%.2f", (cartProduct.product.price * (1 - cartProduct.product.offerPercentage)))
                } else {
                    tvProductCartPrice.text = String.format("%.2f", cartProduct.product.price)

                }
                if (cartProduct.selectedSize != null) {
                    imageCartProductSize.visibility = View.VISIBLE
                    tvCartProductSize.text = cartProduct.selectedSize
                } else {
                    tvCartProductSize.visibility = View.GONE
                    imageCartProductSize.visibility = View.GONE
                }
                if (cartProduct.selectedColor != null) {
                    imageCartProductColor.setImageDrawable(ColorDrawable(cartProduct.selectedColor))
                } else {
                    imageCartProductColor.visibility = View.GONE
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingViewHolder {
        val binding =
            BillingProductsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BillingViewHolder(binding)
    }


    val diffCallback = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffCallback)

    override fun onBindViewHolder(holder: BillingViewHolder, position: Int) {
        val cartProduct = differ.currentList[position]
        holder.bind(cartProduct)

        holder.itemView.setOnClickListener {
            onClick?.invoke(cartProduct)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((CartProduct) -> Unit)? = null
}