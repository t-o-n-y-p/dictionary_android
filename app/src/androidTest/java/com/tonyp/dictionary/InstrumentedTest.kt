package com.tonyp.dictionary

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.tonyp.dictionary.recyclerview.word.WordsItemViewHolder
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class InstrumentedTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun submitDefinitionOnly() {
        onView(withId(R.id.search_view))
            .perform(typeTextIntoSearchView("трава"))
        onView(withId(R.id.words))
            .check(
                matches(
                    allOf(
                        atRecyclerPosition(0, hasDescendant(withText("трава"))),
                        atRecyclerPosition(1, hasDescendant(withText("обвал")))
                    )
                )
            )
            .perform(actionOnItemAtPosition<WordsItemViewHolder>(0, click()))
        onView(withId(R.id.word_text))
            .check(matches(withText("трава")))
        onView(withId(R.id.definition_text))
            .check(matches(withText("1. о чем-н. не имеющем вкуса, безвкусном (разг.)\n2. травянистые растения, обладающие лечебными свойствами, входящие в лекарственные сборы")))
        onView(withId(R.id.add_button))
            .check(matches(isEnabled()))
    }
}