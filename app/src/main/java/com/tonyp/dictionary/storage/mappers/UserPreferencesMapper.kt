package com.tonyp.dictionary.storage.mappers

import com.tonyp.dictionary.service.dto.auth.TokenResponse
import com.tonyp.dictionary.service.dto.auth.UserGroup
import com.tonyp.dictionary.service.dto.auth.UserInfoResponse
import com.tonyp.dictionary.storage.models.UserPreferences
import com.tonyp.dictionary.storage.models.UserRole
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.seconds

object UserPreferencesMapper {

    fun map(
        tokenResponse: TokenResponse,
        userInfoResponse: UserInfoResponse,
        startTime: Instant
    ): UserPreferences {
        return UserPreferences(
            accessToken = tokenResponse.accessToken.orEmpty(),
            refreshToken = tokenResponse.refreshToken.orEmpty(),
            accessTokenExpirationDate = startTime + (tokenResponse.expiresIn ?: 0).seconds,
            refreshTokenExpirationDateTime = startTime + (tokenResponse.refreshExpiresIn ?: 0).seconds,
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