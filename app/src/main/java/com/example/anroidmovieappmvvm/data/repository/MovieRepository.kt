package com.example.anroidmovieappmvvm.data.repository

import com.example.anroidmovieappmvvm.data.models.DetailResult
import com.example.anroidmovieappmvvm.data.models.Result

interface MovieRepository {
    suspend fun getNowPlayingMovies(language: String, page: Int): Result
    suspend fun getTopRatedMovies(language: String, page: Int): Result
    suspend fun getUpcomingMovies(language: String, page: Int): Result
    suspend fun getMovieDetails(language: String, id: Int): DetailResult
}