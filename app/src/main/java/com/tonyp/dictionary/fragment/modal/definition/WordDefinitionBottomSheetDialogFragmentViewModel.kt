package com.tonyp.dictionary.fragment.modal.definition

import androidx.lifecycle.ViewModel
import com.tonyp.dictionary.WizardCache
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WordDefinitionBottomSheetDialogFragmentViewModel @Inject constructor(
    private val cache: WizardCache
) : ViewModel() {

    fun clearCachedSelectedWord() {
        cache.currentlySelectedWord = ""
    }

}