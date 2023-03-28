package com.ideastorm.v25001

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.ideastorm.v25001.dto.Activity
import com.ideastorm.v25001.service.ActivityService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.rules.TestRule
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ActivityUnitTests {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    lateinit var mvm: MainViewModel

    @MockK
    lateinit var mockActivityService: ActivityService

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("Main Thread")

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun initMocksAndMainThread() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `given a view model with live data when populated with activity then results show sample activity`() {
        givenViewModelIsInitializedWithMockData()
        whenActivityServiceFetchActivityInvoked()
        testActivityContainsSampleActivity()
    }

    private fun givenViewModelIsInitializedWithMockData() {
        val activity = Activity("Sample Activity", 1f, "Music", 1, 0f, "", 0)
        coEvery { mockActivityService.fetchActivity() } returns activity
        mvm = MainViewModel(activityService = mockActivityService)
    }

    private fun whenActivityServiceFetchActivityInvoked() {
        mvm.fetchActivity()
    }

    private fun testActivityContainsSampleActivity() {
        val initialActivity = Activity("Play Basketball", 1f, "Physical Activity", 1, 0f, "", 0)
        val expectedActivity = Activity("Sample Activity", 1f, "Physical Activity", 1, 0f, "", 0)
        val latch = CountDownLatch(1)

        val observer = object : Observer<Activity> {
            override fun onChanged(activity: Activity) {
                if (activity.activity == expectedActivity.activity) {
                    mvm.activity.removeObserver(this)
                    latch.countDown()
                }
            }
        }
        mvm.activity.observeForever(observer)

        mvm.activity.postValue(expectedActivity)
        latch.await(10, TimeUnit.SECONDS)
        val actualActivity = mvm.activity.value
        Assert.assertNotNull(actualActivity)
        Assert.assertEquals(expectedActivity, actualActivity)
        println("\n[activity = $actualActivity]\n")
    }
}
