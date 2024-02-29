package com.tonyp.dictionary.recyclerview.word

import androidx.recyclerview.widget.DiffUtil

class WordsItemCallback : DiffUtil.ItemCallback<WordsItem>() {

    override fun areItemsTheSame(p0: WordsItem, p1: WordsItem): Boolean =
        p0 == p1

    override fun areContentsTheSame(p0: WordsItem, p1: WordsItem): Boolean =
        p0.value == p1.value
}