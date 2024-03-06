package com.tonyp.dictionary.fragment.modal.incoming

import com.benasher44.uuid.uuid4
import com.tonyp.dictionary.NetworkCallProcessor
import com.tonyp.dictionary.api.v1.models.MeaningDeleteObject
import com.tonyp.dictionary.api.v1.models.MeaningDeleteRequest
import com.tonyp.dictionary.api.v1.models.MeaningDeleteResponse
import com.tonyp.dictionary.api.v1.models.MeaningUpdateObject
import com.tonyp.dictionary.api.v1.models.MeaningUpdateRequest
import com.tonyp.dictionary.api.v1.models.MeaningUpdateResponse
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class IncomingSuggestionBottomSheetDialogFragmentUseCase @Inject constructor(
    private val networkCallProcessor: NetworkCallProcessor
) {

    suspend fun update(id: String, version: String): Result<MeaningUpdateResponse> {
        val request = MeaningUpdateRequest(
            requestId = uuid4().toString(),
            meaning = MeaningUpdateObject(
                id = id,
                approved = true,
                version = version
            )
        )
        return networkCallProcessor.dictionaryService { update(request) }
    }

    suspend fun delete(id: String, version: String): Result<MeaningDeleteResponse> {
        val request = MeaningDeleteRequest(
            requestId = uuid4().toString(),
            meaning = MeaningDeleteObject(
                id = id,
                version = version
            )
        )
        return networkCallProcessor.dictionaryService { delete(request) }
    }

}