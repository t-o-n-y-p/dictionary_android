package com.tonyp.dictionary.fragment.incoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.idling.CountingIdlingResource
import com.tonyp.dictionary.OptionalIdlingResource
import com.tonyp.dictionary.WizardCache
import com.tonyp.dictionary.api.v1.models.ResponseResult
import com.tonyp.dictionary.databinding.FragmentIncomingBinding
import com.tonyp.dictionary.recyclerview.definition.WordsWithDefinitionAdapter
import com.tonyp.dictionary.recyclerview.definition.WordsWithDefinitionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Optional
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class IncomingFragmentViewModel @Inject constructor(
    private val useCase: IncomingFragmentUseCase,
    private val cache: WizardCache,
    @OptionalIdlingResource private val idlingResource: Optional<CountingIdlingResource>
) : ViewModel() {

    private val mSearchResultState = MutableLiveData<SearchResultState>()
    val searchResultState: LiveData<SearchResultState> get() = mSearchResultState

    private val mContentState = MutableLiveData<List<WordsWithDefinitionItem>>()
    val contentState: LiveData<List<WordsWithDefinitionItem>> get() = mContentState

    private var refreshDataTask: Job = Job()

    private fun loadSearchResultsAndSaveToCache() {
        cache.incomingItems.clear()
        mSearchResultState.value = SearchResultState.Loading
        refreshData()
    }

    fun fillDataFromCache() =
        cache.incomingItems
            .takeIf { it.isEmpty() }
            ?.let { loadSearchResultsAndSaveToCache() }
            ?: let {
                mContentState.value = cache.incomingItems
                mSearchResultState.value =
                    cache.incomingItems
                        .takeIf { it.isEmpty() }
                        ?.let { SearchResultState.NoResults }
                        ?: SearchResultState.Content
            }

    fun refreshData() {
        refreshDataTask.cancel()
        refreshDataTask = viewModelScope.launch {
            idlingResource.ifPresent { it.increment() }
            try {
                useCase.search()
                    .getOrThrow()
                    .takeIf { it.result == ResponseResult.SUCCESS }
                    ?.let { response ->
                        val items = response.meanings.orEmpty().map {
                            WordsWithDefinitionItem(
                                id = it.id.orEmpty(),
                                word = it.word.orEmpty(),
                                definition = it.value.orEmpty(),
                                version = it.version.orEmpty()
                            )
                        }
                        cache.incomingItems = items.toMutableList()
                        mContentState.value = items
                        mSearchResultState.value =
                            items
                                .takeIf { it.isEmpty() }
                                ?.let { SearchResultState.NoResults }
                                ?: SearchResultState.Content
                    }
                    ?: let {
                        mSearchResultState.value = SearchResultState.Error
                    }
            } catch (_: Throwable) {
                mSearchResultState.value = SearchResultState.Error
            } finally {
                idlingResource.ifPresent { it.decrement() }
            }
        }
    }


    fun saveSearchItemAndPosition(item: WordsWithDefinitionItem, position: Int) {
        cache.currentlySelectedIncomingItem = item
        cache.currentlySelectedIncomingItemPosition = position
    }

    fun removeCurrentIncomingItem(binding: FragmentIncomingBinding) {
        cache.incomingItems
            .apply {
                removeAt(cache.currentlySelectedIncomingItemPosition)
            }
            .takeIf { it.isEmpty() }
            ?.let { mSearchResultState.value = SearchResultState.NoResults }
        (binding.resultsContent.wordsWithDefinitions.adapter as? WordsWithDefinitionAdapter)
            ?.apply {
                submitList(cache.incomingItems.slice(0 until itemCount - 1))
            }
    }

    fun getOnScrollListener(adapter: WordsWithDefinitionAdapter, pageSize: Int) =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                (recyclerView.layoutManager as? LinearLayoutManager)?.apply {
                    findLastVisibleItemPosition()
                        .takeIf { it == itemCount - 1 && itemCount < cache.incomingItems.size }
                        ?.let {
                            adapter.submitList(
                                cache.incomingItems.slice(
                                    0
                                            until
                                            min(cache.incomingItems.size, itemCount + pageSize)
                                ))
                        }
                }
            }
        }

    sealed class SearchResultState {

        data object Loading: SearchResultState()

        data object Content: SearchResultState()

        data object NoResults: SearchResultState()

        data object Error: SearchResultState()
    }

}