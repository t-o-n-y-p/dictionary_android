package com.tonyp.dictionary.fragment.recent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tonyp.dictionary.R
import com.tonyp.dictionary.databinding.FragmentRecentBinding
import com.tonyp.dictionary.fragment.modal.definition.WordDefinitionBottomSheetDialogFragment
import com.tonyp.dictionary.recyclerview.word.WordsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecentFragment : Fragment(R.layout.fragment_recent) {

    private lateinit var binding: FragmentRecentBinding
    private val viewModel: RecentFragmentViewModel by viewModels()
    private val adapter: WordsAdapter = WordsAdapter(
        onItemClicked = {
            viewModel.saveRecentItem(it)
            WordDefinitionBottomSheetDialogFragment().show(
                requireActivity().supportFragmentManager,
                WordDefinitionBottomSheetDialogFragment::class.simpleName)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewWords.words.adapter = adapter
        viewModel.recentResultState.observe(viewLifecycleOwner) {
            when (it) {
                RecentFragmentViewModel.RecentResultState.NotSet -> {
                    binding.noResults.noResultsGroup.isVisible = false
                    binding.recyclerViewWords.contentGroup.isVisible = false
                }
                RecentFragmentViewModel.RecentResultState.NoResults -> {
                    binding.noResults.noResultsGroup.isVisible = true
                    binding.recyclerViewWords.contentGroup.isVisible = false
                }
                RecentFragmentViewModel.RecentResultState.Content -> {
                    binding.noResults.noResultsGroup.isVisible = false
                    binding.recyclerViewWords.contentGroup.isVisible = true
                }
            }
        }
        viewModel.fillDataFromPreferences(adapter)
    }
}