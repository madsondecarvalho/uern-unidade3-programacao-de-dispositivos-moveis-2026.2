package com.example.app_movies_unidade_3_uern.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MovieSearchResponse(
    @SerializedName("Search")
    val search: List<MovieSummaryDto>? = null,

    @SerializedName("totalResults")
    val totalResults: String? = null,

    @SerializedName("Response")
    val response: String,

    @SerializedName("Error")
    val error: String? = null
)
