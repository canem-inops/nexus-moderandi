package dev.canem.nexusmoderandi.service

import android.telecom.Call
import android.telecom.CallScreeningService
import dagger.hilt.android.AndroidEntryPoint
import dev.canem.nexusmoderandi.data.repository.AllowListRepository
import dev.canem.nexusmoderandi.data.repository.RejectedCallRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NexusCallScreeningService : CallScreeningService() {

    @Inject lateinit var allowListRepository: AllowListRepository
    @Inject lateinit var rejectedCallRepository: RejectedCallRepository
    @Inject lateinit var callScreener: CallScreener

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onScreenCall(callDetails: Call.Details) {
        val phoneNumber = callDetails.handle?.schemeSpecificPart ?: ""

        if (callDetails.callDirection != Call.Details.DIRECTION_INCOMING) {
            respondToCall(callDetails, CallResponse.Builder().build())
            return
        }

        scope.launch {
            val shouldAllow = isNumberAllowed(phoneNumber)

            if (shouldAllow) {
                respondToCall(callDetails, CallResponse.Builder().build())
            } else {
                respondToCall(
                    callDetails,
                    CallResponse.Builder()
                        .setDisallowCall(true)
                        .setRejectCall(false)
                        .setSkipCallLog(false)
                        .setSkipNotification(true)
                        .build()
                )
                rejectedCallRepository.logRejectedCall(phoneNumber)
            }
        }
    }

    private suspend fun isNumberAllowed(phoneNumber: String): Boolean {
        if (phoneNumber.isBlank()) return false

        if (ContactLookupHelper.isNumberInContacts(this, phoneNumber)) {
            return true
        }

        val allAllowed = allowListRepository.getAllNumbersSync()
        return callScreener.isNumberInAllowList(phoneNumber, allAllowed)
    }
}
