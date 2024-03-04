package com.tonyp.dictionary.fragment.suggestion.definition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tonyp.dictionary.R
import com.tonyp.dictionary.databinding.FragmentDefinitionSuggestionBinding
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
        viewModel.fillFieldsFromCache(binding)
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
                DefinitionSuggestionFragmentViewModel.SubmitState.Success -> {
                    (parentFragment?.parentFragment as? BottomSheetDialogFragment)!!.dismiss()
                    Toast.makeText(context, R.string.your_proposition_has_been_submitted, Toast.LENGTH_SHORT).show()
                }
                DefinitionSuggestionFragmentViewModel.SubmitState.Error -> {
                    (parentFragment?.parentFragment as? BottomSheetDialogFragment)!!.dismiss()
                    Toast.makeText(context, R.string.an_error_has_occurred, Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.submitButton.setOnClickListener {
            binding.alertDefinitionTextInput.text?.apply {
                viewModel.submitDefinition(it.toString())
            }
        }
    }
}