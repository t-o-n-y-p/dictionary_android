package com.tonyp.dictionary.storage.models

import kotlinx.serialization.Serializable

@Serializable
enum class UserRole {

    USER,
    ADMIN,
    BANNED,
    TEST

}