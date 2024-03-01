package com.tonyp.dictionary.service

import com.tonyp.dictionary.service.dto.auth.TokenResponse
import com.tonyp.dictionary.service.dto.auth.UserInfoResponse
import retrofit2.Response

interface AuthService {

    suspend fun login(username: String, password: String): Response<TokenResponse>

    suspend fun refresh(refreshToken: String): Response<TokenResponse>

    suspend fun getUserInfo(authHeaderValue: String): Response<UserInfoResponse>

}