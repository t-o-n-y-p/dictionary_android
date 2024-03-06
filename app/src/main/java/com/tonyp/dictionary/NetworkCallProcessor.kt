package com.tonyp.dictionary

import android.content.SharedPreferences
import com.tonyp.dictionary.service.AuthService
import com.tonyp.dictionary.service.DictionaryService
import com.tonyp.dictionary.service.dto.auth.UserGroup
import com.tonyp.dictionary.storage.cleanup
import com.tonyp.dictionary.storage.get
import com.tonyp.dictionary.storage.mappers.UserPreferencesMapper
import com.tonyp.dictionary.storage.models.UserPreferences
import com.tonyp.dictionary.storage.put
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class NetworkCallProcessor @Inject constructor(
    @SecurePreferences private val securePreferences: SharedPreferences,
    private val dictionaryService: DictionaryService,
    private val authService: AuthService
) {

    suspend fun <T> dictionaryService(
        retries: Int = 1,
        block: suspend DictionaryService.() -> Response<T>
    ): Result<T> =
        runCatching {
            val response = block(dictionaryService)
            when {
                response.code() == 401 && retries > 0 -> {
                    val userInfo = securePreferences.get<UserPreferences>()
                    val tokenResponse = authService {
                        refresh(refreshToken = userInfo?.refreshToken.orEmpty())
                    }
                        .getOrNull()
                        ?: throwSecurityException()
                    val userInfoResponse = authService {
                        authService.getUserInfo()
                    }
                        .getOrNull()
                        ?.takeUnless { it.groups?.contains(UserGroup.BANNED) ?: true }
                        ?: throwSecurityException()
                    securePreferences.put(
                        UserPreferencesMapper.map(
                            tokenResponse,
                            userInfoResponse
                        )
                    )
                    dictionaryService(retries - 1) {
                        block()
                    }
                        .getOrNull()
                        ?: throw IOException()
                }
                else -> response.body() ?: throw IOException()
            }
        }

    suspend fun <T> authService(block: suspend AuthService.() -> Response<T>): Result<T> =
        runCatching {
            val response = block(authService)
            when {
                response.code() == 401 -> throwSecurityException()
                else -> response.body() ?: throw IOException()
            }
        }

    private fun throwSecurityException(): Nothing {
        securePreferences.cleanup<UserPreferences>()
        throw SecurityException()
    }

}