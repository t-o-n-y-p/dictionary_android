package com.tonyp.dictionary.fragment.suggestion.word

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tonyp.dictionary.R
import com.tonyp.dictionary.databinding.FragmentWordWithDefinitionSuggestionBinding
import com.tonyp.dictionary.fragment.FragmentResultConstants
import com.tonyp.dictionary.fragment.setFragmentResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WordWithDefinitionSuggestionFragment : Fragment(R.layout.fragment_word_with_definition_suggestion) {

    private lateinit var binding: FragmentWordWithDefinitionSuggestionBinding
    private val viewModel: WordWithDefinitionSuggestionFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWordWithDefinitionSuggestionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.submitButton.isEnabled = false
        viewModel.submitState.observe(viewLifecycleOwner) {
            when (it) {
                WordWithDefinitionSuggestionFragmentViewModel.SubmitState.NotSet -> {
                    binding.submitButton.isVisible = true
                    binding.submittingButton.isVisible = false
                }
                WordWithDefinitionSuggestionFragmentViewModel.SubmitState.Loading -> {
                    binding.submitButton.isVisible = false
                    binding.submittingButton.isVisible = true
                }
                WordWithDefinitionSuggestionFragmentViewModel.SubmitState.Duplicate -> {
                    binding.submitButton.isVisible = true
                    binding.submittingButton.isVisible = false
                    binding.alertDefinitionTextInputLayout.error =
                        getString(R.string.this_record_already_exists)
                }
                WordWithDefinitionSuggestionFragmentViewModel.SubmitState.Success -> {
                    setFragmentResult(
                        FragmentResultConstants.WORD_WITH_DEFINITION_SUGGESTION_FRAGMENT,
                        FragmentResultConstants.SUGGESTION_STATUS,
                        FragmentResultConstants.SUCCESS
                    )
                }
                WordWithDefinitionSuggestionFragmentViewModel.SubmitState.Error -> {
                    setFragmentResult(
                        FragmentResultConstants.WORD_WITH_DEFINITION_SUGGESTION_FRAGMENT,
                        FragmentResultConstants.SUGGESTION_STATUS,
                        FragmentResultConstants.UNEXPECTED_ERROR
                    )
                }
            }
        }
        binding.alertWordTextInput.addTextChangedListener {
            setSubmitButtonState()
        }
        binding.alertDefinitionTextInput.addTextChangedListener {
            binding.alertDefinitionTextInputLayout.error = null
            setSubmitButtonState()
        }
        binding.submitButton.setOnClickListener {
            viewModel.submitWordWithDefinition(
                word = binding.alertWordTextInput.text.toString(),
                definition = binding.alertDefinitionTextInput.text.toString()
            )
        }
        viewModel.fillFieldsFromCache(binding)
    }

    private fun setSubmitButtonState() =
        (binding.alertWordTextInput.text.isNullOrBlank()
                || binding.alertDefinitionTextInput.text.isNullOrBlank())
            .let { binding.submitButton.isEnabled = !it }
}