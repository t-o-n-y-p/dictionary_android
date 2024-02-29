package com.tonyp.dictionary.service

import com.tonyp.dictionary.api.v1.models.MeaningSearchRequest
import com.tonyp.dictionary.api.v1.models.MeaningSearchResponse
import retrofit2.Response

interface DictionaryService {

    suspend fun search(body: MeaningSearchRequest): Response<MeaningSearchResponse>

}