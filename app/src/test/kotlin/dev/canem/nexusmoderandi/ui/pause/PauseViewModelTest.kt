package dev.canem.nexusmoderandi.ui.pause

import dev.canem.nexusmoderandi.service.PauseManager
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PauseViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var pauseManager: PauseManager
    private lateinit var viewModel: PauseViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        pauseManager = mockk(relaxed = true)
        every { pauseManager.pauseUntil } returns flowOf(0L)
        viewModel = PauseViewModel(pauseManager)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `pause calls pauseManager with correct duration`() = runTest {
        viewModel.pause(1)
        coVerify { pauseManager.pauseFor(1 * 60 * 60 * 1000L) }
    }

    @Test
    fun `pause 4 hours calls pauseManager with correct duration`() = runTest {
        viewModel.pause(4)
        coVerify { pauseManager.pauseFor(4 * 60 * 60 * 1000L) }
    }

    @Test
    fun `resume calls pauseManager resume`() = runTest {
        viewModel.resume()
        coVerify { pauseManager.resume() }
    }
}
