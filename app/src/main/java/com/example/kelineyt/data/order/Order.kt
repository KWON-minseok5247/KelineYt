package com.example.kelineyt.data.order

import android.os.Parcelable
import com.example.kelineyt.data.Address
import com.example.kelineyt.data.CartProduct
import com.example.kelineyt.firebase.FireBaseCommon
import com.google.firebase.auth.FirebaseAuth
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random.Default.nextLong


@Parcelize
// 이게 orders 콜렉션으로 가서 admin이 order 리스트를 확인하는 용도로 사용된다.
data class Order(
    val orderStatus: String = "",
    val totalPrice: Float = 0f,
    val products: List<CartProduct> = emptyList(),
    val address: Address = Address(),
    val userId: String = "",
    val date: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(Date()),
    val orderId: Long = nextLong(0,100_000_000_000) + totalPrice.toLong()

) : Parcelable
