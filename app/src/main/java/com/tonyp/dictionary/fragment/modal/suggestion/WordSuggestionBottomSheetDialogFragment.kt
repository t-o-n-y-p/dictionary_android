package com.tonyp.dictionary.fragment.modal.suggestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tonyp.dictionary.R
import com.tonyp.dictionary.databinding.FragmentWordSuggestionBottomSheetDialogBinding
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
}