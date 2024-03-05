package com.tonyp.dictionary.fragment.profile

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tonyp.dictionary.SecurePreferences
import com.tonyp.dictionary.databinding.FragmentProfileBinding
import com.tonyp.dictionary.storage.cleanup
import com.tonyp.dictionary.storage.get
import com.tonyp.dictionary.storage.models.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileFragmentViewModel @Inject constructor(
    @SecurePreferences private val securePreferences: SharedPreferences
) : ViewModel() {

    private val mLoggedInState = MutableLiveData<LoggedInState>()
    val loggedInState: LiveData<LoggedInState> get() = mLoggedInState

    private lateinit var listener: (SharedPreferences, String?) -> Unit

    private fun getSecurePreferencesListener(
        binding: FragmentProfileBinding
    ): (SharedPreferences, String?) -> Unit =
        { sharedPreferences, key ->
            key
                .takeIf { it == UserPreferences::class.simpleName }
                ?.let { fillDataFromPreferences(binding, sharedPreferences) }
        }

    fun fillDataFromPreferences(
        binding: FragmentProfileBinding,
        securePreferences: SharedPreferences = this.securePreferences
    ) =
        securePreferences.get<UserPreferences>()
            ?.takeIf { it.isLoggedIn() }
            ?.let {  prefs ->
                binding.fragmentProfileLoggedIn.usernameText.text = prefs.username
                binding.fragmentProfileLoggedIn.groupsText.text =
                    prefs.roles.joinToString(separator = "\n") { "• ${it.value}" }
                mLoggedInState.value= LoggedInState.LoggedIn
            }
            ?: let {
                mLoggedInState.value = LoggedInState.LoggedOut
            }

    fun registerUserPreferencesListener(binding: FragmentProfileBinding) {
        listener = getSecurePreferencesListener(binding)
        securePreferences.registerOnSharedPreferenceChangeListener(listener)
    }


    fun unregisterUserPreferencesListener() =
        securePreferences.unregisterOnSharedPreferenceChangeListener(listener)

    fun logout() = securePreferences.cleanup<UserPreferences>()

    sealed class LoggedInState {

        data object LoggedIn: LoggedInState()

        data object LoggedOut: LoggedInState()

    }

}