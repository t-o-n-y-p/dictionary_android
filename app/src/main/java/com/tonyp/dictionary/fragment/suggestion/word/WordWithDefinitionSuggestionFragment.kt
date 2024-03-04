package com.tonyp.dictionary.fragment.suggestion.word

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
import com.tonyp.dictionary.databinding.FragmentWordWithDefinitionSuggestionBinding
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
        viewModel.fillFieldsFromCache(binding)
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
                WordWithDefinitionSuggestionFragmentViewModel.SubmitState.Success -> {
                    dismissBottomSheetAndSendToastWithText(R.string.your_proposition_has_been_submitted)
                }
                WordWithDefinitionSuggestionFragmentViewModel.SubmitState.Error -> {
                    dismissBottomSheetAndSendToastWithText(R.string.an_error_has_occurred)
                }
            }
        }
        binding.submitButton.setOnClickListener {
            binding
                .takeIf {
                    it.alertWordTextInput.text != null
                            && it.alertDefinitionTextInput.text != null
                }?.let {
                    viewModel.submitWordWithDefinition(
                        word = it.alertWordTextInput.text.toString(),
                        definition = it.alertDefinitionTextInput.text.toString()
                    )
                }
        }
    }

    private fun dismissBottomSheetAndSendToastWithText(resId: Int) {
        (parentFragment?.parentFragment as? BottomSheetDialogFragment)!!.dismiss()
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
    }
}