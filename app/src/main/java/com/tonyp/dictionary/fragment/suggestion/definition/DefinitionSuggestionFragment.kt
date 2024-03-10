package com.tonyp.dictionary.fragment.suggestion.definition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tonyp.dictionary.R
import com.tonyp.dictionary.databinding.FragmentDefinitionSuggestionBinding
import com.tonyp.dictionary.fragment.FragmentResultConstants
import com.tonyp.dictionary.fragment.setFragmentResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DefinitionSuggestionFragment : Fragment(R.layout.fragment_definition_suggestion) {

    private lateinit var binding: FragmentDefinitionSuggestionBinding
    private val viewModel: DefinitionSuggestionFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDefinitionSuggestionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.submitButton.isEnabled = false
        viewModel.submitState.observe(viewLifecycleOwner) {
            when (it) {
                DefinitionSuggestionFragmentViewModel.SubmitState.NotSet -> {
                    binding.submitButton.isVisible = true
                    binding.submittingButton.isVisible = false
                }
                DefinitionSuggestionFragmentViewModel.SubmitState.Loading -> {
                    binding.submitButton.isVisible = false
                    binding.submittingButton.isVisible = true
                }
                DefinitionSuggestionFragmentViewModel.SubmitState.Duplicate -> {
                    binding.submitButton.isVisible = true
                    binding.submittingButton.isVisible = false
                    binding.alertDefinitionTextInputLayout.error =
                        getString(R.string.this_record_already_exists)
                }
                DefinitionSuggestionFragmentViewModel.SubmitState.Success -> {
                    setFragmentResult(
                        FragmentResultConstants.DEFINITION_SUGGESTION_FRAGMENT,
                        FragmentResultConstants.SUGGESTION_STATUS,
                        FragmentResultConstants.SUCCESS
                    )
                }
                DefinitionSuggestionFragmentViewModel.SubmitState.Error -> {
                    setFragmentResult(
                        FragmentResultConstants.DEFINITION_SUGGESTION_FRAGMENT,
                        FragmentResultConstants.SUGGESTION_STATUS,
                        FragmentResultConstants.UNEXPECTED_ERROR
                    )
                }
            }
        }
        binding.alertDefinitionTextInput.addTextChangedListener {
            binding.alertDefinitionTextInputLayout.error = null
            binding.submitButton.isEnabled = it?.toString().isNullOrBlank().not()
        }
        binding.submitButton.setOnClickListener {
            binding.alertDefinitionTextInput.text?.apply {
                viewModel.submitDefinition(toString())
            }
        }
        viewModel.fillFieldsFromCache(binding)
    }
}