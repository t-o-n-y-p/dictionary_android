package com.tonyp.dictionary.fragment.suggestion.word

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tonyp.dictionary.SecurePreferences
import com.tonyp.dictionary.WizardCache
import com.tonyp.dictionary.api.v1.models.ResponseResult
import com.tonyp.dictionary.databinding.FragmentWordWithDefinitionSuggestionBinding
import com.tonyp.dictionary.storage.get
import com.tonyp.dictionary.storage.models.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WordWithDefinitionSuggestionFragmentViewModel @Inject constructor(
    @SecurePreferences private val securePreferences: SharedPreferences,
    private val useCase: WordWithDefinitionSuggestionFragmentUseCase,
    private val cache: WizardCache
) : ViewModel() {

    private val mSubmitState = MutableLiveData<SubmitState>(SubmitState.NotSet)
    val submitState: LiveData<SubmitState> get() = mSubmitState

    fun fillFieldsFromCache(binding: FragmentWordWithDefinitionSuggestionBinding) {
        binding.alertWordTextInput.setText(cache.searchInput)
    }

    fun submitWordWithDefinition(word: String, definition: String) {
        viewModelScope.launch {
            mSubmitState.value = SubmitState.Loading
            try {
                val userPreferences =
                    securePreferences.get<UserPreferences>() ?: throw IllegalStateException()
                withContext(Dispatchers.IO) {
                    useCase.create(
                        word = word,
                        definition = definition,
                        proposedBy = userPreferences.username,
                        authHeaderValue = userPreferences.getAuthHeaderValue()
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