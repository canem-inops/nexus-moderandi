package dev.canem.nexusmoderandi.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.canem.nexusmoderandi.data.entity.RejectedCall
import kotlinx.coroutines.flow.Flow

@Dao
interface RejectedCallDao {
    @Query("SELECT * FROM rejected_calls ORDER BY timestamp DESC")
    fun getAll(): Flow<List<RejectedCall>>

    @Insert
    suspend fun insert(rejectedCall: RejectedCall)

    @Query("SELECT COUNT(*) FROM rejected_calls WHERE timestamp >= :since")
    suspend fun countSince(since: Long): Int

    @Query("DELETE FROM rejected_calls")
    suspend fun clearAll()
}
