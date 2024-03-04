package com.tonyp.dictionary.service.impl

import com.tonyp.dictionary.service.AuthService
import com.tonyp.dictionary.service.dto.auth.TokenResponse
import com.tonyp.dictionary.service.dto.auth.UserGroup
import com.tonyp.dictionary.service.dto.auth.UserInfoResponse
import retrofit2.Response

class AuthServiceStubImpl : AuthService {

    override suspend fun login(username: String, password: String): Response<TokenResponse> =
        Response.success(
            TokenResponse(
                accessToken = username + password,
                refreshToken = username + password,
                expiresIn = Int.MAX_VALUE,
                refreshExpiresIn = Int.MAX_VALUE
            )
        )

    override suspend fun refresh(refreshToken: String): Response<TokenResponse> =
        Response.success(
            TokenResponse(
                accessToken = refreshToken,
                refreshToken = refreshToken,
                expiresIn = Int.MAX_VALUE,
                refreshExpiresIn = Int.MAX_VALUE
            )
        )

    override suspend fun getUserInfo(authHeaderValue: String): Response<UserInfoResponse> =
        when {
            authHeaderValue.contains("admin") -> Response.success(
                UserInfoResponse(
                    preferredUsername = "admin",
                    groups = listOf(UserGroup.USER, UserGroup.ADMIN)
                )
            )
            authHeaderValue.contains("banned") -> Response.success(
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