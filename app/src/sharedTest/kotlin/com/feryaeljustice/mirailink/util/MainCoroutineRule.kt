package com.feryaeljustice.mirailink.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Replaces [Dispatchers.Main] with dispatchers driven by one virtual-time scheduler.
 *
 * Use [testDispatcher] for deterministic queued work and
 * [testDispatcherUnconfined] only when eager entry into a coroutine is part of the test.
 * Pass [scheduler] to runTest when the code under test creates work before the test body.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainCoroutineRule(
    val scheduler: TestCoroutineScheduler = TestCoroutineScheduler(),
) : TestWatcher() {
    val testDispatcher: TestDispatcher = StandardTestDispatcher(scheduler)
    val testDispatcherUnconfined: TestDispatcher = UnconfinedTestDispatcher(scheduler)

    /** Installs the deterministic dispatcher before each test. */
    override fun starting(description: Description?) {
        Dispatchers.setMain(testDispatcher)
    }

    /** Restores the real main dispatcher after each test. */
    override fun finished(description: Description?) {
        Dispatchers.resetMain()
    }
}
