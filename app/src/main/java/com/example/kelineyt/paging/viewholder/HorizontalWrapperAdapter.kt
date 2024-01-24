package com.example.kelineyt.paging.viewholder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kelineyt.adapter.BestDealsAdapter
import com.example.kelineyt.databinding.ViewHorizontalWrapperBinding

class HorizontalWrapperAdapter(
    private val adapter: BestDealsAdapter
) : RecyclerView.Adapter<HorizontalWrapperAdapter.HorizontalWrapperViewHolder>() {
    private var lastScrollX = 0

    companion object {
        private const val KEY_SCROLL_X = "horizontal.wrapper.adapter.key_scroll_x"
        const val VIEW_TYPE = 3333
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalWrapperViewHolder {
        return HorizontalWrapperViewHolder(
            ViewHorizontalWrapperBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE
    }

    override fun onBindViewHolder(holder: HorizontalWrapperViewHolder, position: Int) {
        holder.bind(adapter, lastScrollX) { x ->
            lastScrollX = x
        }
    }

    override fun getItemCount(): Int = 1

    fun onSaveState(outState: Bundle) {
        outState.putInt(KEY_SCROLL_X, lastScrollX)
    }

    fun onRestoreState(savedState: Bundle) {
        lastScrollX = savedState.getInt(KEY_SCROLL_X)
    }

    inner class HorizontalWrapperViewHolder(
        private val binding: ViewHorizontalWrapperBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(adapter: BestDealsAdapter, lastScrollX: Int, onScrolled: (Int) -> Unit) {
            val context = binding.root.context
            binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.doOnPreDraw {
                binding.recyclerView.scrollBy(lastScrollX, 0)
            }
            binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    onScrolled(recyclerView.computeHorizontalScrollOffset())
                }
            })
        }
    }
}
