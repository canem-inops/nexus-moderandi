package dev.canem.nexusmoderandi.data.repository

import dev.canem.nexusmoderandi.data.db.AllowedNumberDao
import dev.canem.nexusmoderandi.data.entity.AllowedNumber
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AllowListRepository @Inject constructor(
    private val dao: AllowedNumberDao
) {
    fun getAllNumbers(): Flow<List<AllowedNumber>> = dao.getAll()

    suspend fun addNumber(phoneNumber: String) {
        val normalized = phoneNumber.trim()
        if (normalized.isNotBlank()) {
            dao.insert(AllowedNumber(phoneNumber = normalized))
        }
    }

    suspend fun removeNumber(phoneNumber: String) {
        dao.delete(phoneNumber)
    }

    suspend fun isNumberAllowed(phoneNumber: String): Boolean {
        return dao.exists(phoneNumber)
    }

    suspend fun getAllNumbersSync(): List<AllowedNumber> {
        return dao.getAllSync()
    }
}
