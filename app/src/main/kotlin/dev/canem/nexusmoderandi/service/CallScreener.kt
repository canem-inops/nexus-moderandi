package dev.canem.nexusmoderandi.service

import android.telephony.PhoneNumberUtils
import dev.canem.nexusmoderandi.data.entity.AllowedNumber
import javax.inject.Inject

class CallScreener @Inject constructor() {

    fun isNumberInAllowList(phoneNumber: String, allowList: List<AllowedNumber>): Boolean {
        return allowList.any { PhoneNumberUtils.compare(phoneNumber, it.phoneNumber) }
    }
}
