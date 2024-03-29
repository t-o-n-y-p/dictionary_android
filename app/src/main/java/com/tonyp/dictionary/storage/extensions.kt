package com.tonyp.dictionary.storage

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.serializer

inline fun <reified T> SharedPreferences.get(): T? =
    getString(T::class.simpleName, "{}")?.let { Json.decodeFromString<T>(it) }

inline fun <reified T> SharedPreferences.put(value: T?) =
    value?.let { v ->
        edit {
            putString(
                T::class.simpleName,
                Json.encodeToString(EmptySerializersModule().serializer(), v))
        }
    } ?: edit { putString(T::class.simpleName, null) }

inline fun <reified T> SharedPreferences.cleanup() = put(null as? T)
