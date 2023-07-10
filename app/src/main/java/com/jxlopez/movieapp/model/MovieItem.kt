package com.jxlopez.movieapp.model

data class MovieItem(
    val id: Int,
    val title: String,
    val backDrop: String,
    val poster: String,
    val popularity: Double,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val sectionType: String
)
