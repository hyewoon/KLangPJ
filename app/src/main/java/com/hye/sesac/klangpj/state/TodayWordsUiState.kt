package com.hye.sesac.klangpj.state


data class TodayWordsUiState(
    val word: String = "mock",
    val english: String = "",
    val pos: String = "",
    val wordGrade: String = "등급 없음",
    val examples: List<String> = emptyList(),
    val pronunciation: String = "",
    var isBasicLearned : Boolean = false,
    var isListened : Boolean = false,
    var isExampleLearned : Boolean = false,
    var isWritten : Boolean = false,
    var isRecorded : Boolean = false

){
 fun matchWord() {
     val firstExample = examples[0].toString()
     if (firstExample.contains(word)) {
         firstExample.replace(word, "<b>$word</b>")

     }
 }
    fun getPronunciationUrl() = pronunciation

}
