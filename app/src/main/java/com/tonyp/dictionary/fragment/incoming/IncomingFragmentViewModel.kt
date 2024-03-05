package com.tonyp.dictionary.fragment.incoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tonyp.dictionary.WizardCache
import com.tonyp.dictionary.api.v1.models.MeaningResponseFullObject
import com.tonyp.dictionary.api.v1.models.ResponseResult
import com.tonyp.dictionary.recyclerview.definition.WordsWithDefinitionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class IncomingFragmentViewModel @Inject constructor(
    private val useCase: IncomingFragmentUseCase,
    private val cache: WizardCache
) : ViewModel() {

    private val mSearchResultState = MutableLiveData<SearchResultState>()
    val searchResultState: LiveData<SearchResultState> get() = mSearchResultState

    private val mContentState = MutableLiveData<List<MeaningResponseFullObject>>()
    val contentState: LiveData<List<MeaningResponseFullObject>> get() = mContentState

    private fun loadSearchResultsAndSaveToCache() {
        cache.incomingSearchResults = emptyList()
        viewModelScope.launch {
            mSearchResultState.value = SearchResultState.Loading
            try {
                withContext(Dispatchers.IO) { useCase.search() }
                    .getOrNull()
                    ?.takeIf { it.result == ResponseResult.SUCCESS }
                    ?.let {
                        val meaningObjects = it.meanings.orEmpty()
                        cache.incomingSearchResults = meaningObjects
                        mContentState.value = meaningObjects
                        mSearchResultState.value = SearchResultState.Content
                    }
                    ?: let {
                        mSearchResultState.value = SearchResultState.Error
                    }
            } catch (t: Throwable) {
                mSearchResultState.value = SearchResultState.Error
            }
        }
    }

    fun fillDataFromCache() {
        cache.incomingSearchResults
            .takeIf { it.isEmpty() }
            ?.let { loadSearchResultsAndSaveToCache() }
            ?: let {
                mContentState.value = cache.incomingSearchResults
                mSearchResultState.value = SearchResultState.Content
            }
    }

    fun saveSearchItem(item: WordsWithDefinitionItem) {
        cache.currentlySelectedWord = item.word
        cache.currentlySelectedSearchResults =
            cache.incomingSearchResults.filter { it.id == item.id }
    }

    sealed class SearchResultState {

        data object Loading: SearchResultState()

        data object Content: SearchResultState()

        data object Error: SearchResultState()
    }

}