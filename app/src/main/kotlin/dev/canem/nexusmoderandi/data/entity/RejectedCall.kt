package dev.canem.nexusmoderandi.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rejected_calls")
data class RejectedCall(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val phoneNumber: String,
    val timestamp: Long = System.currentTimeMillis()
)
