package dev.canem.nexusmoderandi.service

import dev.canem.nexusmoderandi.data.entity.AllowedNumber
import javax.inject.Inject

class CallScreener @Inject constructor() {

    fun normalizeNumber(number: String): String {
        return if (number.startsWith("+")) {
            "+" + number.filter { it.isDigit() }
        } else {
            number.filter { it.isDigit() }
        }
    }

    fun isNumberInAllowList(phoneNumber: String, allowList: List<AllowedNumber>): Boolean {
        val normalized = normalizeNumber(phoneNumber)
        return allowList.any { normalizeNumber(it.phoneNumber) == normalized }
    }
}
