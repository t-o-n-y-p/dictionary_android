package com.tonyp.dictionary.fragment.incoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tonyp.dictionary.R
import com.tonyp.dictionary.databinding.FragmentIncomingBinding
import com.tonyp.dictionary.fragment.modal.incoming.IncomingSuggestionBottomSheetDialogFragment
import com.tonyp.dictionary.recyclerview.definition.WordsWithDefinitionAdapter
import com.tonyp.dictionary.recyclerview.definition.WordsWithDefinitionItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IncomingFragment : Fragment(R.layout.fragment_incoming) {

    private lateinit var binding: FragmentIncomingBinding
    private val viewModel: IncomingFragmentViewModel by viewModels()
    private val adapter: WordsWithDefinitionAdapter = WordsWithDefinitionAdapter(
        onItemClicked = {
            viewModel.saveSearchItem(it)
            IncomingSuggestionBottomSheetDialogFragment().show(
                parentFragmentManager,
                IncomingSuggestionBottomSheetDialogFragment::class.simpleName
            )
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIncomingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.resultsContent.wordsWithDefinitions.adapter = adapter
        viewModel.searchResultState.observe(viewLifecycleOwner) {
            when (it) {
                IncomingFragmentViewModel.SearchResultState.Loading -> {
                    binding.resultsLoading.loadingGroup.isVisible = true
                    binding.resultsContent.contentGroup.isVisible = false
                    binding.resultsError.errorGroup.isVisible = false
                }
                IncomingFragmentViewModel.SearchResultState.Content -> {
                    binding.resultsLoading.loadingGroup.isVisible = false
                    binding.resultsContent.contentGroup.isVisible = true
                    binding.resultsError.errorGroup.isVisible = false
                }
                IncomingFragmentViewModel.SearchResultState.Error -> {
                    binding.resultsLoading.loadingGroup.isVisible = false
                    binding.resultsContent.contentGroup.isVisible = false
                    binding.resultsError.errorGroup.isVisible = true
                }
            }
        }
        viewModel.contentState.observe(viewLifecycleOwner) { meaningObjects ->
            val wordsWithDefinition =
                meaningObjects.map {
                    WordsWithDefinitionItem(
                        id = it.id.orEmpty(),
                        word = it.word.orEmpty(),
                        definition = it.value.orEmpty()
                    )
                }
            adapter.submitList(wordsWithDefinition)
        }
        viewModel.fillDataFromCache()
    }

}