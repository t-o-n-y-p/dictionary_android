package com.tonyp.dictionary.fragment.suggestion.definition

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tonyp.dictionary.SecurePreferences
import com.tonyp.dictionary.WizardCache
import com.tonyp.dictionary.api.v1.models.ResponseResult
import com.tonyp.dictionary.databinding.FragmentDefinitionSuggestionBinding
import com.tonyp.dictionary.storage.get
import com.tonyp.dictionary.storage.models.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DefinitionSuggestionFragmentViewModel @Inject constructor(
    @SecurePreferences private val securePreferences: SharedPreferences,
    private val useCase: DefinitionSuggestionFragmentUseCase,
    private val cache: WizardCache
) : ViewModel() {

    private val mSubmitState = MutableLiveData<SubmitState>(SubmitState.NotSet)
    val submitState: LiveData<SubmitState> get() = mSubmitState

    fun fillFieldsFromCache(binding: FragmentDefinitionSuggestionBinding) {
        binding.wordText.text = cache.currentlySelectedWord
    }

    fun submitDefinition(definition: String) {
        viewModelScope.launch {
            mSubmitState.value = SubmitState.Loading
            try {
                val userPreferences =
                    securePreferences.get<UserPreferences>() ?: throw IllegalStateException()
                withContext(Dispatchers.IO) {
                    useCase.create(
                        word = cache.currentlySelectedWord,
                        definition = definition,
                        proposedBy = userPreferences.username
                    )
                }
                    .getOrNull()
                    ?.takeIf { it.result == ResponseResult.SUCCESS }
                    ?.let { mSubmitState.value = SubmitState.Success }
                    ?: let { mSubmitState.value = SubmitState.Error }
            } catch (t: Throwable) {
                mSubmitState.value = SubmitState.Error
            }
        }
    }

    sealed class SubmitState {

        data object NotSet: SubmitState()

        data object Loading: SubmitState()

        data object Success: SubmitState()

        data object Error : SubmitState()
    }

}