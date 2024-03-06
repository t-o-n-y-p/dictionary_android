package com.tonyp.dictionary.service.impl

import android.content.SharedPreferences
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.tonyp.dictionary.service.AuthService
import com.tonyp.dictionary.service.dto.auth.TokenResponse
import com.tonyp.dictionary.service.dto.auth.UserInfoResponse
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface AuthServiceImpl : AuthService {

    @FormUrlEncoded
    @POST("token")
    override suspend fun login(
        @Field("client_id") clientId: String,
        @Field("grant_type") grantType: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<TokenResponse>

    @FormUrlEncoded
    @POST("token")
    override suspend fun refresh(
        @Field("client_id") clientId: String,
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String
    ): Response<TokenResponse>

    @POST("userinfo")
    override suspend fun getUserInfo(): Response<UserInfoResponse>

    companion object {

        fun create(securePreferences: SharedPreferences): AuthService {
            val okHttp = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(AuthInterceptor(securePreferences))
                .build()

            val json = Json {
                coerceInputValues = true
                ignoreUnknownKeys = true
            }

            val retrofit = Retrofit.Builder()
                .client(okHttp)
                .baseUrl("http://192.168.56.1:8081/auth/realms/dictionary-meanings/protocol/openid-connect/")
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()

            return retrofit.create(AuthServiceImpl::class.java)
        }
    }

}