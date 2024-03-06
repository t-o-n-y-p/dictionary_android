package com.tonyp.dictionary.service

import com.tonyp.dictionary.service.dto.auth.TokenResponse
import com.tonyp.dictionary.service.dto.auth.UserInfoResponse
import retrofit2.Response

interface AuthService {

    suspend fun login(
        clientId: String = "dictionary-meanings-service",
        grantType: String = "password",
        username: String,
        password: String
    ): Response<TokenResponse>

    suspend fun refresh(
        clientId: String = "dictionary-meanings-service",
        grantType: String = "refresh_token",
        refreshToken: String
    ): Response<TokenResponse>

    suspend fun getUserInfo(): Response<UserInfoResponse>

}