package com.greentag.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cup_returns")
data class CupReturn(
    @PrimaryKey val uid: String,
    val timestamp: Long
)
