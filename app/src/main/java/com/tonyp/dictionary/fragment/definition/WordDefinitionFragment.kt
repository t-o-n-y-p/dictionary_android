package com.tonyp.dictionary.fragment.definition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tonyp.dictionary.R
import com.tonyp.dictionary.databinding.FragmentWordDefinitionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WordDefinitionFragment : Fragment(R.layout.fragment_word_definition) {

    private lateinit var binding: FragmentWordDefinitionBinding
    private val viewModel: WordDefinitionFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWordDefinitionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.definitionState.observe(viewLifecycleOwner) {
            when (it) {
                WordDefinitionFragmentViewModel.DefinitionState.Loading -> {
                    binding.wordDefinitionLoading.loadingGroup.isVisible = true
                    binding.wordDefinitionContent.contentGroup.isVisible = false
                    binding.wordDefinitionError.errorGroup.isVisible = false
                }
                WordDefinitionFragmentViewModel.DefinitionState.Content -> {
                    binding.wordDefinitionLoading.loadingGroup.isVisible = false
                    binding.wordDefinitionContent.contentGroup.isVisible = true
                    binding.wordDefinitionError.errorGroup.isVisible = false
                }
                WordDefinitionFragmentViewModel.DefinitionState.Error -> {
                    binding.wordDefinitionLoading.loadingGroup.isVisible = false
                    binding.wordDefinitionContent.contentGroup.isVisible = false
                    binding.wordDefinitionError.errorGroup.isVisible = true
                }
            }
        }
        binding.wordDefinitionContent.addButton.setOnClickListener {
            findNavController().navigate(viewModel.getButtonAction())
        }
        viewModel.fillDataFromCache(binding)
    }
}