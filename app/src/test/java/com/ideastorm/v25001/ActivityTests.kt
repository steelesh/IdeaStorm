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

    @Test
    fun 'Given activity data is available when I specifcy 3 participants then I should receive an activity that can be dones with three people'() {
        var activity = Activity(participants: 3)
        Assert.assertTrue(activity.participants.equals(3))
    }

    @Test
    fun 'Given activity data is available when I specify a social activity then I should receive a social activity'() {
        var activity = Activity(type: "social")
        Assert.assertTrue(activity.type.equals("social"))
    }

    @Test
    fun 'Given activity data is available when I specifcy a low price then I should receive an activity that is a low price'() {
        var activity = Activity(price: "low")
        Assert.assertTrue(activity.price.equals("low"))
    }
}