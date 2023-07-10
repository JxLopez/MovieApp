package com.jxlopez.movieapp.data.db.mapper

import com.jxlopez.movieapp.data.datasource.remote.response.ProfileResponse
import com.jxlopez.movieapp.data.db.movies.entities.ProfileEntity
import com.jxlopez.movieapp.model.ProfileItem

fun ProfileResponse.toProfileEntity() = ProfileEntity(
    id,
    name,
    username,
    avatar.tmdb.avatarPath

)

fun ProfileEntity.toProfileItem() = ProfileItem(
    id,
    name,
    username,
    avatar
)