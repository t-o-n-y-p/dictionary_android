package com.tonyp.dictionary.fragment.modal.incoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tonyp.dictionary.WizardCache
import com.tonyp.dictionary.api.v1.models.ResponseResult
import com.tonyp.dictionary.databinding.FragmentWordDefinitionIncomingBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class IncomingSuggestionBottomSheetDialogFragmentViewModel @Inject constructor(
    private val useCase: IncomingSuggestionBottomSheetDialogFragmentUseCase,
    private val cache: WizardCache
) : ViewModel() {

    private val mProcessingState = MutableLiveData<ProcessingState>(ProcessingState.NotSet)
    val processingState: LiveData<ProcessingState> get() = mProcessingState

    fun approveSuggestion() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    useCase.update(
                        id = cache.currentlySelectedIncomingItem.id,
                        version = cache.currentlySelectedIncomingItem.version
                    )
                }
                    .getOrNull()
                    ?.takeIf { it.result == ResponseResult.SUCCESS }
                    ?.let { mProcessingState.value = ProcessingState.Approved }
                    ?: let { mProcessingState.value = ProcessingState.Error }
            } catch (t: Throwable) {
                mProcessingState.value = ProcessingState.Error
            }
        }
    }

    fun declineSuggestion() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    useCase.delete(
                        id = cache.currentlySelectedIncomingItem.id,
                        version = cache.currentlySelectedIncomingItem.version
                    )
                }
                    .getOrNull()
                    ?.takeIf { it.result == ResponseResult.SUCCESS }
                    ?.let { mProcessingState.value = ProcessingState.Declined }
                    ?: let { mProcessingState.value = ProcessingState.Error }
            } catch (t: Throwable) {
                mProcessingState.value = ProcessingState.Error
            }
        }
    }

    fun fillDataFromCache(binding: FragmentWordDefinitionIncomingBinding) {
        binding.wordText.text = cache.currentlySelectedIncomingItem.word
        binding.definitionText.text = cache.currentlySelectedIncomingItem.definition
    }

    sealed class ProcessingState {

        data object NotSet: ProcessingState()

        data object Loading: ProcessingState()

        data object Approved: ProcessingState()

        data object Declined: ProcessingState()

        data object Error : ProcessingState()
    }

}