package com.tonyp.dictionary.fragment.modal.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tonyp.dictionary.R
import com.tonyp.dictionary.databinding.FragmentLoginOnlyBottomSheetDialogBinding
import com.tonyp.dictionary.fragment.FragmentResultConstants
import com.tonyp.dictionary.fragment.dismissWithToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginOnlyBottomSheetDialogFragment :
    BottomSheetDialogFragment(R.layout.fragment_login_only_bottom_sheet_dialog) {

    private lateinit var binding: FragmentLoginOnlyBottomSheetDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginOnlyBottomSheetDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.setFragmentResultListener(
            FragmentResultConstants.LOGIN_FRAGMENT,
            viewLifecycleOwner
        ) { _, bundle ->
            when (bundle.getString(FragmentResultConstants.LOGIN_STATUS)) {
                FragmentResultConstants.SUCCESS -> dismiss()
                FragmentResultConstants.UNEXPECTED_ERROR ->
                    dismissWithToast(R.string.an_unexpected_error_occurred)
            }
        }
    }

}