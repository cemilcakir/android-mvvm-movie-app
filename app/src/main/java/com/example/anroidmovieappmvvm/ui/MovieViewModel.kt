package com.example.anroidmovieappmvvm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.anroidmovieappmvvm.data.models.MovieModel
import com.example.anroidmovieappmvvm.data.models.ResponseModel
import com.example.anroidmovieappmvvm.data.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class ViewModelType {
    NOW_PLAYING, TOP_RATED, UPCOMING
}

class MovieViewModel(val type: ViewModelType) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val repository = MovieRepository()

    private var _response = MutableLiveData<ResponseModel>()

    private var _movies = MutableLiveData<List<MovieModel>>()
    val movies: LiveData<List<MovieModel>>
        get() = _movies

    private var _navigateToDetail = MutableLiveData<Int>()
    val navigateToDetail: LiveData<Int>
        get() = _navigateToDetail

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        uiScope.launch {
            when (type) {
                ViewModelType.NOW_PLAYING -> _response.value = repository.getNowPlayingMovies()
                ViewModelType.TOP_RATED -> _response.value = repository.getTopRatedMovies()
                ViewModelType.UPCOMING -> _response.value = repository.getUpcomingMovies()
            }

            _response.value?.let {
                _movies.value = _response.value?.movies
            }
        }
    }

    fun doneNavigatingToDetail() {
        _navigateToDetail.value = null
    }

    fun navigateToDetails(movieId: Int) {
        _navigateToDetail.value = movieId
    }

    class Factory(val type: ViewModelType) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MovieViewModel(type) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}