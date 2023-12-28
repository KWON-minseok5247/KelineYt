package com.example.kelineyt.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ContentInfoCompat.Flags
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kelineyt.data.Product
import com.example.kelineyt.databinding.BestDealsRvItemBinding
import com.example.kelineyt.databinding.ProductRvItemBinding
import com.example.kelineyt.helper.getProductPrice

// Adapter는 데이터 리스트를 실제 눈으로 볼 수 있게 itemView로 변환하는 중간다리 역할을 한다.

//Adapter가 맡은 역할은 크게 아래의 세가지로 나눌 수 있다.

//RecyclerView에 보여줄 데이터 리스트 관리
//View 객체를 재사용하기 위한 ViewHolder 객체 생성
//데이터 리스트에서 position에 해당하는 데이터를 itemView에 표시



class BestProductsAdapter: RecyclerView.Adapter<BestProductsAdapter.BestProductsViewHolder>() {

    inner class BestProductsViewHolder(private val binding: ProductRvItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product){
            binding.apply {
                val priceAfterOffer = product.offerPercentage.getProductPrice(product.price)
                bestProductsTvNewPrice.text = "$ ${String.format("%.2f",priceAfterOffer)}"
                bestProductsTvPrice.paintFlags = bestProductsTvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                if (product.offerPercentage == null)
                    bestProductsTvNewPrice.visibility = View.INVISIBLE

                Glide.with(itemView).load(product.images[0]).into(bestProductsImgProduct)
                bestProductsTvName.text = product.name
                bestProductsTvPrice.text = "$ ${product.price}"
//                if (product.offerPercentage != null) {
//                    val newPrice = (product.price * product.offerPercentage)
//                    bestProductsTvNewPrice.text = "$ ${newPrice.toString()}"
//                    bestProductsTvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
//                }

//                else {
//                    val newPrice = null
//                    bestProductsTvNewPrice.text = newPrice
//                }

            }
        }
    }

    // RecyclerView의 성능 향상을 위해 사용하는 DiffUtil은 서로 다른 아이템인지를 체크하여 달라진 아이템만 갱신을 도와주는 Util이다. 아래는 자세한 내용
    //https://zion830.tistory.com/86
    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        // 컨트롤 + I를 누르면 필요한 함수를 자동으로 추가할 수 있다.
        // items는 고유값을 비교하는 것
        // 이게 먼저 실행된다. 이게 true로 반환이 되어야 contents로 넘어간다.
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }
        // contents는 아이템을 비교하는 것
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductsViewHolder {
        return BestProductsViewHolder(
            ProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    // getCurrentList() : adapter에서 사용하는 item 리스트에 접근하고 싶다면 사용하면 된다.
    override fun onBindViewHolder(holder: BestProductsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((Product) -> Unit)? = null

}