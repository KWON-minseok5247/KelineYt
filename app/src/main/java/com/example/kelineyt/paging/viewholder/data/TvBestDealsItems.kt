package com.example.kelineyt.paging.viewholder.data

data class TvBestDealsItems(
    val tvBestDeals: String
) {
    override fun toString(): String {
        return "${javaClass.name}{" +
                "smallData=$tvBestDeals}"
    }
}