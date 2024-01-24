package com.example.kelineyt.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kelineyt.R
import com.example.kelineyt.databinding.ListItemFooterBinding

class SampleLoadStateAdapter : LoadStateAdapter<SampleLoadStateAdapter.SampleLoadStateViewHolder>() {

    /**
     * LoadState 값을 받아 로딩 상태에 따라 ProgressBar의 visible 설정 처리
     */
    inner class SampleLoadStateViewHolder(
        private val binding: ListItemFooterBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
//            binding.pbLoading.isVisible = true
            binding.pbLoading.isVisible = loadState is LoadState.Loading
        }
    }

    override fun onBindViewHolder(holder: SampleLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): SampleLoadStateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_footer, parent, false)
        val binding = ListItemFooterBinding.bind(view)
        return SampleLoadStateViewHolder(binding)
    }
}
