package com.tonyp.dictionary

import com.tonyp.dictionary.api.v1.models.MeaningSearchFilterMode
import com.tonyp.dictionary.api.v1.models.MeaningSearchRequest
import com.tonyp.dictionary.api.v1.models.MeaningSearchResponse
import com.tonyp.dictionary.service.DictionaryService
import io.mockk.MockKMatcherScope
import retrofit2.Response

fun MockKMatcherScope.getSearchRequestMatcher(input: String): MeaningSearchRequest =
    and(
        match { it.requestId.isNullOrBlank().not() },
        match { it.meaningFilter?.word == input },
        match { it.meaningFilter?.mode == MeaningSearchFilterMode.STARTS_WITH },
        match { it.meaningFilter?.approved ?: false }
    )

inline fun <reified T : Any> MockKMatcherScope.and(v1: T, v2: T, v3: T, v4: T): T =
    and(
        and(v1, v2),
        and(v3, v4)
    )

typealias DictionarySearchCall = suspend DictionaryService.() -> Response<MeaningSearchResponse>