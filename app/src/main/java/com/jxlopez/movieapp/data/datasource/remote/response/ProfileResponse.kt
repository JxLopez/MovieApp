package com.jxlopez.movieapp.data.datasource.remote.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("avatar")
    val avatar: Avatar,

)

data class Avatar(
    @SerializedName("tmdb")
    val tmdb: TmdbNode
)

data class TmdbNode(
    @SerializedName("avatar_path")
    val avatarPath: String
)
