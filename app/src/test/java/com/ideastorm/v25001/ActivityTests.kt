package com.ideastorm.v25001

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule
import org.junit.rules.TestRule

class ActivityTests {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
}