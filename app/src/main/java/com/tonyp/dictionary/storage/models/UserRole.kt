package com.tonyp.dictionary.storage.models

import kotlinx.serialization.Serializable

@Serializable
enum class UserRole(val value: String) {

    USER("User"),
    ADMIN("Administrator"),
    BANNED("Blocked"),
    TEST("Test user")

}