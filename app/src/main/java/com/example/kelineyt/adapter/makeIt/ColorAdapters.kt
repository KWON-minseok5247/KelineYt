package com.example.kelineyt.adapter.makeIt

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kelineyt.databinding.ColorRvItemBinding

class ColorAdapters: RecyclerView.Adapter<ColorAdapters.ColorAdaptersViewHolder>() {
    private var selectedPosition = -1
    inner class ColorAdaptersViewHolder(val binding: ColorRvItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(color: Int, position: Int) {
            val productColor = ColorDrawable(color)
            binding.apply {
                imageColor.setImageDrawable(productColor)
                // 여기서 클릭을 했을 때와 클릭을 안했을 때의 차이를 나누는 것 같다.
            }
            if (selectedPosition == position) {
                binding.apply {
                    imageShadow.visibility = View.VISIBLE
                    imagePicked.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    imageShadow.visibility = View.INVISIBLE
                    imagePicked.visibility = View.INVISIBLE
                }
            }

        }
    }
    // dummy가 나오는 이유는 viewholder를 제대로 작성하지 않아서 발생하는 문제인 듯 하다.

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ColorAdaptersViewHolder {
        return ColorAdaptersViewHolder(
            ColorRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    private val diffCallback = object: DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ColorAdaptersViewHolder, position: Int) {
        val color = differ.currentList[position]
        holder.bind(color, position)

        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0) { // 이미 선택했을 때 체크된 것을 지우는 용도
                notifyItemChanged(selectedPosition)
            } // 그 이외 변경할 때
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onClick?.invoke(color)
        }
    }
    var onClick : ((Int) -> Unit)? = null


}