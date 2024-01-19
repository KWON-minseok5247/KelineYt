package com.example.kelineyt.paging.viewholder.data

import com.example.kelineyt.data.Product

data class SpecialProductsItems(
    val specialProducts: Product
) {
    override fun toString(): String {
        return "${javaClass.name}{" +
                "smallData=$specialProducts}"
    }
}