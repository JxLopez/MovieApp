package com.jxlopez.movieapp.data.db.movies.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity (
   @PrimaryKey val id: Int,
   val title: String,
   val backDrop: String,
   val poster: String,
   val popularity: Double,
   val releaseDate: String,
   val voteAverage: Double,
   val voteCount: Int,
   val sectionType: String
)