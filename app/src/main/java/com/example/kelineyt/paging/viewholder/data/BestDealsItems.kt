package com.example.kelineyt.paging.viewholder.data

data class BestDealsItems(
    val bestDeals: String
) {
    override fun toString(): String {
        return "${javaClass.name}{" +
                "smallData=$bestDeals}"
    }
}