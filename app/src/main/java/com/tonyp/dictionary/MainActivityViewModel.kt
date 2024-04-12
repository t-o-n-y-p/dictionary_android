package com.tonyp.dictionary

import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.tonyp.dictionary.databinding.ActivityMainBinding
import com.tonyp.dictionary.storage.get
import com.tonyp.dictionary.storage.models.UserPreferences
import com.tonyp.dictionary.storage.models.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.reflect.KClass

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @SecurePreferences private val securePreferences: SharedPreferences,
    private val cache: WizardCache
) : ViewModel() {

    fun initSecurePreferencesListener(binding: ActivityMainBinding) =
        securePreferences.apply {
            adjustMenu(binding)
            registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
                key
                    .takeIf { it == UserPreferences::class.simpleName }
                    ?.let { sharedPreferences.adjustMenu(binding) }
            }
        }

    fun getCurrentFragment() = cache.currentFragment

    fun saveFragmentToCache(kClass: KClass<out Fragment>) {
        cache.currentFragment = kClass
    }

    private fun SharedPreferences.adjustMenu(binding: ActivityMainBinding) {
        binding.bottomNavigation.menu.findItem(R.id.incoming).isVisible =
            get<UserPreferences>()?.roles?.contains(UserRole.ADMIN) ?: false
    }

}