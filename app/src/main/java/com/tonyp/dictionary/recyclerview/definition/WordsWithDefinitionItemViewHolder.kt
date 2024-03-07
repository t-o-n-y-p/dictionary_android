package com.tonyp.dictionary.recyclerview.definition

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tonyp.dictionary.R

class WordsWithDefinitionItemViewHolder(
    view: View,
    private val onItemClicked: (WordsWithDefinitionItem, Int) -> Unit = { _, _ -> }
) : RecyclerView.ViewHolder(view) {

    private val valueTextView: TextView
    by lazy { itemView.findViewById(R.id.value) }
    private val valueDefinitionTextView: TextView
    by lazy { itemView.findViewById(R.id.value_definition) }

    fun bind(item: WordsWithDefinitionItem) {
        valueTextView.text = item.word
        valueDefinitionTextView.text = item.definition
        itemView.setOnClickListener {
            onItemClicked(item, absoluteAdapterPosition)
        }
    }

}