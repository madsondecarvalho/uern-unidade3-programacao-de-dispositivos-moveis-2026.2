package com.example.app_movies_unidade_3_uern.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private const val BASE_URL = "https://www.omdbapi.com/"

    // Chave padrão ativa para testes da OMDb API.
    // Pode ser substituída pela sua chave pessoal gerada em omdbapi.com/apikey.aspx
    const val API_KEY = "4a3b711b"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val omdbApi: OmdbApi by lazy {
        retrofit.create(OmdbApi::class.java)
    }
}
