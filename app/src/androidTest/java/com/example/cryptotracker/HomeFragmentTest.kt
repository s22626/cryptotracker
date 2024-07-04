package com.example.cryptotracker

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cryptotracker.ui.home.HomeFragment
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    @Test
    fun testLanguageSwitch() {
        val scenario = launchFragmentInContainer<HomeFragment>()
        val initialLocale = Locale.getDefault().language

        TimeUnit.SECONDS.sleep(2)
        onView(withId(R.id.totalValue)).check(matches(withText(getExpectedString(initialLocale))))

        performLanguageSwitch()
        scenario.recreate()
        onView(withId(R.id.totalValue)).check(matches(withText(getExpectedString("pl"))))

        performLanguageSwitch()
        scenario.recreate()
        onView(withId(R.id.totalValue)).check(matches(withText(getExpectedString(initialLocale))))
    }

    private fun performLanguageSwitch() {
        onView(withId(R.id.languageSwitch)).perform(click())
    }

    private fun getExpectedString(language: String): String {
        return when (language) {
            "pl" -> "Suma: 0,00 PLN"
            "en" -> "Total Value: 0.0 $"
            else -> throw IllegalArgumentException("Unsupported language: $language")
        }
    }
}