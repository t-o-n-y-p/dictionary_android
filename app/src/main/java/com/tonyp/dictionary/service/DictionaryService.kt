package com.tonyp.dictionary.service

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
import retrofit2.Response

interface DictionaryService {

    suspend fun create(body: MeaningCreateRequest): Response<MeaningCreateResponse>

    suspend fun update(body: MeaningUpdateRequest): Response<MeaningUpdateResponse>

    suspend fun delete(body: MeaningDeleteRequest): Response<MeaningDeleteResponse>

    suspend fun read(body: MeaningReadRequest): Response<MeaningReadResponse>

    suspend fun search(body: MeaningSearchRequest): Response<MeaningSearchResponse>

}