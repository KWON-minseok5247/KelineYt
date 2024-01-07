package com.example.kelineyt.adapter.makeIt

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kelineyt.R
import com.example.kelineyt.data.Address
import com.example.kelineyt.data.Product
import com.example.kelineyt.databinding.AddressRvItemBinding
import com.example.kelineyt.databinding.SizeRvItemBinding

class AddressAdapters : RecyclerView.Adapter<AddressAdapters.AddressViewHolder>() {

    private var selectedPosition = -1
    private var delay : Long = 0

    inner class AddressViewHolder(val binding: AddressRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(address: Address, position: Int) {
            if (selectedPosition == position) {
                binding.buttonAddress.text = address.addressTitle
                binding.buttonAddress.background =
                    ColorDrawable(itemView.context.resources.getColor(R.color.g_dark_blue))
            } else {
                binding.buttonAddress.text = address.addressTitle
//                binding.buttonAddress.background =
//                    ColorDrawable(R.drawable.unselected_button_background)
                binding.buttonAddress.background = ColorDrawable(itemView.context.resources.getColor(R.color.g_white))
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding =
            AddressRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }


    val diffCallback = object : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffCallback)

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = differ.currentList[position]
        holder.bind(address, position)

        holder.binding.buttonAddress.setOnClickListener {
            if (System.currentTimeMillis() > delay) {
                delay = System.currentTimeMillis() + 200
                if (selectedPosition >= 0) { // 이미 클릭되어 있던 포지션을 체크 해제하는 과정
                    notifyItemChanged(selectedPosition)
                    notifyItemChanged(differ.currentList.lastIndex)
                }
                selectedPosition = holder.adapterPosition // 클릭한 포지션을 변경하는 과정
                notifyItemChanged(selectedPosition)
                notifyItemChanged(differ.currentList.lastIndex)

                onClick?.invoke(address)
            } else {
                onDoubleClick?.invoke(address)
            }
//            if (System.currentTimeMillis() <= delay) {
//                // 2번 클릭했을 때
//
//            }


        }


    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((Address) -> Unit)? = null
    var onDoubleClick: ((Address) -> Unit)? = null

}


