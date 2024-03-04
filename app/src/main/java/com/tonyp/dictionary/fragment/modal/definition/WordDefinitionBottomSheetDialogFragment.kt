package com.tonyp.dictionary.fragment.modal.definition

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tonyp.dictionary.R
import com.tonyp.dictionary.databinding.FragmentWordDefinitionBinding
import com.tonyp.dictionary.databinding.FragmentWordDefinitionBottomSheetDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WordDefinitionBottomSheetDialogFragment :
    BottomSheetDialogFragment(R.layout.fragment_word_definition_bottom_sheet_dialog) {

    private lateinit var binding: FragmentWordDefinitionBottomSheetDialogBinding
    private val viewModel: WordDefinitionBottomSheetDialogFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWordDefinitionBottomSheetDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.clearCachedSelectedWord()
    }
}