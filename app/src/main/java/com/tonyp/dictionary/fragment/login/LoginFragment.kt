package com.tonyp.dictionary.fragment.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tonyp.dictionary.R
import com.tonyp.dictionary.databinding.FragmentLoginBinding
import com.tonyp.dictionary.fragment.FragmentResultConstants
import com.tonyp.dictionary.fragment.setFragmentResult
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
        binding.logInButton.isEnabled = false
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
                LoginFragmentViewModel.LoginState.Success -> {
                    setFragmentResult(
                        FragmentResultConstants.LOGIN_FRAGMENT,
                        FragmentResultConstants.LOGIN_STATUS,
                        FragmentResultConstants.SUCCESS
                    )
                    findNavController().navigate(viewModel.getLoggedInAction())
                }
                LoginFragmentViewModel.LoginState.InvalidCredentials -> {
                    binding.logInButton.isVisible = true
                    binding.loggingInButton.isVisible = false
                    binding.alertPasswordTextInputLayout.error =
                        getString(R.string.incorrect_password)
                }
                LoginFragmentViewModel.LoginState.UserIsBanned -> {
                    binding.logInButton.isVisible = true
                    binding.loggingInButton.isVisible = false
                    binding.alertUsernameTextInputLayout.error =
                        getString(R.string.this_user_is_blocked)
                }
                LoginFragmentViewModel.LoginState.Error -> {
                    setFragmentResult(
                        FragmentResultConstants.LOGIN_FRAGMENT,
                        FragmentResultConstants.LOGIN_STATUS,
                        FragmentResultConstants.UNEXPECTED_ERROR
                    )
                }
            }
        }
        binding.alertUsernameTextInput.addTextChangedListener {
            binding.alertUsernameTextInputLayout.error = null
            setLoginButtonState()
        }
        binding.alertPasswordTextInput.addTextChangedListener {
            binding.alertPasswordTextInputLayout.error = null
            setLoginButtonState()
        }
        binding.logInButton.setOnClickListener {
            viewModel.login(
                username = binding.alertUsernameTextInput.text.toString(),
                password = binding.alertPasswordTextInput.text.toString()
            )
        }
    }

    private fun setLoginButtonState() =
        (binding.alertUsernameTextInput.text.isNullOrBlank()
                || binding.alertPasswordTextInput.text.isNullOrBlank())
            .let { binding.logInButton.isEnabled = !it }

}