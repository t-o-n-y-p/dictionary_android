package com.tonyp.dictionary.service.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    @SerialName("access_token")
    val accessToken: String?,
    @SerialName("expires_in")
    val expiresIn: Int?,
    @SerialName("refresh_token")
    val refreshToken: String?,
    @SerialName("refresh_expires_in")
    val refreshExpiresIn: Int?
) {
    fun authHeaderValue(): String = "Bearer $accessToken"
}
