package com.tonyp.dictionary

import androidx.fragment.app.Fragment
import com.tonyp.dictionary.api.v1.models.MeaningResponseFullObject
import com.tonyp.dictionary.fragment.search.SearchFragment
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject
import kotlin.reflect.KClass

@ActivityRetainedScoped
class WizardCache @Inject constructor() {

    var currentFragment: KClass<out Fragment> = SearchFragment::class
    var searchInput: String = ""
    var searchResults: List<MeaningResponseFullObject> = emptyList()
    var incomingSearchResults: List<MeaningResponseFullObject> = emptyList()
    var currentlySelectedWord: String = ""
    var currentlySelectedSearchResults: List<MeaningResponseFullObject> = emptyList()

}