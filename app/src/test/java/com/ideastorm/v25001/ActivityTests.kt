package com.ideastorm.v25001

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ideastorm.v25001.dto.Activity
import com.ideastorm.v25001.service.ActivityService
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    /**
     * Test case for returning non-nullable data
     * @author Steele Shreve
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Given activity data is available when I want non-nullable data then an activity should be returned`() = runTest {
        givenActivityServiceIsInitialized()
        whenActivityDataIsReadAndParsed()
        thenTheActivityShouldNotBeNull()
    }

    /**
     * Function that checks if the activity service layer is initialized
     * @author Steele Shreve
     */
    private fun givenActivityServiceIsInitialized() {
        activityService = ActivityService()
    }

    /**
     * Function that checks if the activity data is read and parsed
     * @author Steele Shreve
     */
    private suspend fun whenActivityDataIsReadAndParsed() {
        activity = activityService.fetchActivity()
        println("\n[activity = $activity]\n")
    }

    /**
     * Function that checks that the activity data is not null
     * @author Steele Shreve
     */
    private fun thenTheActivityShouldNotBeNull() {
        assertNotNull(activity)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Given activity data is available when I specify three participants then the activity should have three participants`() = runTest {
        givenActivityServiceIsInitialized()
        whenActivityDataIsReadAndParsed()
        thenTheActivityShouldHaveThreeParticipants()
    }
    private fun thenTheActivityShouldHaveThreeParticipants() {
        assertNotNull(activity)
        var hasThreeParticipants = false
        if (activity.participants == (3)) {
            hasThreeParticipants = true
        }
        assertTrue(hasThreeParticipants)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Given activity data is available when I specify a social activity then the activity should be social`() = runTest {
        givenActivityServiceIsInitialized()
        whenActivityDataIsReadAndParsed()
        thenTheActivityShouldBeSocial()
    }

    private fun thenTheActivityShouldBeSocial() {
        assertNotNull(activity)
        var isSocial = false
        if (activity.type == ("social")) {
            isSocial = true
        }
        assertTrue(isSocial)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Given activity data is available when I specify low price then the activity should have a low price`() = runTest {
        givenActivityServiceIsInitialized()
        whenActivityDataIsReadAndParsed()
        thenTheActivityShouldHaveLowPrice()
    }

    private fun thenTheActivityShouldHaveLowPrice() {
        assertNotNull(activity)
        var hasLowPrice = false
        if (activity.price == (0f)) {
            hasLowPrice = true
        }
        assertTrue(hasLowPrice)
    }
}
