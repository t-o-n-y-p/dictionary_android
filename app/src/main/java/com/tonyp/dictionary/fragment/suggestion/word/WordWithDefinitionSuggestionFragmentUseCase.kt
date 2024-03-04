package com.tonyp.dictionary.fragment.suggestion.word

import com.benasher44.uuid.uuid4
import com.tonyp.dictionary.api.v1.models.MeaningCreateObject
import com.tonyp.dictionary.api.v1.models.MeaningCreateRequest
import com.tonyp.dictionary.api.v1.models.MeaningCreateResponse
import com.tonyp.dictionary.networkCall
import com.tonyp.dictionary.service.DictionaryService
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class WordWithDefinitionSuggestionFragmentUseCase @Inject constructor(
    private val dictionaryService: DictionaryService
) {

    suspend fun create(
        word: String,
        definition: String,
        proposedBy: String,
        authHeaderValue: String
    ): Result<MeaningCreateResponse> {
        val request = MeaningCreateRequest(
            requestId = uuid4().toString(),
            meaning = MeaningCreateObject(
                word = word,
                value = definition,
                proposedBy = proposedBy
            )
        )
        return networkCall { dictionaryService.create(request, authHeaderValue) }
    }

}