package com.example.android.politicalpreparedness.database

import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElection(election: Election)

    @Query("SELECT * FROM election_table ORDER BY electionDay")
    suspend fun getElections(): List<Election>

    @Query("SELECT * FROM election_table WHERE id = :id")
    suspend fun getElection(id: Int): Election?

    @Delete
    suspend fun deleteElection(election: Election)

    @Query("DELETE FROM election_table")
    suspend fun clear()
}