package com.tonyp.dictionary.fragment.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tonyp.dictionary.WizardCache
import com.tonyp.dictionary.api.v1.models.MeaningResponseFullObject
import com.tonyp.dictionary.api.v1.models.ResponseResult
import com.tonyp.dictionary.databinding.FragmentSearchBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchFragmentViewModel @Inject constructor(
    private val useCase: SearchFragmentUseCase,
    private val cache: WizardCache
) : ViewModel() {

    private val mSearchResultState = MutableLiveData<SearchResultState>()
    val searchResultState: LiveData<SearchResultState> get() = mSearchResultState

    private val mContentState = MutableLiveData<List<MeaningResponseFullObject>>()
    val contentState: LiveData<List<MeaningResponseFullObject>> get() = mContentState

    private var loadingWordsTask: Job = Job()

    fun loadSearchResultsAndSaveToCache(input: String) {
        cache.searchResults = emptyList()
        cache.searchInput = input
        loadingWordsTask.cancel()
        input.takeIf { it.isBlank() }
            ?.let { mSearchResultState.value = SearchResultState.NotSet }
            ?: let {
                loadingWordsTask = viewModelScope.launch {
                    mSearchResultState.value = SearchResultState.Loading
                    try {
                        withContext(Dispatchers.IO) { useCase.search(input) }
                            .takeIf { it.isSuccess && it.getOrNull()?.result == ResponseResult.SUCCESS }
                            ?.let {
                                val meaningObjects = it.getOrNull()?.meanings ?: emptyList()
                                cache.searchResults = meaningObjects
                                mContentState.value = meaningObjects
                                mSearchResultState.value = SearchResultState.Content
                            } ?: let {
                            mSearchResultState.value = SearchResultState.Error
                        }
                    } catch (t: Throwable) {
                        mSearchResultState.value = SearchResultState.Error
                    }
                }
            }
    }

    fun fillDataFromCache(binding: FragmentSearchBinding) {
        binding.searchView.setText(cache.searchInput)
        cache.searchResults
            .takeIf { it.isEmpty() }
            ?.let { loadSearchResultsAndSaveToCache(cache.searchInput) }
            ?: let {
                mContentState.value = cache.searchResults
                mSearchResultState.value = SearchResultState.Content
            }
    }

    sealed class SearchResultState {

        data object NotSet: SearchResultState()

        data object Loading: SearchResultState()

        data object Content: SearchResultState()

        data object Error: SearchResultState()
    }

}