package com.tonyp.dictionary.service.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    val groups: List<UserGroup>
)
