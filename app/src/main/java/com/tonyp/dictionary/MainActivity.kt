package com.tonyp.dictionary

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tonyp.dictionary.fragment.incoming.IncomingFragment
import com.tonyp.dictionary.fragment.profile.ProfileFragment
import com.tonyp.dictionary.fragment.recent.RecentFragment
import com.tonyp.dictionary.fragment.search.SearchFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        menu = bottomNavigationView.menu
        securePreferences.get<UserPreferences>()?.adjustMenu()
        switchToFragment(cache.currentFragment)
        securePreferences.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            key
                .takeIf { it == UserPreferences::class.simpleName }
                ?.let { sharedPreferences.get<UserPreferences>() }
                .adjustMenu()
        }
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.search -> switchToFragment(SearchFragment::class)
                R.id.recent -> switchToFragment(RecentFragment::class)
                R.id.incoming -> switchToFragment(IncomingFragment::class)
                R.id.profile -> switchToFragment(ProfileFragment::class)
                else -> false
            }
        }
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

    private fun UserPreferences?.adjustMenu() =
        takeIf { it?.roles?.contains(UserRole.ADMIN) ?: false }
            ?.let { menu.findItem(R.id.incoming).isVisible = true }
            ?: let {
                menu.findItem(R.id.incoming).isVisible = false
                cache.currentFragment
                    .takeIf { it == IncomingFragment::class }
                    ?.let { menu.performIdentifierAction(R.id.search, 0) }
            }
}