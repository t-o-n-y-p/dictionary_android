package com.tonyp.dictionary

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.FragmentTransaction
import com.tonyp.dictionary.databinding.ActivityMainBinding
import com.tonyp.dictionary.fragment.FragmentResultConstants
import com.tonyp.dictionary.fragment.incoming.IncomingFragment
import com.tonyp.dictionary.fragment.profile.ProfileFragment
import com.tonyp.dictionary.fragment.recent.RecentFragment
import com.tonyp.dictionary.fragment.search.SearchFragment
import com.tonyp.dictionary.fragment.showLogInAlert
import com.tonyp.dictionary.storage.get
import com.tonyp.dictionary.storage.models.UserPreferences
import com.tonyp.dictionary.storage.models.UserRole
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject @SecurePreferences lateinit var securePreferences: SharedPreferences
    @Inject lateinit var cache: WizardCache

    private lateinit var menu: Menu
    private lateinit var binding: ActivityMainBinding

    private val fragmentResultListener =
        FragmentResultListener { fragmentKey, bundle ->
            bundle.getString(FragmentResultConstants.LOGIN_STATUS)
                .takeIf { it == FragmentResultConstants.LOGGED_OUT }
                ?.let {
                    when (fragmentKey) {
                        FragmentResultConstants.INCOMING_SUGGESTION_BOTTOM_SHEET_DIALOG_FRAGMENT -> {
                            menu.performIdentifierAction(R.id.search, 0)
                            binding.bottomNavigation.selectedItemId = R.id.search
                            showLogInAlert()
                        }
                        else -> showLogInAlert()
                    }
                }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigation.apply {
            this@MainActivity.menu = menu
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.search -> switchToFragment(SearchFragment::class)
                    R.id.recent -> switchToFragment(RecentFragment::class)
                    R.id.incoming -> switchToFragment(IncomingFragment::class)
                    R.id.profile -> switchToFragment(ProfileFragment::class)
                    else -> false
                }
            }
            setOnItemReselectedListener {}
        }
        securePreferences.apply {
            get<UserPreferences>()?.adjustMenu()
            registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
                key
                    .takeIf { it == UserPreferences::class.simpleName }
                    ?.let { sharedPreferences.get<UserPreferences>() }
                    .adjustMenu()
            }
        }
        FragmentResultConstants.POSTING_DATA_FRAGMENTS.forEach { fragmentKey ->
            supportFragmentManager.setFragmentResultListener(
                fragmentKey,
                this@MainActivity,
                fragmentResultListener
            )
        }
        switchToFragment(cache.currentFragment)
    }

    private fun switchToFragment(kClass: KClass<out Fragment>): Boolean {
        cache.currentFragment = kClass
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .setCustomAnimations(R.anim.slide_up, R.anim.no_animation)
            .replace(R.id.fragment_container, kClass.createInstance())
            .commit()
        return true
    }

    private fun UserPreferences?.adjustMenu() {
        menu.findItem(R.id.incoming).isVisible = this?.roles?.contains(UserRole.ADMIN) ?: false
    }
}