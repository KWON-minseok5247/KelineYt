package com.example.kelineyt.paging.viewholder

data class ListDataWrapper(
    val type: ListDataType,
    val data: Any
) {
    override fun toString(): String {
        return "${javaClass.name}{" +
                "type=$type, " +
                "data=$data}"
    }
}