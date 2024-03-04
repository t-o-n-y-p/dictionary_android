package com.tonyp.dictionary.storage.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val accessToken: String = "",
    val refreshToken: String = "",
    val accessTokenExpirationDate: Instant = Instant.DISTANT_PAST,
    val refreshTokenExpirationDateTime: Instant = Instant.DISTANT_PAST,
    val username: String = "",
    val roles: List<UserRole> = emptyList()
)
