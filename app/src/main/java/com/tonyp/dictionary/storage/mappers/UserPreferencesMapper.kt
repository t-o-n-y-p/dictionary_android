package com.tonyp.dictionary.storage.mappers

import com.tonyp.dictionary.service.dto.auth.TokenResponse
import com.tonyp.dictionary.service.dto.auth.UserGroup
import com.tonyp.dictionary.service.dto.auth.UserInfoResponse
import com.tonyp.dictionary.storage.models.UserPreferences
import com.tonyp.dictionary.storage.models.UserRole

object UserPreferencesMapper {

    fun map(
        tokenResponse: TokenResponse
    ): UserPreferences {
        return UserPreferences(
            accessToken = tokenResponse.accessToken.orEmpty(),
            refreshToken = tokenResponse.refreshToken.orEmpty()
        )
    }

    fun map(
        tokenResponse: TokenResponse,
        userInfoResponse: UserInfoResponse
    ): UserPreferences {
        return UserPreferences(
            accessToken = tokenResponse.accessToken.orEmpty(),
            refreshToken = tokenResponse.refreshToken.orEmpty(),
            username = userInfoResponse.preferredUsername.orEmpty(),
            roles = map(userInfoResponse.groups.orEmpty())
        )
    }

    private fun map(groups: List<UserGroup>): List<UserRole> =
        groups.map {
            when (it) {
                UserGroup.USER -> UserRole.USER
                UserGroup.ADMIN -> UserRole.ADMIN
                UserGroup.BANNED -> UserRole.BANNED
                UserGroup.TEST -> UserRole.TEST
            }
        }

}