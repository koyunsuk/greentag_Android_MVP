package com.greentag.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CupReturnDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(cupReturn: CupReturn)

    @Query("SELECT * FROM cup_returns")
    suspend fun getAll(): List<CupReturn>

    @Query("SELECT COUNT(*) FROM cup_returns")
    suspend fun getCount(): Int

    @Query("DELETE FROM cup_returns")
    suspend fun deleteAll()
}
