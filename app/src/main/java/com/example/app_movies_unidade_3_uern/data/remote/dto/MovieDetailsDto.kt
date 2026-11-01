package com.example.app_movies_unidade_3_uern.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MovieDetailsDto(
    @SerializedName("Title")
    val title: String? = null,

    @SerializedName("Year")
    val year: String? = null,

    @SerializedName("Genre")
    val genre: String? = null,

    @SerializedName("Plot")
    val plot: String? = null,

    @SerializedName("imdbRating")
    val imdbRating: String? = null,

    @SerializedName("Poster")
    val poster: String? = null,

    @SerializedName("imdbID")
    val imdbId: String,

    @SerializedName("Response")
    val response: String? = null,

    @SerializedName("Error")
    val error: String? = null
)
