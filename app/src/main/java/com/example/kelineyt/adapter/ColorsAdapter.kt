package com.example.kelineyt.adapter

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kelineyt.databinding.ColorRvItemBinding

class ColorsAdapter: RecyclerView.Adapter<ColorsAdapter.ColorsAdapterViewHolder>() {

    private var selectedPosition = -1



    inner class ColorsAdapterViewHolder(private val binding: ColorRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(color: Int, position: Int) {
            // Int를 컬러로 만드는 과정인듯.
            val imageDrawable = ColorDrawable(color)
            binding.imageColor.setImageDrawable(imageDrawable)
            if (position == selectedPosition) { // Color is selected
                binding.apply {
                    imageShadow.visibility = View.VISIBLE
                    imagePicked.visibility = View.VISIBLE
                }
            } else { // Color is not selected
                binding.apply {
                    imageShadow.visibility = View.INVISIBLE
                    imagePicked.visibility = View.INVISIBLE
                }
            }

        }
    }
    private val diffCallback = object : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorsAdapterViewHolder {
        return ColorsAdapterViewHolder(ColorRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ColorsAdapterViewHolder, position: Int) {
        val color = differ.currentList[position]
        holder.bind(color,position)

        // notifyItemChanged: 아이템 한개의 내용이 변경되었을 경우 사용한다.
        // 여기는 초기에 -1로 되어 있으니 색상을 클릭하는 순간, 클릭된 건 notifyItem으로 selectedPosition으로  해당 아이템뷰 bind를 다시 한다.
        // 그러면 처음에 당연히 position == selectedPosition으로 넘어가서 해당 아이템뷰는 클릭한 것으로 묘사됨,
        // 그리고 2번째 이후로부터는 selectedPostion이 0보다 크게 됨. 그러면 position이 달라지게 됨(?)
        // 그래서 해당 itemview는 invisible이 되고 새로 정의한 selectedPostion은 값이 변경되고 visible로 변경됨.
        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0) // 이전에 체크된 아이템뷰가 체크 해제되는 부분
                notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition // 새로운 아이템뷰가 체크되는 부분.
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(color)
            // invoke란?
            // 이름 없이 간편하게 호출될 수 있는 함수
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onItemClick: ((Int) -> Unit)? = null

    //onItemClick은 리사이클러뷰의 아이템을 클릭했을 때 실행되는 콜백 함수입니다.
    //여기서는 onBindViewHolder 메소드 내부에서 아이템뷰의 클릭 이벤트를 처리하는 부분에 사용되고 있습니다.
    //- selectedPosition = holder.adapterPosition : 현재 클릭한 아이템뷰의 위치를 selectedPosition 변수에 저장합니다.
    //- notifyItemChanged(selectedPosition) : 새로 선택된 아이템뷰에 체크 표시를 추가합니다.
    //- onItemClick?.invoke(color) : onItemClick이 null이 아닌 경우에만 실행되며,
    //  클릭한 아이템의 색상을 매개변수로 넘겨서 onItemClick 콜백을 호출합니다.

    //따라서 onItemClick에 원하는 동작을 구현하면, 해당 동작이 클릭한 아이템에 대해 실행됩니다.
    //예를 들어, 클릭한 아이템의 색상을 변경하거나 다른 동작을 수행할 수 있습니다.

}