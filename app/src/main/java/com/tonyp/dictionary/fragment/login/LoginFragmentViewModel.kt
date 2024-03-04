package com.tonyp.dictionary.fragment.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tonyp.dictionary.R
import com.tonyp.dictionary.SecurePreferences
import com.tonyp.dictionary.WizardCache
import com.tonyp.dictionary.storage.mappers.UserPreferencesMapper
import com.tonyp.dictionary.storage.put
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
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
                val instant = Clock.System.now()
                val tokenResponse =
                    withContext(Dispatchers.IO) { useCase.login(username, password) }
                        .getOrNull()
                        ?: let {
                            mLoginState.value = LoginState.Error
                            return@launch
                        }
                val userInfoResponse =
                    withContext(Dispatchers.IO) {
                        useCase.getUserInfo(tokenResponse.authHeaderValue())
                    }
                        .getOrNull()
                        ?: let {
                            mLoginState.value = LoginState.Error
                            return@launch
                        }
                securePreferences.put(
                    UserPreferencesMapper.map(
                        tokenResponse,
                        userInfoResponse,
                        instant
                    )
                )
                mLoginState.value = LoginState.Content
            } catch (t: Throwable) {
                mLoginState.value = LoginState.Error
            }
        }
    }

    fun getLoggedInAction() =
        if (cache.currentlySelectedWord.isBlank())
            R.id.go_to_word_and_definition_proposition
        else
            R.id.go_to_definition_proposition

    sealed class LoginState {

        data object NotSet: LoginState()

        data object Loading: LoginState()

        data object Content: LoginState()

        data object Error : LoginState()
    }

}