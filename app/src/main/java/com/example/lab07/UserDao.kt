package com.example.lab07

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM User")
    suspend fun getAll(): List<User>

    @Query("DELETE FROM User WHERE uid = (SELECT MAX(uid) FROM User WHERE uid > 0)")
    suspend fun deleteLastUser()
}