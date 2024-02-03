package com.example.kelineyt.data

import java.io.Serializable

data class Message(
    var senderUid: String = "",
    var sended_date: String = "",
    var content: String = "",
    var confirmed:Boolean=false
) : Serializable {
}
//채팅방에서 오간 메시지를 저장하는 객체다.
//
//차례대로 보낸 사람의 uid,
//
//보낸 시각,
//
//메시지의 내용,
//
//상대방의 확인 여부를 저장한다.
//
//그리고 각 data class는 인텐트로 전달될 필요가 있어
//
//Serializable을 구현하도록 하였다.