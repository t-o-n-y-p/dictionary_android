package com.tonyp.dictionary.fragment.suggestion.definition

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tonyp.dictionary.SecurePreferences
import com.tonyp.dictionary.WizardCache
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DefinitionSuggestionFragmentViewModel @Inject constructor(
    @SecurePreferences private val securePreferences: SharedPreferences,
    private val useCase: DefinitionSuggestionFragmentUseCase,
    private val cache: WizardCache
) {

    private val mSubmitState = MutableLiveData<SubmitState>(SubmitState.NotSet)
    val submitState: LiveData<SubmitState> get() = mSubmitState

    fun submitDefinition(definition: String) {

    }

    sealed class SubmitState {

        data object NotSet: SubmitState()

        data object Loading: SubmitState()

        data object Content: SubmitState()

        data object Error : SubmitState()
    }

}