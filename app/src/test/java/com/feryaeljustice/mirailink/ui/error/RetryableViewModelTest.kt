package com.feryaeljustice.mirailink.ui.error

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/** Lifecycle and replacement semantics for retry callbacks. */
class RetryableViewModelTest {
    /** Verifies that no action is safe when an error has no registered recovery. */
    @Test
    fun `perform error action is no-op before registration`() {
        // Given
        val viewModel = TestRetryableViewModel()
        var calls = 0

        // When
        viewModel.performErrorAction()

        // Then
        assertThat(calls).isEqualTo(0)
    }

    /** Verifies that the latest registered recovery replaces the previous one. */
    @Test
    fun `perform error action invokes latest registered recovery`() {
        // Given
        val viewModel = TestRetryableViewModel()
        var firstCalls = 0
        var secondCalls = 0
        viewModel.register { firstCalls++ }
        viewModel.register { secondCalls++ }

        // When
        viewModel.performErrorAction()

        // Then
        assertThat(firstCalls).isEqualTo(0)
        assertThat(secondCalls).isEqualTo(1)
    }

    /** Verifies that lifecycle cleanup releases callbacks and captured parameters. */
    @Test
    fun `clearing view model removes recovery action`() {
        // Given
        val viewModel = TestRetryableViewModel()
        var calls = 0
        viewModel.register { calls++ }

        // When
        viewModel.clearForTest()
        viewModel.performErrorAction()

        // Then
        assertThat(calls).isEqualTo(0)
    }


    @Test
    fun equalErrorsFromDifferentOperationsRetainDifferentRecoveries() {
        val vm = TestRetryableViewModel()
        var register = 0
        var login = 0
        val first = vm.fail { register++ }
        val second = vm.fail { login++ }
        assertThat(first).isEqualTo(second)
        vm.register { error("An unrelated operation must not replace these recoveries") }
        vm.recoveryFor(first)()
        vm.recoveryFor(second)()
        assertThat(register).isEqualTo(1)
        assertThat(login).isEqualTo(1)
    }

    @Test
    fun clearingRemovesRecoveriesForPreviouslyDisplayedErrors() {
        val vm = TestRetryableViewModel()
        var calls = 0
        val error = vm.fail { calls++ }
        vm.clearForTest()
        vm.recoveryFor(error)()
        assertThat(calls).isEqualTo(0)
    }

    /** Exposes protected lifecycle APIs only to this unit test. */
    private class TestRetryableViewModel : RetryableViewModel() {
        fun register(action: () -> Unit) = setRecoveryAction(action)
        fun fail(action: () -> Unit) = errorFor(com.feryaeljustice.mirailink.domain.error.DataError.Network.TIMEOUT, action)

        fun clearForTest() = onCleared()
    }
}
