package com.example.anroidmovieappmvvm.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.anroidmovieappmvvm.data.models.DetailModel
import com.example.anroidmovieappmvvm.data.repository.MovieRepository
import com.example.anroidmovieappmvvm.internal.NetworkStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailViewModel(private val movieId: Int, private val repository: MovieRepository) : ViewModel() {

    val viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private var _details = MutableLiveData<DetailModel>()
    val details: LiveData<DetailModel>
        get() = _details

    private var _networkStatus = MutableLiveData<NetworkStatus>()
    val networkStatus: LiveData<NetworkStatus>
        get() = _networkStatus

    private val DEFAULT_LANGUAGE = "en-US"

    init {
        uiScope.launch {
            val (response, status) = repository.getMovieDetails(DEFAULT_LANGUAGE, movieId)
            _details.value = response
            _networkStatus.value = status

        }
    }

    class Factory(private val movieId: Int, private val repository: MovieRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(movieId, repository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
