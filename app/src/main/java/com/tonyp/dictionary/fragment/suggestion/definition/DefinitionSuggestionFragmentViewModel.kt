package com.tonyp.dictionary.fragment.suggestion.definition

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.test.espresso.idling.CountingIdlingResource
import com.tonyp.dictionary.OptionalIdlingResource
import com.tonyp.dictionary.SecurePreferences
import com.tonyp.dictionary.WizardCache
import com.tonyp.dictionary.api.v1.models.ResponseResult
import com.tonyp.dictionary.databinding.FragmentDefinitionSuggestionBinding
import com.tonyp.dictionary.fragment.ServerErrorConstants
import com.tonyp.dictionary.storage.get
import com.tonyp.dictionary.storage.models.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Optional
import javax.inject.Inject

@HiltViewModel
class DefinitionSuggestionFragmentViewModel @Inject constructor(
    @SecurePreferences private val securePreferences: SharedPreferences,
    private val useCase: DefinitionSuggestionFragmentUseCase,
    private val cache: WizardCache,
    @OptionalIdlingResource private val idlingResource: Optional<CountingIdlingResource>
) : ViewModel() {

    private val mSubmitState = MutableLiveData<SubmitState>(SubmitState.NotSet)
    val submitState: LiveData<SubmitState> get() = mSubmitState

    fun fillFieldsFromCache(binding: FragmentDefinitionSuggestionBinding) {
        binding.wordText.text = cache.currentlySelectedItem.value
    }

    fun submitDefinition(definition: String) =
        viewModelScope.launch {
            idlingResource.ifPresent { it.increment() }
            try {
                mSubmitState.value = SubmitState.Loading
                val userPreferences =
                    securePreferences.get<UserPreferences>() ?: throw SecurityException()
                useCase.create(
                    word = cache.currentlySelectedItem.value,
                    definition = definition,
                    proposedBy = userPreferences.username
                )
                    .getOrThrow()
                    .let { response ->
                        when (response.result) {
                            null -> mSubmitState.value = SubmitState.Error
                            ResponseResult.SUCCESS -> mSubmitState.value = SubmitState.Success
                            ResponseResult.ERROR ->
                                mSubmitState.value =
                                    response.errors
                                        ?.takeIf {
                                            it.any {
                                                e -> e.code == ServerErrorConstants.ALREADY_EXISTS
                                            }
                                        }
                                        ?.let { SubmitState.Duplicate }
                                        ?: SubmitState.Error
                        }
                    }
            } catch (_: SecurityException) {
                mSubmitState.value = SubmitState.LoggedOut
            } catch (_: Throwable) {
                mSubmitState.value = SubmitState.Error
            } finally {
                idlingResource.ifPresent { it.decrement() }
            }
        }

    sealed class SubmitState {

        data object NotSet: SubmitState()

        data object Loading: SubmitState()

        data object Success: SubmitState()

        data object Duplicate: SubmitState()

        data object LoggedOut : SubmitState()

        data object Error : SubmitState()
    }

}