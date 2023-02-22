package com.ideastorm.v25001

import android.app.Activity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ActivityTests {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var activityService: ActivityService()

    @Test
    fun 'Given activity data is available when I specifcy 3 participants then I should receive an activity that can be dones with three people'() {
        givenActivityServiceIsInitialized()
        whenActivityDataIsReadAndParsed()
        thenTheActivityShouldHaveThreeParticipants()
    }

    private fun givenActivityServiceIsInitialized() {
        activityService = ActivityService()
    }

    private fun whenActivityDataIsReadAndParsed() {
        TODO("Not yet implemented")
    }

    private fun thenTheActivityShouldHaveThreeParticipants() {
        TODO("Not yet implemented")
    }
}