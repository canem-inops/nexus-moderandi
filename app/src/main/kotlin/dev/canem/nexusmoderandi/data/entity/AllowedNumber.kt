package dev.canem.nexusmoderandi.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "allowed_numbers")
data class AllowedNumber(
    @PrimaryKey
    val phoneNumber: String,
    val addedAt: Long = System.currentTimeMillis()
)
