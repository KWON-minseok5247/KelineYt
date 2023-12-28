package com.example.kelineyt.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


//Parcelable은 안드로이드의 IPC(Inter-Process Communication)를 위해 만들어진 인터페이스다.
// IPC는 같은 시스템에서 실행되는 서로 다른 프로세스 간 데이터를 전송하는 매커니즘이다.
// 안드로이드에선 이걸로 다른 앱 또는 앱 안의 컴포넌트 간에 데이터를 전달해서 통신할 수 있게 된다.
//https://onlyfor-me-blog.tistory.com/754

@Parcelize
data class Product(
    val id: String,
    val name: String,
    val category: String,
    val price: Float,
    val offerPercentage:Float? = null,
    val description: String? = null,
    val colors: List<Int>? = null,
    val sizes: List<String>? = null,
    val images: List<String>,

) : Parcelable {
    constructor(): this("0", "", "", 0f, images = emptyList())
}
