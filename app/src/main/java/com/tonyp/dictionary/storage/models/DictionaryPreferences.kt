package com.tonyp.dictionary.storage.models

import kotlinx.serialization.Serializable

@Serializable
data class DictionaryPreferences(
    val recentWords: List<String> = emptyList()
) {

    fun copyAndAdd(value: String): DictionaryPreferences =
        DictionaryPreferences(
            recentWords.toMutableList().apply {
                remove(value)
                takeIf { it.size == MAX_SIZE }?.removeAt(0)
                add(value)
            }
        )

    companion object {
        const val MAX_SIZE = 5
    }

}
