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
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

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

    private val dictionaryServiceMock: DictionaryService = mockk()
    private val testDispatcher = StandardTestDispatcher()

    private val useCase = SearchFragmentUseCase(
        NetworkCallProcessor(
            securePreferences = mockk(),
            dictionaryService = dictionaryServiceMock,
            authService = mockk()
        )
    )

    @Before
    fun setUp() = Dispatchers.setMain(testDispatcher)

    @Test
    fun testUseCase() =
        runTest {
            val word = "трава"
            coEvery {
                dictionaryServiceMock.search(getSearchRequestMatcher(word))
            } returns Response.success(response)
            val searchResult = useCase.search(word)
            coVerify(exactly = 1) { dictionaryServiceMock.search(getSearchRequestMatcher(word)) }
            assertEquals(Result.success(response), searchResult)
        }
}