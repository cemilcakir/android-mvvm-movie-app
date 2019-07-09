package com.example.anroidmovieappmvvm.data.repository

import com.example.anroidmovieappmvvm.data.models.DetailModel
import com.example.anroidmovieappmvvm.data.models.ResponseModel
import com.example.anroidmovieappmvvm.data.network.MovieWebService

class MovieRepository {

    suspend fun getNowPlayingMovies() : ResponseModel? {
        return try {
            MovieWebService().getNowPlayingMovies()
        } catch (ex: Exception) {
            println("error getMovies $ex")
            null
        }
    }

    suspend fun getTopRatedMovies() : ResponseModel? {
        return try {
            MovieWebService().getTopRatedMovies()
        } catch (ex: Exception) {
            println("error getMovies $ex")
            null
        }
    }

    suspend fun getUpcomingMovies() : ResponseModel? {
        return try {
            MovieWebService().getUpcomingMovies()
        } catch (ex: Exception) {
            println("error getMovies $ex")
            null
        }
    }

    suspend fun getMovieDetails(id: Int) : DetailModel? {
        return try {
            MovieWebService().getMovieDetails(id)
        } catch (ex: Exception) {
            println("error getMovies $ex")
            null
        }
    }
}