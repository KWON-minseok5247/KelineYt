package com.example.kelineyt.adapter.makeIt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kelineyt.data.Product
import com.example.kelineyt.databinding.FragmentProductDetailsBinding
import com.example.kelineyt.databinding.SizeRvItemBinding

class SizeAdapters: RecyclerView.Adapter<SizeAdapters.SizeAdapterViewHolders>() {
    private var selectedPosition = -1
    inner class SizeAdapterViewHolders(val binding: SizeRvItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(size: String, position: Int) {
            binding.apply {
                tvSize.text = size
            }
            // 여기서 만약에 내가 클릭을 했을 때 그림자를 없애고 생기게 하기.
            if (selectedPosition == position) {
                binding.imageShadow.visibility = View.VISIBLE
            } else {
                binding.imageShadow.visibility = View.INVISIBLE
            }
        }
    }
    // dummy가 나오는 이유는 viewholder를 제대로 작성하지 않아서 발생하는 문제인 듯 하다.

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SizeAdapterViewHolders {
        return SizeAdapterViewHolders(
            SizeRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    private val diffCallback = object: DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SizeAdapterViewHolders, position: Int) {
        val size = differ.currentList[position]
        holder.bind(size, position)

        // notifyItemChanged: 아이템 한개의 내용이 변경되었을 경우 사용한다.
        // 여기는 초기에 -1로 되어 있으니 색상을 클릭하는 순간, 클릭된 건 notifyItem으로
        // selectedPosition으로  해당 아이템뷰 bind를 다시 한다.
        // 그러면 처음에 당연히 position == selectedPosition으로 넘어가서 해당 아이템뷰는 클릭한 것으로 묘사됨,
        // 그리고 2번째 이후로부터는 selectedPostion이 0보다 크게 됨. 그러면 position이 달라지게 됨(?)
        // 그래서 해당 itemview는 invisible이 되고 새로 정의한 selectedPostion은 값이 변경되고 visible로 변경됨.

        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0) { // 이미 선택했을 때 체크된 것을 지우는 용도
                notifyItemChanged(selectedPosition)
            } // 2번째 이후부터
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onClick?.invoke(size)
        }
    }
    var onClick : ((String) -> Unit)? = null


}