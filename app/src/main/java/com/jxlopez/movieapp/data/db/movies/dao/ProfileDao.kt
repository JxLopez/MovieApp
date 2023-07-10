package com.jxlopez.movieapp.data.db.movies.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jxlopez.movieapp.data.db.movies.entities.ProfileEntity

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveProfile(profileEntity: ProfileEntity)

    @Query("SELECT * FROM profile")
    fun getProfile(): List<ProfileEntity>
}