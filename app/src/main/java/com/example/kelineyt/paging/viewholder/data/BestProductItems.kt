package com.example.kelineyt.paging.viewholder.data

data class BestProductItems(
    val bestProduct: String
) {
    override fun toString(): String {
        return "${javaClass.name}{" +
                "smallData=$bestProduct}"
    }
}