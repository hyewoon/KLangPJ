package com.hye.sesac.klangpj.state


data class TodayWordUiState(
    val word: String = "",
    val english: String = "",
    val pos: String = "",
    val wordGrade: String? = "등급 없음",
    val examples: List<String> = emptyList(),
    val pronunciation: String = ""
){

}
