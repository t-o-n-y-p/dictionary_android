package com.tonyp.dictionary.fragment.recent

import android.content.SharedPreferences
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

    fun fillDataFromPreferences(adapter: WordsAdapter) {
        adapter.submitList(
            commonPreferences.get<DictionaryPreferences>()?.recentWords?.map { WordsItem(it) }
                ?: emptyList()
        )
    }

    fun saveRecentItem(value: String) {
        cache.currentlySelectedWord = value
    }

}