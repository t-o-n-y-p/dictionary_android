package com.tonyp.dictionary.service.impl

import com.tonyp.dictionary.api.v1.models.MeaningCreateRequest
import com.tonyp.dictionary.api.v1.models.MeaningCreateResponse
import com.tonyp.dictionary.api.v1.models.MeaningResponseFullObject
import com.tonyp.dictionary.api.v1.models.MeaningSearchRequest
import com.tonyp.dictionary.api.v1.models.MeaningSearchResponse
import com.tonyp.dictionary.api.v1.models.ResponseResult
import com.tonyp.dictionary.service.DictionaryService
import retrofit2.Response

class DictionaryServiceStubImpl : DictionaryService {

    override suspend fun create(
        body: MeaningCreateRequest,
        authHeaderValue: String
    ): Response<MeaningCreateResponse> =
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