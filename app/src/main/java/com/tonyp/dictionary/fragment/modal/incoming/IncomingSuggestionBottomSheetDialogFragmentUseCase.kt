package com.tonyp.dictionary.fragment.modal.incoming

import com.benasher44.uuid.uuid4
import com.tonyp.dictionary.api.v1.models.MeaningDeleteObject
import com.tonyp.dictionary.api.v1.models.MeaningDeleteRequest
import com.tonyp.dictionary.api.v1.models.MeaningDeleteResponse
import com.tonyp.dictionary.api.v1.models.MeaningResponseFullObject
import com.tonyp.dictionary.api.v1.models.MeaningUpdateObject
import com.tonyp.dictionary.api.v1.models.MeaningUpdateRequest
import com.tonyp.dictionary.api.v1.models.MeaningUpdateResponse
import com.tonyp.dictionary.networkCall
import com.tonyp.dictionary.service.DictionaryService
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class IncomingSuggestionBottomSheetDialogFragmentUseCase @Inject constructor(
    private val dictionaryService: DictionaryService
) {

    suspend fun update(
        id: String,
        version: String,
        authHeaderValue: String
    ): Result<MeaningUpdateResponse> {
        val request = MeaningUpdateRequest(
            requestId = uuid4().toString(),
            meaning = MeaningUpdateObject(
                id = id,
                approved = true,
                version = version
            )
        )
        return networkCall { dictionaryService.update(request, authHeaderValue) }
    }

    suspend fun delete(
        id: String,
        version: String,
        authHeaderValue: String
    ): Result<MeaningDeleteResponse> {
        val request = MeaningDeleteRequest(
            requestId = uuid4().toString(),
            meaning = MeaningDeleteObject(
                id = id,
                version = version
            )
        )
        return networkCall { dictionaryService.delete(request, authHeaderValue) }
    }

}