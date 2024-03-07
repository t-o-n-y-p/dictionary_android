package com.tonyp.dictionary.recyclerview.word

import com.tonyp.dictionary.api.v1.models.MeaningResponseFullObject

object WordsItemMapper {

    fun map(objects: List<MeaningResponseFullObject>): List<WordsItem> =
        objects
            .groupBy(keySelector = { it.word }, valueTransform = { it.value })
            .map { group ->
                WordsItem(
                    value = group.key.orEmpty(),
                    definitions = mapDefinitions(group.value.filterNotNull())
                )
            }

    private fun mapDefinitions(values: List<String>): String =
        when (values.size) {
            0 -> ""
            1 -> values[0]
            else -> values.mapIndexed { i, e -> "${i + 1}. $e" }.joinToString(separator = "\n")
        }
}