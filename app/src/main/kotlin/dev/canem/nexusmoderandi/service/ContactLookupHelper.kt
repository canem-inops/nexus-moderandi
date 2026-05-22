package dev.canem.nexusmoderandi.service

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract

object ContactLookupHelper {

    fun isNumberInContacts(context: Context, phoneNumber: String): Boolean {
        if (phoneNumber.isBlank()) return false
        val uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(phoneNumber)
        )
        return try {
            context.contentResolver.query(
                uri,
                arrayOf(ContactsContract.PhoneLookup._ID),
                null, null, null
            )?.use { cursor ->
                cursor.moveToFirst()
            } ?: false
        } catch (_: Exception) {
            false
        }
    }
}
