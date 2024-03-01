package com.tonyp.dictionary.fragment.definition

import android.content.SharedPreferences
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import com.tonyp.dictionary.SecurePreferences
import com.tonyp.dictionary.WizardCache
import com.tonyp.dictionary.databinding.FragmentWordDefinitionBinding
import com.tonyp.dictionary.storage.get
import com.tonyp.dictionary.storage.models.UserPreferences
import com.tonyp.dictionary.storage.models.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WordDefinitionFragmentViewModel @Inject constructor(
    @SecurePreferences private val securePreferences: SharedPreferences,
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
        val userPreferences = securePreferences.get<UserPreferences>() ?: UserPreferences()
        when {
            userPreferences.accessToken.isBlank() -> {
                binding.logInToAddButton.isVisible = true
                binding.addButton.isVisible = false
            }
            userPreferences.roles.contains(UserRole.BANNED) -> {
                binding.logInToAddButton.isVisible = false
                binding.addButton.isVisible = false
            }
            else -> {
                binding.logInToAddButton.isVisible = false
                binding.addButton.isVisible = true
            }
        }
    }

}