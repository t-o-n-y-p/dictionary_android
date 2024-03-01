package com.tonyp.dictionary

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tonyp.dictionary.fragment.search.SearchFragment
import com.tonyp.dictionary.storage.get
import com.tonyp.dictionary.storage.models.UserPreferences
import com.tonyp.dictionary.storage.models.UserRole
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    @SecurePreferences
    lateinit var securePreferences: SharedPreferences

    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        menu = findViewById<BottomNavigationView>(R.id.bottom_navigation).menu
        securePreferences.get<UserPreferences>()?.adjustMenu()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SearchFragment())
            .commit()
        securePreferences.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            key
                .takeIf { it == UserPreferences::class.simpleName }
                ?.let { sharedPreferences.get<UserPreferences>() }
                ?.adjustMenu()
        }
    }

    private fun UserPreferences.adjustMenu() =
        takeIf { it.roles.contains(UserRole.ADMIN) }
            ?.let { menu.findItem(R.id.incoming).isVisible = true }
            ?: let { menu.findItem(R.id.incoming).isVisible = false }
}