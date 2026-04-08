package dev.canem.nexusmoderandi.ui.rejectedcalls

import dev.canem.nexusmoderandi.data.entity.RejectedCall
import dev.canem.nexusmoderandi.data.repository.RejectedCallRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RejectedCallsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: RejectedCallRepository
    private lateinit var viewModel: RejectedCallsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        every { repository.getRejectedCalls() } returns flowOf(
            listOf(
                RejectedCall(id = 1, phoneNumber = "+15559999999", timestamp = 1000),
                RejectedCall(id = 2, phoneNumber = "+15558888888", timestamp = 2000)
            )
        )
        viewModel = RejectedCallsViewModel(repository)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `rejectedCalls emits repository data`() = runTest {
        val job = launch(testDispatcher) { viewModel.rejectedCalls.collect {} }
        val calls = viewModel.rejectedCalls.value
        assertEquals(2, calls.size)
        assertEquals("+15559999999", calls[0].phoneNumber)
        job.cancel()
    }

    @Test
    fun `clearAll calls repository`() = runTest {
        viewModel.clearAll()
        coVerify { repository.clearAll() }
    }
}
