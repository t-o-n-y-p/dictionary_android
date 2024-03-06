package com.tonyp.dictionary.service.impl

import com.tonyp.dictionary.api.v1.models.MeaningCreateRequest
import com.tonyp.dictionary.api.v1.models.MeaningCreateResponse
import com.tonyp.dictionary.api.v1.models.MeaningDeleteRequest
import com.tonyp.dictionary.api.v1.models.MeaningDeleteResponse
import com.tonyp.dictionary.api.v1.models.MeaningReadRequest
import com.tonyp.dictionary.api.v1.models.MeaningReadResponse
import com.tonyp.dictionary.api.v1.models.MeaningResponseDeleteObject
import com.tonyp.dictionary.api.v1.models.MeaningResponseFullObject
import com.tonyp.dictionary.api.v1.models.MeaningSearchRequest
import com.tonyp.dictionary.api.v1.models.MeaningSearchResponse
import com.tonyp.dictionary.api.v1.models.MeaningUpdateRequest
import com.tonyp.dictionary.api.v1.models.MeaningUpdateResponse
import com.tonyp.dictionary.api.v1.models.ResponseResult
import com.tonyp.dictionary.service.DictionaryService
import retrofit2.Response

class DictionaryServiceStubImpl : DictionaryService {

    override suspend fun create(body: MeaningCreateRequest): Response<MeaningCreateResponse> =
        Response.success(
            MeaningCreateResponse(
                result = ResponseResult.SUCCESS,
                meaning = MeaningResponseFullObject(
                    id = "123",
                    word = "трава",
                    value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
                    proposedBy = "unittest",
                    approved = true,
                    version = "qwerty"
                )
            )
        )

    override suspend fun update(body: MeaningUpdateRequest): Response<MeaningUpdateResponse> =
        Response.success(
            MeaningUpdateResponse(
                result = ResponseResult.SUCCESS,
                meaning = MeaningResponseFullObject(
                    id = "123",
                    word = "трава",
                    value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
                    proposedBy = "unittest",
                    approved = true,
                    version = "qwerty"
                )
            )
        )

    override suspend fun delete(body: MeaningDeleteRequest): Response<MeaningDeleteResponse> =
        Response.success(
            MeaningDeleteResponse(
                result = ResponseResult.SUCCESS,
                meaning = MeaningResponseDeleteObject(id = "123")
            )
        )

    override suspend fun read(body: MeaningReadRequest): Response<MeaningReadResponse> =
        Response.success(
            MeaningReadResponse(
                result = ResponseResult.SUCCESS,
                meaning = MeaningResponseFullObject(
                    id = "123",
                    word = "трава",
                    value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
                    proposedBy = "unittest",
                    approved = true,
                    version = "qwerty"
                )
            )
        )

    override suspend fun search(body: MeaningSearchRequest): Response<MeaningSearchResponse> =
        Response.success(
            MeaningSearchResponse(
                result = ResponseResult.SUCCESS,
                meanings = listOf(
                    MeaningResponseFullObject(
                        id = "123",
                        word = "трава",
                        value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
                        proposedBy = "unittest",
                        approved = true,
                        version = "qwerty"
                    ),
                    MeaningResponseFullObject(
                        id = "456",
                        word = "трава",
                        value = "травянистые растения, обладающие лечебными свойствами, входящие в лекарственные сборы",
                        proposedBy = "unittest",
                        approved = true,
                        version = "zxcvbn"
                    ),
                    MeaningResponseFullObject(
                        id = "789",
                        word = "обвал",
                        value = "снежные глыбы или обломки скал, обрушившиеся с гор",
                        proposedBy = "t-o-n-y-p",
                        approved = false,
                        version = "asdfgh"
                    )
                )
            )
        )
}