package com.tonyp.dictionary.fragment.search

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.idling.CountingIdlingResource
import com.tonyp.dictionary.CommonPreferences
import com.tonyp.dictionary.OptionalIdlingResource
import com.tonyp.dictionary.SecurePreferences
import com.tonyp.dictionary.WizardCache
import com.tonyp.dictionary.api.v1.models.ResponseResult
import com.tonyp.dictionary.databinding.FragmentSearchBinding
import com.tonyp.dictionary.fragment.modal.login.LoginWithSuggestionBottomSheetDialogFragment
import com.tonyp.dictionary.fragment.modal.suggestion.WordSuggestionBottomSheetDialogFragment
import com.tonyp.dictionary.recyclerview.word.WordsAdapter
import com.tonyp.dictionary.recyclerview.word.WordsItem
import com.tonyp.dictionary.recyclerview.word.WordsItemMapper
import com.tonyp.dictionary.storage.get
import com.tonyp.dictionary.storage.models.DictionaryPreferences
import com.tonyp.dictionary.storage.models.UserPreferences
import com.tonyp.dictionary.storage.put
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Optional
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class SearchFragmentViewModel @Inject constructor(
    @SecurePreferences private val securePreferences: SharedPreferences,
    @CommonPreferences private val commonPreferences: SharedPreferences,
    private val useCase: SearchFragmentUseCase,
    private val cache: WizardCache,
    @OptionalIdlingResource private val idlingResource: Optional<CountingIdlingResource>
) : ViewModel() {

    private val mSearchResultState = MutableLiveData<SearchResultState>()
    val searchResultState: LiveData<SearchResultState> get() = mSearchResultState

    private val mContentState = MutableLiveData<List<WordsItem>>()
    val contentState: LiveData<List<WordsItem>> get() = mContentState

    private var loadingWordsTask: Job = Job()

    fun loadSearchResultsAndSaveToCache(input: String) {
        cache.items = emptyList()
        cache.searchInput = input
        loadingWordsTask.cancel()
        input.takeIf { it.isBlank() }
            ?.let { mSearchResultState.value = SearchResultState.NotSet }
            ?: let {
                loadingWordsTask = viewModelScope.launch {
                    idlingResource.ifPresent { it.increment() }
                    try {
                        mSearchResultState.value = SearchResultState.Loading
                        withContext(Dispatchers.IO) { useCase.search(input) }
                            .getOrNull()
                            ?.takeIf { it.result == ResponseResult.SUCCESS }
                            ?.let { response ->
                                val meaningObjects = WordsItemMapper.map(response.meanings.orEmpty())
                                cache.items = meaningObjects
                                mContentState.value = meaningObjects
                                mSearchResultState.value =
                                    meaningObjects
                                        .takeIf { it.isEmpty() }
                                        ?.let { SearchResultState.NoResults }
                                        ?: SearchResultState.Content
                            }
                            ?: let {
                                mSearchResultState.value = SearchResultState.Error
                            }
                    } catch (t: Throwable) {
                        mSearchResultState.value = SearchResultState.Error
                    } finally {
                        idlingResource.ifPresent { it.decrement() }
                    }
                }
            }
    }

    fun fillDataFromCache(binding: FragmentSearchBinding) {
        binding.searchView.setText(cache.searchInput)
        cache.items
            .takeIf { it.isEmpty() }
            ?.let { loadSearchResultsAndSaveToCache(cache.searchInput) }
            ?: let {
                mContentState.value = cache.items
                mSearchResultState.value = SearchResultState.Content
            }
    }

    fun saveSearchItem(item: WordsItem) {
        cache.currentlySelectedItem = item
        commonPreferences.put(
            commonPreferences.get<DictionaryPreferences>()?.copyAndAdd(item.value)
                ?: DictionaryPreferences(listOf(item.value))
        )
    }

    fun clearCachedSelectedItem() {
        cache.currentlySelectedItem = WordsItem()
    }

    fun getBottomSheetFragmentToOpen() =
        securePreferences.get<UserPreferences>()
            ?.takeIf { it.isLoggedIn() }
            ?.let { WordSuggestionBottomSheetDialogFragment() }
            ?: LoginWithSuggestionBottomSheetDialogFragment()

    fun getOnScrollListener(adapter: WordsAdapter, pageSize: Int) =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                (recyclerView.layoutManager as? LinearLayoutManager)?.apply {
                    findLastVisibleItemPosition()
                        .takeIf { it == itemCount - 1 && itemCount < cache.items.size }
                        ?.let {
                            adapter.submitList(
                                cache.items.slice(
                                    0
                                            until
                                            min(cache.items.size, itemCount + pageSize)))
                        }
                }
            }
        }

    sealed class SearchResultState {

        data object NotSet: SearchResultState()

        data object Loading: SearchResultState()

        data object Content: SearchResultState()

        data object NoResults: SearchResultState()

        data object Error: SearchResultState()
    }

}