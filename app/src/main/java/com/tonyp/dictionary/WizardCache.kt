package com.tonyp.dictionary

import com.tonyp.dictionary.api.v1.models.MeaningResponseFullObject
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class WizardCache @Inject constructor() {

    var searchInput: String = ""
    var searchResults: List<MeaningResponseFullObject> = emptyList()
    var currentlySelectedWord: String = ""
    var currentlySelectedSearchResults: List<MeaningResponseFullObject> = emptyList()

}