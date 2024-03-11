package com.tonyp.dictionary

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.BoundedMatcher
import com.google.android.material.search.SearchView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.anything

@Suppress("UNCHECKED_CAST")
fun typeTextIntoSearchView(text: String): ViewAction =
    object : ViewAction {
        override fun getDescription(): String = "typeTextIntoSearchView"

        override fun getConstraints(): Matcher<View> = anything() as Matcher<View>

        override fun perform(uiController: UiController?, view: View?) =
            (view as SearchView).setText(text)
    }

fun atRecyclerPosition(position: Int, matcher: Matcher<View>) =
    object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description?) {}

        override fun matchesSafely(item: RecyclerView): Boolean =
            matcher.matches(item.findViewHolderForAdapterPosition(position)!!.itemView)
    }