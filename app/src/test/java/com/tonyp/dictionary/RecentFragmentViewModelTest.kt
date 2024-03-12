package com.tonyp.dictionary

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.tonyp.dictionary.fragment.recent.RecentFragmentViewModel
import com.tonyp.dictionary.recyclerview.word.WordsAdapter
import com.tonyp.dictionary.recyclerview.word.WordsItem
import com.tonyp.dictionary.storage.models.DictionaryPreferences
import io.mockk.Runs
import io.mockk.called
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkConstructor
import io.mockk.verify
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

class RecentFragmentViewModelTest {

    private val commonPreferences: SharedPreferences = mockk()
    private val cache: WizardCache = mockk {
        every { currentlySelectedItem = any() } just Runs
    }
    private val adapter: WordsAdapter = mockk {
        every { submitList(any()) } just Runs
    }
    private val viewModel: RecentFragmentViewModel
    by lazy { RecentFragmentViewModel(commonPreferences, cache) }

    @Test
    fun testFillDataFromEmptyCommonPreferences() {
        every {
            commonPreferences.getString(DictionaryPreferences::class.simpleName, any())
        } returns null
        viewModel.fillDataFromPreferences(adapter)
        verify(exactly = 0) { adapter.submitList(any()) }
        verify {
            anyConstructed<MutableLiveData<RecentFragmentViewModel.RecentResultState>>().value =
                RecentFragmentViewModel.RecentResultState.NoResults
        }
    }

    @Test
    fun testFillDataFromEmptyListCommonPreferences() {
        every {
            commonPreferences.getString(DictionaryPreferences::class.simpleName, any())
        } returns "{\"recentWords\":[]}"
        viewModel.fillDataFromPreferences(adapter)
        verify(exactly = 0) { adapter.submitList(any()) }
        verify {
            anyConstructed<MutableLiveData<RecentFragmentViewModel.RecentResultState>>().value =
                RecentFragmentViewModel.RecentResultState.NoResults
        }
    }

    @Test
    fun testFillDataFromCommonPreferencesWithContent() {
        every {
            commonPreferences.getString(DictionaryPreferences::class.simpleName, any())
        } returns "{\"recentWords\":[\"трава\",\"обвал\"]}"
        viewModel.fillDataFromPreferences(adapter)
        verify(exactly = 1) {
            adapter.submitList(
                listOf(
                    WordsItem(value = "трава"),
                    WordsItem(value = "обвал")
                )
            )
        }
        verify {
            anyConstructed<MutableLiveData<RecentFragmentViewModel.RecentResultState>>().value =
                RecentFragmentViewModel.RecentResultState.Content
        }
    }

    @Test
    fun testSaveRecentItem() {
        val item = WordsItem(value = "трава")
        viewModel.saveRecentItem(item)
        verify { cache.currentlySelectedItem = WordsItem(value = "трава") }
    }

    companion object {

        @JvmStatic
        @BeforeClass
        fun setUp() {
            mockkConstructor(MutableLiveData::class)
            every {
                anyConstructed<MutableLiveData<RecentFragmentViewModel.RecentResultState>>().value = any()
            } just Runs
        }

        @JvmStatic
        @AfterClass
        fun tearDown() = unmockkConstructor(MutableLiveData::class)
    }
}