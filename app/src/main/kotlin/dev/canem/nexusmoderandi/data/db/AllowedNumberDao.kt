package dev.canem.nexusmoderandi.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.canem.nexusmoderandi.data.entity.AllowedNumber
import kotlinx.coroutines.flow.Flow

@Dao
interface AllowedNumberDao {
    @Query("SELECT * FROM allowed_numbers ORDER BY addedAt DESC")
    fun getAll(): Flow<List<AllowedNumber>>

    @Query("SELECT * FROM allowed_numbers")
    suspend fun getAllSync(): List<AllowedNumber>

    @Query("SELECT EXISTS(SELECT 1 FROM allowed_numbers WHERE phoneNumber = :number)")
    suspend fun exists(number: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(allowedNumber: AllowedNumber)

    @Query("DELETE FROM allowed_numbers WHERE phoneNumber = :number")
    suspend fun delete(number: String)
}
