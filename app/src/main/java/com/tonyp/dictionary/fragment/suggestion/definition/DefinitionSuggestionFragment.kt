package com.tonyp.dictionary.fragment.suggestion.definition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tonyp.dictionary.R
import com.tonyp.dictionary.databinding.FragmentDefinitionSuggestionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DefinitionSuggestionFragment : Fragment(R.layout.fragment_definition_suggestion) {

    private lateinit var binding: FragmentDefinitionSuggestionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDefinitionSuggestionBinding.inflate(inflater)
        return binding.root
    }
}