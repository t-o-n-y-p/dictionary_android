package com.tonyp.dictionary.service.impl

import android.content.SharedPreferences
import com.tonyp.dictionary.service.AuthService
import com.tonyp.dictionary.service.dto.auth.TokenResponse
import com.tonyp.dictionary.service.dto.auth.UserGroup
import com.tonyp.dictionary.service.dto.auth.UserInfoResponse
import com.tonyp.dictionary.storage.get
import com.tonyp.dictionary.storage.models.UserPreferences
import retrofit2.Response

class AuthServiceStubImpl(private val securePreferences: SharedPreferences) : AuthService {

    override suspend fun login(
        clientId: String,
        grantType: String,
        username: String,
        password: String
    ): Response<TokenResponse> =
        Response.success(
            TokenResponse(
                accessToken = username + password,
                refreshToken = username + password
            )
        )

    override suspend fun refresh(
        clientId: String,
        grantType: String,
        refreshToken: String
    ): Response<TokenResponse> =
        Response.success(
            TokenResponse(
                accessToken = refreshToken,
                refreshToken = refreshToken
            )
        )

    override suspend fun getUserInfo(): Response<UserInfoResponse> {
        val userPreferences = securePreferences.get<UserPreferences>() ?: UserPreferences()
        return when {
            userPreferences.accessToken.contains("admin") -> Response.success(
                UserInfoResponse(
                    preferredUsername = "admin",
                    groups = listOf(UserGroup.USER, UserGroup.ADMIN)
                )
            )
            userPreferences.accessToken.contains("banned") -> Response.success(
                UserInfoResponse(
                    preferredUsername = "banned",
                    groups = listOf(UserGroup.USER, UserGroup.ADMIN, UserGroup.BANNED)
                )
            )
            else -> Response.success(
                UserInfoResponse(
                    preferredUsername = "unittest",
                    groups = listOf(UserGroup.USER)
                )
            )
        }
    }

}