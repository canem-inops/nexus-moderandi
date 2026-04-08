package dev.canem.nexusmoderandi.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.canem.nexusmoderandi.data.entity.AllowedNumber
import dev.canem.nexusmoderandi.data.entity.RejectedCall

@Database(
    entities = [AllowedNumber::class, RejectedCall::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun allowedNumberDao(): AllowedNumberDao
    abstract fun rejectedCallDao(): RejectedCallDao
}
