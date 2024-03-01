package com.tonyp.dictionary.service.dto.auth

import kotlinx.serialization.Serializable

@Serializable
enum class UserGroup {

    USER,
    ADMIN,
    BANNED,
    TEST

}