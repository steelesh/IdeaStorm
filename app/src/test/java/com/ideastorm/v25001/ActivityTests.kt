package com.ideastorm.v25001

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ideastorm.v25001.dto.Activity
import com.ideastorm.v25001.service.ActivityService
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ActivityTests {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private lateinit var activityService : ActivityService
    private var activity = Activity("", 0f, "",0,0f,"", 0)

    @Test
    fun `Given activity data is available when I want non-nullable data then an activity should be returned`() = runTest {
        givenActivityServiceIsInitialized()
        whenActivityDataIsReadAndParsed()
        thenTheActivityShouldNotBeNull()
    }
    private fun givenActivityServiceIsInitialized() {
        activityService = ActivityService()
    }
    private suspend fun whenActivityDataIsReadAndParsed() {
        activity = activityService.fetchActivity()
        println("\n[activity = $activity]\n")
    }
    private fun thenTheActivityShouldNotBeNull() {
        assertNotNull(activity)
    }

    @Test
    fun `Given activity data is available when I specify three participants then the activity should have three participants`() = runTest {
        givenActivityServiceIsInitialized()
        whenActivityDataIsReadAndParsed()
        thenTheActivityShouldHaveThreeParticipants()
    }
    private fun thenTheActivityShouldHaveThreeParticipants() {
        assertNotNull(activity)
        var hasThreeParticipants = false
        if (activity.participants.equals((3))) {
            hasThreeParticipants = true
        }
        assertTrue(hasThreeParticipants)
    }

    @Test
    fun `Given activity data is available when I specify a social activity then the activity should be social`() = runTest {
        givenActivityServiceIsInitialized()
        whenActivityDataIsReadAndParsed()
        thenTheActivityShouldBeSocial()
    }

    private fun thenTheActivityShouldBeSocial() {
        assertNotNull(activity)
        var isSocial = false
        if (activity.type.equals(("social"))) {
            isSocial = true
        }
        assertTrue(isSocial)
    }

    @Test
    fun `Given activity data is available when I specify low price then the activity should have a low price`() = runTest {
        givenActivityServiceIsInitialized()
        whenActivityDataIsReadAndParsed()
        thenTheActivityShouldHaveLowPrice()
    }

    private fun thenTheActivityShouldHaveLowPrice() {
        assertNotNull(activity)
        var hasLowPrice = false
        if (activity.price < 30.0) {
            hasLowPrice = true
        }
        assertTrue(hasLowPrice)
    }
}
