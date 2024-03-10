package com.tonyp.dictionary.fragment.modal.suggestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tonyp.dictionary.R
import com.tonyp.dictionary.databinding.FragmentWordSuggestionBottomSheetDialogBinding
import com.tonyp.dictionary.fragment.FragmentResultConstants
import com.tonyp.dictionary.fragment.dismissWithToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WordSuggestionBottomSheetDialogFragment :
    BottomSheetDialogFragment(R.layout.fragment_word_suggestion_bottom_sheet_dialog) {

    private lateinit var binding: FragmentWordSuggestionBottomSheetDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWordSuggestionBottomSheetDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomSheetContent.getFragment<NavHostFragment>()
            .childFragmentManager
            .setFragmentResultListener(
                FragmentResultConstants.WORD_WITH_DEFINITION_SUGGESTION_FRAGMENT,
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