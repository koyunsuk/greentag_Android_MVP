package com.greentag.app

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CupReturnDao {

    @Insert
    suspend fun insert(cup: CupReturn)

    @Query("SELECT * FROM cup_returns ORDER BY timestamp DESC")
    suspend fun getAll(): List<CupReturn>

    @Query("DELETE FROM cup_returns")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM cup_returns")
    suspend fun getCount(): Int
}
