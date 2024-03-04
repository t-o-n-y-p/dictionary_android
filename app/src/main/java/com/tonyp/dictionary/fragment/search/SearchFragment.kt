package com.tonyp.dictionary.fragment.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tonyp.dictionary.R
import com.tonyp.dictionary.databinding.FragmentSearchBinding
import com.tonyp.dictionary.fragment.modal.definition.WordDefinitionBottomSheetDialogFragment
import com.tonyp.dictionary.recyclerview.word.WordsAdapter
import com.tonyp.dictionary.recyclerview.word.WordsItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchFragmentViewModel by viewModels()
    private val adapter: WordsAdapter = WordsAdapter(
        onItemClicked = {
            viewModel.saveSearchItem(it)
            binding.searchView.editText.clearFocus()
            WordDefinitionBottomSheetDialogFragment().show(
                parentFragmentManager,
                WordDefinitionBottomSheetDialogFragment::class.simpleName)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchViewContent.searchResultsContent.words.adapter = adapter
        binding.searchView.editText.addTextChangedListener {
            viewModel.loadSearchResultsAndSaveToCache(input = it?.toString() ?: "")
        }
        viewModel.searchResultState.observe(viewLifecycleOwner) {
            when (it) {
                SearchFragmentViewModel.SearchResultState.NotSet -> {
                    binding.searchViewContent.searchInvitation.invitationGroup.isVisible = true
                    binding.searchViewContent.searchResultsLoading.loadingGroup.isVisible = false
                    binding.searchViewContent.searchResultsContent.contentGroup.isVisible = false
                    binding.searchViewContent.searchResultsError.errorGroup.isVisible = false
                }
                SearchFragmentViewModel.SearchResultState.Loading -> {
                    binding.searchViewContent.searchInvitation.invitationGroup.isVisible = false
                    binding.searchViewContent.searchResultsLoading.loadingGroup.isVisible = true
                    binding.searchViewContent.searchResultsContent.contentGroup.isVisible = false
                    binding.searchViewContent.searchResultsError.errorGroup.isVisible = false
                }
                SearchFragmentViewModel.SearchResultState.Content -> {
                    binding.searchViewContent.searchInvitation.invitationGroup.isVisible = false
                    binding.searchViewContent.searchResultsLoading.loadingGroup.isVisible = false
                    binding.searchViewContent.searchResultsContent.contentGroup.isVisible = true
                    binding.searchViewContent.searchResultsError.errorGroup.isVisible = false
                }
                SearchFragmentViewModel.SearchResultState.Error -> {
                    binding.searchViewContent.searchInvitation.invitationGroup.isVisible = false
                    binding.searchViewContent.searchResultsLoading.loadingGroup.isVisible = false
                    binding.searchViewContent.searchResultsContent.contentGroup.isVisible = false
                    binding.searchViewContent.searchResultsError.errorGroup.isVisible = true
                }
            }
        }
        viewModel.contentState.observe(viewLifecycleOwner) { meaningObjects ->
            val words = meaningObjects.mapNotNull { obj -> obj.word?.let { WordsItem(it) } }.distinct()
            adapter.submitList(words)
        }
        binding.searchView.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_suggest -> {
                    binding.searchView.editText.clearFocus()
                    val fragmentToOpen = viewModel.getBottomSheetFragmentToOpen()
                    fragmentToOpen.show(
                        parentFragmentManager,
                        fragmentToOpen::class.simpleName)
                    true
                }
                else -> false
            }
        }
        viewModel.fillDataFromCache(binding)
        binding.searchView.inflateMenu(R.menu.search_view_menu)
        binding.searchView.show()
    }
}