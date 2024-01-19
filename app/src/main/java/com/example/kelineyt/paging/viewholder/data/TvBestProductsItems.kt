package com.example.kelineyt.paging.viewholder.data

data class TvBestProductsItems(
    val tvBestProduct: String
) {
    override fun toString(): String {
        return "${javaClass.name}{" +
                "smallData=$tvBestProduct}"
    }
}