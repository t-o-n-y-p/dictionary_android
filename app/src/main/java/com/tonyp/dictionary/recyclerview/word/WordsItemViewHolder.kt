package com.tonyp.dictionary.recyclerview.word

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tonyp.dictionary.R

class WordsItemViewHolder(
    view: View,
    private val onItemClicked: (WordsItem) -> Unit = {}
) : RecyclerView.ViewHolder(view) {

    private val textView: TextView by lazy { itemView.findViewById(R.id.value) }

    fun bind(item: WordsItem) {
        textView.text = item.value
        itemView.setOnClickListener {
            onItemClicked(item)
        }
    }

}