package dev.canem.nexusmoderandi.service

import dev.canem.nexusmoderandi.data.entity.AllowedNumber
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CallScreenerTest {

    private lateinit var screener: CallScreener

    @Before
    fun setup() {
        screener = CallScreener()
    }

    @Test
    fun `exact match`() {
        val allowList = listOf(AllowedNumber(phoneNumber = "+5511912345678"))
        assertTrue(screener.isNumberInAllowList("+5511912345678", allowList))
    }

    @Test
    fun `international format matches national format`() {
        val allowList = listOf(AllowedNumber(phoneNumber = "+5511912345678"))
        assertTrue(screener.isNumberInAllowList("11912345678", allowList))
    }

    @Test
    fun `national format matches international format`() {
        val allowList = listOf(AllowedNumber(phoneNumber = "11912345678"))
        assertTrue(screener.isNumberInAllowList("+5511912345678", allowList))
    }

    @Test
    fun `local number matches full number`() {
        val allowList = listOf(AllowedNumber(phoneNumber = "912345678"))
        assertTrue(screener.isNumberInAllowList("+5511912345678", allowList))
    }

    @Test
    fun `full number matches local number`() {
        val allowList = listOf(AllowedNumber(phoneNumber = "+5511912345678"))
        assertTrue(screener.isNumberInAllowList("912345678", allowList))
    }

    @Test
    fun `formatted number matches raw digits`() {
        val allowList = listOf(AllowedNumber(phoneNumber = "(11) 91234-5678"))
        assertTrue(screener.isNumberInAllowList("+5511912345678", allowList))
    }

    @Test
    fun `different numbers do not match`() {
        val allowList = listOf(AllowedNumber(phoneNumber = "+5511912345678"))
        assertFalse(screener.isNumberInAllowList("+5511999999999", allowList))
    }

    @Test
    fun `empty allow list returns false`() {
        assertFalse(screener.isNumberInAllowList("+5511912345678", emptyList()))
    }

    @Test
    fun `finds match among multiple entries`() {
        val allowList = listOf(
            AllowedNumber(phoneNumber = "+5511911111111"),
            AllowedNumber(phoneNumber = "+5511922222222"),
            AllowedNumber(phoneNumber = "+5511933333333")
        )
        assertTrue(screener.isNumberInAllowList("922222222", allowList))
    }

    @Test
    fun `US number formats match`() {
        val allowList = listOf(AllowedNumber(phoneNumber = "+1 (555) 123-4567"))
        assertTrue(screener.isNumberInAllowList("5551234567", allowList))
    }
}
