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
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.min

@AndroidEntryPoint
class IncomingFragment : Fragment(R.layout.fragment_incoming) {

    private lateinit var binding: FragmentIncomingBinding
    private val viewModel: IncomingFragmentViewModel by viewModels()
    private val adapter: WordsWithDefinitionAdapter = WordsWithDefinitionAdapter(
        onItemClicked = { item, position ->
            viewModel.saveSearchItemAndPosition(item, position)
            IncomingSuggestionBottomSheetDialogFragment(
                onRemoveItem = { viewModel.removeCurrentIncomingItem(binding) }
            ).show(
                parentFragmentManager,
                IncomingSuggestionBottomSheetDialogFragment::class.simpleName
            )
        }
    )
    private val pageSize = 10

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
        binding.resultsContent.wordsWithDefinitions.apply {
            adapter = this@IncomingFragment.adapter
            addOnScrollListener(viewModel.getOnScrollListener(this@IncomingFragment.adapter, pageSize))
        }
        binding.swipeToRefreshLayout.apply {
            setColorSchemeResources(R.color.grey_primary)
            setProgressBackgroundColorSchemeResource(R.color.grey_background)
            setOnRefreshListener { viewModel.refreshData() }
        }
        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.swipe_to_refresh -> {
                    binding.swipeToRefreshLayout.isRefreshing = true
                    viewModel.refreshData()
                    true
                }
                else -> false
            }
        }
        viewModel.searchResultState.observe(viewLifecycleOwner) {
            when (it) {
                IncomingFragmentViewModel.SearchResultState.Loading -> {
                    binding.resultsLoading.loadingGroup.isVisible = true
                    binding.resultsContent.contentGroup.isVisible = false
                    binding.resultsError.errorGroup.isVisible = false
                    binding.noResults.noResultsGroup.isVisible = false
                }
                IncomingFragmentViewModel.SearchResultState.Content -> {
                    binding.resultsLoading.loadingGroup.isVisible = false
                    binding.resultsContent.contentGroup.isVisible = true
                    binding.resultsError.errorGroup.isVisible = false
                    binding.noResults.noResultsGroup.isVisible = false
                    binding.swipeToRefreshLayout.isRefreshing = false
                }
                IncomingFragmentViewModel.SearchResultState.Error -> {
                    binding.resultsLoading.loadingGroup.isVisible = false
                    binding.resultsContent.contentGroup.isVisible = false
                    binding.resultsError.errorGroup.isVisible = true
                    binding.noResults.noResultsGroup.isVisible = false
                    binding.swipeToRefreshLayout.isRefreshing = false
                }
                IncomingFragmentViewModel.SearchResultState.NoResults -> {
                    binding.resultsLoading.loadingGroup.isVisible = false
                    binding.resultsContent.contentGroup.isVisible = false
                    binding.resultsError.errorGroup.isVisible = false
                    binding.noResults.noResultsGroup.isVisible = true
                    binding.swipeToRefreshLayout.isRefreshing = false
                }
            }
        }
        viewModel.contentState.observe(viewLifecycleOwner) {
            adapter.submitList(it.slice(0 until min(it.size, pageSize)))
        }
        viewModel.fillDataFromCache()
    }

}