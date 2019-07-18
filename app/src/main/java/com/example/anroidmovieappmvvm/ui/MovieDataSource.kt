package com.example.anroidmovieappmvvm.ui

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.anroidmovieappmvvm.data.models.MovieModel
import com.example.anroidmovieappmvvm.data.models.Result
import com.example.anroidmovieappmvvm.data.repository.MovieRepository
import com.example.anroidmovieappmvvm.internal.NetworkStatus
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MovieDataSource(private val repository: MovieRepository, private val type: ViewModelType) :
    PageKeyedDataSource<Int, MovieModel>() {
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, MovieModel>) {
        GlobalScope.launch {
            val result: Result = when (type) {
                ViewModelType.NOW_PLAYING -> repository.getNowPlayingMovies(page = 1)
                ViewModelType.TOP_RATED -> repository.getTopRatedMovies(page = 1)
                ViewModelType.UPCOMING -> repository.getUpcomingMovies(page = 1)
            }

            if (result.networkStatus == NetworkStatus.SUCCESS && result.responseModel != null) {
                callback.onResult(result.responseModel.movies, 1, result.responseModel.page + 1)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieModel>) {
        GlobalScope.launch {
            val page = params.key + 1
            val result: Result = when (type) {
                ViewModelType.NOW_PLAYING -> repository.getNowPlayingMovies(page = page)
                ViewModelType.TOP_RATED -> repository.getTopRatedMovies(page = page)
                ViewModelType.UPCOMING -> repository.getUpcomingMovies(page = page)
            }

            if (result.networkStatus == NetworkStatus.SUCCESS && result.responseModel != null) {
                callback.onResult(result.responseModel.movies, page)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieModel>) {
        GlobalScope.launch {
            val result: Result = when (type) {
                ViewModelType.NOW_PLAYING -> repository.getNowPlayingMovies(page = params.key)
                ViewModelType.TOP_RATED -> repository.getTopRatedMovies(page = params.key)
                ViewModelType.UPCOMING -> repository.getUpcomingMovies(page = params.key)
            }

            if (result.networkStatus == NetworkStatus.SUCCESS && result.responseModel != null && result.responseModel.page > 1) {
                callback.onResult(result.responseModel.movies, result.responseModel.page - 1)
            }
        }
    }

}

class MovieDataSourceFactory(private val repository: MovieRepository, private val type: ViewModelType) :
    androidx.paging.DataSource.Factory<Int, MovieModel>() {
    val sourceLiveData = MutableLiveData<MovieDataSource>()
    var latestSource: MovieDataSource? = null
    override fun create(): androidx.paging.DataSource<Int, MovieModel> {
        latestSource = MovieDataSource(repository, type)
        sourceLiveData.postValue(latestSource)
        return latestSource!!
    }
}