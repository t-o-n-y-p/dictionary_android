package com.tonyp.dictionary.fragment.modal.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tonyp.dictionary.R
import com.tonyp.dictionary.databinding.FragmentLoginBottomSheetDialogBinding
import com.tonyp.dictionary.fragment.FragmentResultConstants
import com.tonyp.dictionary.fragment.dismissWithToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginWithSuggestionBottomSheetDialogFragment :
    BottomSheetDialogFragment(R.layout.fragment_login_bottom_sheet_dialog) {

    private lateinit var binding: FragmentLoginBottomSheetDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBottomSheetDialogBinding.inflate(inflater)
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