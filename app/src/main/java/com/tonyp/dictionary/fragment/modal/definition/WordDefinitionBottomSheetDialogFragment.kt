package com.tonyp.dictionary.fragment.modal.definition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.get
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tonyp.dictionary.R
import com.tonyp.dictionary.databinding.FragmentWordDefinitionBottomSheetDialogBinding
import com.tonyp.dictionary.fragment.FragmentResultConstants
import com.tonyp.dictionary.fragment.dismissWithToast
import com.tonyp.dictionary.fragment.login.LoginFragment
import com.tonyp.dictionary.fragment.suggestion.definition.DefinitionSuggestionFragment
import com.tonyp.dictionary.fragment.suggestion.word.WordWithDefinitionSuggestionFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WordDefinitionBottomSheetDialogFragment :
    BottomSheetDialogFragment(R.layout.fragment_word_definition_bottom_sheet_dialog) {

    private lateinit var binding: FragmentWordDefinitionBottomSheetDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWordDefinitionBottomSheetDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val childFragmentManager =
            binding.bottomSheetContent
                .getFragment<NavHostFragment>()
                .childFragmentManager
        childFragmentManager
            .setFragmentResultListener(
                FragmentResultConstants.LOGIN_FRAGMENT,
                viewLifecycleOwner
            ) { _, bundle ->
                when (bundle.getString(FragmentResultConstants.LOGIN_STATUS)) {
                    FragmentResultConstants.UNEXPECTED_ERROR ->
                        dismissWithToast(R.string.an_unexpected_error_occurred)
                }
            }
        childFragmentManager
            .setFragmentResultListener(
                FragmentResultConstants.DEFINITION_SUGGESTION_FRAGMENT,
                viewLifecycleOwner
            ) { _, bundle ->
                when (bundle.getString(FragmentResultConstants.SUGGESTION_STATUS)) {
                    FragmentResultConstants.SUCCESS ->
                        dismissWithToast(R.string.your_proposition_has_been_submitted)
                    FragmentResultConstants.UNEXPECTED_ERROR ->
                        dismissWithToast(R.string.an_unexpected_error_occurred)
                }
            }
    }
}