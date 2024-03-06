package com.tonyp.dictionary.fragment.search

import com.benasher44.uuid.uuid4
import com.tonyp.dictionary.NetworkCallProcessor
import com.tonyp.dictionary.api.v1.models.MeaningSearchFilter
import com.tonyp.dictionary.api.v1.models.MeaningSearchFilterMode
import com.tonyp.dictionary.api.v1.models.MeaningSearchRequest
import com.tonyp.dictionary.api.v1.models.MeaningSearchResponse
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SearchFragmentUseCase @Inject constructor(
    private val networkCallProcessor: NetworkCallProcessor
) {

    suspend fun search(input: String): Result<MeaningSearchResponse> {
        val request = MeaningSearchRequest(
            requestId = uuid4().toString(),
            meaningFilter = MeaningSearchFilter(
                word = input,
                mode = MeaningSearchFilterMode.STARTS_WITH,
                approved = true
            )
        )
        return networkCallProcessor.dictionaryService { search(request) }
    }

}