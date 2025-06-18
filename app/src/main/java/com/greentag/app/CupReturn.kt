package com.greentag.app

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cup_returns")
data class CupReturn(
    @PrimaryKey val uid: String,
    val alias: String?,
    val customer: String?,
    val location: String?,
    val status: String?,
    val size: String?,
    val timestamp: Long
)
