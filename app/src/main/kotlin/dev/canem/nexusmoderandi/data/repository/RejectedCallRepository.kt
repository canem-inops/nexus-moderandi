package dev.canem.nexusmoderandi.data.repository

import dev.canem.nexusmoderandi.data.db.RejectedCallDao
import dev.canem.nexusmoderandi.data.entity.RejectedCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RejectedCallRepository @Inject constructor(
    private val dao: RejectedCallDao
) {
    fun getRejectedCalls(): Flow<List<RejectedCall>> = dao.getAll()

    suspend fun logRejectedCall(phoneNumber: String) {
        dao.insert(RejectedCall(phoneNumber = phoneNumber))
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}
