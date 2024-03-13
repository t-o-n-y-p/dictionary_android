package com.tonyp.dictionary

import com.tonyp.dictionary.api.v1.models.MeaningResponseFullObject
import com.tonyp.dictionary.api.v1.models.MeaningSearchResponse
import com.tonyp.dictionary.api.v1.models.ResponseResult
import com.tonyp.dictionary.fragment.search.SearchFragmentUseCase
import com.tonyp.dictionary.service.DictionaryService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class SearchFragmentUseCaseTest {

    private val response = MeaningSearchResponse(
        result = ResponseResult.SUCCESS,
        meanings = listOf(
            MeaningResponseFullObject(
                id = "123",
                word = "трава",
                value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
                proposedBy = "unittest",
                approved = true,
                version = "qwerty"
            )
        )
    )

    private val dictionaryService: DictionaryService = mockk()
    private val testDispatcher = StandardTestDispatcher()
    private val callProcessor: NetworkCallProcessor = mockk()

    private val useCase = SearchFragmentUseCase(callProcessor)

    @Before
    fun setUp() = Dispatchers.setMain(testDispatcher)

    @Test
    fun testUseCaseSuccess() =
        runTest {
            val word = "трава"
            coEvery {
                dictionaryService.search(getSearchRequestMatcher(word))
            } returns Response.success(response)
            coEvery {
                callProcessor.dictionaryService(any(), captureCoroutine<DictionarySearchCall>())
            } coAnswers {
                Result.success(secondArg<DictionarySearchCall>()(dictionaryService).body()!!)
            }
            assertEquals(Result.success(response), useCase.search(word))
            coVerify(exactly = 1) { dictionaryService.search(getSearchRequestMatcher(word)) }
        }

    @Test
    fun testUseCaseFailure() =
        runTest {
            val word = "обвал"
            val exception = IOException()
            coEvery {
                dictionaryService.search(getSearchRequestMatcher(word))
            } returns Response.error(400, "error".toResponseBody())
            coEvery {
                callProcessor.dictionaryService(any(), captureCoroutine<DictionarySearchCall>())
            } coAnswers {
                secondArg<DictionarySearchCall>()(dictionaryService)
                Result.failure(exception)
            }
            useCase.search(word).apply {
                assertTrue(isFailure)
                assertEquals(exception, exceptionOrNull())
            }
            coVerify(exactly = 1) { dictionaryService.search(getSearchRequestMatcher(word)) }
        }
}