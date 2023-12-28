package com.example.kelineyt.adapter.makeIt

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kelineyt.R
import com.example.kelineyt.data.CartProduct
import com.example.kelineyt.databinding.CartProductItemBinding
import com.example.kelineyt.fragments.makeIt.ProductDetailsFragments
import com.example.kelineyt.fragments.makeIt.ProductDetailsFragmentsArgs
import java.io.ByteArrayOutputStream

class CartProductAdapters: RecyclerView.Adapter<CartProductAdapters.CartProductViewHolders>() {
    inner class CartProductViewHolders(val binding: CartProductItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(cartProduct: CartProduct) {
            Glide.with(itemView).load(cartProduct.product.images[0]).into(binding.imageCartProduct)
            binding.apply {
                tvProductCartName.text = cartProduct.product.name
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
                tvCartProductQuantity.text = cartProduct.quantity.toString()
            }

//            binding.imagePlus.setOnClickListener {
//                onIncreaseClick?.invoke(cartProduct)
//            }


        }
    }
    // dummy가 나오는 이유는 viewholder를 제대로 작성하지 않아서 발생하는 문제인 듯 하다.

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartProductViewHolders {
        return CartProductViewHolders(
            CartProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    private val diffCallback = object: DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }
//    private fun bitmapToString(bitmap: Bitmap): String {
//        val byteArrayOutputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
//        val byteArray = byteArrayOutputStream.toByteArray()
//        return Base64.encodeToString(byteArray, Base64.DEFAULT)
//    }
//
//    private fun stringToBitmap(encodedString: String): Bitmap {
//        val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
//        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
//    }

    val differ = AsyncListDiffer(this,diffCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CartProductViewHolders, position: Int) {
        val cartProduct = differ.currentList[position]
        holder.bind(cartProduct)

        holder.itemView.setOnClickListener {
            onClick?.invoke(cartProduct)
        }

        holder.binding.imagePlus.setOnClickListener {
            onIncreaseClick?.invoke(cartProduct)
        }

        holder.binding.imageMinus.setOnClickListener {
            onDecreaseClick?.invoke(cartProduct)
//            holder.binding.tvCartProductQuantity.setText((cartProduct.quantity - 1).toString())

        }

//        holder.itemView.setOnClickListener {
//            onIncreaseClick?.invoke(cartProduct)
//        }
//
//        holder.itemView.setOnClickListener {
//            onDecreaseClick?.invoke(cartProduct)
//        }

    }

    var onClick : ((CartProduct) -> Unit)? = null
    var onIncreaseClick : ((CartProduct) -> Unit)? = null
    var onDecreaseClick : ((CartProduct) -> Unit)? = null


}