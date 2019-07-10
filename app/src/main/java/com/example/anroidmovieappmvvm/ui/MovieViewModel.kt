package com.example.anroidmovieappmvvm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.anroidmovieappmvvm.data.models.MovieModel
import com.example.anroidmovieappmvvm.data.models.ResponseModel
import com.example.anroidmovieappmvvm.data.repository.MovieRepository
import com.example.anroidmovieappmvvm.internal.NetworkStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class ViewModelType {
    NOW_PLAYING, TOP_RATED, UPCOMING
}

class MovieViewModel(private val type: ViewModelType, private val repository: MovieRepository) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _movies = MutableLiveData<List<MovieModel>>()
    val movies: LiveData<List<MovieModel>>
        get() = _movies

    private var _networkStatus = MutableLiveData<NetworkStatus>()
    val networkStatus: LiveData<NetworkStatus>
        get() = _networkStatus

    private var _navigateToDetail = MutableLiveData<Int>()
    val navigateToDetail: LiveData<Int>
        get() = _navigateToDetail

    private val DEFAULT_LANGUAGE = "en-US"

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        uiScope.launch {
            when (type) {
                ViewModelType.NOW_PLAYING -> {
                    val (response, status) = repository.getNowPlayingMovies(DEFAULT_LANGUAGE, 1)
                    _movies.value = response?.movies
                    _networkStatus.value = status
                }
                ViewModelType.TOP_RATED -> {
                    val (response, status) = repository.getTopRatedMovies(DEFAULT_LANGUAGE, 1)
                    _movies.value = response?.movies
                    _networkStatus.value = status
                }
                ViewModelType.UPCOMING -> {
                    val (response, status) = repository.getUpcomingMovies(DEFAULT_LANGUAGE, 1)
                    _movies.value = response?.movies
                    _networkStatus.value = status
                }
            }
        }
    }

    fun doneNavigatingToDetail() {
        _navigateToDetail.value = null
    }

    fun navigateToDetails(movieId: Int) {
        _navigateToDetail.value = movieId
    }

    class Factory(private val type: ViewModelType, private val repository: MovieRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MovieViewModel(type, repository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}