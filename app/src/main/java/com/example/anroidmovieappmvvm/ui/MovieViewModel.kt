package com.example.anroidmovieappmvvm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.example.anroidmovieappmvvm.data.models.MovieModel
import com.example.anroidmovieappmvvm.data.repository.MovieRepository
import com.example.anroidmovieappmvvm.internal.NetworkStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

enum class ViewModelType {
    NOW_PLAYING, TOP_RATED, UPCOMING
}

class MovieViewModel(type: ViewModelType, repository: MovieRepository) : ViewModel() {

    private var _networkStatus = MutableLiveData<NetworkStatus>()
    val networkStatus: LiveData<NetworkStatus>
        get() = _networkStatus

    private var _navigateToDetail = MutableLiveData<Int>()
    val navigateToDetail: LiveData<Int>
        get() = _navigateToDetail

    private val dataSourceFactory = MovieDataSourceFactory(repository, type)
    val movies: LiveData<PagedList<MovieModel>> =
        dataSourceFactory.toLiveData(
            pageSize = 20
        )

    fun doneNavigatingToDetail() {
        _navigateToDetail.value = null
    }

    fun navigateToDetails(movieId: Int) {
        _navigateToDetail.value = movieId
    }

    class Factory(private val type: ViewModelType, private val repository: MovieRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MovieViewModel(type, repository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}