package com.tonyp.dictionary.fragment.definition

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tonyp.dictionary.R
import com.tonyp.dictionary.databinding.FragmentSearchBinding
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
        viewModel.fillDataFromCache(binding)
    }
}