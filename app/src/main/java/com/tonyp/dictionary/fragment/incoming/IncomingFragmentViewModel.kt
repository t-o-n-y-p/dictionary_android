package com.tonyp.dictionary.fragment.incoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tonyp.dictionary.WizardCache
import com.tonyp.dictionary.api.v1.models.ResponseResult
import com.tonyp.dictionary.databinding.FragmentIncomingBinding
import com.tonyp.dictionary.recyclerview.definition.WordsWithDefinitionAdapter
import com.tonyp.dictionary.recyclerview.definition.WordsWithDefinitionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class IncomingFragmentViewModel @Inject constructor(
    private val useCase: IncomingFragmentUseCase,
    private val cache: WizardCache
) : ViewModel() {

    private val mSearchResultState = MutableLiveData<SearchResultState>()
    val searchResultState: LiveData<SearchResultState> get() = mSearchResultState

    private val mContentState = MutableLiveData<List<WordsWithDefinitionItem>>()
    val contentState: LiveData<List<WordsWithDefinitionItem>> get() = mContentState

    private fun loadSearchResultsAndSaveToCache() {
        cache.incomingItems.clear()
        viewModelScope.launch {
            mSearchResultState.value = SearchResultState.Loading
            try {
                withContext(Dispatchers.IO) { useCase.search() }
                    .getOrNull()
                    ?.takeIf { it.result == ResponseResult.SUCCESS }
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
            } catch (t: Throwable) {
                mSearchResultState.value = SearchResultState.Error
            }
        }
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

    fun saveSearchItemAndPosition(item: WordsWithDefinitionItem, position: Int) {
        cache.currentlySelectedIncomingItem = item
        cache.currentlySelectedIncomingItemPosition = position
    }

    fun removeCurrentIncomingItem(binding: FragmentIncomingBinding) {
        cache.incomingItems.removeAt(cache.currentlySelectedIncomingItemPosition)
        (binding.resultsContent.wordsWithDefinitions.adapter as? WordsWithDefinitionAdapter)
            ?.apply {
                notifyItemRemoved(cache.currentlySelectedIncomingItemPosition)
                val slice = cache.incomingItems.slice(0 until itemCount - 1)
                submitList(slice)
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