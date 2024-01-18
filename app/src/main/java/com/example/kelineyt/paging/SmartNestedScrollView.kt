package com.example.kelineyt.paging


import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView


class SmartNestedScrollView : NestedScrollView {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = measuredHeight
        val childCount = childCount
        var heightNeededToDisplayAllChildren = 0
        var shouldScroll = false
        for (i in 0 until childCount) {
            heightNeededToDisplayAllChildren += getChildAt(i).height
            if (heightNeededToDisplayAllChildren > height) {
                shouldScroll = true
                break // no need to go through all the children once the
            }
        }
        isNestedScrollingEnabled = shouldScroll
    }
}
