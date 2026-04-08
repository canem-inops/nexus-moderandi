package dev.canem.nexusmoderandi.ui.allowlist

import dev.canem.nexusmoderandi.data.entity.AllowedNumber
import dev.canem.nexusmoderandi.data.repository.AllowListRepository
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
class AllowListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: AllowListRepository
    private lateinit var viewModel: AllowListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        every { repository.getAllNumbers() } returns flowOf(
            listOf(AllowedNumber(phoneNumber = "+15551234567"))
        )
        viewModel = AllowListViewModel(repository)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `allowedNumbers emits repository data`() = runTest {
        // stateIn(WhileSubscribed) requires an active collector to start
        val job = launch(testDispatcher) { viewModel.allowedNumbers.collect {} }
        val numbers = viewModel.allowedNumbers.value
        assertEquals(1, numbers.size)
        assertEquals("+15551234567", numbers[0].phoneNumber)
        job.cancel()
    }

    @Test
    fun `onInputChange updates inputNumber`() {
        viewModel.onInputChange("+15559999999")
        assertEquals("+15559999999", viewModel.inputNumber)
    }

    @Test
    fun `addNumber calls repository and clears input`() = runTest {
        viewModel.onInputChange("+15559999999")
        viewModel.addNumber()

        coVerify { repository.addNumber("+15559999999") }
        assertEquals("", viewModel.inputNumber)
    }

    @Test
    fun `addNumber does nothing for blank input`() = runTest {
        viewModel.onInputChange("   ")
        viewModel.addNumber()

        coVerify(exactly = 0) { repository.addNumber(any()) }
    }

    @Test
    fun `removeNumber calls repository`() = runTest {
        viewModel.removeNumber("+15551234567")
        coVerify { repository.removeNumber("+15551234567") }
    }
}
