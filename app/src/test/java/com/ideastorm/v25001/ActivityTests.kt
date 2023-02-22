package com.ideastorm.v25001

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ideastorm.v25001.dto.Activity
import com.ideastorm.v25001.service.ActivityService
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ActivityTests {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private lateinit var activityService : ActivityService
    private var activity = Activity("", 0f, "",0,0f,"", 0)

    @Test
    fun `Given activity data is available when I want non-nullable data then should return an activity`() = runTest {
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
}
