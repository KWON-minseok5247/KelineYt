package com.example.kelineyt.data

// object가 있어서 sealed class로 정의한다.

// https://kotlinworld.com/165 sealed class와 object의 관계를 자세하게 알려준다.
sealed class Category(val category: String) {
    object Chair: Category("Chair")
    object Cupboard: Category("Cupboard")
    object Table: Category("Table")
    object Accessory: Category("Accessory")
    object Furniture: Category("Furniture")
}