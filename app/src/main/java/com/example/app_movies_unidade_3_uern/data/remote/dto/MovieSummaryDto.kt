package com.example.app_movies_unidade_3_uern.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MovieSummaryDto(
    @SerializedName("Title")
    val title: String? = null,

    @SerializedName("Year")
    val year: String? = null,

    @SerializedName("imdbID")
    val imdbId: String,

    @SerializedName("Type")
    val type: String? = null,

    @SerializedName("Poster")
    val poster: String? = null
)
