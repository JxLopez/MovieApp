package com.jxlopez.movieapp.data.db.mapper

import com.jxlopez.movieapp.data.datasource.remote.response.MovieRated
import com.jxlopez.movieapp.data.db.movies.entities.MovieRatedByUserEntity
import com.jxlopez.movieapp.model.MovieRatedByUserItem

fun MovieRated.toMovieRatedByUserEntity() = MovieRatedByUserEntity(
    id,
    title,
    backdropPath ?: "",
    posterPath ?: "",
    popularity,
    releaseDate,
    voteAverage,
    voteCount,
    rating
)

fun MovieRatedByUserEntity.toMovieRatedByUserItem() = MovieRatedByUserItem(
    id,
    title,
    backDrop ?: "",
    poster ?: "",
    popularity,
    releaseDate,
    voteAverage,
    voteCount,
    rating
)