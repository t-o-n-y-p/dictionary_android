package com.tonyp.dictionary.fragment.recent

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tonyp.dictionary.CommonPreferences
import com.tonyp.dictionary.WizardCache
import com.tonyp.dictionary.recyclerview.word.WordsAdapter
import com.tonyp.dictionary.recyclerview.word.WordsItem
import com.tonyp.dictionary.storage.get
import com.tonyp.dictionary.storage.models.DictionaryPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecentFragmentViewModel @Inject constructor(
    @CommonPreferences private val commonPreferences: SharedPreferences,
    private val cache: WizardCache
) : ViewModel() {

    private val mRecentResultState = MutableLiveData<RecentResultState>(RecentResultState.NotSet)

    val recentResultState: LiveData<RecentResultState> get() = mRecentResultState

    fun fillDataFromPreferences(adapter: WordsAdapter) =
        commonPreferences.get<DictionaryPreferences>()
            ?.recentWords
            ?.takeIf { it.isNotEmpty() }
            ?.map { WordsItem(value = it) }
            ?.let {
                adapter.submitList(it.asReversed())
                mRecentResultState.value = RecentResultState.Content
            }
            ?: let {
                mRecentResultState.value = RecentResultState.NoResults
            }

    fun saveRecentItem(item: WordsItem) {
        cache.currentlySelectedItem = item
    }

    sealed class RecentResultState {

        data object NotSet : RecentResultState()

        data object Content : RecentResultState()

        data object NoResults : RecentResultState()

    }

}