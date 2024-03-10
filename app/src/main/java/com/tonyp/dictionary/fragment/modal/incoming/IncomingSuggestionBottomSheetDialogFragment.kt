package com.tonyp.dictionary.fragment.modal.incoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tonyp.dictionary.R
import com.tonyp.dictionary.databinding.FragmentWordDefinitionIncomingBinding
import com.tonyp.dictionary.fragment.dismissWithToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IncomingSuggestionBottomSheetDialogFragment(
    private val onRemoveItem: () -> Unit = {}
) : BottomSheetDialogFragment(R.layout.fragment_word_definition_incoming) {

    private lateinit var binding: FragmentWordDefinitionIncomingBinding
    private val viewModel: IncomingSuggestionBottomSheetDialogFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWordDefinitionIncomingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.processingState.observe(viewLifecycleOwner) {
            when (it) {
                IncomingSuggestionBottomSheetDialogFragmentViewModel.ProcessingState.NotSet -> {
                    binding.actionButtons.isVisible = true
                    binding.processingButton.isVisible = false
                }
                IncomingSuggestionBottomSheetDialogFragmentViewModel.ProcessingState.Loading -> {
                    binding.actionButtons.isVisible = false
                    binding.processingButton.isVisible = true
                }
                IncomingSuggestionBottomSheetDialogFragmentViewModel.ProcessingState.Approved -> {
                    dismissWithToast(R.string.the_proposition_has_been_approved)
                    onRemoveItem()
                }
                IncomingSuggestionBottomSheetDialogFragmentViewModel.ProcessingState.Declined -> {
                    dismissWithToast(R.string.the_proposition_has_been_declined)
                    onRemoveItem()
                }
                IncomingSuggestionBottomSheetDialogFragmentViewModel.ProcessingState.AlreadyApproved -> {
                    dismissWithConcurrentModificationAlert(
                        R.string.the_proposition_has_been_already_approved
                    )
                }
                IncomingSuggestionBottomSheetDialogFragmentViewModel.ProcessingState.AlreadyDeclined -> {
                    dismissWithConcurrentModificationAlert(
                        R.string.the_proposition_has_been_already_declined
                    )
                }
                IncomingSuggestionBottomSheetDialogFragmentViewModel.ProcessingState.Error -> {
                    dismissWithToast(R.string.an_unexpected_error_occurred)
                }
            }
        }
        binding.approveButton.setOnClickListener {
            viewModel.approveSuggestion()
        }
        binding.declineButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext(), R.style.Alert)
                .setTitle(getString(R.string.decline_this_suggestion))
                .setMessage(getString(R.string.this_action_cannot_be_undone))
                .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                    viewModel.declineSuggestion()
                }
                .show()
        }
        viewModel.fillDataFromCache(binding)
    }

    private fun dismissWithConcurrentModificationAlert(messageId: Int) {
        dismiss()
        MaterialAlertDialogBuilder(requireContext(), R.style.Alert)
            .setTitle(getString(R.string.concurrent_modification))
            .setMessage(getString(messageId))
            .setPositiveButton(getString(R.string.ok)) { _, _ -> }
            .show()
        onRemoveItem()
    }

}