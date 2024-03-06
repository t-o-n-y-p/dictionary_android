package com.tonyp.dictionary.storage.models

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val accessToken: String = "",
    val refreshToken: String = "",
    val username: String = "",
    val roles: List<UserRole> = emptyList()
) {

    fun getAuthHeaderValue() = "Bearer $accessToken"

    fun isLoggedIn() = accessToken.isNotBlank() && roles.isNotEmpty()

}
