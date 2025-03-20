package com.hye.sesac.klangpj.state


data class TodayWordUiState(
    val word: String = "",
    val english: String = "",
    val pos: String = "",
    val wordGrade: String? = "등급 없음",
    val examples: List<String> = emptyList(),
    val pronunciation: String = ""
){
 fun matchWord(){
    val firstExample =  examples[0].toString()
     if(firstExample.contains(word)){
         firstExample.replace(word, "<b>$word</b>")
     }

 }
}
