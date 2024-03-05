package com.tonyp.dictionary.fragment.definition

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tonyp.dictionary.R
import com.tonyp.dictionary.SecurePreferences
import com.tonyp.dictionary.WizardCache
import com.tonyp.dictionary.api.v1.models.ResponseResult
import com.tonyp.dictionary.databinding.FragmentWordDefinitionBinding
import com.tonyp.dictionary.storage.get
import com.tonyp.dictionary.storage.models.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WordDefinitionFragmentViewModel @Inject constructor(
    @SecurePreferences private val securePreferences: SharedPreferences,
    private val useCase: WordDefinitionFragmentUseCase,
    private val cache: WizardCache
) : ViewModel() {

    private val mDefinitionState = MutableLiveData<DefinitionState>(DefinitionState.Loading)
    val definitionState: LiveData<DefinitionState> get() = mDefinitionState

    fun fillDataFromCache(binding: FragmentWordDefinitionBinding) {
        binding.wordDefinitionContent.wordText.text = cache.currentlySelectedWord
        cache.currentlySelectedSearchResults
            .takeIf { it.isEmpty() }
            ?.let { loadDefinitionToCache(binding) }
            ?: let {
                fillDefinitionText(binding)
                mDefinitionState.value = DefinitionState.Content
            }
    }

    private fun fillDefinitionText(binding: FragmentWordDefinitionBinding) {
        binding.wordDefinitionContent.definitionText.text =
            cache.currentlySelectedSearchResults
                .takeIf { it.size == 1 }
                ?.let { it[0].value }
                ?: cache.currentlySelectedSearchResults
                    .mapIndexed { i, e -> "${i + 1}. ${e.value}" }
                    .joinToString(separator = "\n")
    }

    private fun loadDefinitionToCache(binding: FragmentWordDefinitionBinding) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { useCase.search(cache.currentlySelectedWord) }
                    .getOrNull()
                    ?.takeIf { it.result == ResponseResult.SUCCESS }
                    ?.let {
                        val meaningObjects = it.meanings.orEmpty()
                        cache.currentlySelectedSearchResults = meaningObjects
                        fillDefinitionText(binding)
                        mDefinitionState.value = DefinitionState.Content
                    }
                    ?: let {
                        mDefinitionState.value = DefinitionState.Error
                    }
            } catch (t: Throwable) {
                mDefinitionState.value = DefinitionState.Error
            }
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