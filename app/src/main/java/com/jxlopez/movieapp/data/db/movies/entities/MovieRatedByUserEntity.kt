package com.jxlopez.movieapp.data.db.movies.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_rated_user")
data class MovieRatedByUserEntity (
    @PrimaryKey val id: Int,
    val title: String,
    val backDrop: String,
    val poster: String,
    val popularity: Double,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val rating: Double
)
