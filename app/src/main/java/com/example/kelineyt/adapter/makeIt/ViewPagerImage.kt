package com.example.kelineyt.adapter.makeIt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kelineyt.R
import com.example.kelineyt.data.Product
import com.example.kelineyt.databinding.SizeRvItemBinding
import com.example.kelineyt.databinding.ViewpagerImageItemBinding
import kotlinx.coroutines.NonDisposableHandle.parent

class ViewPagerImage : RecyclerView.Adapter<ViewPagerImage.PagerViewHolder>() {

    private val diffCallback = object: DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)


    inner class PagerViewHolder(val binding: ViewpagerImageItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: String?) {
            Glide.with(itemView).load(image).into(binding.imageProductDetails)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        return PagerViewHolder(
            ViewpagerImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val image = differ.currentList[position]
        holder.bind(image)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}
