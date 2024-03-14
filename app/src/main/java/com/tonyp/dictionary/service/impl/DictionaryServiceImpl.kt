package com.tonyp.dictionary.service.impl

import android.content.SharedPreferences
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.tonyp.dictionary.api.v1.models.MeaningCreateRequest
import com.tonyp.dictionary.api.v1.models.MeaningCreateResponse
import com.tonyp.dictionary.api.v1.models.MeaningDeleteRequest
import com.tonyp.dictionary.api.v1.models.MeaningDeleteResponse
import com.tonyp.dictionary.api.v1.models.MeaningReadRequest
import com.tonyp.dictionary.api.v1.models.MeaningReadResponse
import com.tonyp.dictionary.api.v1.models.MeaningSearchRequest
import com.tonyp.dictionary.api.v1.models.MeaningSearchResponse
import com.tonyp.dictionary.api.v1.models.MeaningUpdateRequest
import com.tonyp.dictionary.api.v1.models.MeaningUpdateResponse
import com.tonyp.dictionary.service.DictionaryService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface DictionaryServiceImpl : DictionaryService {

    @POST("create")
    override suspend fun create(@Body body: MeaningCreateRequest): Response<MeaningCreateResponse>

    @POST("update")
    override suspend fun update(@Body body: MeaningUpdateRequest): Response<MeaningUpdateResponse>

    @POST("delete")
    override suspend fun delete(@Body body: MeaningDeleteRequest): Response<MeaningDeleteResponse>

    @POST("read")
    override suspend fun read(@Body body: MeaningReadRequest): Response<MeaningReadResponse>

    @POST("search")
    override suspend fun search(@Body body: MeaningSearchRequest): Response<MeaningSearchResponse>

    companion object {

        fun create(securePreferences: SharedPreferences): DictionaryService {
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
                .baseUrl("http://192.168.0.103:8080/api/v1/meaning/")
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()

            return retrofit.create(DictionaryServiceImpl::class.java)
        }
    }

}