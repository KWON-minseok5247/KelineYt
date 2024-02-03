package com.example.kelineyt.data

import android.os.Parcelable
import java.io.Serializable

data class ChatRoom(
    val users: Map<String, Boolean>? = HashMap(),
    var messages: Map<String,Message>? = HashMap()
) : Serializable {
}
//채팅방의 정보를 저장하는 ChatRoom 객체이다.
//
//Firebase RealtimeDatabase는 JSON 형태로 데이터를 저장하기 떄문에,
//
//Map형태로 데이터를 전달하는 것이 좋다.
//
//나는 일전에 값을 List형태로 전달해보았으나 런타임 오류로 실패했기에,Map을 전달하는 것을 권장한다.
//
//채팅방에 포함된 사용자는 users에 저장하고,
//
//해당 채팅방에서 오간 메시지는 messages에 저장하기로 하였다.