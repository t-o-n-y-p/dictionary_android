package com.tonyp.dictionary.fragment.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tonyp.dictionary.R
import com.tonyp.dictionary.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loginState.observe(viewLifecycleOwner) {
            when (it) {
                LoginFragmentViewModel.LoginState.NotSet -> {
                    binding.logInButton.isVisible = true
                    binding.loggingInButton.isVisible = false
                }
                LoginFragmentViewModel.LoginState.Loading -> {
                    binding.logInButton.isVisible = false
                    binding.loggingInButton.isVisible = true
                }
                LoginFragmentViewModel.LoginState.Content -> {
                    findNavController().navigate(viewModel.getLoggedInAction())
                }
                LoginFragmentViewModel.LoginState.Error -> {
                    binding.logInButton.isVisible = true
                    binding.loggingInButton.isVisible = false
                }
            }
        }
        binding.logInButton.setOnClickListener {
            viewModel.login(
                username = binding.alertUsernameTextInput.text?.toString().orEmpty(),
                password = binding.alertPasswordTextInput.text?.toString().orEmpty()
            )
        }
    }

}