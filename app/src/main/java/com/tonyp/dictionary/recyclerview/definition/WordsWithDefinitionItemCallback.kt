package com.tonyp.dictionary.recyclerview.definition

import androidx.recyclerview.widget.DiffUtil

class WordsWithDefinitionItemCallback : DiffUtil.ItemCallback<WordsWithDefinitionItem>() {

    override fun areItemsTheSame(
        oldItem: WordsWithDefinitionItem,
        newItem: WordsWithDefinitionItem
    ) = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: WordsWithDefinitionItem,
        newItem: WordsWithDefinitionItem
    ) = oldItem.id == newItem.id
}