package com.tonyp.dictionary

import androidx.fragment.app.Fragment
import com.tonyp.dictionary.fragment.search.SearchFragment
import com.tonyp.dictionary.recyclerview.definition.WordsWithDefinitionItem
import com.tonyp.dictionary.recyclerview.word.WordsItem
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject
import kotlin.reflect.KClass

@ActivityRetainedScoped
class WizardCache @Inject constructor() {

    var currentFragment: KClass<out Fragment> = SearchFragment::class
    var searchInput: String = ""
    var items: List<WordsItem> = emptyList()
    var currentlySelectedItem: WordsItem = WordsItem()
    var incomingItems: MutableList<WordsWithDefinitionItem> = mutableListOf()
    var currentlySelectedIncomingItem: WordsWithDefinitionItem = WordsWithDefinitionItem()
    var currentlySelectedIncomingItemPosition: Int = 0

}