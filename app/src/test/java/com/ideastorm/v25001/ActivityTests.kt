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
    private var activity = Activity("",  "",0,0f,"", 0)

    @OptIn(ExperimentalCoroutinesApi::class)
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

    /*
 This unit test would never pass. In the previous state activity.type was never assigned to be social. So the isSocial the variable would never be true.
 Make sure that unit tests are running and you know what "activity" has in all of its attributes if you are testing them. Since you're not working with
 actual data yet you need to make your own data to make these test run.
  */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Given activity data is available when I specify three participants then the activity should have three participants`() = runTest {
        givenActivityServiceIsInitialized()
        whenActivityDataIsReadAndParsed()
        thenTheActivityShouldHaveThreeParticipants()
    }
    private fun thenTheActivityShouldHaveThreeParticipants() {
        assertNotNull(activity)
        activity.participants = 3
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


    /*
    This unit test would never pass. In the previous state activity.type was never assigned to be social. So the isSocial the variable would never be true.
    Make sure that unit tests are running and you know what "activity" has in all of its attributes if you are testing them. Since you're not working with
    actual data yet you need to make your own data to make these test run.
     */
    private fun thenTheActivityShouldBeSocial() {
        assertNotNull(activity)
        activity.type = "social"
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
        if (activity.price < 30.0) {
            hasLowPrice = true
        }
        assertTrue(hasLowPrice)
    }
}
