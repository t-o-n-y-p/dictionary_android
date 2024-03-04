package com.tonyp.dictionary.recyclerview.definition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tonyp.dictionary.R

class WordsWithDefinitionAdapter(
    private val onItemClicked: (WordsWithDefinitionItem) -> Unit
): ListAdapter<WordsWithDefinitionItem, WordsWithDefinitionItemViewHolder>(WordsWithDefinitionItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WordsWithDefinitionItemViewHolder(
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_words_with_definitions_item, parent, false),
            onItemClicked = onItemClicked
        )

    override fun onBindViewHolder(holder: WordsWithDefinitionItemViewHolder, position: Int) =
        holder.bind(getItem(position))


}