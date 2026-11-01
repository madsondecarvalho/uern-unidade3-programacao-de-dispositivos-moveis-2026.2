package com.example.app_movies_unidade_3_uern.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    val id: String, // imdbID
    val title: String,
    val year: String,
    val genre: String,
    val plot: String,
    val rating: String,
    val posterUrl: String,
    val isFavorite: Boolean = false,
    val lastUpdatedTimestamp: Long = System.currentTimeMillis()
)
