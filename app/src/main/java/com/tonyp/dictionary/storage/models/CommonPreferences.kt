package com.tonyp.dictionary.storage.models

import kotlinx.serialization.Serializable

@Serializable
data class CommonPreferences(
    val recentWords: List<String> = emptyList()
)
