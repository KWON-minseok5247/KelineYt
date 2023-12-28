package com.example.kelineyt.adapter.makeIt

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
    private var selectedAddress = -1

    inner class AddressViewHolder(val binding: AddressRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(address: Address) {
            if (selectedAddress >= 0) {
                binding.buttonAddress.text = address.addressTitle
                binding.buttonAddress.background = ColorDrawable(itemView.resources.getColor(R.color.g_dark_blue))

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
        holder.bind(address)
//
        if (selectedAddress >= 0) { //
            notifyItemChanged(position)
        } else {
            selectedAddress = holder.adapterPosition
            notifyItemChanged(position)
        }



        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((Address) -> Unit)? = null
}


