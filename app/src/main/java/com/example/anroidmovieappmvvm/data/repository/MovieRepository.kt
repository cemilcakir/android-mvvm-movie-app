package com.example.anroidmovieappmvvm.data.repository

import com.example.anroidmovieappmvvm.data.models.DetailResult
import com.example.anroidmovieappmvvm.data.models.Result
import com.example.anroidmovieappmvvm.data.network.DEFAULT_LANGUAGE

interface MovieRepository {
    suspend fun getNowPlayingMovies(language: String = DEFAULT_LANGUAGE, page: Int): Result
    suspend fun getTopRatedMovies(language: String = DEFAULT_LANGUAGE, page: Int): Result
    suspend fun getUpcomingMovies(language: String = DEFAULT_LANGUAGE, page: Int): Result
    suspend fun getMovieDetails(language: String = DEFAULT_LANGUAGE, id: Int): DetailResult
}