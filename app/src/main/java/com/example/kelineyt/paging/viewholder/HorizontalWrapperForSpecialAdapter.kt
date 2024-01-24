package com.example.kelineyt.paging.viewholder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kelineyt.adapter.BestDealsAdapter
import com.example.kelineyt.adapter.SpecialProductsAdapter
import com.example.kelineyt.adapter.makeIt.TemporarySpecialProductsAdapter
import com.example.kelineyt.databinding.ViewHorizontalWrapperBinding
import com.example.kelineyt.databinding.ViewHorizontalWrapperForSpecialBinding

class HorizontalWrapperForSpecialAdapter(
    private val adapter: TemporarySpecialProductsAdapter
) : RecyclerView.Adapter<HorizontalWrapperForSpecialAdapter.HorizontalWrapperForSpecialViewHolder>() {
    private var lastScrollX = 0

    companion object {
        private const val KEY_SCROLL_X = "horizontal.wrapper.adapter.key_scroll_x"
        const val VIEW_TYPE = 4444
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalWrapperForSpecialViewHolder {
        return HorizontalWrapperForSpecialViewHolder(
            ViewHorizontalWrapperForSpecialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE
    }

    override fun onBindViewHolder(holder: HorizontalWrapperForSpecialViewHolder, position: Int) {
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

    inner class HorizontalWrapperForSpecialViewHolder(
        private val binding: ViewHorizontalWrapperForSpecialBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(adapter: TemporarySpecialProductsAdapter, lastScrollX: Int, onScrolled: (Int) -> Unit) {
            val context = binding.root.context
            binding.recyclerViewForSpeical.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewForSpeical.adapter = adapter
            binding.recyclerViewForSpeical.doOnPreDraw {
                binding.recyclerViewForSpeical.scrollBy(lastScrollX, 0)
            }
            binding.recyclerViewForSpeical.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    onScrolled(recyclerView.computeHorizontalScrollOffset())
                }
            })
        }
    }
}
