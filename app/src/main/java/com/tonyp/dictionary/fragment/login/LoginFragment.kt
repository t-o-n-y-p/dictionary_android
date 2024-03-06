package com.tonyp.dictionary.fragment.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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
                    try {
                        findNavController().navigate(viewModel.getLoggedInAction())
                    } catch (_: IllegalStateException) {
                        (parentFragment as? BottomSheetDialogFragment)!!.dismiss()
                    }
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
                    (parentFragment as? BottomSheetDialogFragment)?.dismiss()
                        ?: (parentFragment?.parentFragment as? BottomSheetDialogFragment)!!.dismiss()
                    Toast.makeText(context, R.string.an_error_has_occurred, Toast.LENGTH_SHORT).show()
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
                username = binding.alertUsernameTextInput.text?.toString().orEmpty(),
                password = binding.alertPasswordTextInput.text?.toString().orEmpty()
            )
        }
    }

    private fun setLoginButtonState() =
        (binding.alertUsernameTextInput.text?.isBlank() ?: true
                || binding.alertPasswordTextInput.text?.isBlank() ?: true)
            .let { binding.logInButton.isEnabled = !it }

}