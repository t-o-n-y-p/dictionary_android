package com.tonyp.dictionary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tonyp.dictionary.fragment.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SearchFragment())
            .commit()
    }
}