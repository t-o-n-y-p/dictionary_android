package com.tonyp.dictionary.fragment.definition

import androidx.lifecycle.ViewModel
import com.tonyp.dictionary.WizardCache
import com.tonyp.dictionary.databinding.FragmentWordDefinitionBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WordDefinitionFragmentViewModel @Inject constructor(
    private val cache: WizardCache
) : ViewModel() {

    fun fillDataFromCache(binding: FragmentWordDefinitionBinding) {
        binding.wordText.text = cache.currentlySelectedWord
        binding.definitionText.text =
            cache.currentlySelectedSearchResults
                .takeIf { it.size == 1 }
                ?.let { it[0].value }
                ?: cache.currentlySelectedSearchResults
                    .mapIndexed { i, e -> "${i + 1}. ${e.value}" }
                    .joinToString(separator = "\n")
    }

}