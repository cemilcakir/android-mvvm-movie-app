package com.example.anroidmovieappmvvm.data.repository

import com.example.anroidmovieappmvvm.data.models.DetailResult
import com.example.anroidmovieappmvvm.data.models.Result
import com.example.anroidmovieappmvvm.data.network.MovieWebService
import com.example.anroidmovieappmvvm.internal.NetworkStatus
import com.example.anroidmovieappmvvm.internal.NoConnectivityException

class MovieRepositoryImpl(private val webService: MovieWebService) : MovieRepository {

    override suspend fun getNowPlayingMovies(language: String, page: Int): Result {
        return try {
            val response = webService.getNowPlayingMovies(language, page)
            val status = NetworkStatus.SUCCESS

            Result(response, status)
        } catch (e: NoConnectivityException) {
            val status = NetworkStatus.NO_CONNECTIVITY

            Result(null, status)
        }
    }

    override suspend fun getTopRatedMovies(language: String, page: Int): Result {
        return try {
            val response = webService.getTopRatedMovies(language, page)
            val status = NetworkStatus.SUCCESS

            Result(response, status)
        } catch (e: NoConnectivityException) {
            val status = NetworkStatus.NO_CONNECTIVITY

            Result(null, status)
        }
    }

    override suspend fun getUpcomingMovies(language: String, page: Int): Result {
        return try {
            val response = webService.getUpcomingMovies(language, page)
            val status = NetworkStatus.SUCCESS

            Result(response, status)
        } catch (e: NoConnectivityException) {
            val status = NetworkStatus.NO_CONNECTIVITY

            Result(null, status)
        }
    }

    override suspend fun getMovieDetails(language: String, id: Int): DetailResult {
        return try {
            val response = webService.getMovieDetails(id, language)
            val status = NetworkStatus.SUCCESS

            DetailResult(response, status)
        } catch (e: NoConnectivityException) {
            val status = NetworkStatus.NO_CONNECTIVITY

            DetailResult(null, status)
        }
    }
}