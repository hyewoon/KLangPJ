package com.hye.mylibrary.data.model


data class FireStoreWords (
    val english: String = "",
    val frequency: Int= 0,
    val korean: String = "",
    val wordClass: String = "",

)

data class ExampleInfo(
    val type: String = "",
    val example: String =""
)