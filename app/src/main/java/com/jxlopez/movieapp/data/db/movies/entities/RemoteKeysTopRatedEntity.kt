package com.jxlopez.movieapp.data.db.movies.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys_top_rated")
data class RemoteKeysTopRatedEntity(
    @PrimaryKey val movieId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
