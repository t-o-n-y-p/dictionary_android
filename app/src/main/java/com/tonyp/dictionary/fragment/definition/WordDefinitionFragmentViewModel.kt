package com.tonyp.dictionary.fragment.definition

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.test.espresso.idling.CountingIdlingResource
import com.tonyp.dictionary.OptionalIdlingResource
import com.tonyp.dictionary.R
import com.tonyp.dictionary.SecurePreferences
import com.tonyp.dictionary.WizardCache
import com.tonyp.dictionary.api.v1.models.ResponseResult
import com.tonyp.dictionary.databinding.FragmentWordDefinitionBinding
import com.tonyp.dictionary.recyclerview.word.WordsItemMapper
import com.tonyp.dictionary.storage.get
import com.tonyp.dictionary.storage.models.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Optional
import javax.inject.Inject

@HiltViewModel
class WordDefinitionFragmentViewModel @Inject constructor(
    @SecurePreferences private val securePreferences: SharedPreferences,
    private val useCase: WordDefinitionFragmentUseCase,
    private val cache: WizardCache,
    @OptionalIdlingResource private val idlingResource: Optional<CountingIdlingResource>
) : ViewModel() {

    private val mDefinitionState = MutableLiveData<DefinitionState>(DefinitionState.Loading)
    val definitionState: LiveData<DefinitionState> get() = mDefinitionState

    fun fillDataFromCache(binding: FragmentWordDefinitionBinding) {
        binding.wordDefinitionContent.wordText.text = cache.currentlySelectedItem.value
        cache.currentlySelectedItem.definitions
            .takeIf { it.isEmpty() }
            ?.let { loadDefinitionToCache(binding) }
            ?: let {
                binding.wordDefinitionContent.definitionText.text =
                    cache.currentlySelectedItem.definitions
                mDefinitionState.value = DefinitionState.Content
            }
    }

    private fun loadDefinitionToCache(binding: FragmentWordDefinitionBinding) =
        viewModelScope.launch {
            idlingResource.ifPresent { it.increment() }
            try {
                mDefinitionState.value = DefinitionState.Loading
                withContext(Dispatchers.IO) { useCase.search(cache.currentlySelectedItem.value) }
                    .getOrNull()
                    ?.takeIf { it.result == ResponseResult.SUCCESS }
                    ?.let { response ->
                        val items = WordsItemMapper.map(response.meanings.orEmpty())
                            .ifEmpty { listOf(cache.currentlySelectedItem) }
                        cache.currentlySelectedItem = items[0]
                        cache.currentlySelectedItem.definitions
                            .takeIf { it.isNotEmpty() }
                            ?.let { binding.wordDefinitionContent.definitionText.text = it }
                        mDefinitionState.value = DefinitionState.Content
                    }
                    ?: let {
                        mDefinitionState.value = DefinitionState.Error
                    }
            } catch (t: Throwable) {
                mDefinitionState.value = DefinitionState.Error
            } finally {
                idlingResource.ifPresent { it.decrement() }
            }
        }

    fun getButtonAction() =
        securePreferences
            .takeIf { it.get<UserPreferences>()?.isLoggedIn() ?: false }
            ?.let { R.id.go_to_proposition }
            ?: R.id.go_to_login

    sealed class DefinitionState {

        data object Loading: DefinitionState()

        data object Content: DefinitionState()

        data object Error: DefinitionState()
    }

}