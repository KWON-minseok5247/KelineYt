package com.example.kelineyt.adapter.makeIt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kelineyt.data.Product
import com.example.kelineyt.databinding.ItemLoadingBinding
import com.example.kelineyt.databinding.ProductRvItemBinding

// Adapter는 데이터 리스트를 실제 눈으로 볼 수 있게 itemView로 변환하는 중간다리 역할을 한다.

//Adapter가 맡은 역할은 크게 아래의 세가지로 나눌 수 있다.

//RecyclerView에 보여줄 데이터 리스트 관리
//View 객체를 재사용하기 위한 ViewHolder 객체 생성
//데이터 리스트에서 position에 해당하는 데이터를 itemView에 표시


class BestProductsAdapters : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    inner class BestProductsViewHolder(val binding: ProductRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
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
    }

    // 아이템뷰에 프로그레스바가 들어가는 경우
    inner class LoadingViewHolder(private val binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun getItemViewType(position: Int): Int {
        // 게시물과 프로그레스바 아이템뷰를 구분할 기준이 필요하다.
        return when (differ.currentList[position].id
        ) {
            " " -> VIEW_TYPE_LOADING
            else -> VIEW_TYPE_ITEM
        }
    }


    val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                BestProductsViewHolder(
                    ProductRvItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemLoadingBinding.inflate(layoutInflater, parent, false)
                LoadingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is BestProductsViewHolder){
        val product = differ.currentList[position]
        holder.bind(product)

            // fun bind에 하기 보다는 bindViewHolder에 하는 게 더 효율적인가?
        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
        }else{ // null일 경우를 대비해서 만들어두긴 했다.

        }
    }

//    override fun onBindViewHolder(holder: BestProductsViewHolder, position: Int) {
//        val product = differ.currentList[position]
//        holder.bind(product)
//    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((Product) -> Unit)? = null

//    fun setList(notice: MutableList<Content>) {
//        items.addAll(notice)
//        items.add(Content(" ", " ")) // progress bar 넣을 자리
//    }

//    fun deleteLoading(){
//        differ.currentList.removeAt(differ.currentList.lastIndex)
//        // 로딩이 완료되면 프로그레스바를 지움
//    }
}