package com.tonyp.dictionary.fragment.modal.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tonyp.dictionary.R
import com.tonyp.dictionary.databinding.FragmentLoginOnlyBottomSheetDialogBinding
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

}