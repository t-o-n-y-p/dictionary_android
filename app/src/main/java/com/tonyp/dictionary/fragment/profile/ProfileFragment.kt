package com.tonyp.dictionary.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tonyp.dictionary.R
import com.tonyp.dictionary.databinding.FragmentProfileBinding
import com.tonyp.dictionary.fragment.modal.login.LoginOnlyBottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loggedInState.observe(viewLifecycleOwner) {
            when (it) {
                ProfileFragmentViewModel.LoggedInState.LoggedIn -> {
                    binding.fragmentProfileLoggedIn.loggedInGroup.isVisible = true
                    binding.fragmentProfileLoggedOut.loggedOutGroup.isVisible = false
                }
                ProfileFragmentViewModel.LoggedInState.LoggedOut -> {
                    binding.fragmentProfileLoggedIn.loggedInGroup.isVisible = false
                    binding.fragmentProfileLoggedOut.loggedOutGroup.isVisible = true
                }
            }
        }
        binding.fragmentProfileLoggedOut.proceedButton.setOnClickListener {
            LoginOnlyBottomSheetDialogFragment().show(
                requireActivity().supportFragmentManager,
                LoginOnlyBottomSheetDialogFragment::class.simpleName
            )
        }
        binding.fragmentProfileLoggedIn.logOutButton.setOnClickListener {
            viewModel.logout()
        }
        viewModel.fillDataFromPreferences(binding)
        viewModel.registerUserPreferencesListener(binding)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.unregisterUserPreferencesListener()
    }

}