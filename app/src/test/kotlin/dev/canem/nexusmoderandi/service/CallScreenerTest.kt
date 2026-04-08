package dev.canem.nexusmoderandi.service

import dev.canem.nexusmoderandi.data.entity.AllowedNumber
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CallScreenerTest {

    private lateinit var screener: CallScreener

    @Before
    fun setup() {
        screener = CallScreener()
    }

    // -- normalizeNumber tests --

    @Test
    fun `normalizeNumber strips formatting from local number`() {
        assertEquals("5551234567", screener.normalizeNumber("(555) 123-4567"))
    }

    @Test
    fun `normalizeNumber preserves leading plus`() {
        assertEquals("+15551234567", screener.normalizeNumber("+1 (555) 123-4567"))
    }

    @Test
    fun `normalizeNumber handles plain digits`() {
        assertEquals("5551234567", screener.normalizeNumber("5551234567"))
    }

    @Test
    fun `normalizeNumber handles international format with plus`() {
        assertEquals("+442071234567", screener.normalizeNumber("+44 207 123 4567"))
    }

    @Test
    fun `normalizeNumber handles empty string`() {
        assertEquals("", screener.normalizeNumber(""))
    }

    @Test
    fun `normalizeNumber handles dashes and dots`() {
        assertEquals("5551234567", screener.normalizeNumber("555.123.4567"))
    }

    // -- isNumberInAllowList tests --

    @Test
    fun `isNumberInAllowList returns true for exact match`() {
        val allowList = listOf(AllowedNumber(phoneNumber = "+15551234567"))
        assertTrue(screener.isNumberInAllowList("+15551234567", allowList))
    }

    @Test
    fun `isNumberInAllowList matches despite formatting differences`() {
        val allowList = listOf(AllowedNumber(phoneNumber = "(555) 123-4567"))
        assertTrue(screener.isNumberInAllowList("5551234567", allowList))
    }

    @Test
    fun `isNumberInAllowList matches international with different formatting`() {
        val allowList = listOf(AllowedNumber(phoneNumber = "+1-555-123-4567"))
        assertTrue(screener.isNumberInAllowList("+1 (555) 123-4567", allowList))
    }

    @Test
    fun `isNumberInAllowList returns false for non-matching number`() {
        val allowList = listOf(AllowedNumber(phoneNumber = "+15551234567"))
        assertFalse(screener.isNumberInAllowList("+15559999999", allowList))
    }

    @Test
    fun `isNumberInAllowList returns false for empty list`() {
        assertFalse(screener.isNumberInAllowList("+15551234567", emptyList()))
    }

    @Test
    fun `isNumberInAllowList distinguishes numbers with and without country code`() {
        val allowList = listOf(AllowedNumber(phoneNumber = "+15551234567"))
        // "5551234567" normalizes to "5551234567", "+15551234567" normalizes to "+15551234567"
        // These are different — the plus prefix matters
        assertFalse(screener.isNumberInAllowList("5551234567", allowList))
    }

    @Test
    fun `isNumberInAllowList finds match among multiple entries`() {
        val allowList = listOf(
            AllowedNumber(phoneNumber = "+15551111111"),
            AllowedNumber(phoneNumber = "+15552222222"),
            AllowedNumber(phoneNumber = "+15553333333")
        )
        assertTrue(screener.isNumberInAllowList("+15552222222", allowList))
    }
}
