package com.jxlopez.movieapp.data.db.mapper

import com.jxlopez.movieapp.data.datasource.remote.response.Movie
import com.jxlopez.movieapp.data.datasource.remote.response.MovieResponse
import com.jxlopez.movieapp.data.db.movies.entities.MovieEntity
import com.jxlopez.movieapp.data.db.movies.entities.MovieTrendingEntity
import com.jxlopez.movieapp.model.MovieItem

fun Movie.toMovieEntity(sectionType: String) = MovieEntity(
    id,
    title,
    backdropPath ?: "",
    posterPath ?: "",
    popularity,
    releaseDate,
    voteAverage,
    voteCount,
    sectionType
)

fun Movie.toMovieTrendingEntity() = MovieTrendingEntity(
    id,
    title,
    backdropPath ?: "",
    posterPath ?: "",
    popularity,
    releaseDate,
    voteAverage,
    voteCount
)

fun MovieItem.toMovieEntity() = MovieEntity(
    id,
    title,
    backDrop,
    poster,
    popularity,
    releaseDate,
    voteAverage,
    voteCount,
    sectionType
)

fun MovieEntity.toMovieItem() = MovieItem(
    id,
    title,
    backDrop,
    poster,
    popularity,
    releaseDate,
    voteAverage,
    voteCount,
    sectionType
)

fun MovieTrendingEntity.toMovieItem() = MovieItem(
    id,
    title,
    backDrop,
    poster,
    popularity,
    releaseDate,
    voteAverage,
    voteCount,
    ""
)