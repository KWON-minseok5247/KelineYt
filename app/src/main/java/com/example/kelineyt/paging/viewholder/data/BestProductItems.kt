package com.example.kelineyt.paging.viewholder.data

import com.example.kelineyt.data.Product

data class BestProductItems(
    val bestProduct: Product
) {
    override fun toString(): String {
        return "${javaClass.name}{" +
                "smallData=$bestProduct}"
    }
}