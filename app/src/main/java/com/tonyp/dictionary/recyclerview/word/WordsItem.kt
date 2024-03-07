package com.tonyp.dictionary.recyclerview.word

data class WordsItem(
    val value: String = "",
    val definitions: List<String> = emptyList()
)
