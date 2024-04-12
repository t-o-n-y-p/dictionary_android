package com.tonyp.dictionary.service.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    @SerialName("preferred_username")
    val preferredUsername: String? = null,
    val groups: List<UserGroup>? = null
)
