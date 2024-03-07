package com.tonyp.dictionary.fragment.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tonyp.dictionary.R
import com.tonyp.dictionary.SecurePreferences
import com.tonyp.dictionary.WizardCache
import com.tonyp.dictionary.service.dto.auth.UserGroup
import com.tonyp.dictionary.storage.mappers.UserPreferencesMapper
import com.tonyp.dictionary.storage.put
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginFragmentViewModel @Inject constructor(
    @SecurePreferences private val securePreferences: SharedPreferences,
    private val useCase: LoginFragmentUseCase,
    private val cache: WizardCache
) : ViewModel() {

    private val mLoginState = MutableLiveData<LoginState>(LoginState.NotSet)
    val loginState: LiveData<LoginState> get() = mLoginState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                mLoginState.value = LoginState.Loading
                val tokenResponse =
                    withContext(Dispatchers.IO) { useCase.login(username, password) }
                        .getOrThrow()
                securePreferences.put(
                    UserPreferencesMapper.map(tokenResponse)
                )
                val userInfoResponse =
                    withContext(Dispatchers.IO) { useCase.getUserInfo() }
                        .getOrThrow()
                        .takeUnless { it.groups?.contains(UserGroup.BANNED) ?: true }
                        ?: let {
                            mLoginState.value = LoginState.UserIsBanned
                            return@launch
                        }
                securePreferences.put(
                    UserPreferencesMapper.map(tokenResponse, userInfoResponse)
                )
                mLoginState.value = LoginState.Success
            } catch (_: SecurityException) {
                mLoginState.value = LoginState.InvalidCredentials
            } catch (_: Throwable) {
                mLoginState.value = LoginState.Error
            }
        }
    }

    fun getLoggedInAction() =
        cache.currentlySelectedItem.value
            .takeIf { it.isBlank() }
            ?.let { R.id.go_to_word_and_definition_proposition }
            ?: R.id.go_to_definition_proposition

    sealed class LoginState {

        data object NotSet: LoginState()

        data object Loading: LoginState()

        data object Success: LoginState()

        data object InvalidCredentials : LoginState()

        data object UserIsBanned : LoginState()

        data object Error : LoginState()
    }

}