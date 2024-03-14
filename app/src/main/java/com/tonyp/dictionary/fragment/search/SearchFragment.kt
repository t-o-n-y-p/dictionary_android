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
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.min

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchFragmentViewModel by viewModels()
    private val adapter: WordsAdapter = WordsAdapter(
        onItemClicked = {
            viewModel.saveSearchItem(it)
            binding.searchView.editText.clearFocus()
            WordDefinitionBottomSheetDialogFragment().show(
                requireActivity().supportFragmentManager,
                WordDefinitionBottomSheetDialogFragment::class.simpleName)
        }
    )
    private val pageSize = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchViewContent.searchResultsContent.words.apply {
            adapter = this@SearchFragment.adapter
            addOnScrollListener(viewModel.getOnScrollListener(this@SearchFragment.adapter, pageSize))
        }
        viewModel.searchResultState.observe(viewLifecycleOwner) {
            when (it) {
                SearchFragmentViewModel.SearchResultState.NotSet -> {
                    binding.searchViewContent.searchInvitation.invitationGroup.isVisible = true
                    binding.searchViewContent.searchResultsLoading.loadingGroup.isVisible = false
                    binding.searchViewContent.searchResultsContent.contentGroup.isVisible = false
                    binding.searchViewContent.searchResultsError.errorGroup.isVisible = false
                    binding.searchViewContent.searchNoResults.noResultsGroup.isVisible = false
                }
                SearchFragmentViewModel.SearchResultState.Loading -> {
                    binding.searchViewContent.searchInvitation.invitationGroup.isVisible = false
                    binding.searchViewContent.searchResultsLoading.loadingGroup.isVisible = true
                    binding.searchViewContent.searchResultsContent.contentGroup.isVisible = false
                    binding.searchViewContent.searchResultsError.errorGroup.isVisible = false
                    binding.searchViewContent.searchNoResults.noResultsGroup.isVisible = false
                }
                SearchFragmentViewModel.SearchResultState.Content -> {
                    binding.searchViewContent.searchInvitation.invitationGroup.isVisible = false
                    binding.searchViewContent.searchResultsLoading.loadingGroup.isVisible = false
                    binding.searchViewContent.searchResultsContent.contentGroup.isVisible = true
                    binding.searchViewContent.searchResultsError.errorGroup.isVisible = false
                    binding.searchViewContent.searchNoResults.noResultsGroup.isVisible = false
                }
                SearchFragmentViewModel.SearchResultState.Error -> {
                    binding.searchViewContent.searchInvitation.invitationGroup.isVisible = false
                    binding.searchViewContent.searchResultsLoading.loadingGroup.isVisible = false
                    binding.searchViewContent.searchResultsContent.contentGroup.isVisible = false
                    binding.searchViewContent.searchResultsError.errorGroup.isVisible = true
                    binding.searchViewContent.searchNoResults.noResultsGroup.isVisible = false
                }
                SearchFragmentViewModel.SearchResultState.NoResults -> {
                    binding.searchViewContent.searchInvitation.invitationGroup.isVisible = false
                    binding.searchViewContent.searchResultsLoading.loadingGroup.isVisible = false
                    binding.searchViewContent.searchResultsContent.contentGroup.isVisible = false
                    binding.searchViewContent.searchResultsError.errorGroup.isVisible = false
                    binding.searchViewContent.searchNoResults.noResultsGroup.isVisible = true
                }
            }
        }
        viewModel.contentState.observe(viewLifecycleOwner) {
            adapter.submitList(it.slice(0 until min(it.size, pageSize)))
        }
        viewModel.fillDataFromCache(binding)
        binding.searchView.apply {
            editText.addTextChangedListener {
                viewModel.loadSearchResultsAndSaveToCache(input = it?.toString().orEmpty())
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_suggest -> {
                        binding.searchView.editText.clearFocus()
                        viewModel.clearCachedSelectedItem()
                        viewModel.getBottomSheetFragmentToOpen().apply {
                            show(
                                this@SearchFragment.requireActivity().supportFragmentManager,
                                this::class.simpleName
                            )
                        }
                        true
                    }
                    else -> false
                }
            }
            inflateMenu(R.menu.search_view)
            show()
        }
    }
}