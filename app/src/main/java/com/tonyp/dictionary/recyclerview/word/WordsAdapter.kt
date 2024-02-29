package com.tonyp.dictionary.recyclerview.word

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tonyp.dictionary.R

class WordsAdapter(
    private val onItemClicked: (String) -> Unit
) : ListAdapter<WordsItem, WordsItemViewHolder>(WordsItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsItemViewHolder =
        WordsItemViewHolder(
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_words_item, parent, false),
            onItemClicked = onItemClicked)

    override fun onBindViewHolder(holder: WordsItemViewHolder, position: Int) =
        holder.bind(getItem(position))


}