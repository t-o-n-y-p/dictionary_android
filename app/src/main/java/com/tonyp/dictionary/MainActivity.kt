package com.tonyp.dictionary

import android.os.Bundle
import androidx.activity.viewModels
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
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    private val fragmentResultListener =
        FragmentResultListener { fragmentKey, bundle ->
            bundle.getString(FragmentResultConstants.LOGIN_STATUS)
                .takeIf { it == FragmentResultConstants.LOGGED_OUT }
                ?.let {
                    when (fragmentKey) {
                        FragmentResultConstants.INCOMING_SUGGESTION_BOTTOM_SHEET_DIALOG_FRAGMENT ->
                            binding.bottomNavigation.apply {
                                menu.performIdentifierAction(R.id.search, 0)
                                selectedItemId = R.id.search
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
        viewModel.initSecurePreferencesListener(binding)
        FragmentResultConstants.POSTING_DATA_FRAGMENTS.forEach { fragmentKey ->
            supportFragmentManager.setFragmentResultListener(
                fragmentKey,
                this@MainActivity,
                fragmentResultListener
            )
        }
        switchToFragment(viewModel.getCurrentFragment())
        binding.bottomNavigation.apply {
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
    }

    private fun switchToFragment(kClass: KClass<out Fragment>): Boolean {
        viewModel.saveFragmentToCache(kClass)
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .setCustomAnimations(R.anim.slide_up, R.anim.no_animation)
            .replace(R.id.fragment_container, kClass.createInstance())
            .commit()
        return true
    }
}