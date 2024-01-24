package com.example.kelineyt.paging.viewholder.data

import com.example.kelineyt.data.Product

data class BestDealsItems(
    val bestDeals: Product
) {
    override fun toString(): String {
        return "${javaClass.name}{" +
                "smallData=$bestDeals}"
    }
}