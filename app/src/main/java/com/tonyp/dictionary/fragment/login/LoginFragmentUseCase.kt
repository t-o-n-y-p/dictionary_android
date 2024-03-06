package com.tonyp.dictionary.fragment.login

import com.tonyp.dictionary.NetworkCallProcessor
import com.tonyp.dictionary.service.dto.auth.TokenResponse
import com.tonyp.dictionary.service.dto.auth.UserInfoResponse
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class LoginFragmentUseCase @Inject constructor(
    private val networkCallProcessor: NetworkCallProcessor
) {

    suspend fun login(username: String, password: String): Result<TokenResponse> =
        networkCallProcessor.authService {
            login(username = username, password = password)
        }

    suspend fun getUserInfo(): Result<UserInfoResponse> =
        networkCallProcessor.authService { getUserInfo() }
}