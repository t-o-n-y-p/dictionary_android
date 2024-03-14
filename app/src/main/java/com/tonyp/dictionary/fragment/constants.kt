package com.tonyp.dictionary.fragment

object FragmentResultConstants {

    const val LOGIN_FRAGMENT = "LoginFragment"
    const val DEFINITION_SUGGESTION_FRAGMENT = "DefinitionSuggestionFragment"
    const val WORD_WITH_DEFINITION_SUGGESTION_FRAGMENT = "WordWithDefinitionSuggestionFragment"
    const val WORD_DEFINITION_BOTTOM_SHEET_DIALOG_FRAGMENT =
        "WordDefinitionBottomSheetDialogFragment"
    const val INCOMING_SUGGESTION_BOTTOM_SHEET_DIALOG_FRAGMENT =
        "IncomingSuggestionBottomSheetDialogFragment"
    const val LOGIN_WITH_SUGGESTION_BOTTOM_SHEET_DIALOG_FRAGMENT =
        "LoginWithSuggestionBottomSheetDialogFragment"
    const val WORD_SUGGESTION_BOTTOM_SHEET_DIALOG_FRAGMENT =
        "WordSuggestionBottomSheetDialogFragment"
    const val LOGIN_STATUS = "loginStatus"
    const val SUGGESTION_STATUS = "suggestionStatus"
    const val SUCCESS = "success"
    const val LOGGED_OUT = "loggedOut"
    const val UNEXPECTED_ERROR = "unexpectedError"

    val POSTING_DATA_FRAGMENTS = listOf(
        WORD_DEFINITION_BOTTOM_SHEET_DIALOG_FRAGMENT,
        INCOMING_SUGGESTION_BOTTOM_SHEET_DIALOG_FRAGMENT,
        LOGIN_WITH_SUGGESTION_BOTTOM_SHEET_DIALOG_FRAGMENT,
        WORD_SUGGESTION_BOTTOM_SHEET_DIALOG_FRAGMENT
    )
}

object ServerErrorConstants {

    const val ALREADY_EXISTS = "ALREADY_EXISTS"
    const val NOT_FOUND = "NOT_FOUND"
    const val CONCURRENT_MODIFICATION = "CONCURRENT_MODIFICATION"

}